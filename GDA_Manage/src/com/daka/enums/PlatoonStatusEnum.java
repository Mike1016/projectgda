package com.daka.enums;

public enum PlatoonStatusEnum
{
	STATE_FREEZE_HAVE_NOT (0,"未冻结"),
	STATE_FREEZE_YES (1,"冻结"),
	STATE_FREEZE_NO (2,"解冻");
	
	
	
	private int value;
	private String desc;

	PlatoonStatusEnum(int value, String desc){
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
