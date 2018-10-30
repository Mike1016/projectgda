package com.daka.entry.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.daka.entry.TrayLogVO;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class TrayLogDTO extends TrayLogVO{
	
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


}
