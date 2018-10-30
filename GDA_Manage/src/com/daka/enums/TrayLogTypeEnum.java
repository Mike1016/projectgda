package com.daka.enums;

public enum TrayLogTypeEnum
{  	  

	TYPE_WEALTH (1,"财富值"),
	TYPE_VITALITY (2,"生命力"),
	TYPE_OTHER (3,"其他实物");
	
	
	private int value;
	private String desc;

	TrayLogTypeEnum(int value, String desc){
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
