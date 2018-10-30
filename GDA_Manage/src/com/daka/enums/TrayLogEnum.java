package com.daka.enums;

public enum TrayLogEnum {
	
	//奖品类型
	TYPE_WELTH (1,"财富值"),
	TYPE_VITALITY (2,"生命力"),
	TYPE_OTHER (3,"其他实物"),
	
	//奖品状态
	STATE_WAIT_FOR(1,"待发货"),
	STATE_DELIVER(2,"发货");
	
	private int value;
	private String desc;
	
	TrayLogEnum(int value,String desc){
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
