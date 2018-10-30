package com.daka.api.activitylog;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daka.api.base.Result;
import com.daka.service.activitylog.ActivityLogService;

@RequestMapping("/app/activitylog")
@RestController
public class ActivityLogProvider {
	@Autowired
	private ActivityLogService als;
	
	/**活动值记录
	 * @param request
	 * @param sessionId
	 * @param type  活动值类型 1-转盘扣除 2-交易获取
	 * @param page 页码数
	 * @return
	 */
	@RequestMapping("/activityRecord")
	public Result activityRecord(HttpServletRequest request,String sessionId,int type,int page){
		try {
			return als.activityRecord(request, sessionId, type, page);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("系统异常，请联系系统管理员", 0);
		}
	}

}
