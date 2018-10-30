package com.daka.dao.bank;

import com.daka.entry.BankVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;

import java.util.List;
import java.util.Map;

public interface IBankDAO {

    void saveBank(BankVO bankVO) throws Exception; //保存银行卡信息

    List<BankVO> queryBankByCustomerId(Integer customerId) throws Exception; //根据用户id查询用户银行卡信息

    List<PageData> queryBanklistPage(Page page) throws Exception; //后台 分页查询

    void deleteById(Integer id) throws Exception; //根据id删除
    
    BankVO selectByCustomerId(Map<String, Object> map) throws Exception; //根据用户和银行卡id查询银行信息
}
