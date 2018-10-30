package com.daka.service.activitylog;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.daka.api.base.Result;
import com.daka.constants.SystemConstant;
import com.daka.dao.activitylog.IActivityLogDAO;
import com.daka.dao.customer.ICustomerDAO;
import com.daka.dao.vitalityLog.IVitalityLogDAO;
import com.daka.entry.ActivityLogVO;
import com.daka.entry.CustomerVO;
import com.daka.enums.ActivityLogEnum;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.service.customer.CustomerService;
import com.daka.service.dictionaries.DictionariesService;
import com.daka.util.DateUtils;

@Service
public class ActivityLogService {
	@Autowired
	private IActivityLogDAO atd;
	@Autowired
	private ICustomerDAO customerdao;
	@Autowired
	private IVitalityLogDAO vitalitylogdao;
	@Autowired
	private DictionariesService dictionariesService;
	@Autowired
	private CustomerService customerservice;
	
	
	private final Logger logger=Logger.getLogger(ActivityLogService.class);
	
	/**用户查询自己的不同类型的活动值记录
	 * @param request
	 * @param sessionId
	 * @param type  1-转盘扣除 2-交易获取
	 * @param page  页码数
	 * @return
	 * @throws Exception
	 */
	public Result activityRecord(HttpServletRequest request,String sessionId,int type,int page) throws Exception{
		CustomerVO vo=SessionContext.getConstomerInfoBySessionId(request, sessionId);
		Map<String, Object> map=this.pagination(page, vo.getId(), type);
		return Result.newSuccess(atd.findBycustomer(map), 1);
	}
	
	/**APP封装分页数据
	 * @param page  页码数
	 * @param customerId  用户
	 * @return
	 */
	public Map<String,Object> paging(int page,int customerId){
		Map<String, Object> map=new HashMap<String, Object>();
		if(page<1){//页码数小于第一页，页码数为第一页
			page=1;
		}
		page=(page-1)*(SystemConstant.APP_PAGE_NUMBER);
		map.put("page", page);
		map.put("customerId", customerId);
		return map;
	}
	
	/**APP封装分页数据
	 * @param page  页码数
	 * @param customerId  用户
	 * @param type 类型
	 * @return
	 */
	public Map<String,Object> pagination(int page,int customerId,int type){
		Map<String, Object> map=new HashMap<String, Object>();
		if(page<1){//页码数小于第一页，页码数为第一页
			page=1;
		}
		page=(page-1)*(SystemConstant.APP_PAGE_NUMBER);
		map.put("page", page);
		map.put("customerId", customerId);
		map.put("type", type);
		return map;
	}
	
	/**每日按等级扣除用户的生命值并且生成日志
	 * 
	 */
	@Transactional(rollbackFor =
		{ Exception.class, RuntimeException.class, Throwable.class }, readOnly = false)
	public void dailDeductingVitality() throws Exception{
		Map<String, Integer> map=new HashMap<String, Integer>();
		map.put("vitality1", Integer.valueOf(dictionariesService.dictionaries.get("M1")));
		map.put("vitality2", Integer.valueOf(dictionariesService.dictionaries.get("M2")));
		map.put("vitality3", Integer.valueOf(dictionariesService.dictionaries.get("M3")));
		map.put("vitality4", Integer.valueOf(dictionariesService.dictionaries.get("M4")));
		map.put("vitality5", Integer.valueOf(dictionariesService.dictionaries.get("M5")));
		map.put("vitality6", Integer.valueOf(dictionariesService.dictionaries.get("M6")));
		map.put("vitality7", Integer.valueOf(dictionariesService.dictionaries.get("M7")));
		map.put("vitality8", Integer.valueOf(dictionariesService.dictionaries.get("M8")));
		map.put("vitality9", Integer.valueOf(dictionariesService.dictionaries.get("M9")));
		map.put("vitality10", Integer.valueOf(dictionariesService.dictionaries.get("M10")));
		vitalitylogdao.insertVitalityLog();//生成相应日志
		logger.info("每日按等级扣除用户的生命值------------------生成相应日志");
		customerdao.updateDaliyLife(map);//每日按等级扣除用户的生命值
		logger.info("每日按等级扣除用户的生命值------------------扣除用户的生命值");
		customerservice.updateLimitingVitailty();//将用户生命力限制在0--100内
		logger.info("每日按等级扣除用户的生命值------------------将用户生命力限制在0--100内");
	}
	
	/**如果排单财富值获取的活动值大于等于一点，获取活动值，并添加记录
	 * @param customerId  用户id
	 * @param account  金额
	 * @throws Exception
	 */
	public void insertPlacingActivity(int customerId,BigDecimal account) throws Exception{
		if((account.compareTo(new BigDecimal(SystemConstant.ACTIVITY_RATIO)))>=0){//如果排单财富值获取的活动值大于等于一点，才获取活动值
			CustomerVO customervo=customerdao.getCustomerById(customerId);
			int activity=customervo.getActivity();//用户的当前活动值
			Integer intAccount=Integer.valueOf(String.valueOf(account));
			int count=intAccount/SystemConstant.ACTIVITY_RATIO;//排单获得的活动值
			activity+=count;
			
			CustomerVO customer=new CustomerVO();
			customer.setId(customerId);
			customer.setActivity(activity);
			customerdao.updateMessage(customer);//修改用户活动值
			logger.info("排单获取活动值------------------修改用户活动值");
			
			ActivityLogVO vo=new ActivityLogVO();
			vo.setCustomerId(customerId);
			vo.setType(ActivityLogEnum.TYPE_TRANSACTION.getValue());
			vo.setAccount(intAccount);
			vo.setCount(count);
			vo.setCreateTime(DateUtils.getCurrentTimeYMDHMS());
			atd.insert(vo);//添加用户获取活动值记录
			logger.info("排单获取活动值------------------添加用户获取活动值记录");
			
		}
		
	}
	
	
}
