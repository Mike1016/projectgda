package com.daka.api.bank;

import com.daka.api.base.Result;
import com.daka.api.customer.CustomerProvider;
import com.daka.entry.BankVO;
import com.daka.entry.CustomerVO;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.service.bank.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/app/bank")
@RestController
public class BankProvider {

    @Autowired
    private BankService bankService;

    /**
     * 添加银行卡信息
     * @param request
     * @param sessionId
     * @param bankVO
     * @return
     */
    @RequestMapping("/saveBank")
    public Result saveBank(HttpServletRequest request, String sessionId, BankVO bankVO, String authCode) {
        try {
            CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            if (bankVO.getCardHolder() == null || "".equals(bankVO.getCardHolder()) || bankVO.getCardNumber() == null || "".equals(bankVO.getCardNumber())) {
                return Result.newFailure("持卡人或卡号为空!",0);
            }
            if (!authCode.equals(CustomerProvider.SMSMap.get(cus.getPhone()))) {
                return Result.newFailure("验证码不正确",0);
            }
            bankVO.setCustomerId(cus.getId());
            return bankService.saveBank(bankVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员！",0);
        }
    }

    /**
     * 展示银行卡信息
     * @param request
     * @param sessionId
     * @return
     */
    @RequestMapping("/showBank")
    public Result showBank(HttpServletRequest request, String sessionId) {
        try {
            CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            List<BankVO> data = bankService.queryBankByCustomerId(cus.getId());
            if (null == data || data.size() == 0) {
                return Result.newFailure("暂无银行卡数据",0);
            }
            return Result.newSuccess(data,"操作成功",1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员！",0);
        }
    }
    
    /**
     *删除银行卡信息
     * @param request
     * @param sessionId
     * @return
     */
    @RequestMapping("/deleteBank")
    public Result deleteBank(HttpServletRequest request, String sessionId, BankVO bankVo) {
    	//校验
    	if(bankVo.getId() == null) {
    		return Result.newFailure("银行卡信息不能为空");
    	}
    	try {   		
			return bankService.deleteBank(request, sessionId, bankVo);
		} catch (Exception e) {
			return Result.newFailure("操作失败",0);
		}
    }
}
