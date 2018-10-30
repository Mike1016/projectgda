package com.daka.enums;

public enum PlatoonDateEnum {

	HOUR_TIME(60 * 60 * 1000, "时间戳"),
	TWODATELONG(2*24*60*60*1000,"2天的时间戳"),
	TENDATELONG(10*24*60*60*1000,"10天的时间戳"),
	ONEDATELONG(24*60*60*1000,"24小时的时间戳"),
	THREEDATELONG(3*60*60*1000,"3小时的时间戳");

	private long value;
	private String desc;

	PlatoonDateEnum(long value, String desc){
		this.value = value;
		this.desc = desc;
	}

	public Long getHourTime(String hour) {
		return Long.valueOf(hour) * this.getValue();
	}
	
	public long getValue() {
		return value;
	}
	
	public String getDesc() {
		return desc;
	}
}
