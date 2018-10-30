package com.daka.service.tray;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.daka.api.base.Result;
import com.daka.constants.SystemConstant;
import com.daka.dao.activitylog.IActivityLogDAO;
import com.daka.dao.customer.ICustomerDAO;
import com.daka.dao.tray.ITrayDAO;
import com.daka.dao.tray.log.ITrayLogDAO;
import com.daka.entry.ActivityLogVO;
import com.daka.entry.CustomerVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.entry.TrayLogVO;
import com.daka.entry.TrayVO;
import com.daka.enums.ActivityLogEnum;
import com.daka.enums.TrayEnum;
import com.daka.enums.TrayLogEnum;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.service.activitylog.ActivityLogService;
import com.daka.service.customer.CustomerService;
import com.daka.util.DateUtils;

@Service
public class TrayService {

    @Autowired
    private ITrayDAO traydao;
    @Autowired
    private ICustomerDAO customerdao;
    @Autowired
    private ITrayLogDAO traylogdao;
    @Autowired
    private CustomerService customerservice;
    @Autowired
    private IActivityLogDAO activitylogdao;
    @Autowired
    private ActivityLogService activitylogservice;

    private Lock increasesVitalityLock = new ReentrantLock();//抽奖财富值增加锁

    /**
     * 用户选择转盘档数进行抽奖，系统返回奖品信息
     *
     * @param customerId 抽奖的用户
     * @param trayGrade  转盘档数  3/6/9
     * @return trayNo   奖品编号
     */
    public Result insertStartLottery(int customerId, int trayGrade) throws Exception {
        CustomerVO cvo = customerdao.queryById(customerId);
        if (cvo.getActivity() < (trayGrade * SystemConstant.CONSUMPTION_MULTIPLE)) {//用户活力值小于档数*倍率
            return Result.newFailure("活动值不足，请多多排单增加活动值或者重新选择档数", 0);
        }
        List<TrayVO> list = traydao.findByGrade(trayGrade);//该档数的转盘内容
        int trayId = this.randomMath(list);//根据不同档次的转盘进行抽奖得到奖品编号
        if (trayId == -1) {
            return Result.newFailure("请联系管理员", 0);
        }
        TrayVO tray = traydao.findTrayById(trayId);//根据奖品编号得到奖品的种类和数量
        int type = tray.getType();//奖品类型 1:财富值 2:生命力 3:其他实物  财富值生命力直接发货
        CustomerVO vo = customerdao.queryById(customerId);//抽奖的用户
        TrayLogVO tvo = new TrayLogVO();//奖品发货记录
        tvo.setCustomerId(customerId);//用户ID
        tvo.setCreateTime(DateUtils.getCurrentTimeYMDHMS());//创建时间
        tvo.setAccount(tray.getAccount());//财富值/生命值/实物数量
        tvo.setPrizes(tray.getPrizes());//奖品说明
        tvo.setVitality(0 - tray.getTrayGrade() * (SystemConstant.CONSUMPTION_MULTIPLE));//消耗的活动值
        if (type == TrayEnum.TYPE_WELTH.getValue()) {//如果奖品是财富值
            increasesVitalityLock.lock();//增加财富值前锁住财富值
            vo = customerdao.queryById(customerId);
            vo.setWealth(vo.getWealth().add(new BigDecimal(tray.getAccount())));
            customerdao.updateMessage(vo);//增加用财富值
            increasesVitalityLock.unlock();//解锁
            tvo.setType(TrayLogEnum.TYPE_WELTH.getValue());//类型财富值
            tvo.setState(TrayLogEnum.STATE_DELIVER.getValue());//已发货
        }
        if (type == TrayEnum.TYPE_VITALITY.getValue()) {//如果奖品是生命力
            vo.setVitality(vo.getVitality() + tray.getAccount());//增加生命力
            tvo.setType(TrayLogEnum.TYPE_VITALITY.getValue());//类型为生命力
            tvo.setState(TrayLogEnum.STATE_DELIVER.getValue());//已发货
        }
        if (type == TrayEnum.TYPE_OTHER.getValue()) {//如果奖品是实物
            tvo.setType(TrayLogEnum.TYPE_OTHER.getValue());//类型为实物
            tvo.setState(TrayLogEnum.STATE_WAIT_FOR.getValue());//待发货
        }
        vo.setActivity(vo.getActivity() - (trayGrade * SystemConstant.CONSUMPTION_MULTIPLE));
        customerdao.updateMessage(vo);//增加生命力
        customerservice.updateLimitingVitailty();//限制用户生命力在0--100之间
        ActivityLogVO alvo = new ActivityLogVO();//活动值消耗记录
        alvo.setCustomerId(customerId);
        alvo.setType(ActivityLogEnum.TYPE_TURNTABLE.getValue());//转盘扣除活动值
        alvo.setCount(trayGrade * SystemConstant.CONSUMPTION_MULTIPLE);//消耗的活动值是trayGrade的三倍
        alvo.setCreateTime(DateUtils.getCurrentTimeYMDHMS());//变化时间
        activitylogdao.insert(alvo);//添加活动值消耗记录
        traylogdao.insert(tvo);//添加发货信息
        if (trayId != SystemConstant.THANK_PATRONAGE) {//如果奖品id不是1(谢谢惠顾)：返回奖品信息和中奖提示
            String msg = "恭喜您获得:" + tray.getPrizes();
            return Result.newSuccess(tray, msg, 1);
        } else {//如果奖品id是1：谢谢惠顾，直接返回谢谢惠顾
            return Result.newFailure(tray, "谢谢惠顾", 1);
        }
    }


    /**
     * 根据一个档次的转盘信息进行抽奖，得到奖品编号
     *
     * @param list 一个档次的转盘信息
     * @return 奖品编号
     */
    public int randomMath(List<TrayVO> list) {
        return getRandomByProbability(list);
    }

    /**
     * 用户的中奖纪录
     *
     * @param request
     * @param sessionId
     * @return
     * @throws Exception
     */
    public Result awardDetail(HttpServletRequest request, String sessionId, int page) throws Exception {
        CustomerVO vo = SessionContext.getConstomerInfoBySessionId(request, sessionId);
        Map<String, Object> map = activitylogservice.paging(page, vo.getId());
        List<TrayLogVO> trayloglist = traylogdao.findByCustomer(map);//用户的抽奖记录
        if (trayloglist.size() == 0) {//抽奖记录为空
            return Result.newSuccess("暂无数据", -1);
        }
        return Result.newSuccess(trayloglist, 1);
    }

    /**
     * 分页查询转盘信息	可以根据档数查询同一档数的信息
     *
     * @param request 含有转盘档数信息
     * @return
     * @throws Exception
     */
    public Object queryTraylistPage(HttpServletRequest request) throws Exception {
        PageData pd = new PageData(request);
        Page page = new Page();
        page.setPd(pd);
        List<PageData> traylist = traydao.queryTraylistPage(page);
        JSONObject result = new JSONObject();
        result.put("status", "200");
        result.put("message", "");
        result.put("count", page.getTotalResult());
        result.put("data", traylist);
        return result;
    }

    /**
     * 修改转盘奖品信息
     *
     * @param list
     * @throws Exception
     */
    public Result updateByGrade(TrayVO vo, MultipartFile file) throws Exception {
        TrayVO tray = traydao.findTrayById(vo.getId());
        BigDecimal totalProbability = traydao.sumProbability(tray);//计算此奖品所在的档数其他奖品的概率之和
        totalProbability = totalProbability.add(vo.getProbability());//该奖品所在的档数的所有奖品概率之和
        int temp = totalProbability.compareTo(new BigDecimal(1));//该奖品所在的档数的所有奖品概率之和与1作比较
        if (temp > 0) {//该奖品所在的档数的所有奖品概率之和大于1
            return Result.newFailure("该奖品的设置概率过大 ，该档数奖品概率之和大于100%，请重新设置奖品概率", 0);
        }
        vo.setCreateTime(DateUtils.getCurrentTimeYMDHMS());//维护时间

        //下载奖品的图片保存到服务器上
        String location = SystemConstant.LOCAL_PATH + vo.getId() + File.separator;
        Path path = Paths.get(location);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        Files.write(Paths.get(location + file.getOriginalFilename()), file.getBytes());
        String filename = file.getOriginalFilename();

        this.zoomImage(filename, filename, SystemConstant.IMAGE_WIDTH, SystemConstant.IMAGE_HEIGHT);//图片大小转成相同大小

        vo.setContextImg(filename);//保存奖品图片的路径
        traydao.update(vo);
        return Result.newFailure("修改成功", 1);
    }

    /**
     * 将图片转换成制定大小的图片
     *
     * @param srcFileName 原图片路径
     * @param tagFileName 转换后的路径
     * @param width       转换后的宽
     * @param height      转换后的高
     * @throws IOException
     */
    public static void zoomImage(String srcFileName, String tagFileName, int width, int height) throws IOException {
        BufferedImage bi = ImageIO.read(new File(srcFileName));
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        tag.getGraphics().drawImage(bi, 0, 0, width, height, null);
        ImageIO.write(tag, "jpg", new File(tagFileName));
    }

    /**
     * 查询被修改的奖品信息
     *
     * @param id 奖品id
     * @return
     * @throws Exception
     */
    public Result queryTrayById(int id) throws Exception {
        TrayVO vo = traydao.findTrayById(id);
        if (vo == null) {
            return Result.newFailure("选择错误，请重试", 0);
        }
        return Result.newSuccess(vo, 1);
    }

    /**
     * 查询一个档数的奖品信息
     *
     * @param trayGrade 转盘档数
     * @return
     */
    public Result queryTurntableByTrayGradet(int trayGrade) throws Exception {
        if (trayGrade == TrayEnum.TRAYGRADE_THREE.getValue() || trayGrade == TrayEnum.TRAYGRADE_SIX.getValue() || trayGrade == TrayEnum.TRAYGRADE_NINE.getValue()) {
            List<TrayVO> list = traydao.findByGrade(trayGrade);
            return Result.newSuccess(list, 1);
        } else {
            return Result.newFailure("档数选择错误，请重试", 0);
        }
    }


    /**
     * 修改奖品信息
     *
     * @param: [trayVO]
     * @return: void
     */
    public Result updatePrize(TrayVO trayVO) throws Exception {
        TrayVO tray = traydao.findTrayById(trayVO.getId());
        BigDecimal totalProbability = traydao.sumProbability(tray);//计算此奖品所在的档数其他奖品的概率之和
        totalProbability = totalProbability.add(trayVO.getProbability());//该奖品所在的档数的所有奖品概率之和
        int temp = totalProbability.compareTo(new BigDecimal(1));//该奖品所在的档数的所有奖品概率之和与1作比较
        if (temp > 0) {//该奖品所在的档数的所有奖品概率之和大于1
            return Result.newFailure("该奖品的设置概率过大 ，该档数奖品概率之和大于100%，请重新设置奖品概率", 2);
        } else {
            traydao.update(trayVO);
            return Result.newSuccess("操作成功", 1);
        }
    }

    /**
     * @param param 奖品id，1000000次随机出现的次数
     * @return trayId
     */
    private Integer getRandomByProbability(List<TrayVO> param) {
        Map<Integer, Double> no2Probability = buildNo2Probbility(param);
        if (no2Probability.isEmpty()) {
            return -1;
        }

        List<Integer> allGoods = new ArrayList<>();
        for (Entry<Integer, Double> entry : no2Probability.entrySet()) {
            double num = entry.getValue();
            Integer no = entry.getKey();
            for (int i = 0; i < num; i++) {//增加奖品出现的次数个奖品id
                allGoods.add(no);
            }
        }
        Collections.shuffle(allGoods);//随机打乱list中的元素
        Random rd = new Random();
        return allGoods.get(rd.nextInt(allGoods.size()));//随机取奖品id
    }

    /**
     * @param param 同档次奖品信息
     * @return 奖品id，1000000次随机出现的次数
     */
    private Map<Integer, Double> buildNo2Probbility(List<TrayVO> param) {
        if (null == param || param.isEmpty()) {
            return new HashMap<>();
        }
        Map<Integer, Double> result = new HashMap<>();
        for (TrayVO trayVO : param) {
            result.put(trayVO.getId(), (trayVO.getProbability().multiply(new BigDecimal(1000000))).doubleValue());
        }
        return result;
    }

}
