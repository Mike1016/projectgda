package com.daka.entry;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PlatoonVO {

	private java.lang.Integer id;
	private java.lang.Integer customerId;
	private String orderNo;
	private java.math.BigDecimal account;
	private String createTime;
	private String matchTime;
	private String paymentTime;
	private String confirmTime;
	private java.lang.Integer state;
	private java.lang.Integer status;

	public PlatoonVO() {}

	public PlatoonVO(Integer id, Integer state, String matchTime) {
		this.id = id;
		this.state = state;
		this.matchTime = matchTime;
	}

	public java.lang.Integer getId() {
		return id;
	}

	public java.lang.Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(java.lang.Integer customerId) {
		this.customerId = customerId;
	}

	public java.lang.Integer getStatus() {
		return status;
	}

	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public String getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public java.math.BigDecimal getAccount() {
		return account;
	}

	public void setAccount(java.math.BigDecimal account) {
		this.account = account;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public java.lang.Integer getState() {
		return state;
	}

	public void setState(java.lang.Integer state) {
		this.state = state;
	}
}
