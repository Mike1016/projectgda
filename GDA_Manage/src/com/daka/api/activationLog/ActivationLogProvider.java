package com.daka.api.activationLog;

import com.daka.api.base.Result;
import com.daka.entry.CustomerVO;
import com.daka.service.activationLog.ActivationLogService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/app/activationLog")
public class ActivationLogProvider {

    @Autowired    					 
    private ActivationLogService activationLogService; //激活

    
    
	/**激活码明细
	 * @param request
	 * @param sessionId
	 * @param type  类型：1系统赠送 2激活 3转出 4转入
	 * @return
	*/		
	@RequestMapping("/getActivationInfo")	
	public Result getActivationInfo(HttpServletRequest request, String sessionId, int type) {
		
		if(sessionId == null) {
			return Result.newFailure("sessionId不能为空");
		}
		try {
			return activationLogService.selectActivationSystemInfo(request,sessionId,type);		
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return Result.newFailure("获取失败",0);
	}
	
	/**激活
	 * @param request
	 * @param sessionId
	 * @return
	*/	
	@RequestMapping("/activationState")
	public Result activationState(HttpServletRequest request, String sessionId, CustomerVO vo) {		
		try {
			if(sessionId.equals("") || vo.getId().equals("")) {
				return Result.newFailure("参数不能为空");
			}
			return activationLogService.insertActivationState(request, sessionId ,vo);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("操作失败",0);
		}
	}
}
