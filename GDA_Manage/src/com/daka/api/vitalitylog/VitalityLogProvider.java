package com.daka.api.vitalitylog;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daka.api.base.Result;
import com.daka.service.vitalityLog.VitalityLogService;
@RequestMapping("/app/vitalitylog")
@RestController
public class VitalityLogProvider {
	@Autowired
	private VitalityLogService vitalityLogService;
	
	/**查询用户的不同类型生命值记录
	 * @param request
	 * @param sessionId
	 * @param type 类型: 0-每天扣除 1-转盘 2-团队
	 * @param page 页码数
	 * @return
	 */
	@RequestMapping("/lifeDeduction")
	public Result lifeDeduction(HttpServletRequest request,String sessionId,int type,int page){
		try {
			return vitalityLogService.findByCustomer(request, sessionId, type,page);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("系统错误，请联系管理员", 0);
		}
	}

}
