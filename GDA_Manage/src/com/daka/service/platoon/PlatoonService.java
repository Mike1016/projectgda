package com.daka.service.platoon;

import com.daka.api.base.Result;
import com.daka.api.platoon.PlatoonProvider;
import com.daka.constants.SystemConstant;
import com.daka.dao.dynamicTimerTask.IDynamicTimerTaskDAO;
import com.daka.dao.flashbackLog.IFlashbackLogDAO;
import com.daka.dao.platoon.IPlatoonDAO;
import com.daka.dao.sellout.ISelloutDAO;
import com.daka.entry.*;
import com.daka.entry.dto.CustomerDTO;
import com.daka.entry.dto.PlatoonDTO;
import com.daka.entry.dto.SelloutDTO;
import com.daka.enums.*;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.quartz.ConfirmCountdownTask;
import com.daka.quartz.FrozenCountDownTask;
import com.daka.quartz.PlatoonMatchingTask;
import com.daka.quartz.QuartzManager;
import com.daka.service.bank.BankService;
import com.daka.service.customer.CustomerService;
import com.daka.service.dictionaries.DictionariesService;
import com.daka.service.sellout.SelloutService;
import com.daka.util.DateUtil;
import com.daka.util.DateUtils;
import com.daka.util.alipay.UUID;
import com.daka.util.sms.SMSUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class PlatoonService {

    @Autowired
    private IPlatoonDAO platoonDAO;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SelloutService selloutService;
    @Autowired
    private BankService bankService;
    @Autowired
    private ISelloutDAO selloutDAO;
    @Autowired
    private IFlashbackLogDAO flashbackLogDAO;
    @Autowired
    private IDynamicTimerTaskDAO dynamicTimerTaskDAO;

    private final Logger logger = Logger.getLogger(PlatoonProvider.class);

    private static Lock platoon_lock = new ReentrantLock();

    private static Integer seconds = 3*60*60; //三个小时的秒数

    /**
     * 生成排单
     *
     * @param customerId 用户Id
     * @param account    排单金额
     * @throws Exception 异常
     */
    public void savePlatoon(Integer customerId, BigDecimal account) throws Exception {
        String orderNo = String.valueOf(UUID.next()); //订单号

		PlatoonVO platoonVO = new PlatoonVO();
		platoonVO.setOrderNo(orderNo);
		platoonVO.setAccount(account);
		platoonVO.setCustomerId(customerId);
		platoonVO.setCreateTime(DateUtils.getCurrentTimeYMDHMS());
		platoonVO.setState(PlatoonStateEnum.STATE_MATE.getValue());
		platoonDAO.savePlatoon(platoonVO);

		//匹配
		this.matching(account, platoonVO.getId(), customerId, orderNo);
	}

    /**
     * 匹配
     *
     * @param account   财富值
     * @param platoonId 排单ID
     * @throws Exception 异常
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, Throwable.class})
    public void matching(BigDecimal account, Integer platoonId, Integer customerId, String orderNo) throws Exception {
        CustomerVO customerVO = customerService.queryById(customerId); //当前用户
        //查询匹配的数据
        List<SelloutVO> selloutVOS = selloutDAO.matchingSellout(customerId, account);
        List<SelloutVO> matchSelloutList = new ArrayList<>(); //匹配上的单据
        BigDecimal accumulative = BigDecimal.ZERO; //累加金额
        for (SelloutVO selloutVO : selloutVOS) {
			accumulative = accumulative.add(selloutVO.getAccount()); //累加
            if (accumulative.compareTo(account) > 0) {
                break;
            }
            selloutVO.setPlatoonId(platoonId); //设置排单
            matchSelloutList.add(selloutVO);
        }

        //批量修改匹配的单据
        if (!matchSelloutList.isEmpty()) {
            selloutDAO.updateBatchPlatoonId(matchSelloutList); //修改匹配单据的排单ID
            //判断是否匹配完成
            BigDecimal selloutSum = selloutDAO.selectSelloutSumByPlatoonId(platoonId);

			if (account.compareTo(selloutSum) == 0) {
				platoonDAO.updatePlatoonState(new PlatoonVO(platoonId, PlatoonStateEnum.STATE_PAYMENT.getValue(), DateUtils.getCurrentTimeYMDHMS())); //修改排单状态为待付款
				List<SelloutDTO> selloutVOList = selloutDAO.selectSelloutByPlatoonId(platoonId); //获取匹配的单据
				selloutDAO.updateBatchState(selloutVOList); //修改匹配的单据状态

                String message = SystemEnum.MESSAGE_2.getMessage(orderNo); //短信信息
                SMSUtil.sendSms(customerVO.getPhone(), message);
                //发送短信
                message = SystemEnum.MESSAGE_3.getMessage(orderNo); //短信信息
                for (SelloutDTO selloutDTO : selloutVOList) {
                    SMSUtil.sendSms(selloutDTO.getPhone(), message);
                }
                //删除匹配的定时任务
                QuartzManager.removeJob(orderNo);

				//创建24小时定时任务 倒计时
				QuartzManager.addJob(orderNo, ConfirmCountdownTask.class, "0 0 * * *  ?", "用户【" + customerId + "】24小时匹配订单");
			} else {
				//如果未匹配完成添加半小时定时任务
				if (!QuartzManager.isExistJob(orderNo)) {
					QuartzManager.addJob(orderNo, PlatoonMatchingTask.class, "0 0/1 * * * ? *", "用户【" + customerId + "】半小时匹配订单");
				}
			}
		} else {
			//如果未匹配完成添加半小时定时任务
			if (!QuartzManager.isExistJob(orderNo)) {
				QuartzManager.addJob(orderNo, PlatoonMatchingTask.class, "0 0/1 * * * ? *", "用户【" + customerId + "】半小时匹配订单");
			}
		}
	}

    /**
     * 买入财富
     */
    public Result saveOrUpdatePlatoon(Integer customerId, BigDecimal account) throws Exception {
        try {
            logger.info("================= 买入财富 start =================");
            platoon_lock.lock();

            Integer platoonCycle = Integer.valueOf(SystemEnum.PLATOONCYCLE.getDictionarieValue()); //周期
            Integer platoonMax = Integer.valueOf(SystemEnum.PLATOONMAX.getDictionarieValue()); //排单最高数量
            List<PlatoonVO> cyclePlatoonList = platoonDAO.selectCyclePlatoon(customerId, platoonCycle); //获取用户周期内的订单

            //周期单数校验
            if (null != cyclePlatoonList && cyclePlatoonList.size() > platoonMax) {
                return Result.newFailure("超出周期限制的单数(" + platoonMax + "单)");
            }

            //间隔时间校验
            Long orderCycle = PlatoonDateEnum.HOUR_TIME.getHourTime(SystemEnum.ORDERCYCLE.getDictionarieValue()); //时间间隔
            if (!cyclePlatoonList.isEmpty()) {
                long orderTime = DateUtils.getTimestampByTime(cyclePlatoonList.get(0).getCreateTime()); //获取最后订单时间戳
                if (System.currentTimeMillis() - orderTime < orderCycle) {
                    return Result.newFailure("每单间隔" + SystemEnum.ORDERCYCLE.getDictionarieValue() + "H");
                }
            }

            //同时只能一次排单
            PlatoonVO platoonVO = platoonDAO.selectPlatoonOnlyByCustomerId(customerId);
            if (null != platoonVO) {
                return Result.newFailure("存在未完成的排单");
            }

            //匹配
            savePlatoon(customerId, account);

            logger.info("================= 买入财富 end =================");
        } catch (Exception e) {
            throw e;
        } finally {
            platoon_lock.unlock();
        }
        return Result.newSuccess("买入成功");
    }

    /**
     * 根据用户ID和状态查询买入订单信息
     *
     * @param customerId
     * @param state
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> showPlatoonOrder(Integer customerId, Integer state) throws Exception {
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("customerId", customerId);
        paraMap.put("state", state);
        List<Map<String, Object>> orderList = new ArrayList<>();
        List<PlatoonVO> platoonVOList = platoonDAO.showPlatoonOrder(paraMap);
        for (PlatoonVO VO : platoonVOList
        ) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", VO.getId());
            resultMap.put("orderNo", VO.getOrderNo());
            resultMap.put("account", VO.getAccount());
            resultMap.put("createTime", VO.getCreateTime());
            resultMap.put("matchTime", VO.getMatchTime());
            resultMap.put("paymentTime", VO.getPaymentTime());
            resultMap.put("confirmTime", VO.getConfirmTime());
            if (VO.getMatchTime() != null) {
                resultMap.put("matchTimeStamp", DateUtils.getTimestampByTime(VO.getMatchTime()));
                resultMap.put("nowTimeStamp", System.currentTimeMillis());
            }
            if (VO.getPaymentTime() != null) {
                resultMap.put("paymentTimeStamp", DateUtils.getTimestampByTime(VO.getPaymentTime()));
                resultMap.put("nowTimeStamp", System.currentTimeMillis());
            }
            List<SelloutVO> selloutVOList = selloutDAO.getSelloutVOList(VO.getId());
            resultMap.put("selloutList", selloutVOList);
            orderList.add(resultMap);
        }
        return orderList;
    }

    /**
     * 根据订单id查询订单详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    public PlatoonDTO showOrderByPlatoonId(Integer id) throws Exception {
        PlatoonVO platoonVO = platoonDAO.queryPlatoonById(id);
        List<SelloutVO> selloutVOList = selloutDAO.getSelloutVOList(id);
        PlatoonDTO platoonDTO = new PlatoonDTO();
        BeanUtils.copyProperties(platoonVO, platoonDTO);
        platoonDTO.setSelloutVOList(selloutVOList);
        return platoonDTO;
    }

    /**
     * 确认完成
     *
     * @param id 订单ID
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, Throwable.class})
    public Result confirmCompleted(Integer id) throws Exception {
        //获取订单细信息
        PlatoonVO platoonVO = platoonDAO.queryPlatoonById(id);
        //校验状态是否正确
        List<SelloutVO> selloutVOList = selloutDAO.getSelloutVOList(id);
        for (SelloutVO selloutVO : selloutVOList) {
            if (selloutVO.getState() != SelloutStateEnum.STATE_CFMD.getValue()) {
                return Result.newFailure("还有卖家未确认完成");
            }
        }
        //修改订单状态为已完成, 并且冻结
        platoonVO.setState(PlatoonStateEnum.STATE_OK.getValue());
        platoonVO.setStatus(PlatoonStatusEnum.STATE_FREEZE_YES.getValue());
        platoonVO.setConfirmTime(DateUtils.getCurrentTimeYMDHMS());
        platoonDAO.updatePlatoonState(platoonVO);
        selloutDAO.updateCompleted(id);
        if (QuartzManager.isExistJob(platoonVO.getOrderNo())) {
			QuartzManager.removeJob(platoonVO.getOrderNo());
		}
        //创建解冻的定时任务
        QuartzManager.addJob(platoonVO.getOrderNo(), FrozenCountDownTask.class, "0 0/1 * * * ? *", "用户【" + platoonVO.getCustomerId() + "】解冻定时任务");
        return Result.newSuccess("成功");
    }

    /**
     * 查询卖方订单id查询卖方信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Map<String,Object> showOtherInfo(Integer id) throws Exception {
        Map<String,Object> resultMap = new HashMap<>();
        SelloutVO selloutVO = selloutDAO.getSelloutVOById(id);
        CustomerVO customerVO = platoonDAO.showOtherBasicInfo(id);
        List<BankVO> bankList = platoonDAO.showOtherBankInfo(id);

        String nowTime = DateUtil.getTime();
        Integer diffSeconds = DateUtil.getDiffSecond(nowTime,selloutVO.getMatchTime());
        if (diffSeconds < seconds) {
            String customerPhone = customerVO.getPhone();
            String phone = customerPhone.substring(0,3)+"****"+customerPhone.substring(customerPhone.length()-4,customerPhone.length());
            customerVO.setPhone(phone);
            resultMap.put("customerVO",customerVO);
            resultMap.put("bankList",bankList);
        }
        resultMap.put("img",selloutVO.getPaymentImg());
        resultMap.put("customerVO",customerVO);
        resultMap.put("bankList",bankList);
        return resultMap;
    }

    /**
     * 上传截图
     *
     * @param id 卖出订单ID
     * @return
     * @throws Exception
     */
    public void uploadMap(HttpServletRequest request, Integer id, MultipartFile paymentImg) throws Exception {
        SelloutVO selloutVO = selloutService.getSelloutVOById(id);
        String fileName = paymentImg.getOriginalFilename();
        // 获取图片的扩展名
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 新的图片文件名 = 获取时间戳+"."图片扩展名
        String time = String.valueOf(SystemEnum.PAYMENT.getDesc() + System.currentTimeMillis());
        String newFileName = time + "." + extensionName;

        String location = SystemConstant.LOCAL_PATH + SystemEnum.PAYMENT.getDesc() + File.separator;
        File filePath = new File(location);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdirs();
        }
        Files.write(Paths.get(location + newFileName), paymentImg.getBytes());
        SelloutVO vo = new SelloutVO();
        vo.setId(selloutVO.getId());
        vo.setState(SelloutStateEnum.STATE_PAYMENT_YES.getValue());
        vo.setPaymentImg(SystemConstant.LOCAL_IMAGES + File.separator + SystemEnum.PAYMENT.getDesc() + File.separator + newFileName);
        vo.setCollectionTime(DateUtil.getTime());
        selloutService.updateSellout(vo);
    }

    /**
     * 确认已付款
     *
     * @param platoonId  拍档ID
     * @param customerId 用户ID
     * @throws Exception 异常
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, Throwable.class})
    public Result determinePayment(Integer platoonId, Integer customerId) throws Exception {
        //1.修改状态
        if (null == platoonId || null == customerId) {
            throw new Exception("未接收到请求参数");
        }
        CustomerVO customerVO = customerService.queryById(customerId);
        if (null == customerVO) {
            throw new Exception("未查询到此用户");
        }
        PlatoonVO platoonVO = platoonDAO.queryPlatoonById(platoonId);
        if (null == platoonVO) {
            throw new Exception("未查询到此订单");
        }
        //2.判断是否存在未付款的卖出订单
        SelloutVO selloutVO = new SelloutVO();
        selloutVO.setPlatoonId(platoonVO.getId());
        selloutVO.setState(SelloutStateEnum.STATE_PAYMENT_NO.getValue());
        List<SelloutVO> selloutVOS = selloutDAO.queryPlatoonSonOrder(selloutVO);
        if (selloutVOS != null && selloutVOS.size() > 0) {
            return Result.newSuccess("还有未付款订单", 0);
        }
        platoonVO.setPaymentTime(DateUtil.getTime());
        platoonVO.setState(PlatoonStateEnum.STATE_CONFIRM.getValue());
        platoonDAO.updatePlatoonVO(platoonVO);//修改状态
        //3.创建定时任务
        if (QuartzManager.isExistJob(platoonVO.getOrderNo())) {//是否存在定时任务
            QuartzManager.removeJob(platoonVO.getOrderNo());//存在就删除该定时任务
        }
        QuartzManager.addJob(platoonVO.getOrderNo(), ConfirmCountdownTask.class, "0 0 * * *  ?", "待确认定时任务");
        //4.发送短信
        List<SelloutDTO> list = selloutDAO.querySelloutCustomer(platoonVO.getId());
        if (null == list && list.size() == 0) {
            throw new Exception("未查询到相关字订单");
        }
        for (SelloutDTO selloutDTO : list) {
            //给卖家发送短信
            String message = SystemEnum.MESSAGE_3.getMessage(selloutDTO.getOrderNo());
            SMSUtil.sendSms(selloutDTO.getPhone(), message);
        }
        return Result.newFailure("确认成功", 1);
    }

    /**
     * 根据订单ID查询买方
     *
     * @param id
     * @return
     * @throws Exception
     */
    public PlatoonVO queryPlatoonById(Integer id) throws Exception {
        return platoonDAO.queryPlatoonById(id);
    }

    /**
     * 后台分页查询
     *
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> queryPlatoonlistPage(Page page) throws Exception {
        return platoonDAO.queryPlatoonlistPage(page);
    }

    /**
     * 成长中财富
     *
     * @param request
     * @param sessionId
     * @return
     */
    public Result getWealthGrowUp(HttpServletRequest request, String sessionId) throws Exception {
        Map<String, Object> paramsMap = new HashMap<String, Object>();

        CustomerVO customerVo = SessionContext.getConstomerInfoBySessionId(request, sessionId);
        List<PlatoonVO> platoonvoList = platoonDAO.getPlatoonByCustomerId(customerVo.getId());
        List<PlatoonVO> platoonvoLists = new ArrayList<PlatoonVO>();
        if (!(platoonvoList == null)) {
            for (int i = 0; i < platoonvoList.size(); i++) {
                PlatoonVO platoonVo = platoonvoList.get(i);
                //state为 3.已完成 且 status为 1.冻结
                if (platoonVo.getState() == PlatoonStateEnum.STATE_OK.getValue() && platoonVo.getStatus() == PlatoonStatusEnum.STATE_FREEZE_YES.getValue()) {
                    platoonvoLists.add(platoonVo);
                }
            }
            //排单总金额
            BigDecimal platoonResult = platoonDAO.getPlatoonSum(customerVo.getId());
            //卖出总金额
            BigDecimal selloutResult = selloutDAO.getSelloutSum(customerVo.getId());

            paramsMap.put("platoonvoLists", platoonvoLists);
            paramsMap.put("platoonResult", platoonResult);
            paramsMap.put("selloutResult", selloutResult);
            paramsMap.put("Date", DateUtils.getCurrentTimeYMDHMS());

            return Result.newSuccess(paramsMap, 1);
        }
        return Result.newFailure("获取失败", 0);
    }

    /**
     * 已收益财富
     *
     * @param request
     * @param sessionId
     * @return
     */
    public Result getWealthGain(HttpServletRequest request, String sessionId) throws Exception {
        Map<String, Object> paramsMap = new HashMap<String, Object>();

        CustomerVO customerVo = SessionContext.getConstomerInfoBySessionId(request, sessionId);
        List<PlatoonVO> platoonvoList = platoonDAO.getPlatoonByCustomerId(customerVo.getId());
        List<PlatoonVO> platoonvoLists = new ArrayList<PlatoonVO>();
        if (!(platoonvoList == null)) {
            for (int i = 0; i < platoonvoList.size(); i++) {
                PlatoonVO platoonVo = platoonvoList.get(i);
                //state为 3.已完成 且 status为 2.解冻
                if (platoonVo.getState() == PlatoonStateEnum.STATE_OK.getValue() && platoonVo.getStatus() == PlatoonStatusEnum.STATE_FREEZE_NO.getValue()) {
                    platoonvoLists.add(platoonVo);
                }
            }
            paramsMap.put("platoonvoLists", platoonvoLists);
            paramsMap.put("Date", DateUtils.getCurrentTimeYMDHMS());
            return Result.newSuccess(paramsMap, 1);
        }
        return Result.newFailure("获取失败", 0);
    }

    /**
     * ============================================后台  交易回溯===========================================
     *
     * @param id
     * @param state
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, Throwable.class})
    public Result toFlashBack(int id, int state) throws Exception {
        PlatoonVO platoonVO = platoonDAO.queryPlatoonById(id);
        if (null == platoonVO) {
            return Result.newSuccess("未找到该订单");
        }
        PlatoonVO platoon = new PlatoonVO();
        platoon.setId(id);
        platoon.setStatus(PlatoonStatusEnum.STATE_FREEZE_HAVE_NOT.getValue());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderNo", platoonVO.getOrderNo());

        //1.删除定时任务
        DynamicTimerTaskVO taskVO = dynamicTimerTaskDAO.queryDynamicTimerTaskByTaskName(platoonVO.getOrderNo());
        if (taskVO != null) {
            QuartzManager.removeJob(platoonVO.getOrderNo());
        }
        //2.保存交易回溯日志
        saveFlashbackLog(platoon);
        switch (state) {
            case 1://待付款(1)-->回溯 待匹配(0)
                //3.修改状态并发送短信
                platoon.setState(PlatoonStateEnum.STATE_MATE.getValue());
                platoon.setCreateTime(DateUtil.getTime());
                updateBatch(platoon, SelloutStateEnum.STATE_MATE.getValue());
                //4.创建定时任务  0 0 0 * * ? *
                QuartzManager.addJob(platoonVO.getOrderNo(), PlatoonMatchingTask.class, "0 0/5 * * * ? *", "等待倒计时");
                break;
            case 2://待确认(2)-->回溯 待付款(1)
                //3.修改状态并发送短信
                platoon.setState(PlatoonStateEnum.STATE_PAYMENT.getValue());
                platoon.setMatchTime(DateUtil.getTime());
                updateBatch(platoon, SelloutStateEnum.STATE_PAYMENT_NO.getValue());
                //4.创建定时任务  24小时等待倒计时定时任务
                QuartzManager.addJob(platoonVO.getOrderNo(), ConfirmCountdownTask.class, "0 0/5 * * * ? *", "等待倒计时");
                break;
            case 3://已完成(3)-->回溯 待确认(2)
                //判断订单是冻结状态（1）还是解冻状态（2）
                if (platoonVO.getStatus() == PlatoonStatusEnum.STATE_FREEZE_NO.getValue()) {
                    return Result.newSuccess("该订单已经完成并返利，不能进行回溯");
                } else {
                    //3.修改状态并发送短信
                    platoon.setState(PlatoonStateEnum.STATE_CONFIRM.getValue());
                    platoon.setPaymentTime(DateUtil.getTime());
                    updateBatch(platoon, SelloutStateEnum.STATE_PAYMENT_YES.getValue());
                    //4.创建定时任务  24小时等待倒计时定时任务
                    QuartzManager.addJob(platoonVO.getOrderNo(), ConfirmCountdownTask.class, "0 0/5 * * * ? *", "等待倒计时");
                }
                break;
            case 4://失效(4)-->回溯 待匹配(0)
                //3.修改状态并发送短信
                platoon.setState(PlatoonStateEnum.LOSE_EFFICACY.getValue());
                platoon.setCreateTime(DateUtil.getTime());
                updateBatch(platoon, SelloutStateEnum.STATE_MATE.getValue());
                break;
            default:
                return Result.newSuccess("该订单不能进行回溯");
        }
        return Result.newSuccess("回溯成功");
    }

    /**
     * 排单的状态改变，从而引起排单匹配的卖出订单的状态的改变
     * 发送短信
     */
    public void updateBatch(PlatoonVO platoonVO, Integer selloutState) throws Exception {
        if (null == platoonVO || null == selloutState) {
            throw new Exception("未接收到数据");
        }
        //1.根据id（排单的id）查询排单信息、卖出信息
        PlatoonDTO platoonDTO = platoonDAO.queryPlatoonCustomer(platoonVO.getId());
        if (null == platoonDTO) {
            throw new Exception("没有找到该排单");
        }
        //2.修改状态
        //修改排单状态
        platoonDAO.updatePlatoonVO(platoonVO);
        //修改卖出状态
        List<SelloutDTO> list = selloutDAO.querySelloutCustomer(platoonVO.getId());
        List<SelloutVO> selloutVOList = new ArrayList<SelloutVO>();
        StringBuffer stringBuffer = new StringBuffer();
        for (SelloutDTO selloutDTO : list) {
            SelloutVO vo = new SelloutVO();
            vo.setId(selloutDTO.getId());
            vo.setState(selloutState);
            selloutVOList.add(vo);
            if (selloutDTO.getId() != null){
                stringBuffer.append(selloutDTO.getId() + ",");
            }
        }
        if (null == selloutVOList || selloutVOList.size() == 0) {
            throw new Exception("没有数据");
        }
        selloutDAO.updateBatch(selloutVOList);
        //3.发送短信
        //短信内容
        String message = SystemEnum.MESSAGE_5.getMessage(platoonDTO.getOrderNo());
        //给排单用户发送短信
        if (platoonVO.getState() != PlatoonStateEnum.LOSE_EFFICACY.getValue()) {
            SMSUtil.sendSms(platoonDTO.getPhone(), message);
        }
        for (SelloutDTO selloutDTO : list) {
            //给卖家发送短信
            message = SystemEnum.MESSAGE_5.getMessage(selloutDTO.getOrderNo());
            SMSUtil.sendSms(selloutDTO.getPhone(), message);
        }
        //修改(清空)付款截图
        String ids;
        if (stringBuffer.length() > 0){
            ids = stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
        }else{
            ids = "-1";
        }
        if (platoonVO.getState() == PlatoonStateEnum.STATE_PAYMENT.getValue()) {
            selloutDAO.updatePaymentImgNull(ids);
        }
        //修改(清空)卖出的匹配订单信息
        if (platoonVO.getState() == PlatoonStateEnum.STATE_MATE.getValue() || platoonVO.getState() == PlatoonStateEnum.LOSE_EFFICACY.getValue()) {
            selloutDAO.updatePlatoonIdNull(ids);
        }
    }

    /**
     * 保存交易回溯日志
     *
     * @param platoonVO
     */
    public void saveFlashbackLog(PlatoonVO platoonVO) throws Exception {
        if (null == platoonVO) {
            throw new Exception("未接受到数据");
        }
        //1.根据id（排单的id）查询排单信息、卖出信息
        PlatoonDTO platoonDTO = platoonDAO.queryPlatoonCustomer(platoonVO.getId());
        if (null == platoonDTO) {
            throw new Exception("没有找到该排单");
        }
        //2.保存交易回溯日志
        List<SelloutDTO> list = selloutDAO.querySelloutCustomer(platoonVO.getId());
        List<TlashbackLogVO> tlashbackLogVOList = new ArrayList<TlashbackLogVO>();
        for (SelloutDTO selloutDTO : list) {
            TlashbackLogVO tlashbackLogVO = new TlashbackLogVO();
            tlashbackLogVO.setPlatoonId(platoonVO.getId());
            tlashbackLogVO.setSelloutId(selloutDTO.getId());
            tlashbackLogVO.setPlatoonState(platoonDTO.getState());
            tlashbackLogVO.setSelloutState(selloutDTO.getState());
            tlashbackLogVO.setPlatoonAccount(platoonDTO.getAccount());
            tlashbackLogVO.setSelloutAccount(selloutDTO.getAccount());
            tlashbackLogVO.setCreateTime(DateUtil.getTime());
            tlashbackLogVOList.add(tlashbackLogVO);
        }
        if (tlashbackLogVOList != null && tlashbackLogVOList.size() > 0) {
            flashbackLogDAO.saveFlashbackLog(tlashbackLogVOList);
        }
    }

    /**
     * 完善信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    public boolean perfectPersonalInfo(Integer id) throws Exception {
        CustomerVO customerVO = customerService.queryById(id);
        if (customerVO.getSecurityPassword() != null && customerVO.getRealName() != null
                && customerVO.getIdentityCard() != null && customerVO.getAlipay() != null
                && customerVO.getWeChat() != null) {
            List<BankVO> bankVO = bankService.queryBankByCustomerId(id);
            if (bankVO != null && bankVO.size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 匹配控时
     *
     * @return
     */
    public boolean matchingTimeControl() throws Exception {
        String stat = DictionariesService.dictionaries.get("OPEN_TIME");
        String end = DictionariesService.dictionaries.get("CLOSE_TIME");
        if (null == stat || null == end) {
            return false;
        }
        Long statTime = DateUtils.getTimestampByTime(DateUtil.getDay() + " " + stat);
        Long endTime = DateUtils.getTimestampByTime(DateUtil.getDay() + " " + end);
        Long nowTime = System.currentTimeMillis();
        if (endTime - statTime <= 0 || endTime - nowTime <= 0) {
            return false;
        }
        return true;
    }
}
