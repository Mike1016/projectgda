package com.daka.api.tray;

import com.daka.api.base.Result;
import com.daka.constants.SystemConstant;
import com.daka.entry.CustomerVO;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.service.tray.TrayService;
import com.daka.service.tray.log.TrayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/app/tray")
@RestController
public class TrayProvider {
	
	@Autowired
	private TrayService trayservice;
	@Autowired
	private TrayLogService traylogservice;
	/**转盘信息
	 * @param request
	 * @param sessionId
	 * @param trayGrade  转盘档数
	 * @return  奖品图片路径，奖品编号
	 */
	@RequestMapping("/turnTable")
	public Result turnTable(HttpServletRequest request,String sessionId,int trayGrade){
		if(trayGrade<SystemConstant.TRAY_GRADE_MIN||trayGrade>SystemConstant.TRAY_GRADE_MAX){//如果抽奖档数在1--3之外
			return Result.newFailure("转盘档数选择错误，请重试", 0);
		}
		try {
			return trayservice.queryTurntableByTrayGradet(trayGrade);//转盘信息
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("系统异常，请联系系统管理员", 0);
		}
	}
	
	/**所有用户的中奖记录
	 * @param request
	 * @param sessionId
	 * @return
	 */
	@RequestMapping("/awardRecord")
	public Result awardRecord(HttpServletRequest request,String sessionId){
		try {
			return traylogservice.awardRecord();
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("系统异常，请联系系统管理员", 0);
		}
	}
	
	
	/**用户抽奖接口
	 * @param request
	 * @param sessionId
	 * @param trayGrade   档数：3/6/9档
	 * @return 奖品信息
	 * @throws Exception
	 */
	@RequestMapping("/luckDraw")
	public Result luckDraw(HttpServletRequest request,String sessionId,int trayGrade){
		CustomerVO vo=SessionContext.getConstomerInfoBySessionId(request, sessionId);
		if(trayGrade<SystemConstant.TRAY_GRADE_MIN||trayGrade>SystemConstant.TRAY_GRADE_MAX){//如果抽奖档数在1--3之外
			return Result.newFailure("转盘档数选择错误，请重试", 0);
		}
		try {
			return trayservice.insertStartLottery(vo.getId(), trayGrade);//用户抽奖并且得到奖品,奖品存入玩家信息中
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("系统异常，请联系系统管理员", 0);
		}
	}
	
	/**抽奖明细
	 * @param request
	 * @param sessionId
	 * @param page 页码数
	 * @return
	 */
	@RequestMapping("/lotteryRecord")
	public Result lotteryRecord(HttpServletRequest request,String sessionId,int page){
		try {
			return trayservice.awardDetail(request, sessionId,page);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("系统异常，请联系系统管理员", 0);
		}
	}

}
