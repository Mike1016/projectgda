package com.daka.entry;


public class RecommendLogVO {

	private java.lang.Integer id;
	private java.lang.Integer customerId;
	private java.lang.Integer refereeId;
	private String level;
	private java.math.BigDecimal selfLastAccount;
	private java.math.BigDecimal childAccount;
	private java.math.BigDecimal realAccount;
	private java.math.BigDecimal account;
	private java.math.BigDecimal ratio;
	private String createTime;

	public Integer getRefereeId() {
		return refereeId;
	}

	public void setRefereeId(Integer refereeId) {
		this.refereeId = refereeId;
	}

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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public java.math.BigDecimal getSelfLastAccount() {
		return selfLastAccount;
	}

	public void setSelfLastAccount(java.math.BigDecimal selfLastAccount) {
		this.selfLastAccount = selfLastAccount;
	}

	public java.math.BigDecimal getChildAccount() {
		return childAccount;
	}

	public void setChildAccount(java.math.BigDecimal childAccount) {
		this.childAccount = childAccount;
	}

	public java.math.BigDecimal getRealAccount() {
		return realAccount;
	}

	public void setRealAccount(java.math.BigDecimal realAccount) {
		this.realAccount = realAccount;
	}

	public java.math.BigDecimal getAccount() {
		return account;
	}

	public void setAccount(java.math.BigDecimal account) {
		this.account = account;
	}

	public java.math.BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(java.math.BigDecimal ratio) {
		this.ratio = ratio;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
