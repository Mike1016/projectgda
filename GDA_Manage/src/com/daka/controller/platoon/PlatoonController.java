package com.daka.controller.platoon;

import com.daka.api.base.Result;
import com.daka.controller.CommonController;
import com.daka.entry.DictionariesVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.entry.SelloutVO;
import com.daka.quartz.QuartzManager;
import com.daka.service.dictionaries.DictionariesService;
import com.daka.service.platoon.PlatoonService;
import com.daka.service.sellout.SelloutService;
import com.daka.util.DateUtil;
import com.daka.util.DateUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/platoon")
public class PlatoonController extends CommonController {

    @Autowired
    PlatoonService platoonService;

    @Autowired
    SelloutService selloutService;

    @Autowired
    DictionariesService dictionariesService;

    private static Lock lock = new ReentrantLock();
    /**
     * 跳转到list.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toList")
    public Object toList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/platoon/list");
        return mv;
    }

    /**
     * 查询
     * @param request
     * @return
     */
    @RequestMapping("/queryData")
    public Object queryData(HttpServletRequest request) {
        PageData pd = new PageData(request);
        Page page = new Page();
        page.setPd(pd);
        List<PageData> platoonList = null;
        try {
            platoonList = platoonService.queryPlatoonlistPage(page);
            JSONObject result = new JSONObject();
            result.put("status", "200");
            result.put("message", "");
            result.put("count", page.getTotalResult());
            result.put("data", platoonList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("",0);
        }
    }

    /**
     * 交易回溯 (排单)
     * @param request
     * @return
     */
    @RequestMapping("/toFlashBack")
    public Result toFlashBack(HttpServletRequest request ,int id ,int state){
        try {
            //交易回溯
            return platoonService.toFlashBack(id,state);
        }catch (Exception e){
            e.printStackTrace();
            return Result.newSuccess("回溯失败",0);
        }
    }

    /**
     * 跳转到matching.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toMatching")
    public Object toMatching(HttpServletRequest request,String account){
        ModelAndView mv = new ModelAndView("/platoon/matching");
        String id = request.getParameter("id");
        Map<String,Object> map = new HashMap<String ,Object>();
        map.put("id",id);
        map.put("paloonAccount",account);
        mv.addObject("result", map);
        return mv;
    }

    /**
     * 可匹配数据查询
     * @param request
     * @return
     */
    @RequestMapping("/querySelloutData")
    public Object querySelloutData(HttpServletRequest request) {
        PageData pd = new PageData(request);
        Page page = new Page();
        page.setPd(pd);
        List<PageData> selloutList = null;
        try {
            selloutList = selloutService.queryStateSelloutlistPage(page);
            JSONObject result = new JSONObject();
            result.put("status", "200");
            result.put("message", "");
            result.put("count", page.getTotalResult());
            result.put("data", selloutList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("",0);
        }
    }

    /**
     * 匹配
     * @param request
     * @return
     */
    @RequestMapping("/matching")
    public Result matching(HttpServletRequest request,String paloonAccount){
        try {
            lock.lock();
            SelloutVO selloutVO = (SelloutVO) getParam(request, SelloutVO.class);
            selloutService.matching(selloutVO,paloonAccount);
            return Result.newSuccess("匹配成功",1);
        }catch (Exception e){
            e.printStackTrace();
            return Result.newSuccess("匹配失败",0);
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * 开启匹配
     * @param request
     * @return
     */
    @RequestMapping("/openMatching")
    public Result openMatching(HttpServletRequest request){
        try {
            QuartzManager.startJobs();
            return Result.newSuccess("开启成功",1);
        }catch (Exception e){
            e.printStackTrace();
            return Result.newSuccess("开启失败",0);
        }
    }

    /**
     * 关闭匹配
     * @param request
     * @return
     */
    @RequestMapping("/closeMatching")
    public Result closeMatching(HttpServletRequest request){
        try {
            QuartzManager.shutdownJobs();
            return Result.newSuccess("关闭成功",1);
        }catch (Exception e){
            e.printStackTrace();
            return Result.newSuccess("关闭失败",0);
        }
    }

    /**
     * 匹配控时
     * @return
     */
    @RequestMapping("/matchingTimeControl")
    public Result matchingTimeControl(String stat,String end){
        try {
            if (null == stat || stat =="" || null == end || end ==""){
                return Result.newSuccess("请选择控时时间段",1);
            }
            Long statTime = DateUtils.getTimestampByTime(DateUtil.getDay()+" "+stat);
            Long endTime = DateUtils.getTimestampByTime(DateUtil.getDay()+" "+end);
            if (endTime - statTime <= 0 ) {
                return Result.newSuccess("每天的结束时间必须大于来时时间",0);
            }
            DictionariesVO vo = new DictionariesVO();
            vo.setType("OPEN_TIME");
            vo.setParameter(stat);
            dictionariesService.updateDictionaries(vo);
            vo.setType("CLOSE_TIME");
            vo.setParameter(end);
            dictionariesService.updateDictionaries(vo);
            return Result.newSuccess("控时开始",1);
        }catch (Exception e){
            e.printStackTrace();
            return Result.newSuccess("控时失败",0);
        }
    }
}
