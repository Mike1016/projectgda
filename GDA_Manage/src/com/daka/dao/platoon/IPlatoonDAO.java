package com.daka.dao.platoon;

import com.daka.entry.*;
import com.daka.entry.dto.PlatoonDTO;
import com.daka.entry.dto.SelloutDTO;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IPlatoonDAO {
	List<PlatoonVO> queryPlatoonVO() throws Exception; //取得PlatoonVO对象

	PlatoonVO selectPlatoonVOByOrderNo(String orderNo) throws Exception;

	void savePlatoon(PlatoonVO platoonVO) throws Exception; //保存

	List<PlatoonVO> selectCyclePlatoon(@Param("customerId") Integer customerId, @Param("day") Integer day) throws Exception; //获取用户周期内的订单

	List<PlatoonVO> showPlatoonOrder(Map<String, Object> map) throws Exception; //根据用户ID和state查询

	List<PlatoonVO> completed(Map<String, Object> map) throws Exception; //根据用户ID和state查询

	PlatoonVO queryPlatoonById(Integer id) throws Exception; //根据ID查询订单

	void updatePlatoonVO(PlatoonVO platoonVO) throws Exception; //修改

	List<PageData> queryPlatoonlistPage(Page page) throws Exception; //后台分页查询

	void updatePlatoonState(PlatoonVO platoonVO) throws Exception; //修改排单的状态

	List<PlatoonVO> getPlatoonByCustomerId(int id) throws Exception; //根据用户id查询排单信息

	List<PlatoonVO> getPlatoonInfoById(int platoonId) throws Exception; //根据排单id查询排单信息

	BigDecimal querySumAccountByCustomerId(Integer customerId) throws Exception; //根据用户id查询此用户的冻结账户财富值是否大于0

	PlatoonDTO queryPlatoonCustomer(int id) throws Exception; //查询排单的用户电话

	BigDecimal getPlatoonSum(int id) throws Exception; //根据用户id查询排单总金额

	List<PlatoonVO> queryUnexpiredOrder(int customerId) throws Exception;//查询未失效的订单

	void updateBatch(List<PlatoonVO> list) throws Exception; //批量修改

	CustomerVO showOtherBasicInfo(Integer id) throws Exception; //查询卖方信息

	List<BankVO> showOtherBankInfo(Integer id) throws Exception; //查询卖方银行卡信息

	PlatoonVO selectPlatoonOnlyByCustomerId(Integer customerId) throws Exception; //通过用户Id查询是否存在排单
	
	List<PlatoonDTO> getSelloutDtoByPlatoonId(int id) throws Exception; //根据用户id查询买入财富值DTO信息       更新卖出财富值记录
	
    List<PlatoonDTO> getSelloutDtoBonusInfo(int id) throws Exception; //根据排单id查询卖出奖金背包信息DTO
	
}
