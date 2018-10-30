package com.daka.service.bank;

import com.daka.api.base.Result;
import com.daka.dao.bank.IBankDAO;
import com.daka.entry.BankVO;
import com.daka.entry.CustomerVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.util.BankCardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Service
public class BankService {

    @Autowired
    private IBankDAO iBankDAO;

    /**
     * 添加银行卡信息
     * @param bankVO
     * @throws Exception
     */
    public Result saveBank(BankVO bankVO) throws Exception {
        String bankName = BankCardUtil.getname(bankVO.getCardNumber()); //通过银行卡号获取银行名称
        if (bankName == null) {
            return Result.newFailure("未获取到银行卡信息",0);
        }
        bankName = bankName.substring(0,bankName.indexOf("·"));
        bankVO.setBankName(bankName);
        iBankDAO.saveBank(bankVO);
        return Result.newSuccess("添加银行卡成功！",1);
    }

    /**
     * 根据用户id查询用户银行卡信息
     * @param customerId
     * @return
     * @throws Exception
     */
    public List<BankVO> queryBankByCustomerId(Integer customerId) throws Exception{
        return iBankDAO.queryBankByCustomerId(customerId);
    }

    /**
     * 分页查询
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> queryBanklistPage(Page page) throws Exception{
        return iBankDAO.queryBanklistPage(page);
    }

    /**
     * 根据id删除
     * @param id
     * @throws Exception
     */
    public void deleteById(Integer id) throws Exception{
        iBankDAO.deleteById(id);
    }
    
    /**
     *删除银行卡信息
     * @param request
     * @param sessionId
     * @param bankVO
     * @return
     */
    public Result deleteBank(HttpServletRequest request, String sessionId, BankVO bankVo) throws Exception{
   	 	Map<String, Object> map = new HashMap<String, Object>();
    	CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
    	
    	map.put("customerId", cus.getId());
    	map.put("id", bankVo.getId());    	
    	BankVO bank = iBankDAO.selectByCustomerId(map);
    	
    	if(bank == null) {
    		return Result.newSuccess("删除信息有误");
    	}
    	
    	iBankDAO.deleteById(bank.getId());
    	
    	return Result.newSuccess("操作成功");
    }
}
