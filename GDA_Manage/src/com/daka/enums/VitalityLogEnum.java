package com.daka.enums;

public enum VitalityLogEnum {
	
	//生命值记录类型
	TYPE_DAILY_DEDUCTIONS(0,"每日扣除"),
	TYPE_TURNTABLE(1,"转盘增加"),
	TYPE_TEAM(2,"团队增加"),
	TYPE_BACKSTAGE(3,"后台增加");
	
	private int value;
	private String desc;
	
	VitalityLogEnum(int value,String desc){
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
