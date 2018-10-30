package com.daka.enums;

public enum ActivityLogEnum {
	
	//活动值记录类型
	TYPE_TURNTABLE(1,"转盘扣除"),
	TYPE_TRANSACTION(1,"交易获取");
	
	private int value;
	private String desc;
	
	ActivityLogEnum(int value,String desc){
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
