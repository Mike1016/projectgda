package com.daka.enums;

public enum SelloutStateEnum
{

	STATE_MATE (0,"待匹配"),
	STATE_PAYMENT_NO (1,"未付款(已匹配)"),
	STATE_PAYMENT_YES (2,"已付款(等待确认)"),
	STATE_CFMD (3,"已确认"),
	STATE_FINISH(4,"已完成");
	
	
	private int value;
	private String desc;

	SelloutStateEnum(int value, String desc){
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
