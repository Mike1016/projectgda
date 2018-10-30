package com.daka.service.customer;


import com.daka.api.base.Result;
import com.daka.constants.SystemConstant;
import com.daka.dao.activationLog.IActivationLogDAO;
import com.daka.dao.customer.ICustomerDAO;
import com.daka.dao.dictionaries.IDictionariesDAO;
import com.daka.dao.platoon.IPlatoonDAO;
import com.daka.dao.sellout.ISelloutDAO;
import com.daka.dao.vitalityLog.IVitalityLogDAO;
import com.daka.entry.*;
import com.daka.entry.dto.CustomerDTO;
import com.daka.enums.*;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.quartz.QuartzManager;
import com.daka.relation.RelationUtil;
import com.daka.service.platoon.PlatoonService;
import com.daka.util.*;
import com.daka.util.session.IdCardComplex;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Service
public class CustomerService {

	@Autowired
	private ICustomerDAO customerDao; //用户
	@Autowired
	private IDictionariesDAO dictionariesDao; //字典
	@Autowired
	IActivationLogDAO activationLogDAO; //激活
	@Autowired
	private IVitalityLogDAO vitalitylogdao;
	@Autowired
	private PlatoonService platoonService;
	@Autowired
	private IPlatoonDAO iPlatoonDAO;
	@Autowired
	private ISelloutDAO iSelloutDAO;

	public static ExpiryMap<String, String> current_users = new ExpiryMap<>(
			1000 * 60 * 30);

	private static Lock lock = new ReentrantLock();

	private Lock degradeLock = new ReentrantLock(); //降级的锁

	private Lock upgradeLock = new ReentrantLock(); //升级的锁
	
	private Lock activationLock = new ReentrantLock(); //激活码转增的锁

	/**
	 * 分页模糊查询
	 *
	 * @param page
	 * @return
	 */
	public List<PageData> queryCustomerlistPage(Page page) throws Exception {
		return customerDao.queryCustomerlistPage(page);
	}

	/**
	 * 完善个人信息
	 *
	 * @param customerVO
	 */
	@Transactional(rollbackFor = {Exception.class, RuntimeException.class, Throwable.class})
	public Result updateMessage(CustomerVO customerVO) throws Exception {
		if (customerVO.getIdentityCard() != null) {
			boolean idCardIsLegal = IdCardComplex.is18ByteIdCardComplex(customerVO.getIdentityCard()); //判断身份证是否合法
			if (!idCardIsLegal) {
				return Result.newFailure("身份证不合法", 0);
			}
		}
		customerDao.updateMessage(customerVO);
		List<PlatoonVO> platoonVOList = iPlatoonDAO.getPlatoonByCustomerId(customerVO.getId());
		if (null == platoonVOList || platoonVOList.size() == 0) {
			boolean perfectPersonalInfo = platoonService.perfectPersonalInfo(customerVO.getId());
			if (perfectPersonalInfo) {
				platoonService.savePlatoon(customerVO.getId(), new BigDecimal(SystemEnum.PLATOONAUTOMATIC.getDictionarieValue()));
			}
		}
		return Result.newSuccess("修改成功！", 1);
	}

	/**
	 * 查询原始密码
	 *
	 * @param id 用户id
	 * @return
	 */
	public CustomerVO queryCustomerById(Integer id) throws Exception {
		return customerDao.queryCustomerById(id);
	}

	/**
	 * 删除
	 *
	 * @param id
	 */
	public int deleteById(Integer id) throws Exception {
		return customerDao.deleteById(id);
	}

	/**
	 * 根据id查询
	 *
	 * @param id
	 * @return
	 */
	public CustomerVO queryById(Integer id) throws Exception {
		return customerDao.queryById(id);
	}

	/**
	 * 根据传入的type做添加或修改
	 *
	 * @param customerVO
	 * @param type       （add  添加  unseal 解封  title 封号  activation 激活）
	 * @throws Exception
	 */
	public void saveOrUpadteByType(CustomerVO customerVO, String type) throws Exception {
		if (null == customerVO) {
			throw new Exception("未接收到数据");
		}
		if (CustomerCustomEnum.ADD.getValue().equals(type)) {
			if (null == customerVO.getActivationNumber()){
				customerVO.setActivationNumber(0);
			}
			if (customerVO.getActivationNumber() >= 100){
				customerVO.setActivationNumber(100);
			}
			//添加
			CustomerVO vo = customerDao.checkUserNameInfo(customerVO.getUserName());
			if (vo != null){
				throw new Exception("用户已存在");
			}
			customerVO.setUserType(CustomerUserTypeEnum.SYSTEM_USER.getValue());
			customerVO.setExtensionCode(CodeUtil.getRandomChar(6));
			customerVO.setLevel(CustomerLevelEnum.VIP_LEVEL_M1.getValue());
			customerVO.setRegesterTime(DateUtil.getTime());
			if (customerVO.getPassword() != null && customerVO.getPassword() != "") {
				customerVO.setPassword(MD5.md5(customerVO.getPassword()));
			}
			if (customerVO.getSecurityPassword() != null && customerVO.getSecurityPassword() != "") {
				customerVO.setSecurityPassword(MD5.md5(customerVO.getSecurityPassword()));
			}
			customerVO.setRealName(customerVO.getUserName());
			customerVO.setAgentId(-1);
		} else if (CustomerCustomEnum.UNSEAL.getValue().equals(type)) {
			//1.解封
			customerVO.setState(CustomerStateEnum.IS_ACTIVE_NO.getValue());
			//2.升级
			updateUpgrade(customerVO.getId());
		} else if (CustomerCustomEnum.TITLE.getValue().equals(type)) {
			//1.封号
			customerVO.setState(CustomerStateEnum.IS_ACTIVE_BAN.getValue());
			//2.降级
			degrade(customerVO.getId());
			//3.让排单失效
			List<PlatoonVO> platoonVOS = iPlatoonDAO.queryUnexpiredOrder(customerVO.getId());
			if (platoonVOS.size() > 0) {
				iPlatoonDAO.updateBatch(platoonVOS);
				StringBuffer stringBuffer = new StringBuffer();
				for (PlatoonVO platoonVO : platoonVOS) {
					//删除排单定时任务
					if (QuartzManager.isExistJob(platoonVO.getOrderNo())){
						QuartzManager.removeJob(platoonVO.getOrderNo());
					}
					if (platoonVO.getId() != null){
						stringBuffer.append(platoonVO.getId() + ",");
					}
				}
				String ids;
				if (stringBuffer.length() > 0){
					ids = stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
				}else{
					ids = "-1";
				}
				//查询排单下的子订单
				List<SelloutVO> selloutVOS = iSelloutDAO.querySelloutByPlatoonIds(ids);
				//删除卖出订单定时任务
				for (SelloutVO selloutVO : selloutVOS) {
					if (QuartzManager.isExistJob(selloutVO.getOrderNo())){
						QuartzManager.removeJob(selloutVO.getOrderNo());
					}
				}
			}

		} else if (CustomerCustomEnum.ACTIVATION.getValue().equals(type)) {
			//激活
			customerVO.setState(CustomerStateEnum.IS_ACTIVE_YES.getValue());
		} else {
			//修改
			if (customerVO.getPassword() != null && customerVO.getPassword() != "") {
				customerVO.setPassword(MD5.md5(customerVO.getPassword()));
			}
			if (customerVO.getSecurityPassword() != null && customerVO.getSecurityPassword() != "") {
				customerVO.setSecurityPassword(MD5.md5(customerVO.getSecurityPassword()));
			}
		}
		customerDao.updateMessage(customerVO);
	}

	/**
	 * 批量、单个更新用户的激活数、生命力
	 *
	 * @param customerVO
	 * @throws Exception
	 */
	@Transactional(rollbackFor = {Exception.class, RuntimeException.class, Throwable.class})
	public void batchUpdate(CustomerVO customerVO) throws Exception {
		if (null == customerVO) {
			throw new Exception("没有数据");
		}
		//激活数为空
		if (customerVO.getActivationNumber() == null) {
			customerVO.setActivationNumber(0);
		}
		//生命力为空
		if (customerVO.getVitality() == null) {
			customerVO.setVitality(0);
		}
		if (customerVO.getVitality() < 0 || customerVO.getVitality() > SystemConstant.VITALITY_UPPER_LIMIT || customerVO.getActivationNumber() < 0) {
			throw new Exception("数字超过规定的范围");
		}
		//查询所有用户
		List<CustomerVO> list = queryAll(customerVO);
		List<CustomerVO> newList = new ArrayList<CustomerVO>();
		if (null == list || list.size() == 0) {
			throw new Exception("没有已激活的用户");
		}
		//对用户的激活数、生命力做修改
		for (int i = 0; i < list.size(); i++) {
			int activationNumber = list.get(i).getActivationNumber() + customerVO.getActivationNumber();
			int vitality = list.get(i).getVitality() + customerVO.getVitality();
			if (vitality > SystemConstant.VITALITY_UPPER_LIMIT) {
				vitality = SystemConstant.VITALITY_UPPER_LIMIT;
			}
			CustomerVO customer = new CustomerVO();
			customer.setId(list.get(i).getId());
			customer.setActivationNumber(activationNumber);
			customer.setVitality(vitality);
			//保存修改之后的数据
			newList.add(customer);
		}
		//批量更新
		customerDao.updateBatch(newList);
		//保存赠送激活数的日志
		if (customerVO.getActivationNumber() > 0) {
			saveActivationLog(newList, customerVO.getActivationNumber());
		}
		if (customerVO.getVitality() > 0) {//后台进行了添加生命力操作，   添加生命力变化日志
			insertvitalitylogBath(newList, customerVO.getVitality());
		}

	}

	/**
	 * 查询所有用户
	 *
	 * @return
	 * @throws Exception
	 */
	public List<CustomerVO> queryAll(CustomerVO customerVO) throws Exception {
		return customerDao.queryAll(customerVO);
	}

	/**
	 * 限制用户的生命在0--100之间
	 *
	 * @throws Exception
	 */
	public void updateLimitingVitailty() throws Exception {
		customerDao.updateVitalityLower();
		customerDao.updateVitalityUpper();
	}

	/**
	 * 注册
	 */
	public Result insertRegister(CustomerVO vo) throws Exception {

		lock.lock();

		try {
			//获取注册上线人数参数
			int register = Integer.valueOf(SystemEnum.REGISTER.getDictionarieValue());
			
			//获取今日注册人数
			int count = customerDao.selectIdCount();

			//注册上限
			if (count >= register) {
				return Result.newFailure("今日注册人数已达到上限");
			}

			//用户名唯一
			CustomerVO customerVO = customerDao.checkUserNameInfo(vo.getUserName());
			if (!(customerVO == null)) {
				return Result.newFailure("用户名已重复");
			}

			//手机号注册几个用户
			DictionariesVO dictionaries = dictionariesDao.selectParameter(SystemConstant.DICTIONARIES_PHONE);
			int countPhone = customerDao.getUserNameCount(vo.getPhone());
			if (countPhone >= Integer.parseInt(dictionaries.getParameter())) {
				return Result.newFailure("手机号注册用户不能超过三个");
			}

			CustomerVO customerVo = new CustomerVO();

			//根据二维码设置父id
			if(vo.getExtensionCode() == null) {
				customerVo.setAgentId(-1);
			}
			
			CustomerVO parentCustomer = customerDao.getExtensionCodInfo(vo.getExtensionCode());
			if (!(parentCustomer == null)) {
				customerVo.setAgentId(parentCustomer.getId());
			}

			//设置注册信息
			customerVo.setUserName(vo.getUserName());
			customerVo.setLevel(CustomerLevelEnum.VIP_LEVEL_M1.getValue());
			customerVo.setPhone(vo.getPhone());
			customerVo.setPassword(MD5.md5(String.valueOf(vo.getPassword())));
			customerVo.setExtensionCode(CodeUtil.getRandomChar(6));
			customerVo.setRegesterTime(DateUtils.getCurrentTimeYMDHMS());

			customerDao.saveRegister(customerVo);

			String pId = null == parentCustomer ? "-1" : null == parentCustomer.getId() ? "-1" : String.valueOf(parentCustomer.getId());
			RelationUtil.updateTreeRelation(String.valueOf(customerVo.getId()), pId); //更新树形结构
		} catch (Exception e) {
			e.printStackTrace();
			lock.unlock();
		} finally {
			lock.unlock();
		}

		return Result.newSuccess("注册成功", 1);
	}

	/**
	 * 登录
	 *
	 * @param
	 * @return
	 */
	public Result login(HttpServletRequest request, CustomerVO vo) throws Exception {
		//返回信息
		String msg = "";

		//判断激活状态（封号）	
		CustomerVO customer = customerDao.checkUserNameInfo(vo.getUserName());		
		if(customer != null) {
			if (customer.getState() == CustomerStateEnum.IS_ACTIVE_BAN.getValue()) {
				return Result.newFailure("此用户已封号，不能登录", 0);
			}
		}

		CustomerVO customerVo = new CustomerVO();
		customerVo.setUserName(vo.getUserName());
		customerVo.setPassword(MD5.md5(vo.getPassword()));

		//校验用户信息
		CustomerVO customerLogin = customerDao.login(customerVo);
		if (customerLogin == null) {
			return Result.newFailure("请重新输入用户名或者密码", 0);
		}

		//判断激活状态（未激活）
		if (customerLogin.getState() == CustomerStateEnum.IS_ACTIVE_NO.getValue()) {
			msg = "请联系直属领导激活账户";
		}
		if (customerLogin.getState() == CustomerStateEnum.IS_ACTIVE_YES.getValue()) {
			msg = "登录成功";
		}

		//返回sessiondId
		final String sessionId = SessionContext.createSession(request, SystemConstant.APP_USER, customerLogin);
		current_users.put(vo.getUserName(), vo.getUserName());
		return Result.newSuccess(new HashMap<String, String>() {
			{
				put("sessionId", sessionId);
			}
		}, msg);
	}

	/**
	 * 忘记密码
	 *
	 * @return
	 */
	public Result retrievePass(CustomerVO vo) throws Exception {

		//用户名唯一
		CustomerVO customerUserName = customerDao.checkUserNameInfo(vo.getUserName());
		if (customerUserName.getUserName() == null) {
			return Result.newFailure("用户名不存在");
		}

		//设置找回密码信息
		CustomerVO customerVo = new CustomerVO();
		customerVo.setId(customerUserName.getId());
		customerVo.setUserName(vo.getUserName());
		customerVo.setPassword(MD5.md5(vo.getPassword()));
		customerDao.updateMessage(customerVo);

		return Result.newSuccess("修改密码成功", 1);
	}

	/**
	 * 保存赠送激活数的日志信息
	 *
	 * @param list
	 * @throws Exception
	 */
	public void saveActivationLog(List<CustomerVO> list, int activationNumber) throws Exception {
		List<ActivationLogVO> newList = new ArrayList<ActivationLogVO>();
		for (CustomerVO vo : list) {
			ActivationLogVO activationLogVO = new ActivationLogVO();
			activationLogVO.setType(ActivationLogTypeEnum.TYPE_SYSTEM.getValue());
			activationLogVO.setAccount(activationNumber);
			activationLogVO.setCustomerId(vo.getId());
			activationLogVO.setCreateTime(DateUtil.getTime());
			newList.add(activationLogVO);
		}
		activationLogDAO.saveBatch(newList);
	}

	/**
	 * 根据改变生命力的用户来添加生命力日志
	 *
	 * @param list
	 * @param vitality 后台管理统一增加的生命力数值
	 * @throws Exception
	 */
	public void insertvitalitylogBath(List<CustomerVO> list, int vitality) throws Exception {
		List<VitalityLogVO> vitalityloglist = new ArrayList<VitalityLogVO>();//生命力日志
		for (CustomerVO customerVO : list) {
			VitalityLogVO vitalitylog = new VitalityLogVO();
			vitalitylog.setCustomerId(customerVO.getId());
			vitalitylog.setCreateTime(DateUtils.getCurrentTimeYMDHMS());
			vitalitylog.setType(VitalityLogEnum.TYPE_BACKSTAGE.getValue());
			if ((customerVO.getVitality() + vitality) > SystemConstant.VITALITY_UPPER_LIMIT) {//增加的生命力超过上限，增加的记录为上限减掉用户当前的生命力
				vitalitylog.setVitality(SystemConstant.VITALITY_UPPER_LIMIT - customerVO.getVitality());
			} else {
				vitalitylog.setVitality(vitality);
			}
			vitalityloglist.add(vitalitylog);
		}
		vitalitylogdao.insertBatch(vitalityloglist);
	}

	/**
	 * 团队中心
	 *
	 * @param customerId 用户ID
	 * @return CustomerDTO
	 * @throws Exception 异常
	 */
	public CustomerDTO getTeamCenter(Integer customerId) throws Exception {
		CustomerDTO customerDTO = null;
		CustomerVO customerVO = customerDao.queryCustomerById(customerId); //用户
		if (null != customerVO) {
			customerDTO = new CustomerDTO();
			List<String> teamList = RelationUtil.findDownTeam(String.valueOf(customerId));
			if (null != teamList) {
				String teamIds = StringUtils.join(teamList, ",")
						.replaceAll("#.*?(?=,)", "")
						.replaceAll("#[\\d]", ""); //将集合团队转换字符串id结果：(1,2,3,4,5)
				List<CustomerVO> effectiveTeamList =
						customerDao.selectEffectiveTeamList(Tools.getChilds(teamIds, "id")); //有效团队
				customerDTO.setTeamCount(null == effectiveTeamList ? 0 : effectiveTeamList.size()); //团队人数
			}
			customerDTO.setPushCount(customerDao.selectPushCount(customerId)); //直推人数
			customerDTO.setLevel(customerVO.getLevel()); //等级
			customerDTO.setActivationCount(customerVO.getActivationNumber()); //激活数
		}
		return customerDTO;
	}

	/**
	 * 激活码转增
	 *
	 * @param request   请求信息
	 * @param sessionId 登录用户
	 * @return JSON
	 */
	public Result savaActivation(HttpServletRequest request, String sessionId, CustomerVO vo, int account) throws Exception {		
		
	try {
		    activationLock.lock();
		
			boolean result = false;
			
			CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
			CustomerVO customer = customerDao.checkUserNameInfo(vo.getUserName()); 
			String sessionIds = String.valueOf(cus.getId());
	
			//判断转增是否为自己
			if (cus.getId().equals(customer.getId())) {
				return Result.newFailure("不能给自己转增", 0);
			}
	 
			//判断激活数
			CustomerVO sessionCustomer = customerDao.queryCustomerById(cus.getId());
			if (sessionCustomer != null && sessionCustomer.getActivationNumber() == SystemConstant.ZERO || sessionCustomer.getActivationNumber() <= account) {
				return Result.newFailure("激活数不够", 0);
			}
			
			//判断用户信息
			CustomerVO customerVoInfo = customerDao.queryCustomerById(customer.getId());
			if (customerVoInfo != null) {
				//判断用户名封号
				if (customerVoInfo.getState() == CustomerStateEnum.IS_ACTIVE_BAN.getValue()) {
					return Result.newFailure("此用户已封号，不能转增", 0);
				}
				//判断安全密码  
				CustomerVO customerVo = customerDao.queryCustomerById(cus.getId());
				if (MD5.md5(customerVo.getSecurityPassword()).equals(MD5.md5(vo.getSecurityPassword()))) {
	
					return Result.newFailure("安全密码错误", 0);
				}
			}
			
			//查询三代以内父id 
			String agentId = customerDao.selectAgentIdListById(customer.getId());
			String[] agentIds = agentId.split(",");
			
			//判断三代以内
			if (agentIds != null && agentIds.length >= SystemConstant.CUSTOMER_AGENTID_NUM) {
				for (int i = 2; i <= SystemConstant.CUSTOMER_AGENTID_NUM; i++) { 
					if ((agentIds[i]).equals(sessionIds)) {	
						  result = true;
						  break;
					}				
				}
				 	
			} else {
				for (int i = 2; i <= agentIds.length; i++) { 
					if ((agentIds[i]).equals(sessionIds)) {	
						  result = true;
						  break;
					}
				}
			}
			
			if(result == false) {
				return Result.newFailure("此用户不属于三代以内，不能转增", 0);	
			}

			//设置转出信息（激活日志）
			ActivationLogVO activationLog = new ActivationLogVO();
			activationLog.setType(ActivationLogTypeEnum.TYPE_ROLL_OUT.getValue());
			activationLog.setAccount(account);
			activationLog.setCustomerId(cus.getId());
			activationLog.setActivator(customer.getId());
			activationLog.setCreateTime(DateUtils.getCurrentTimeYMDHMS());
			activationLogDAO.saveActivatorLog(activationLog);
	
			//设置转出信息（用户） 
			sessionCustomer.setId(cus.getId());
			sessionCustomer.setActivationNumber(sessionCustomer.getActivationNumber() - account);
			customerDao.updateMessage(sessionCustomer);
	
			//设置转入信息（激活日志）
			activationLog.setType(ActivationLogTypeEnum.TYPE_SWITCH.getValue());
			activationLog.setAccount(account);
			activationLog.setCustomerId(customer.getId());
			activationLog.setActivator(cus.getId());
			activationLog.setCreateTime(DateUtils.getCurrentTimeYMDHMS());
			activationLogDAO.saveActivatorLog(activationLog);
	
			//设置转入信息（用户）
			customer.setId(customer.getId());
			customer.setActivationNumber(customerVoInfo.getActivationNumber() + account);
			customerDao.updateMessage(customer);

		
		} catch (Exception e) {
			e.printStackTrace();
			activationLock.unlock();
		} finally {
			activationLock.unlock();
		}
		
		return Result.newSuccess("转增成功", 1);
	}

	/**
	 * 直推人数
	 *
	 * @param request   请求信息
	 * @param sessionId 登录用户
	 * @return JSON
	 */
	public Result agentNum(HttpServletRequest request, String sessionId) throws Exception {
		CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
		List<CustomerVO> customerList = customerDao.selectAgentIdList(cus.getId());

		return Result.newSuccess(customerList, 1);
	}

	/**
	 * 降级
	 *
	 * @param customerId
	 * @throws Exception
	 */
	@Transactional(rollbackFor =
			{Exception.class, RuntimeException.class, Throwable.class}, readOnly = false)
	public void degrade(Integer customerId) throws Exception {

		degradeLock.lock();
		try {
			int recursionNum = 0;
			// 1.通过用户id查找父级所有的id
			String agentIds = customerDao.selectAgentIdListById(customerId);
			// 2.截取此用户的直属上级id
			String[] agent = agentIds.split(",");
			if (agent.length < 3) { //直属上级没有直属上级
				return;
			}
			String agentId = agent[2];
			if ("-1".equals(agentId)) { //查询到上级id为-1,则
				return;
			}
			// 3.通过此父级的id查询此父级的直属下级的级别是否满足条件
			List<String> levelList = customerDao.queryLevelByAgentId(Integer.valueOf(agentId));
			CustomerVO customerVO = customerDao.queryCustomerById(Integer.valueOf(agentId));
			String level = customerVO.getLevel();
			String temp = getChildLevel(level);
			if ("".equals(temp)) { //如果上级是M1，则不用降级
				return;
			}
			int childCount = 0;
			for (int i = 0; i < levelList.size(); i++) {
				if (temp.equals(levelList.get(i))) {
					childCount += 1;
				}
			}
			// 4.如果满足条件，则级别保持不变，到此结束
			if (childCount >= 3) {
				return;
			} else {
				// 5.如果不满足条件了，则降级，继续向上找降级的这个用户的直属上级
				customerVO.setLevel(temp);
				customerDao.updateMessage(customerVO);
				recursionNum = 1;
			}
			// 6.降级的这个用户的直属上级的直属下级的级别是否满足条件
			// 7.如果满足条件，则级别保持不变，到此结束
			// 8.···
			if (recursionNum == 1) {
				degrade(customerVO.getId());
			} else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			degradeLock.unlock();
		}


	}

	/**
	 * 通过当前的用户等级得到低一级的等级
	 *
	 * @param level
	 * @return
	 */
	private String getChildLevel(String level) {
		String[] levelLists = {"M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10"};
		for (int i = 0; i < levelLists.length; i++) {
			if (level.equals(levelLists[i])) {
				if (i == 0) {
					return "";
				}
				return levelLists[i - 1];
			}
		}
		return "";
	}


	/**
	 * 通过当前的用户等级得到高一级的等级
	 *
	 * @param level
	 * @return
	 */
	private static String getHighLevel(String level) {
		String[] levelLists = {"M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10"};
		for (int i = 0; i < levelLists.length; i++) {
			if (level.equals(levelLists[i])) {
				if (i == 0) {
					return "";
				}
				return levelLists[i + 1];
			}
		}
		return "";
	}

	/**
	 * 升级
	 *
	 * @param customerId
	 * @throws Exception
	 */
	@Transactional(rollbackFor =
			{Exception.class, RuntimeException.class, Throwable.class}, readOnly = false)
	public void updateUpgrade(int customerId) throws Exception {

		upgradeLock.lock();

		try {
			// 1.排除封号
			CustomerVO customerType = customerDao.getCustomerById(customerId);
			if (customerType != null && customerType.getState() == CustomerStateEnum.IS_ACTIVE_BAN.getValue()) {
				return;
			}

			// 2.注册   1)判空
			String agentIdNums = customerDao.selectAgentIdListById(customerId);
			String[] agentIds = agentIdNums.split(",");
			if (agentIds.length < 3) {
				return;
			}

			// 2)排除上级id为-1
			String agentId = agentIds[2];
			if ("-1".equals(agentId)) {
				return;
			}

			// 3).通过此父级的id查询此父级的直属下级的级别是否满足条件
			List<String> levelList = customerDao.queryLevelByAgentId(Integer.valueOf(agentId)); //直属子集的集合
			
			CustomerVO customerVO = customerDao.queryCustomerById(Integer.valueOf(agentId));
			String level = getChildLevel(customerVO.getLevel()); 
			if ("".equals(level)) {
				level = "M1";
			}
			if (levelList.size() < 3) {
				return;
			}
			int childCount = 0;
			for (String childLevel : levelList) {
				if (childLevel.equals(level) || childLevel.compareTo(customerVO.getLevel()) == 0) {
					childCount += 1;
				}
			}
			if (childCount >= 3) {
				String highLevel = getHighLevel(customerVO.getLevel());
				if ("".equals(highLevel)) {
					highLevel = "M2";
				}
				customerVO.setLevel(highLevel);
				customerVO.setId(customerVO.getId());
				customerDao.updateMessage(customerVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			upgradeLock.unlock();
		}

	}

	/**
	 * 财务中心的所有金额显示
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Result financialCenter(Integer id) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		BigDecimal selloutAccount = iSelloutDAO.getSelloutSum(id);
		BigDecimal platoonAccount = iPlatoonDAO.getPlatoonSum(id);
		CustomerVO customerVO = queryCustomerById(id);
		resultMap.put("selloutAccount", selloutAccount);
		resultMap.put("platoonAccount", platoonAccount);
		resultMap.put("wealth", customerVO.getWealth());
		resultMap.put("bonus", customerVO.getBonus());
		return Result.newSuccess(resultMap, "成功", 1);
	}

	/**
	 * 修改用户头像
	 * @param customerVO
	 * @throws Exception
	 */
	public void updatePersonalImage(CustomerVO customerVO) throws Exception{
		customerDao.updateMessage(customerVO);
	}
}
