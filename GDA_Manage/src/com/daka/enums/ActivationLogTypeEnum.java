package com.daka.enums;

public enum ActivationLogTypeEnum {
	
	 
	TYPE_SYSTEM (1,"系统赠送"),
	TYPE_ACTIVATION (2,"激活"),
	TYPE_ROLL_OUT (3,"转出"),
	TYPE_SWITCH (4,"转入");
	
	private int value;
	private String desc;
	
	ActivationLogTypeEnum(int value,String desc){
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
