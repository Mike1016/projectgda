package com.daka.enums;

public enum DynamicTimerTaskStateEnum
{

	RUNNING(1, "运行中"), CANCLE(-1, "取消");

	private int value;
	private String desc;

	DynamicTimerTaskStateEnum(int value, String desc)
	{
		this.value = value;
		this.desc = desc;
	}

	public int getValue()
	{
		return this.value;
	}

	public String getDesc()
	{
		return this.desc;
	}

}
