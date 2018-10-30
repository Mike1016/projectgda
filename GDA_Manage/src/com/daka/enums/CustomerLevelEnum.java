package com.daka.enums;

import java.util.HashMap;
import java.util.Map;

public enum CustomerLevelEnum {

	VIP_LEVEL_M1("M1", "会员级别M1", 1),
	VIP_LEVEL_M2("M2", "会员级别M2", 2),
	VIP_LEVEL_M3("M3", "会员级别M3", 3),
	VIP_LEVEL_M4("M4", "会员级别M4", 4),
	VIP_LEVEL_M5("M5", "会员级别M5", 5),
	VIP_LEVEL_M6("M6", "会员级别M6", 6),
	VIP_LEVEL_M7("M7", "会员级别M7", 7),
	VIP_LEVEL_M8("M8", "会员级别M8", 8),
	VIP_LEVEL_M9("M9", "会员级别M9", 9),
	VIP_LEVEL_M10("M10", "会员级别M10", 10);

	private String value;
	private String desc;
	private int level;

	CustomerLevelEnum(String value, String desc, int level) {
		this.value = value;
		this.desc = desc;
		this.level = level;
		CustomerLevel.levelMap.put(value, level);
	}

	public String getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public int getLevel() {
		return level;
	}

	public static Integer getLevel(final String level) {
		return new CustomerLevel() {
			Integer getLevel() {
				return levelMap.get(level);
			}
		}.getLevel();
	}
}

interface CustomerLevel {
	Map<String, Integer> levelMap = new HashMap<>(); //等级
}
