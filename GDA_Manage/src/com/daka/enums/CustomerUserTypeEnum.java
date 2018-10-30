package com.daka.enums;

public enum CustomerUserTypeEnum {

    REGISTER_USER(0,"注册用户"),
    SYSTEM_USER(1,"系统用户");


    private int value;
    private String desc;

    CustomerUserTypeEnum(int value, String desc)
    {
        this.value = value;
        this.desc = desc;
    }

    public int getValue()
    {
        return this.value;
    }

    public String getDesc()
    {
        return this.desc;
    }
}
