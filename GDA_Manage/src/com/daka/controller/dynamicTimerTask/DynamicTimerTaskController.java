package com.daka.controller.dynamicTimerTask;

import com.daka.api.base.Result;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.service.dynamicTimerTask.DynamicTimerTaskService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/dynamicTimerTask")
public class DynamicTimerTaskController {
    @Autowired
    DynamicTimerTaskService dynamicTimerTaskService;

    /**
     * 跳转到list.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toList")
    public Object toList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/dynamicTimerTask/list");
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
        List<PageData> dynamicTimerTaskList = null;
        try {
            dynamicTimerTaskList = dynamicTimerTaskService.queryDynamicTimerTasklistPage(page);
            JSONObject result = new JSONObject();
            result.put("status", "200");
            result.put("message", "");
            result.put("count", page.getTotalResult());
            result.put("data", dynamicTimerTaskList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("",0);
        }
    }
}
