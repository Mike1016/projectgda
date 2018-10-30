package com.daka.service.activationLog;

import com.daka.api.base.Result;
import com.daka.constants.SystemConstant;
import com.daka.dao.activationLog.IActivationLogDAO;
import com.daka.dao.customer.ICustomerDAO;
import com.daka.entry.ActivationLogVO;
import com.daka.entry.CustomerVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.entry.dto.IActivationLogDTO;
import com.daka.enums.ActivationLogTypeEnum;
import com.daka.enums.CustomerStateEnum;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.service.customer.CustomerService;
import com.daka.util.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 激活日志 Service
 */
@Service
public class ActivationLogService {
	@Autowired
	private IActivationLogDAO activationLogDAO;
	@Autowired
	private ICustomerDAO customerDAO; //用户
	@Autowired
	private CustomerService customerService;
	/**
	 * 后台批量添加
	 * @param list
	 * @throws Exception
	 */
	public void saveBatch(List<ActivationLogVO> list) throws Exception{
		activationLogDAO.saveBatch(list);
	}

	/**
	 * 分页模糊查询
	 *
	 * @param page
	 * @return
	 */
	public List<PageData> queryActivationLoglistPage(Page page) throws Exception {
		return activationLogDAO.queryActivationLoglistPage(page);
	}
	
	/**激活码明细
	 * @param request
	 * @param sessionId
	 * @param type  
	 * @return
	*/		
	public Result selectActivationSystemInfo(HttpServletRequest request, String sessionId,int type) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
		
		//激活码类型 ：1系统赠送 2激活 3转出 4转入
		map.put("type",type);
		map.put("id", cus.getId());	
		
		List<IActivationLogDTO> activationLogList = activationLogDAO.selectActivationLogInfo(map);
 		if(!(activationLogList == null)) {
 			
			return Result.newSuccess(activationLogList,1);
		}
 		return Result.newFailure("获取失败",0);
	}
	
	/**激活
	 * @param request
	 * @param sessionId
	 * @return
	*/
	public Result insertActivationState(HttpServletRequest request, String sessionId, CustomerVO vo) throws Exception {
		
		CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
		CustomerVO customerVo = customerDAO.queryCustomerById(vo.getId()); 
		
		//判断激活数
		CustomerVO sessionCustomer = customerDAO.queryCustomerById(cus.getId());
		if (sessionCustomer != null && sessionCustomer.getActivationNumber() == SystemConstant.ZERO) {
			return Result.newFailure("激活数不够，不能激活操作", 0);
		}
		
		//被激活用户状态为 0.未激活
		if(customerVo != null && customerVo.getState() == CustomerStateEnum.IS_ACTIVE_NO.getValue()) {
			
			//设置激活状态
			CustomerVO customers = new CustomerVO();
			customers.setId(vo.getId());
			customers.setState(CustomerStateEnum.IS_ACTIVE_YES.getValue());
			customerDAO.updateMessage(customers);
						 
			//设置用户激活数量
			CustomerVO customer = customerDAO.queryCustomerById(cus.getId());
			customer.setActivationNumber(customer.getActivationNumber() - 1);
			customer.setId(cus.getId());
			customerDAO.updateMessage(customer);
			
			//设置激活日志
			ActivationLogVO activationLog = new ActivationLogVO();
			activationLog.setType(ActivationLogTypeEnum.TYPE_ACTIVATION.getValue());
			activationLog.setAccount(1);
			activationLog.setActivator(vo.getId());
			activationLog.setCustomerId(cus.getId());
			activationLog.setCreateTime(DateUtils.getCurrentTimeYMDHMS());
			activationLogDAO.saveActivatorLog(activationLog);
			
			//激活后判断升级
			customerService.updateUpgrade(customerVo.getId());
			
			return Result.newSuccess("操作成功",1);
		}
		return Result.newFailure("操作失败",0);
	}

	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void delectActivationLogById(Integer id) throws Exception{
		activationLogDAO.delectActivationLogById(id);
	}
}
