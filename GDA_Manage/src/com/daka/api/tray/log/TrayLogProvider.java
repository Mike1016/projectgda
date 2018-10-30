package com.daka.api.tray.log;

import com.daka.api.base.Result;
import com.daka.service.tray.log.TrayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RequestMapping("/app/trayLog")
@RestController
public class TrayLogProvider {
	
	@Autowired private TrayLogService trayLogservice;
	
	
	/** 抽奖所获财富
	 * @param request
	 * @param sessionId
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping("/getTrayWealth")
	public Result getTrayWealth(HttpServletRequest request, String sessionId) throws Exception {
		if(sessionId == null) {
			Result.newFailure("sessionId不能为空");
		}
		try {
			return trayLogservice.getTrayWealth(request, sessionId);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("获取失败",0);
		}
	}

}
