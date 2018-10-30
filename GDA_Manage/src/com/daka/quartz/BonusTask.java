package com.daka.quartz;

import com.daka.dao.bonusLog.IBonusLogDAO;
import com.daka.dao.customer.ICustomerDAO;
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
 * 每日利息
 */
public class BonusTask implements Job {

	private final Logger logger = Logger.getLogger(this.getClass());

	private Lock bonusLock = new ReentrantLock();

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) SpringUtils.getBean("transactionManager");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			bonusLock.lock();
			logger.info("=================start BonusTask execute=====================");

			IBonusLogDAO bonusLogDAO = SpringUtils.getBean(IBonusLogDAO.class);
			ICustomerDAO customerDAO = SpringUtils.getBean(ICustomerDAO.class);

			bonusLogDAO.insertBonusLog(); //添加日志
			customerDAO.updateCustomerWealth(); //更新用户每日利息财富值

			transactionManager.commit(status);
			logger.info("=================end BonusTask execute=====================");
		} catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
			logger.error("=================== error BonusTask =====================");
		} finally {
			bonusLock.unlock();
		}
	}
}
