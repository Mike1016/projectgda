package com.daka.service.vitalityLog;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.daka.api.base.Result;
import com.daka.dao.vitalityLog.IVitalityLogDAO;
import com.daka.entry.CustomerVO;
import com.daka.entry.VitalityLogVO;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.service.activitylog.ActivityLogService;

@Service
public class VitalityLogService {
	@Autowired
	private IVitalityLogDAO vitalitylogdao;
	@Autowired
	private ActivityLogService activitylogservice;
	
	/**增加一条记录
	 * @param vo
	 * @throws Exception
	 */
	public void insert(VitalityLogVO vo) throws Exception{
		vitalitylogdao.insert(vo);
	}
	
	/**查询用户的不同类型生命值记录
	 * @param customerId
	 * @param type 类型: 0-每天扣除 1-转盘 2-团队
	 * @return
	 * @throws Exception
	 */
	public Result findByCustomer(HttpServletRequest request,String sessionId,int type,int page) throws Exception{
		CustomerVO vo=SessionContext.getConstomerInfoBySessionId(request, sessionId);
		Map<String, Object> map=activitylogservice.pagination(page, vo.getId(), type);
		List<VitalityLogVO> list=this.findAll(map);//查询用户的不同类型生命值记录
		if(list.size()==0){//查出来的集合是空
			return Result.newFailure("暂无数据", 0);
		}
		return Result.newSuccess(list, 1);
	}
	
	public List<VitalityLogVO> findAll(Map<String, Object> map) throws Exception{
		return vitalitylogdao.findAll(map);
	}

	
}
