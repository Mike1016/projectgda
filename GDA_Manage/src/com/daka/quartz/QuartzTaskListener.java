package com.daka.quartz;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.daka.dao.customer.ICustomerDAO;
import com.daka.dao.dynamicTimerTask.IDynamicTimerTaskDAO;
import com.daka.entry.DynamicTimerTaskVO;
import com.daka.relation.RelationUtil;
import com.daka.util.SpringUtils;

@Component
public class QuartzTaskListener implements ApplicationListener<ContextRefreshedEvent>
{
	Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0)
	{
		if (arg0.getApplicationContext().getParent() == null)
		{
			try
			{
				// 启动定时任务
				startupQueueTask();

				// 构建用户树形关系
				initCustomerRelation();
			} catch (Exception e)
			{
				logger.error("=============初始化用户树形关系或者启动定时任务Error================");
				e.printStackTrace();
			}

		}
	}

	private void initCustomerRelation()
	{
		ICustomerDAO dao = SpringUtils.getBean(ICustomerDAO.class);
		List<Map<String, String>> allCustomer = dao.queryAllCustomerWithRelation();
		if (null == allCustomer)
		{
			return;
		}
		RelationUtil.initTree(allCustomer);
	}

	private void startupQueueTask()
	{
		IDynamicTimerTaskDAO dao = SpringUtils.getBean(IDynamicTimerTaskDAO.class);
		List<DynamicTimerTaskVO> queryDynamicTimerTask = dao.queryDynamicTimerTask();
		Class<? extends org.quartz.Job> jobSubClass = null;

		for (DynamicTimerTaskVO tem : queryDynamicTimerTask)
		{
			try
			{
				String classPath = tem.getTaskJobClass().split(" ")[1];
				jobSubClass = Class.forName(classPath).asSubclass(org.quartz.Job.class);
			} catch (ClassNotFoundException e)
			{
				logger.error(
						"任务名为：【" + tem.getTaskName() + "】，表达式为【" + tem.getCronExpression() + "】的定时任务启动时发生Job处理类初始化异常");
				e.printStackTrace();
			}
			try
			{
				QuartzManager.initTask(tem.getTaskName(), jobSubClass, tem.getCronExpression(), tem.getTaskDesc(), null == tem.getParamJsonStr() ? "" : tem.getParamJsonStr());
				logger.error("任务名为：【" + tem.getTaskName() + "】，表达式为【" + tem.getCronExpression() + "】的定时启动成功");
			} catch (Exception e)
			{
				e.printStackTrace();
				logger.error("任务名为：【" + tem.getTaskName() + "】，表达式为【" + tem.getCronExpression() + "】的定时任务启动失败");
			}
		}
		// queryDynamicTimerTask.forEach((tem) -> {
		// });
	}

}
