package com.daka.entry;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BonusLogVO {

	private java.lang.Integer id;
	private java.lang.Integer customerId;
	private java.math.BigDecimal originAccount;
	private java.math.BigDecimal account;
	private java.math.BigDecimal interest;
	private String createTime;


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

	public java.math.BigDecimal getOriginAccount() {
		return originAccount;
	}

	public void setOriginAccount(java.math.BigDecimal originAccount) {
		this.originAccount = originAccount;
	}

	public java.math.BigDecimal getAccount() {
		return account;
	}

	public void setAccount(java.math.BigDecimal account) {
		this.account = account;
	}

	public java.math.BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(java.math.BigDecimal interest) {
		this.interest = interest;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
