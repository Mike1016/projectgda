package com.daka.enums;

public enum TrayLogStateEnum
{

	STATE_NO (1,"代发货"),
	STATE_YES (2,"已完成");
	
	
	private int value;
	private String desc;

	TrayLogStateEnum(int value, String desc){
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
