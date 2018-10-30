package com.daka.quartz;

import java.util.Date;
import java.util.Map;

import net.sf.json.JSONObject;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import com.daka.constants.SystemConstant;
import com.daka.dao.dynamicTimerTask.IDynamicTimerTaskDAO;
import com.daka.entry.DynamicTimerTaskVO;
import com.daka.enums.DynamicTimerTaskStateEnum;
import com.daka.util.SpringUtils;

/**
 * 定时任务管理类
 * 
 * @author yanhaipeng
 * @date 2018-9-27
 */
@Component
public class QuartzManager
{
	// 创建一个SchedulerFactory工厂实例
	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();

	// 任务组
	private static String JOB_GROUP_NAME = "GDA_JOBGROUP_NAME";

	// 触发器组
	private static String TRIGGER_GROUP_NAME = "GDA_TRIGGERGROUP_NAME";

	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * <p>
	 * 推荐使用
	 * </p>
	 * 
	 * @param jobName
	 *            --任务名(不可重复) 任务名
	 * @param cls
	 *            -- 继承Job类class对象 任务
	 * @param time
	 *            --crons表达式 时间设置，可参考quartz说明文档
	 */
	public static void addJob(String jobName, Class<? extends Job> cls, String time, String taskDesc)
	{
		try
		{
			// 通过SchedulerFactory构建Scheduler对象
			Scheduler sched = gSchedulerFactory.getScheduler();

			// 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
			JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();

			jobDetail.getJobDataMap().put("taskDesc", taskDesc);
			// 创建一个新的TriggerBuilder来规范一个触发器
			// 给触发器起一个名字和组名
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME)
					.withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
			sched.scheduleJob(jobDetail, trigger);
			if (!sched.isShutdown())
			{
				// 启动
				sched.start();
			}

			DynamicTimerTaskVO param = new DynamicTimerTaskVO();
			param.setTaskId(String.valueOf(jobName));
			param.setTaskName(String.valueOf(jobName));
			param.setCronExpression(time);
			param.setTaskJobClass(jobDetail.getJobClass().toString());
			param.setTaskDesc(taskDesc);
			param.setCreateTime(SystemConstant.DATE_FORMAT_2.format(new Date()));
			param.setState(DynamicTimerTaskStateEnum.RUNNING.getValue());
			SpringUtils.getBean(IDynamicTimerTaskDAO.class).saveDynamicTimerTask(param);

		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void initTask(String jobName, Class<? extends Job> cls, String time, String taskDesc, String paramJsonStr)
	{
		try
		{
			// 通过SchedulerFactory构建Scheduler对象
			Scheduler sched = gSchedulerFactory.getScheduler();

			// 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
			JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();

			jobDetail.getJobDataMap().put("taskDesc", taskDesc);
			jobDetail.getJobDataMap().put("paramJsonStr",paramJsonStr);
			// 创建一个新的TriggerBuilder来规范一个触发器
			// 给触发器起一个名字和组名
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME)
					.withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
			sched.scheduleJob(jobDetail, trigger);
			if (!sched.isShutdown())
			{
				// 启动
				sched.start();
			}

		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名 （带参数） *
	 * <p>
	 * 推荐使用
	 * </p>
	 * 
	 * @param jobName
	 *            --定时任务名称(不可重复) 任务名
	 * @param cls
	 *            --继承Job的类class 任务
	 * @param time
	 *            --crons表达式 时间设置，参考quartz说明文档
	 * @param parameter
	 *            -- 传参
	 */
	public static void addJob(String jobName, Class<? extends Job> cls, String time, Map<String, Object> parameter, String taskDesc)
	{
		try
		{
			// 通过SchedulerFactory构建Scheduler对象
			Scheduler sched = gSchedulerFactory.getScheduler();

			// 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
			JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();

			// 传参数
			jobDetail.getJobDataMap().put(SystemConstant.TIGGER_PARAM, parameter);

			// 创建一个新的TriggerBuilder来规范一个触发器
			// 给触发器起一个名字和组名
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME)
					.withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
			sched.scheduleJob(jobDetail, trigger);
			if (!sched.isShutdown())
			{
				// 启动
				sched.start();
			}

			DynamicTimerTaskVO param = new DynamicTimerTaskVO();
			param.setTaskId(String.valueOf(jobName));
			param.setTaskName(String.valueOf(jobName));
			param.setCronExpression(time);
			param.setTaskJobClass(jobDetail.getJobClass().toString());
			param.setTaskDesc(taskDesc);
			param.setCreateTime(SystemConstant.DATE_FORMAT_2.format(new Date()));
			param.setState(DynamicTimerTaskStateEnum.RUNNING.getValue());
			param.setParamJsonStr(JSONObject.fromObject(parameter).toString());
			SpringUtils.getBean(IDynamicTimerTaskDAO.class).saveDynamicTimerTask(param);

		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加一个定时任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 */
	@Deprecated
	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
			Class<? extends Job> jobClass, String time)
	{
		try
		{
			// 任务名，任务组，任务执行类
			Scheduler sched = gSchedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();

			// 触发器
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
					.withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
			sched.scheduleJob(jobDetail, trigger);
			if (!sched.isShutdown())
			{
				// 启动
				sched.start();
			}
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加一个定时任务 （带参数）
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 */
	@Deprecated
	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName,
			Class<? extends Job> jobClass, String time, Map<String, Object> parameter)
	{
		try
		{
			Scheduler sched = gSchedulerFactory.getScheduler();

			// 任务名，任务组，任务执行类
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();

			// 传参数
			jobDetail.getJobDataMap().put(SystemConstant.TIGGER_PARAM, parameter);

			// 触发器
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
					.withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
			sched.scheduleJob(jobDetail, trigger);
			if (!sched.isShutdown())
			{
				// 启动
				sched.start();
			}
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 *            --任务名称 任务名
	 * @param time
	 *            --crons表达式 新的时间设置
	 */
	public static void modifyJobTime(String jobName, String time)
	{
		try
		{
			// 通过SchedulerFactory构建Scheduler对象
			Scheduler sched = gSchedulerFactory.getScheduler();

			// 通过触发器名和组名获取TriggerKey
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);

			// 通过TriggerKey获取CronTrigger
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
			if (trigger == null)
			{
				return;
			}
			String oldTime = trigger.getCronExpression();
			String taskDesc = trigger.getJobDataMap().getString("taskDesc");
			if (!oldTime.equalsIgnoreCase(time))
			{
				// 通过任务名和组名获取JobKey
				JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);

				JobDetail jobDetail = sched.getJobDetail(jobKey);
				Class<? extends Job> objJobClass = jobDetail.getJobClass();
				removeJob(jobName);
				addJob(jobName, objJobClass, time, taskDesc);
			}
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改一个任务的触发时间
	 * 
	 * @param triggerName
	 *            任务名称
	 * @param triggerGroupName
	 *            传过来的任务名称
	 * @param time
	 *            更新后的时间规则
	 */
	@Deprecated
	public static void modifyJobTime(String triggerName, String triggerGroupName, String time)
	{
		try
		{
			// 通过SchedulerFactory构建Scheduler对象
			Scheduler sched = gSchedulerFactory.getScheduler();

			// 通过触发器名和组名获取TriggerKey
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

			// 通过TriggerKey获取CronTrigger
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
			if (trigger == null)
			{
				return;
			}
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(trigger.getCronExpression());
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(time))
			{
				// 重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder)
						.withSchedule(CronScheduleBuilder.cronSchedule(time)).build();

				// 按新的trigger重新设置job执行
				sched.rescheduleJob(triggerKey, trigger);
			}
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 *            任务名称
	 */
	public static void removeJob(String jobName)
	{
		try
		{
			Scheduler sched = gSchedulerFactory.getScheduler();

			// 通过触发器名和组名获取TriggerKey
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);

			// 通过任务名和组名获取JobKey
			JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);

			// 停止触发器
			sched.pauseTrigger(triggerKey);

			// 移除触发器
			sched.unscheduleJob(triggerKey);

			// 删除任务
			sched.deleteJob(jobKey);
			SpringUtils.getBean(IDynamicTimerTaskDAO.class).delDynamicTimerTaskByTaskName(jobName);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 移除一个任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 */
	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName)
	{
		try
		{
			Scheduler sched = gSchedulerFactory.getScheduler();

			// 通过触发器名和组名获取TriggerKey
			TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

			// 通过任务名和组名获取JobKey
			JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);

			// 停止触发器
			sched.pauseTrigger(triggerKey);

			// 移除触发器
			sched.unscheduleJob(triggerKey);

			// 删除任务
			sched.deleteJob(jobKey);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 启动所有定时任务
	 */
	public static void startJobs()
	{
		try
		{
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 关闭所有定时任务
	 */
	public static void shutdownJobs()
	{
		try
		{
			Scheduler sched = gSchedulerFactory.getScheduler();
			if (!sched.isShutdown())
			{
				sched.shutdown();
			}
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 判断定时任务是否存在
	 *
	 * @param jobName
	 *            任务名称
	 */
	public static boolean isExistJob(String jobName)
	{
		try
		{
			DynamicTimerTaskVO taskVO = SpringUtils.getBean(IDynamicTimerTaskDAO.class).queryDynamicTimerTaskByTaskName(jobName);
			if (taskVO == null) {
				return false;
			}
			return true;
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}