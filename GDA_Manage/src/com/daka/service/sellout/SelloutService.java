package com.daka.service.sellout;

import com.daka.api.base.Result;
import com.daka.dao.customer.ICustomerDAO;
import com.daka.dao.platoon.IPlatoonDAO;
import com.daka.dao.sellout.ISelloutDAO;
import com.daka.entry.*;
import com.daka.entry.dto.PlatoonDTO;
import com.daka.entry.dto.SelloutDTO;
import com.daka.enums.*;
import com.daka.interceptor.appsession.SessionContext;

import com.daka.service.dictionaries.DictionariesService;
import com.daka.service.platoon.PlatoonService;
import com.daka.util.DateUtils;
import com.daka.util.MD5;

import com.daka.util.sms.SMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.daka.util.DateUtil;
import com.daka.util.alipay.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

@Service
public class SelloutService {

    @Autowired
    private ISelloutDAO iSelloutDAO; //卖出
    @Autowired
    private ICustomerDAO customerDAO; //用户
    @Autowired
    private IPlatoonDAO iPlatoonDAO; //排单
    @Autowired
    private PlatoonService platoonService;

    private static Integer seconds = 3*60*60; //三个小时的秒数

    /**
     * 分页查询
     *
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> querySelloutlistPage(Page page) throws Exception {
        return iSelloutDAO.querySelloutlistPage(page);
    }

    /**
     * 卖出订单
     *
     * @param selloutVO
     * @param customerId
     * @param securityPassword
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, Throwable.class})
    public Result saveSellout(SelloutVO selloutVO, Integer customerId, String securityPassword) throws Exception {
        boolean perfectPersonalInfo = platoonService.perfectPersonalInfo(customerId);
        if (perfectPersonalInfo == false) {
            return Result.newFailure("信息不全，请去完善！", 0);
        }
        CustomerVO customerVO = customerDAO.queryCustomerById(customerId);
        // 判断安全密码是否为空
        if (null == securityPassword) {
            return Result.newFailure("安全密码为空！", 0);
        }
        // 判断输入的安全密码是否和用户的一致
        if (!(MD5.md5(securityPassword).equals(customerVO.getSecurityPassword()))) {
            return Result.newFailure("安全密码不正确！", 0);
        }

        // 判断此用户最近一次卖出订单的时间，卖出订单1天1次
        String time = iSelloutDAO.querySelloutTime(customerId);
        if (time != null) {
            time = time.substring(0, 10);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = String.valueOf(sdf.format(new Date()));
        if (time != null && nowTime.equals(time)) {
            return Result.newFailure("您今天卖出订单的资格已经用完了!");
        }
        Integer orderPeriod = Integer.valueOf(SystemEnum.SELLOUTORDERPERIOD.getDictionarieValue()); //排单周期
        List<SelloutVO> selloutVOList = iSelloutDAO.querySelloutOrderByCustomerId(customerId,orderPeriod);
        Integer selloutOrderMax = Integer.valueOf(DictionariesService.dictionaries.get("selloutOrderMax")); //周期内排单最多限制
        if ((!(null == selloutVOList || selloutVOList.size() == 0)) && (selloutVOList.size() > selloutOrderMax)) {
            return Result.newFailure("您这一周期内卖出订单的资格已经用完了!");
        }

        // type=1 财富背包
        if (selloutVO.getType() == SelloutTypeEnum.TYPE_WEALTH.getValue()) {
            if (customerVO.getWealth().compareTo(BigDecimal.ZERO) == 0) {
                return Result.newFailure("财富背包余额不足!", 0);
            }
            BigDecimal platoonAccount = iPlatoonDAO.querySumAccountByCustomerId(customerId);
            if (platoonAccount.compareTo(BigDecimal.ZERO) <= 0) { // 冻结的账户财富值是否大于0，大于0可以卖出，否则不予卖出
                return Result.newFailure("冻结的账户财富值不足", 0);
            }
            if ((selloutVO.getAccount()).compareTo(new BigDecimal(DictionariesService.dictionaries.get("selloutWealthMin"))) < 0) { //卖出的财富背包的金额要大于2000
                return Result.newFailure("卖出的奖金小于" + DictionariesService.dictionaries.get("selloutWealthMin"), 0);
            }
            BigDecimal temp = selloutVO.getAccount().divide(new BigDecimal(DictionariesService.dictionaries.get("selloutWealthMultiple")), 2, RoundingMode.HALF_UP);
            if (new BigDecimal(temp.intValue()).compareTo(temp) != 0) { //卖出的币种是否为500的正整数倍
                return Result.newFailure("输入的财富背包的金额必须为" + DictionariesService.dictionaries.get("selloutWealthMultiple") + "的倍数", 0);
            }
            selloutVO.setCustomerId(customerId);
            if (iSelloutDAO.querySumAccountByType(selloutVO).add(selloutVO.getAccount()).compareTo(customerVO.getWealth()) > 0) {
                return Result.newFailure("剩余的账户财富值不足", 0);
            }
        }

        // type=2 奖金背包
        if (selloutVO.getType() == SelloutTypeEnum.TYPE_BONUS.getValue()) {
            if (customerVO.getBonus() == null) {
                return Result.newFailure("奖金背包余额不足!", 0);
            }
            if (customerVO.getVitality() < Integer.parseInt(DictionariesService.dictionaries.get("selloutBonusVitality"))) { //生命力低于50，不可发布卖出奖金背包
                return Result.newFailure("生命力低于" + DictionariesService.dictionaries.get("selloutBonusVitality") + "不予卖出", 0);
            }
            if ((selloutVO.getAccount()).compareTo(new BigDecimal(DictionariesService.dictionaries.get("selloutBonusMin"))) < 0) { //卖出的奖金背包的金额要大于1000
                return Result.newFailure("卖出的奖金小于" + DictionariesService.dictionaries.get("selloutBonusMin"), 0);
            }
            BigDecimal temp = selloutVO.getAccount().divide(new BigDecimal(DictionariesService.dictionaries.get("selloutBonusMultiple")), 2, RoundingMode.HALF_UP);
            if (new BigDecimal(temp.intValue()).compareTo(temp) != 0) { //卖出的币种是否为1000的正整数倍
                return Result.newFailure("输入的奖金背包的金额必须为" + DictionariesService.dictionaries.get("selloutBonusMultiple") + "的倍数", 0);
            }
            selloutVO.setCustomerId(customerId);
            if (iSelloutDAO.querySumAccountByType(selloutVO).add(selloutVO.getAccount()).compareTo(customerVO.getBonus()) > 0) {
                return Result.newFailure("剩余的账户奖金背包金额不足", 0);
            }
        }
        selloutVO.setOrderNo(String.valueOf(UUID.next()));
        selloutVO.setCustomerId(customerId);
        selloutVO.setCreateTime(DateUtil.getTime());
        selloutVO.setState(SelloutStateEnum.STATE_MATE.getValue());
        iSelloutDAO.saveSellout(selloutVO);
        return Result.newSuccess("卖出成功", 1);
    }

    /**
     * 根据用户id和状态查询卖出订单信息
     *
     * @param customerId
     * @param state
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> showSelloutOrder(Integer customerId, Integer state) throws Exception {
        Map<String, Object> paraMap = new HashMap<>();
        paraMap.put("customerId", customerId);
        paraMap.put("state", state);
        List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
        List<SelloutVO> selloutVOList = iSelloutDAO.showSelloutOrder(paraMap);
        for (SelloutVO VO : selloutVOList
        ) {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("id",VO.getId());
            resultMap.put("orderNo", VO.getOrderNo());
            resultMap.put("account", VO.getAccount());
            resultMap.put("createTime", VO.getCreateTime());
            resultMap.put("matchTime", VO.getMatchTime());
            resultMap.put("collectionTime", VO.getCollectionTime());
            resultMap.put("confirmTime", VO.getConfirmTime());
            if (VO.getMatchTime() != null) {
                resultMap.put("matchTimeStamp", DateUtils.getTimestampByTime(VO.getMatchTime()));
                resultMap.put("nowTimeStamp", System.currentTimeMillis());
            }
            orderList.add(resultMap);
        }
        return orderList;
    }

    /**
     * 根据卖出订单的id修改卖出订单的状态为确认收款
     *
     * @param selloutVO
     * @throws Exception
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, Throwable.class})
    public Result updateState(SelloutVO selloutVO, Integer customerId) throws Exception {
        SelloutVO vo = iSelloutDAO.getSelloutVOById(selloutVO.getId());
        CustomerVO customerVO = customerDAO.queryById(customerId);
        if (vo.getType() == SelloutTypeEnum.TYPE_WEALTH.getValue()) { // 扣除财富背包的金额
            BigDecimal wealthAccount = customerVO.getWealth().subtract(vo.getAccount());
            customerVO.setWealth(wealthAccount);
            customerDAO.updateMessage(customerVO);
        }
        if (vo.getType() == SelloutTypeEnum.TYPE_BONUS.getValue()) { //扣除奖金背包的金额
            BigDecimal bonusAccount = customerVO.getBonus().subtract(vo.getAccount());
            customerVO.setBonus(bonusAccount);
            customerDAO.updateMessage(customerVO);
        }
        selloutVO.setState(SelloutStateEnum.STATE_CFMD.getValue());
        selloutVO.setConfirmTime(DateUtil.getTime());
        iSelloutDAO.updateState(selloutVO);
        PlatoonVO platoonVO = platoonService.queryPlatoonById(vo.getPlatoonId());
        CustomerVO cus = customerDAO.queryCustomerById(platoonVO.getCustomerId());
        String message = SystemEnum.MESSAGE_3.getMessage(platoonVO.getOrderNo());
        SMSUtil.sendSms(cus.getPhone(),message);
        return Result.newSuccess("确认成功！", 1);
    }

    /**
     * 修改卖方信息
     *
     * @param selloutVO
     * @throws Exception
     */
    public void updateSellout(SelloutVO selloutVO) throws Exception {
        iSelloutDAO.updateSellout(selloutVO);
    }

    /**
     * 根据卖单ID查询卖方信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    public SelloutVO getSelloutVOById(Integer id) throws Exception {
        return iSelloutDAO.getSelloutVOById(id);
    }

    /**
     * 根据卖出订单id查看对方基本信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Map<String,Object> showOtherBasicInfo(Integer id) throws Exception {
        Map<String,Object> resultMap = new HashMap<>();
        CustomerVO customerVO = iSelloutDAO.showOtherBasicInfo(id);
        SelloutVO selloutVO = iSelloutDAO.getSelloutVOById(id);
        String nowTime = DateUtil.getTime();
        Integer diffSeconds = DateUtil.getDiffSecond(nowTime,selloutVO.getMatchTime());
        if (diffSeconds < seconds) {
            String customerPhone = customerVO.getPhone();
            String phone = customerPhone.substring(0,3)+"****"+customerPhone.substring(customerPhone.length()-4,customerPhone.length());
            customerVO.setPhone(phone);
            resultMap.put("customerVO",customerVO);
        }
        resultMap.put("customerVO",customerVO);
        return resultMap;
    }

    /**
     * 根据卖出订单id查看对方银行卡信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    public List<BankVO> showOtherBankInfo(Integer id) throws Exception {
        return iSelloutDAO.showOtherBankInfo(id);
    }

    /**
     * 根据卖出订单id查看买方付款截图
     *
     * @param id
     * @return
     * @throws Exception
     */
    public String showPaymentImg(Integer id) throws Exception {
        return iSelloutDAO.showPaymentImg(id);
    }

    /**
     * 查询用户是否存在
     *
     * @param userName 用户账号
     * @return
     * @throws Exception
     */
    public Result checkUserNameInfo(String userName) throws Exception {
        CustomerVO customerVO = customerDAO.checkUserNameInfo(userName);
        if (null == customerVO) {
            return Result.newSuccess("用户不存在",0);
        } else {
            if (customerVO.getState()== CustomerStateEnum.IS_ACTIVE_BAN.getValue()){
                return Result.newSuccess("此账号已被封号",0);
            }else {
                return Result.newSuccess(customerVO,"",1);
            }
        }
    }


    /**
     * 卖出财富值记录
     *
     * @param request
     * @param sessionId
     * @return
     */
    public Result getWealthSellInfo(HttpServletRequest request, String sessionId) throws Exception {

        CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
        List<PlatoonDTO> selloutVoList = iPlatoonDAO.getSelloutDtoByPlatoonId(cus.getId()); 
      
        if(selloutVoList != null) {
        	return Result.newSuccess(selloutVoList, 1);
        }
        return Result.newFailure("获取失败", "0");
    }

    /**
     * 买入财富值记录
     *
     * @param request
     * @param sessionId
     * @return
     */
    public Result getWealthBuyInfo(HttpServletRequest request, String sessionId) throws Exception {
    	
        CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
        List<SelloutDTO> platoonVoList = iSelloutDAO.selectPlatoonInfoList(cus.getId());
        
        if(platoonVoList != null) {
        	return Result.newSuccess(platoonVoList, 1);
        }
        return Result.newFailure("获取失败", 0);
    }

    /**
     * 卖出奖金记录
     *
     * @param request
     * @param sessionId
     * @return
     */
    public Result getWealthBounsInfo(HttpServletRequest request, String sessionId) throws Exception {

        CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
        List<PlatoonDTO> selloutVoList = iPlatoonDAO.getSelloutDtoBonusInfo(cus.getId());
        if(selloutVoList != null){
        	 return Result.newSuccess(selloutVoList, 1);
        }
        return Result.newFailure("获取失败", 0);
    }


    /**
     * 分页查询
     *
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> queryStateSelloutlistPage(Page page) throws Exception {
        return iSelloutDAO.queryStateSelloutlistPage(page);
    }

    /**
     * 手动匹配
     *
     * @param selloutVO
     * @param platoonAccount 排单金额
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, Throwable.class})
    public void matching(SelloutVO selloutVO, String platoonAccount) throws Exception {
        if (null == selloutVO || null == platoonAccount) {
            throw new Exception("数据异常");
        }
        //1.查询排单已经被匹配的数据
        selloutVO.getPlatoonId();
        selloutVO.setState(SelloutStateEnum.STATE_PAYMENT_NO.getValue());
        List<SelloutVO> selloutVOList = iSelloutDAO.querySelloutByPlatoonId(selloutVO);
        //2.判断是否存在排单已经被匹配的数据
        if (selloutVOList.size() > 0) {//存在
            Long account = 0l;
            for (SelloutVO vo : selloutVOList) {
                account += Long.valueOf(String.valueOf(vo.getAccount()));
            }
            //判断已匹配金额是否达到排单金额
            if (BigDecimal.valueOf(Long.valueOf(platoonAccount)).compareTo(BigDecimal.valueOf(account)) <= 0) {
                throw new Exception("排单匹配已完成");
            } else {
                //还需要匹配的金额
                BigDecimal surplusAccount = BigDecimal.valueOf(Long.valueOf(platoonAccount)).subtract(BigDecimal.valueOf(account));
                //开始匹配
                BigDecimal matchingAccount = updateStartMatching(selloutVO, surplusAccount);
                if (matchingAccount.compareTo(new BigDecimal(0)) == 0) {
                    //修改排单状态
                    PlatoonVO platoonVO = new PlatoonVO();
                    platoonVO.setId(selloutVO.getPlatoonId());
                    platoonVO.setState(PlatoonStateEnum.STATE_PAYMENT.getValue());
                    iPlatoonDAO.updatePlatoonState(platoonVO);
                }
            }
        } else {//不存在
            //开始匹配
            BigDecimal matchingAccount = updateStartMatching(selloutVO, BigDecimal.valueOf(Long.valueOf(platoonAccount)));
            if (matchingAccount.compareTo(new BigDecimal(0)) == 0) {
                //修改排单状态
                PlatoonVO platoonVO = new PlatoonVO();
                platoonVO.setId(selloutVO.getPlatoonId());
                platoonVO.setState(PlatoonStateEnum.STATE_PAYMENT.getValue());
                iPlatoonDAO.updatePlatoonState(platoonVO);
            }
        }
    }

    /**
     * 开始匹配
     *
     * @param selloutVO
     * @param surplusAccount 还需要匹配的金额
     * @return 返回剩余可匹配的金额
     * @throws Exception
     */
    public BigDecimal updateStartMatching(SelloutVO selloutVO, BigDecimal surplusAccount) throws Exception {
        if (surplusAccount.compareTo(selloutVO.getAccount()) >= 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //修改卖出订单信息
            SelloutVO vo = new SelloutVO();
            vo.setId(selloutVO.getId());
            vo.setOrderNo(selloutVO.getOrderNo());
            vo.setPlatoonId(selloutVO.getPlatoonId());
            String time = sdf.format(new Date());
            vo.setMatchTime(time);
            vo.setState(SelloutStateEnum.STATE_PAYMENT_NO.getValue());
            iSelloutDAO.updateSellout(vo);

            //返回剩余可匹配的金额
            return surplusAccount.subtract(selloutVO.getAccount());
        } else {
            return new BigDecimal(-1);
        }
    }

    /**
     * 后台卖出
     *
     * @param selloutVO
     * @throws Exception
     */
    public void saveSelloutInfo(SelloutVO selloutVO) throws Exception {
        selloutVO.setOrderNo(String.valueOf(UUID.next()));
        selloutVO.setCreateTime(DateUtil.getTime());
        selloutVO.setState(SelloutStateEnum.STATE_MATE.getValue());
        iSelloutDAO.saveSellout(selloutVO);
    }

}
