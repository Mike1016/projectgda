package com.daka.entry;


public class CustomerVO{

	private java.lang.Integer id;
	private java.lang.Integer agentId;
	private String userName;
	private String level;
	private String headImg;
	private String city;
	private String weChat;
	private String alipay;
	private String identityCard;
	private String phone;
	private String extensionCode;
	private String password;
	private String securityPassword;
	private java.lang.Integer activationNumber;
	private java.math.BigDecimal wealth;
	private java.math.BigDecimal bonus;
	private java.lang.Integer vitality;
	private java.lang.Integer activity;
	private String regesterTime;
	private String address;
	private java.lang.Integer state;
	private String realName;
	private java.lang.Integer userType;

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public CustomerVO() {}
	public CustomerVO(Integer id, Integer state) {
		this.id = id;
		this.state = state;
	}

	public java.lang.Integer getId() {
		return id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(java.lang.Integer agentId) {
		this.agentId = agentId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getWeChat() {
		return weChat;
	}

	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getExtensionCode() {
		return extensionCode;
	}

	public void setExtensionCode(String extensionCode) {
		this.extensionCode = extensionCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecurityPassword() {
		return securityPassword;
	}

	public void setSecurityPassword(String securityPassword) {
		this.securityPassword = securityPassword;
	}

	public java.lang.Integer getActivationNumber() {
		return activationNumber;
	}

	public void setActivationNumber(java.lang.Integer activationNumber) {
		this.activationNumber = activationNumber;
	}

	public java.math.BigDecimal getWealth() {
		return wealth;
	}

	public void setWealth(java.math.BigDecimal wealth) {
		this.wealth = wealth;
	}

	public java.math.BigDecimal getBonus() {
		return bonus;
	}

	public void setBonus(java.math.BigDecimal bonus) {
		this.bonus = bonus;
	}

	public java.lang.Integer getVitality() {
		return vitality;
	}

	public void setVitality(java.lang.Integer vitality) {
		this.vitality = vitality;
	}

	public java.lang.Integer getActivity() {
		return activity;
	}

	public void setActivity(java.lang.Integer activity) {
		this.activity = activity;
	}

	public String getRegesterTime() {
		return regesterTime;
	}

	public void setRegesterTime(String regesterTime) {
		this.regesterTime = regesterTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public java.lang.Integer getState() {
		return state;
	}

	public void setState(java.lang.Integer state) {
		this.state = state;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Override
	public String toString() {
		return "CustomerVO [id=" + id + ", agentId=" + agentId + ", userName=" + userName + ", level=" + level
				+ ", headImg=" + headImg + ", city=" + city + ", weChat=" + weChat + ", alipay=" + alipay
				+ ", identityCard=" + identityCard + ", phone=" + phone + ", extensionCode=" + extensionCode
				+ ", password=" + password + ", securityPassword=" + securityPassword + ", activationNumber="
				+ activationNumber + ", wealth=" + wealth + ", bonus=" + bonus + ", vitality=" + vitality
				+ ", activity=" + activity + ", regesterTime=" + regesterTime + ", address=" + address + ", state="
				+ state + ", realName=" + realName + "]";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
