package com.daka.enums;

public enum TrayEnum {
	
	//转盘档数
	TRAYGRADE_THREE (1,"三档，小转盘"),
	TRAYGRADE_SIX(2,"六档，中转盘"),
	TRAYGRADE_NINE(3,"九档，大转盘"),
	
	//奖品类型
	TYPE_WELTH (1,"财富值"),
	TYPE_VITALITY (2,"生命力"),
	TYPE_OTHER (3,"其他实物"),
	
	//奖品编号
	TRAYNO_ONE(0,"一等奖"),
	TRAYNO_TWO(1,"二等奖"),
	TRAYNO_THREE(2,"三等奖"),
	TRAYNO_FOUR(3,"四等奖"),
	TRAYNO_FIVE(4,"五等奖"),
	TRAYNO_SIX(5,"六等奖"),
	TRAYNO_SEVEN(6,"七等奖"),
	TRAYNO_EIGHT(7,"八等奖");
	
	private int value;
	private String desc;
	
	TrayEnum(int value,String desc){
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
