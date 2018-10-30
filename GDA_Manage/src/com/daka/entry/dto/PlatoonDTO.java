package com.daka.entry.dto;

import java.util.ArrayList;
import java.util.List;

import com.daka.entry.SelloutVO;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.daka.entry.PlatoonVO;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PlatoonDTO extends PlatoonVO{

	private Long longMatchTime;

	private Long nowTime;

	private String userName;

	private String phone;

	List<PlatoonVO> platoonList = new ArrayList<PlatoonVO>();

	List<SelloutVO> selloutVOList = new ArrayList<>();

	public List<SelloutVO> getSelloutVOList() {
		return selloutVOList;
	}

	public void setSelloutVOList(List<SelloutVO> selloutVOList) {
		this.selloutVOList = selloutVOList;
	}

	public List<PlatoonVO> getPlatoonList() {
		return platoonList;
	}

	public void setPlatoonList(List<PlatoonVO> platoonList) {
		this.platoonList = platoonList;
	}

	public Long getLongMatchTime() {
		return longMatchTime;
	}

	public void setLongMatchTime(Long longMatchTime) {
		this.longMatchTime = longMatchTime;
	}

	public Long getNowTime() {
		return nowTime;
	}

	public void setNowTime(Long nowTime) {
		this.nowTime = nowTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
