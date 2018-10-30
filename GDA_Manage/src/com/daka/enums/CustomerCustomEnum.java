package com.daka.enums;

public enum CustomerCustomEnum {

    ADD ("add","添加"),
    UNSEAL ("unseal","解封"),
    TITLE ("title","封号"),
    ACTIVATION ("activation","激活");


    private String value;
    private String desc;

    CustomerCustomEnum(String value, String desc)
    {
        this.value = value;
        this.desc = desc;
    }

    public String getValue()
    {
        return this.value;
    }

    public String getDesc()
    {
        return this.desc;
    }

}
