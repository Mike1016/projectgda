package com.daka.entry;


public class TrayLogVO {

    private java.lang.Integer id;
    private java.lang.Integer customerId;
    private java.lang.Integer type;
    private java.lang.Integer account;
    private String createTime;
    private java.lang.Integer state;
    private String prizes;
    private Integer vitality;


    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(java.lang.Integer customerId) {
        this.customerId = customerId;
    }

    public java.lang.Integer getType() {
        return type;
    }

    public void setType(java.lang.Integer type) {
        this.type = type;
    }

    public java.lang.Integer getAccount() {
        return account;
    }

    public void setAccount(java.lang.Integer account) {
        this.account = account;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public java.lang.Integer getState() {
        return state;
    }

    public void setState(java.lang.Integer state) {
        this.state = state;
    }

	public String getPrizes() {
		return prizes;
	}

	public void setPrizes(String prizes) {
		this.prizes = prizes;
	}

	public Integer getVitality() {
		return vitality;
	}

	public void setVitality(Integer vitality) {
		this.vitality = vitality;
	}
}
