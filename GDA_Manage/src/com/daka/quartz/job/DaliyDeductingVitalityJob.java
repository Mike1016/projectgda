package com.daka.quartz.job;

import com.daka.service.activitylog.ActivityLogService;
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

public class DaliyDeductingVitalityJob implements Job {

	private final Logger logger = Logger.getLogger(DaliyDeductingVitalityJob.class);

	private Lock daliyDeductingVitalityLock = new ReentrantLock();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) SpringUtils.getBean("transactionManager");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			daliyDeductingVitalityLock.lock();
			ActivityLogService activitylogservice = SpringUtils.getBean(ActivityLogService.class);
			logger.info("业务日志-----------开始扣除所有激活用户生命力-------");
			activitylogservice.dailDeductingVitality();//每日按等级扣除用户的生命值并且生成日志

			transactionManager.commit(status);
			logger.info("业务日志-----------扣除生命力操作结束-------");
		} catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
			logger.error("业务日志--------------" + e.getCause());
		} finally {
			daliyDeductingVitalityLock.unlock();
		}
	}

}
