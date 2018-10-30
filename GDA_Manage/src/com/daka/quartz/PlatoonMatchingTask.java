package com.daka.quartz;

import com.daka.dao.platoon.IPlatoonDAO;
import com.daka.entry.PlatoonVO;
import com.daka.service.platoon.PlatoonService;
import com.daka.util.SpringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 匹配定时任务
 */
public class PlatoonMatchingTask implements Job {
	private final Logger logger = Logger.getLogger(this.getClass());

	private Lock platoonMatchingLock = new ReentrantLock();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) SpringUtils.getBean("transactionManager");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			platoonMatchingLock.lock();
			logger.info("=================start PlatoonMatchingTask execute=====================");

			String orderNo = context.getJobDetail().getKey().getName();
			IPlatoonDAO platoonDAO = SpringUtils.getBean(IPlatoonDAO.class);
			PlatoonVO platoonVO = platoonDAO.selectPlatoonVOByOrderNo(orderNo); //获取排单信息
			PlatoonService platoonService = SpringUtils.getBean(PlatoonService.class);

			//匹配
			platoonService.matching(platoonVO.getAccount(), platoonVO.getId(), platoonVO.getCustomerId(), platoonVO.getOrderNo());

			transactionManager.commit(status);
			logger.info("=================end PlatoonMatchingTask execute=====================");
		} catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
			logger.error("=================== error PlatoonMatchingTask =====================");
		} finally {
			platoonMatchingLock.unlock();
		}
	}
}
