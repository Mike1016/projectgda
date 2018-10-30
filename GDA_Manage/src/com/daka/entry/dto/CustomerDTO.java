package com.daka.entry.dto;

import com.daka.entry.BankVO;
import com.daka.entry.CustomerVO;
import com.daka.entry.PlatoonVO;
import com.daka.entry.SelloutVO;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CustomerDTO extends CustomerVO {

	private Integer teamCount; //团队人数
	private Integer pushCount; //直推人数
	private Integer activationCount; //激活码数量
	private BigDecimal extensionAccount; //推荐金额
	private BigDecimal account;//金额
	private String userName; //姓名
	private Integer id1; //直属一级
	private Integer id2; //直属二级
	private Integer id3; //直属三级

	List<BankVO> bankList = new ArrayList<BankVO>(); 
	List<PlatoonVO> platoonList = new ArrayList<PlatoonVO>();
	List<PlatoonDTO> platoonDtoList = new ArrayList<PlatoonDTO>();
	List<SelloutDTO> selloutDtoList = new ArrayList<SelloutDTO>();
	List<IActivationLogDTO> IActivationLogDtoList = new ArrayList<IActivationLogDTO>();
	
	public List<IActivationLogDTO> getIActivationLogDtoList() {
		return IActivationLogDtoList;
	}

	public void setIActivationLogDtoList(List<IActivationLogDTO> iActivationLogDtoList) {
		IActivationLogDtoList = iActivationLogDtoList;
	}

	public List<PlatoonDTO> getPlatoonDtoList() {
		return platoonDtoList;
	}

	public void setPlatoonDtoList(List<PlatoonDTO> platoonDtoList) {
		this.platoonDtoList = platoonDtoList;
	}
	
	public List<SelloutDTO> getSelloutDtoList() {
		return selloutDtoList;
	}

	public void setSelloutDtoList(List<SelloutDTO> selloutDtoList) {
		this.selloutDtoList = selloutDtoList;
	}

	List<SelloutVO> selloutList = new ArrayList<SelloutVO>();

	public BigDecimal getAccount() {
		return account;
	}

	public void setAccount(BigDecimal account) {
		this.account = account;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}	
	
	public List<PlatoonVO> getPlatoonList() {
		return platoonList;
	}

	public void setPlatoonList(List<PlatoonVO> platoonList) {
		this.platoonList = platoonList;
	}

	public BigDecimal getExtensionAccount() {
		return extensionAccount;
	}

	public void setExtensionAccount(BigDecimal extensionAccount) {
		this.extensionAccount = extensionAccount;
	}	

    public List<BankVO> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankVO> bankList) {
        this.bankList = bankList;
    }

	public Integer getTeamCount() {
		return teamCount;
	}

	public void setTeamCount(Integer teamCount) {
		this.teamCount = teamCount;
	}

	public Integer getPushCount() {
		return pushCount;
	}

	public void setPushCount(Integer pushCount) {
		this.pushCount = pushCount;
	}

	public Integer getActivationCount() {
		return activationCount;
	}

	public void setActivationCount(Integer activationCount) {
		this.activationCount = activationCount;
	}

	public List<SelloutVO> getSelloutList() {
		return selloutList;
	}

	public void setSelloutList(List<SelloutVO> selloutList) {
		this.selloutList = selloutList;
	}
	public Integer getId1() {
		return id1;
	}

	public void setId1(Integer id1) {
		this.id1 = id1;
	}

	public Integer getId2() {
		return id2;
	}

	public void setId2(Integer id2) {
		this.id2 = id2;
	}

	public Integer getId3() {
		return id3;
	}

	public void setId3(Integer id3) {
		this.id3 = id3;
	}
}
