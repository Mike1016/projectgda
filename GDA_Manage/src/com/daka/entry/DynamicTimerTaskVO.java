package com.daka.entry;

import java.io.Serializable;

public class DynamicTimerTaskVO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6383990730621780427L;

	/**
	 * ID
	 */
	private int id;

	/**
	 * 任务ID
	 */
	private String taskId = "";

	/**
	 * 任务描述
	 */
	private String taskDesc;

	/**
	 * 表达式
	 */
	private String cronExpression;

	/**
	 * 状态
	 */
	private int state;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 处理类
	 */
	private String taskJobClass;

	/**
	 * 
	 */
	private int status;

	private String paramJsonStr;

	public String getParamJsonStr() {
		return null == paramJsonStr ? "" : paramJsonStr;
	}

	public void setParamJsonStr(String paramJsonStr) {
		this.paramJsonStr = paramJsonStr;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}

	public String getTaskDesc()
	{
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc)
	{
		this.taskDesc = taskDesc;
	}

	public String getCronExpression()
	{
		return cronExpression;
	}

	public void setCronExpression(String cronExpression)
	{
		this.cronExpression = cronExpression;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	public String getTaskJobClass()
	{
		return taskJobClass;
	}

	public void setTaskJobClass(String taskJobClass)
	{
		this.taskJobClass = taskJobClass;
	}

}
