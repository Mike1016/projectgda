package com.daka.enums;

public enum SelloutTypeEnum
{

	TYPE_WEALTH (1,"财富背包"),
	TYPE_BONUS (2,"奖金背包");

	
	
	private int value;
	private String desc;

	SelloutTypeEnum(int value, String desc){
		this.value = value;
		this.desc = desc;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getDesc() {
		return desc;
	}
}
