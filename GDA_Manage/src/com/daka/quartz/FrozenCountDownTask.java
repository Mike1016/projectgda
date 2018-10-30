package com.daka.quartz;

import com.daka.dao.customer.ICustomerDAO;
import com.daka.dao.platoon.IPlatoonDAO;
import com.daka.entry.PlatoonVO;
import com.daka.enums.PlatoonStatusEnum;
import com.daka.service.activitylog.ActivityLogService;
import com.daka.service.recommendLog.RecommendLogService;
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
 * 解冻定时任务
 */
public class FrozenCountDownTask implements Job {

	private final Logger logger = Logger.getLogger(ConfirmCountdownTask.class);

	private Lock frozenCountDownLock = new ReentrantLock();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) SpringUtils.getBean("transactionManager");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			frozenCountDownLock.lock();
			logger.info("=================start FrozenCountDownTask execute=====================");

			String orderNo = context.getJobDetail().getKey().getName();
			RecommendLogService recommendLogService = SpringUtils.getBean(RecommendLogService.class);
			IPlatoonDAO platoonDAO = SpringUtils.getBean(IPlatoonDAO.class);
			ICustomerDAO customerDAO = SpringUtils.getBean(ICustomerDAO.class);
			ActivityLogService activityLogService = SpringUtils.getBean(ActivityLogService.class);

			//删除定时任务
			QuartzManager.removeJob(orderNo);

			//解冻
			PlatoonVO platoonVO = platoonDAO.selectPlatoonVOByOrderNo(orderNo); //获取排单信息

			//更新用户财富值
			customerDAO.updateCustomerWealthById(platoonVO.getAccount(), platoonVO.getCustomerId());

			//推荐奖及代数
			recommendLogService.saveOrUpdateRecommend(platoonVO.getCustomerId(), platoonVO.getAccount());

			//增加活动值
			activityLogService.insertPlacingActivity(platoonVO.getCustomerId(), platoonVO.getAccount());

			//更新状态
			platoonVO.setStatus(PlatoonStatusEnum.STATE_FREEZE_NO.getValue());
			platoonDAO.updatePlatoonState(platoonVO);

			transactionManager.commit(status);
			logger.info("=================end FrozenCountDownTask execute=====================");
		} catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
			logger.error("=================== error FrozenCountDownTask =====================");
		} finally {
			frozenCountDownLock.unlock();
		}
	}
}
