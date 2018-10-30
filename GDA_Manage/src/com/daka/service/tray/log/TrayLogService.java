package com.daka.service.tray.log;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.daka.api.base.Result;
import com.daka.dao.tray.log.ITrayLogDAO;
import com.daka.entry.CustomerVO;
import com.daka.entry.TrayLogVO;
import com.daka.enums.TrayLogStateEnum;
import com.daka.enums.TrayLogTypeEnum;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.service.activitylog.ActivityLogService;

@Service
public class TrayLogService {
	

	@Autowired 
	private ITrayLogDAO iTrayLogDao; //中奖
	@Autowired
	private ActivityLogService activitylogservice;

	
	
	/** 抽奖所获财富
	 * @param request
	 * @param sessionId
	 * @return 
	 * @throws Exception
	 */
	public Result getTrayWealth(HttpServletRequest request, String sessionId) throws Exception{
		
		CustomerVO  customerVo = SessionContext.getConstomerInfoBySessionId(request, sessionId);
		List<TrayLogVO> trayLogVoList = iTrayLogDao.trayLogList(customerVo.getId());
		List<TrayLogVO> trayLogVoLists = new ArrayList<TrayLogVO>();
		if(!(trayLogVoList == null)){
			for(int i = 0; i < trayLogVoList.size(); i++) {
				TrayLogVO trayLogVo = trayLogVoList.get(i);
				
				//获取奖品类型为 1.财富值 且 状态为 2.已完成
				if(trayLogVo.getType() == TrayLogTypeEnum.TYPE_WEALTH.getValue() && trayLogVo.getState() == TrayLogStateEnum.STATE_YES.getValue()) {
					trayLogVoLists.add(trayLogVo);
				}
			}
			return Result.newSuccess(trayLogVoLists,1);
		}
		return Result.newFailure("获取失败",0);
	}
	
	/**所有用户的抽奖记录
	 * @param request
	 * @param sessionId
	 * @param page 页码数
	 * @return
	 */
	public Result awardRecord() throws Exception{
		return Result.newSuccess(iTrayLogDao.findAll(), 1);//分页显示所有用户的中奖纪录
	}
	
}
