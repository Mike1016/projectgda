package com.daka.entry.dto;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.daka.entry.ActivationLogVO;



@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class IActivationLogDTO extends ActivationLogVO{
	
	private String userName;
	private String customers;
	private String activators;
	

	public String getCustomers() {
		return customers;
	}

	public void setCustomers(String customers) {
		this.customers = customers;
	}

	public String getActivators() {
		return activators;
	}

	public void setActivators(String activators) {
		this.activators = activators;
	}

	List<ActivationLogVO> activationLogList = new ArrayList<ActivationLogVO>(); 
	
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<ActivationLogVO> getActivationLogList() {
		return activationLogList;
	}

	public void setActivationLogList(List<ActivationLogVO> activationLogList) {
		this.activationLogList = activationLogList;
	}
}
