package com.daka.enums;

public enum CustomerStateEnum
{

	IS_ACTIVE_NO (0,"未激活"),
    IS_ACTIVE_YES (1,"已激活"),
    IS_ACTIVE_BAN (2,"封号");
    
 
	private int value;
	private String desc;

	CustomerStateEnum(int value, String desc)
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
