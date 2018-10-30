package com.daka.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.daka.constants.SystemConstant;

public class TaskJob implements Job
{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
//		System.out.println("任务ID" + context.getFireInstanceId());
//		System.out.println("下次执行时间：" + SystemConstant.DATE_FORMAT_2.format(context.getNextFireTime()));
//		System.out.println("=========Start========");
//		try
//		{
//			Thread.sleep(1000);
//		} catch (InterruptedException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("=========End========");
//		System.out.println("");

	}

	// public static void main(String[] args)
	// {
	// QuartzManager manage = new QuartzManager();
	// manage.addJob("测试任务", TaskJob.class, "*/5 * * * * ?", "");
	// for (int i = 0; i < 3; i++)
	// {
	// try
	// {
	// Thread.sleep(5000);
	// if (i == 2)
	// {
	// System.out.println("=====修改==========");
	// manage.modifyJobTime("测试任务", "*/1 * * * * ?");
	// }
	// } catch (InterruptedException e)
	// {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }

}
