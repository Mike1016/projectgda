package com.daka.enums;

import com.daka.service.dictionaries.DictionariesService;

public enum SystemEnum {
	PLATOONAUTOMATIC("platoonAutomatic"),
	REGISTER("register"),
	ORDERCYCLE("orderCycle"),
	SELLOUTORDERPERIOD("selloutOrderPeriod"),
	PLATOONMAX("platoonMax"),
	PLATOONCYCLE("platoonCycle"),
	PLATOONACCOUNTMULTIPLE("platoonAccountMultiple"),
	PLATOONACCOUNTMIN("platoonAccountMin"),
	MESSAGE_1("MESSAGE_1"),
	MESSAGE_2("MESSAGE_2"),
	MESSAGE_3("MESSAGE_3"),
	MESSAGE_4("MESSAGE_4"),
	MESSAGE_5("MESSAGE_5"),
	MESSAGE("MESSAGE"),
	PAYMENT("payment"),
	WXIMG("wximg"),
	LOGO("LOGO");

	private String desc;

	SystemEnum(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public String getDictionarieValue() {
		return String.valueOf(DictionariesService.dictionaries.get(this.getDesc()));
	}

	public String getMessage(String message) {
		return getDictionarieValue().replaceAll(SystemEnum.MESSAGE.getDesc(), message);
	}
}
