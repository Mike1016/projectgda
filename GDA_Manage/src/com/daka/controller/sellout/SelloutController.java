package com.daka.controller.sellout;

import com.daka.api.base.Result;
import com.daka.controller.CommonController;
import com.daka.entry.*;
import com.daka.enums.CustomerStateEnum;
import com.daka.quartz.QuartzManager;
import com.daka.service.platoon.PlatoonService;
import com.daka.service.sellout.SelloutService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/sellout")
public class SelloutController extends CommonController {

    @Autowired
    SelloutService selloutService;

    @Autowired
    PlatoonService platoonService;

    private static Lock lock = new ReentrantLock();

    /**
     * 跳转到list.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toList")
    public Object toList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/sellout/list");
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
        List<PageData> selloutList = null;
        try {
            selloutList = selloutService.querySelloutlistPage(page);
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
     * 交易回溯（卖出）
     * @param request
     * @return
     */
    @RequestMapping("/toFlashBack")
    public Result toFlashBack(HttpServletRequest request ,Integer id){
        try {
            if (null == id){
                return Result.newSuccess("回溯失败",0);
            }
            SelloutVO selloutVO = selloutService.getSelloutVOById(id);
            if (null == selloutVO){
                return Result.newSuccess("回溯失败",0);
            }
            PlatoonVO platoonVO = platoonService.queryPlatoonById(selloutVO.getPlatoonId());
            if (null == platoonVO){
                return Result.newSuccess("回溯失败",0);
            }
            return platoonService.toFlashBack(selloutVO.getPlatoonId(),platoonVO.getState());
        }catch (Exception e){
            e.printStackTrace();
            return Result.newSuccess("回溯失败",0);
        }
    }

    /**
     * 跳转到add.jsp
     * @param request
     * @return
     */
    @RequestMapping("/add")
    public Object toAdd(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/sellout/add");
        return mv;
    }

    /**
     * 发布卖出
     * @param request
     * @return
     */
    @RequestMapping("/save")
    public Object save(HttpServletRequest request, String wealth) {
        try {
            lock.lock();
            SelloutVO selloutVO = (SelloutVO) getParam(request, SelloutVO.class);
            selloutVO.setType(1);
            if (null == selloutVO.getAccount() || "".equals(selloutVO.getAccount())) {
                return Result.newSuccess("请填写卖出金额", 0);
            }
            if (BigDecimal.valueOf(Long.valueOf(wealth)).compareTo(selloutVO.getAccount()) < 0) {
                return Result.newSuccess("财富值不足", 0);
            }
            selloutService.saveSelloutInfo(selloutVO);
            return Result.newSuccess("发布成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("发布失败", 0);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 判断用户是否存在
     * @param request
     * @return
     */
    @RequestMapping("/whetherHaveUser")
    public Object whetherHaveUser(HttpServletRequest request) {
        try {
             String userName = request.getParameter("userName");
             if (null == userName || "".equals(userName)){
                 return Result.newSuccess("请输入用户名",0);
             }
             return selloutService.checkUserNameInfo(userName);
        }catch (Exception e){
            e.printStackTrace();
            return Result.newSuccess("",0);
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
}
