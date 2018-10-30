package com.daka.api.notice;

import com.daka.api.base.Result;
import com.daka.entry.NoticeVO;
import com.daka.service.notice.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/app/notice")
@RestController
public class NoticeProvider {

    @Autowired
    private NoticeService noticeService;

    @RequestMapping("/queryNotice")
    public Result queryNotice(HttpServletRequest request,String sessionId) {
        try {
            List<NoticeVO> data = noticeService.queryNotice();
            if (null == data) {
                return Result.newFailure("暂无公告！",0);
            }
            return Result.newSuccess(data,"成功",1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员",0);
        }
    }

}
