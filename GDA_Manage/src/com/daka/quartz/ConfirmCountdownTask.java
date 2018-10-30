package com.daka.quartz;

import com.daka.dao.customer.ICustomerDAO;
import com.daka.dao.platoon.IPlatoonDAO;
import com.daka.dao.sellout.ISelloutDAO;
import com.daka.entry.CustomerVO;
import com.daka.entry.PlatoonVO;
import com.daka.entry.SelloutVO;
import com.daka.enums.CustomerStateEnum;
import com.daka.enums.PlatoonStateEnum;
import com.daka.service.customer.CustomerService;
import com.daka.util.DateUtils;
import com.daka.util.SpringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 24小时定时任务
 */
public class ConfirmCountdownTask implements Job {

	private final Logger logger = Logger.getLogger(ConfirmCountdownTask.class);

	private Lock confirmCountdownLock = new ReentrantLock();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) SpringUtils.getBean("transactionManager");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			confirmCountdownLock.lock();
			logger.info("=================start ConfirmCountdownTask execute=====================");

			String orderNo = context.getJobDetail().getKey().getName();
			IPlatoonDAO platoonDAO = SpringUtils.getBean(IPlatoonDAO.class);
			ISelloutDAO selloutDAO = SpringUtils.getBean(ISelloutDAO.class);
			ICustomerDAO customerDAO = SpringUtils.getBean(ICustomerDAO.class);
			CustomerService customerService = SpringUtils.getBean(CustomerService.class);

			PlatoonVO platoonVO = platoonDAO.selectPlatoonVOByOrderNo(orderNo); //获取排单信息

			//封号
			customerDAO.updateCustomerStateByPlatoonId(platoonVO.getId());
			customerDAO.updateMessage(new CustomerVO(platoonVO.getCustomerId(), CustomerStateEnum.IS_ACTIVE_BAN.getValue()));

			//降级
			customerService.degrade(platoonVO.getCustomerId());
			List<SelloutVO> selloutVOS = selloutDAO.getSelloutVOList(platoonVO.getId());
			for (SelloutVO selloutVO : selloutVOS) {
				customerService.degrade(selloutVO.getCustomerId());
			}

			//修改排单状态为失效
			platoonDAO.updatePlatoonState(new PlatoonVO(platoonVO.getId(), PlatoonStateEnum.LOSE_EFFICACY.getValue(), DateUtils.getCurrentTimeYMDHMS()));

			//删除匹配定时任务
			QuartzManager.removeJob(orderNo);

			transactionManager.commit(status);
			logger.info("=================end ConfirmCountdownTask execute=====================");
		} catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
			logger.error("=================== error ConfirmCountdownTask =====================");
		} finally {
			confirmCountdownLock.unlock();
		}
	}

}
