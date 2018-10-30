package com.daka.entry;


import java.math.BigDecimal;

public class SelloutVO {

	private java.lang.Integer id;
	private java.lang.Integer platoonId;
	private String orderNo;
	private java.lang.Integer type;
	private java.math.BigDecimal account;
	private String paymentImg;
	private String createTime;
	private java.lang.Integer state;
	private Integer customerId;
	private String confirmTime;
	private String matchTime;
	private String collectionTime;

	public SelloutVO() {
	}

	public SelloutVO(Integer id, Integer platoonId, String orderNo, Integer type, BigDecimal account, String paymentImg, String createTime, Integer state, Integer customerId, String confirmTime, String matchTime, String collectionTime) {
		this.id = id;
		this.platoonId = platoonId;
		this.orderNo = orderNo;
		this.type = type;
		this.account = account;
		this.paymentImg = paymentImg;
		this.createTime = createTime;
		this.state = state;
		this.customerId = customerId;
		this.confirmTime = confirmTime;
		this.matchTime = matchTime;
		this.collectionTime = collectionTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPlatoonId() {
		return platoonId;
	}

	public void setPlatoonId(Integer platoonId) {
		this.platoonId = platoonId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getAccount() {
		return account;
	}

	public void setAccount(BigDecimal account) {
		this.account = account;
	}

	public String getPaymentImg() {
		return paymentImg;
	}

	public void setPaymentImg(String paymentImg) {
		this.paymentImg = paymentImg;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}

	public String getCollectionTime() {
		return collectionTime;
	}

	public void setCollectionTime(String collectionTime) {
		this.collectionTime = collectionTime;
	}
}
