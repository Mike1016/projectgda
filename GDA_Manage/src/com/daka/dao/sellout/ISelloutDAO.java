package com.daka.dao.sellout;

import com.daka.entry.*;
import com.daka.entry.dto.SelloutDTO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ISelloutDAO {

    List<PageData> querySelloutlistPage(Page page) throws Exception;//分页查询

    void saveSellout(SelloutVO selloutVO) throws Exception; //卖出订单

    List<SelloutVO> showSelloutOrder(Map<String,Object> map) throws Exception; //根据用户id和状态查询卖出订单

    void updateState(SelloutVO selloutVO) throws Exception; //根据卖出订单的id修改卖出订单的状态

    CustomerVO showOtherBasicInfo(Integer id) throws Exception; //根据卖出订单id查看买方基本信息

    List<BankVO> showOtherBankInfo(Integer id) throws Exception; //根据卖出订单id查看买方银行卡信息
    
    SelloutVO selectSelloutVO(String orderNo);//查询卖方信息
    
    List<SelloutVO> querySelloutVO()throws Exception;//查询卖方信息
    
    void updateSellout(SelloutVO selloutVO);//修改卖方信息
    
    List<SelloutVO> getSelloutVO(Integer id)throws Exception;//根据订单ID查询卖方信息
    
    SelloutVO getSelloutVOById(Integer id)throws Exception;//根据卖单id查询卖方信息
    
    List<SelloutVO> getSelloutVOByAccount(BigDecimal account)throws Exception;//根据金额查询卖方信息列表
    
    List<SelloutVO> getSelloutVOList(Integer id)throws Exception;//根据订单ID查询卖方信息列表

    String showPaymentImg(Integer id) throws Exception; //根据卖出订单id查看买方的付款截图
    
    List<SelloutVO> getSelloutInfo(int customerId) throws Exception; //根据用户id查询卖出信息 
    
    List<SelloutVO> getSelloutByPlatoonId(int id) throws Exception; //根据排单id查询卖出信息

    List<PageData> queryStateSelloutlistPage(Page page) throws Exception;//分页查询

    List<SelloutVO> querySelloutByPlatoonId(SelloutVO selloutVO) throws Exception; //根据platoonId 查询出正在匹配的排单数据

    String querySelloutTime(Integer customerId) throws Exception; //根据用户id查询此用户最近一次卖出订单的时间

    BigDecimal querySumAccountByType(SelloutVO selloutVO) throws Exception; //根据用户id，状态和类型查询此用户的所有卖出财富值

    List<SelloutDTO> querySelloutCustomer(Integer platoonId) throws Exception; //查询卖出订单的用户电话

    void updateBatch(List<SelloutVO> list) throws Exception;//批量修改信息

    void updatePaymentImgNull(@Param("ids") String ids) throws Exception;//修改(清空)付款截图

    void updatePlatoonIdNull(@Param("ids") String ids) throws Exception;//修改(清空)卖出匹配订单信息
    
    List<SelloutVO> getSelloutInfoById (int id) throws Exception; //根据用户id查询卖出信息 
    
    BigDecimal getSelloutSum(int id) throws Exception; //根据用户id查询卖出总金额

    List<SelloutVO> querySelloutOrderByCustomerId(@Param("customerId") Integer customerId,@Param("day")Integer day) throws Exception; //根据用户ID查询此用户的所有卖出订单信息

	List<SelloutVO> matchingSellout(@Param("customerId") Integer customerId, @Param("account") BigDecimal account) throws Exception; //匹配数据

	void updateBatchPlatoonId(List<SelloutVO> list) throws Exception; //批量更新单据

	void updateBatchState(List<SelloutDTO> list) throws Exception; //批量更新状态

	List<SelloutDTO> selectSelloutByPlatoonId(Integer platoonId) throws Exception; //通过排单获取单据

	BigDecimal selectSelloutSumByPlatoonId(Integer platoonId) throws Exception; //获取匹配的金额

    void updateCompleted(Integer id) throws Exception; //已完成s

    List<SelloutVO> queryPlatoonSonOrder(SelloutVO selloutVO) throws Exception;//根据排单ID、和卖出订单状态查询相关子订单
    
    List<SelloutDTO> selectPlatoonInfoList(int id) throws Exception; //根据用户id查询排单信息DTO

    List<SelloutVO> querySelloutByPlatoonIds(@Param("platoonIds") String platoonIds) throws Exception;//查询排单下相关的所有子订单
}
