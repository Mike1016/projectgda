package com.daka.controller.notice;

import com.daka.api.base.Result;
import com.daka.entry.NoticeVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.service.notice.NoticeService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    /**
     * 跳转页面到list.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toList")
    public Object toList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("notice/list");
        return mv;
    }

    /**
     * 跳转页面到add.jsp
     * @param request
     * @return
     */
    @RequestMapping("/insert")
    public Object insert(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("notice/add");
        return mv;
    }

    /**
     * 分页查询数据
     * @param request
     * @return
     */
    @RequestMapping("/queryData")
    public Object queryData(HttpServletRequest request) {
        PageData pd = new PageData(request);
        Page page = new Page();
        page.setPd(pd);
        List<Map<String,Object>> nList = null;
        JSONObject result = new JSONObject();
        try {
            nList = noticeService.queryDatalistPage(page);
            result.put("status", "200");
            result.put("message", "OK");
            result.put("count", page.getTotalResult());
            result.put("data", nList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result deleteNoticeById(HttpServletRequest request,String id) {
        try {
            noticeService.deleteNoticeById(id);
            return Result.newSuccess("删除成功",1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("删除失败",0);
        }
    }

    /**
     * 跳转页面到review.jsp
     * @param request
     * @return
     */
    @RequestMapping("/to_review")
    public Object toReview(HttpServletRequest request)
    {
        String id = request.getParameter("id");
        ModelAndView mv = new ModelAndView("/notice/review");
        Map<String, Object> map = null;
        try {
            map = noticeService.queryNoticeById(id);
            mv.addObject("result", map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }

    /**
     * 修改
     * @param request
     * @param id
     * @param title
     * @param content
     * @param create_time
     * @return
     */
    @RequestMapping("/updateNotice")
    public Result updateNotice(HttpServletRequest request,String id,String title,String content,String create_time) {
        try {
            noticeService.updateNoticeById(id, title, content, create_time);
            return Result.newSuccess("修改成功",1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("修改失败",0);
        }
    }

    /**
     * 添加
     * @param request
     * @param noticeVO
     * @return
     */
    @RequestMapping("/insertNotice")
    public Result insertNotice(HttpServletRequest request, NoticeVO noticeVO) {
        try {
            noticeService.insertNotice(noticeVO);
            return Result.newSuccess("添加成功",1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("添加失败",0);
        }
    }
}
