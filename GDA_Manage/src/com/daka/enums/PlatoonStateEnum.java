package com.daka.enums;

public enum PlatoonStateEnum
{

	STATE_MATE (0,"带匹配"),
	STATE_PAYMENT (1,"待付款"),
	STATE_CONFIRM (2,"待确认"),
	STATE_OK (3,"已完成"),
	LOSE_EFFICACY(4,"失效");
	
	private int value;
	private String desc;

	PlatoonStateEnum(int value, String desc){
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
