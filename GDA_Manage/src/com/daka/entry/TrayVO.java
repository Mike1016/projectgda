package com.daka.entry;


public class TrayVO {

	private java.lang.Integer id;
	private java.lang.Integer trayNo;
	private java.lang.Integer trayGrade;
	private java.lang.Integer type;
	private java.lang.Integer account;
	private String contextImg;
	private String prizes;
	private java.math.BigDecimal probability;
	private String createTime;


	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getTrayNo() {
		return trayNo;
	}

	public void setTrayNo(java.lang.Integer trayNo) {
		this.trayNo = trayNo;
	}

	public java.lang.Integer getTrayGrade() {
		return trayGrade;
	}

	public void setTrayGrade(java.lang.Integer trayGrade) {
		this.trayGrade = trayGrade;
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

	public String getContextImg() {
		return contextImg;
	}

	public void setContextImg(String contextImg) {
		this.contextImg = contextImg;
	}

	public java.math.BigDecimal getProbability() {
		return probability;
	}

	public void setProbability(java.math.BigDecimal probability) {
		this.probability = probability;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPrizes() {
		return prizes;
	}

	public void setPrizes(String prizes) {
		this.prizes = prizes;
	}
}
