package com.daka.api.sellout;

import com.daka.api.base.Result;
import com.daka.constants.SystemConstant;
import com.daka.entry.BankVO;
import com.daka.entry.CustomerVO;
import com.daka.entry.SelloutVO;
import com.daka.enums.CustomerStateEnum;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.service.customer.CustomerService;
import com.daka.service.sellout.SelloutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/app/sellout")
@RestController
public class SelloutProvider {

    @Autowired
    private SelloutService selloutService;
    @Autowired
    private CustomerService customerService;


    /**
     * 查询卖出订单
     * @param request
     * @param sessionId
     * @param state 0待匹配，1未付款（已匹配），2已付款（等待确认），3已确认，4已完成
     * @return
     */
    @RequestMapping("/showSelloutOrder")
    public Result showSelloutOrder(HttpServletRequest request,String sessionId,Integer state) {
        try {
            CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            List<Map<String, Object>> data = selloutService.showSelloutOrder(cus.getId(),state);
            if (null == data || data.size() == 0) {
                return Result.newFailure("暂无数据",0);
            }
            return Result.newSuccess(data,"获取成功",1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员！",0);
        }
    }

    /**
     * 卖出订单
     * @param request
     * @param sessionId
     * @param selloutVO
     * @param securityPassword
     * @return
     */
    @RequestMapping("/saveSellout")
    public Result saveSellout(HttpServletRequest request, String sessionId,SelloutVO selloutVO, String securityPassword) {
        try {
            CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            if (null == selloutVO.getType()) {
            	return Result.newFailure("未选择背包类型");
			}
            CustomerVO vo = customerService.queryCustomerById(cus.getId());
            if (vo.getState() == CustomerStateEnum.IS_ACTIVE_NO.getValue() || vo.getState() ==CustomerStateEnum.IS_ACTIVE_BAN.getValue()) {
                return Result.newFailure("账号已被封号,请先去激活！");
            }
            return selloutService.saveSellout(selloutVO,cus.getId(),securityPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员！",0);
        }
    }

    /**
     * 通过卖出订单id确认收款
     * @param request
     * @param sessionId
     * @param selloutVO
     * @return
     */
    @RequestMapping("/updateState")
    public Result updateState(HttpServletRequest request,String sessionId,SelloutVO selloutVO) {
        try {
            CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            return selloutService.updateState(selloutVO,cus.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员！",0);
        }
    }

    /**
     * 通过卖出订单id查询对方信息
     * @param request
     * @param sessionId
     * @param id
     * @return
     */
    @RequestMapping("/showOtherInfo")
    public Result showOtherInfo(HttpServletRequest request,String sessionId,Integer id) {
        try {
            Map<String,Object> map = selloutService.showOtherBasicInfo(id);
            List<BankVO> bankList = selloutService.showOtherBankInfo(id);
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("map",map);
            resultMap.put("bankList",bankList);
            return Result.newSuccess(resultMap, "获取成功",1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("获取失败",0);
        }
    }

    /**
     * 通过卖出订单id查询对方付款截图
     * @param request
     * @param sessionId
     * @param id
     * @return
     */
    @RequestMapping("/showPaymentImg")
    public Result showPaymentImg(HttpServletRequest request, String sessionId,Integer id) {
        try {
            String data = selloutService.showPaymentImg(id);
            if (data == null) {
                return Result.newFailure("暂无数据",0);
            }
            return Result.newSuccess(data,"获取成功",1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("获取失败",0);
        }
    }
    
    /**
     * 买入（卖出）财富值（奖金）记录
     * @param request
     * @param sessionId
     * @param type  0：买入财富值，1：卖出财富值，2： 卖出奖金
     * @return
     */
    @RequestMapping("/getWealthInfo")
    public Result getWealthInfo(HttpServletRequest request, String sessionId, int type) {
    	//校验
    	if(sessionId == null) {
    		return Result.newFailure("sessionId不能为空");
    	}
    	try {
        	//买入财富值记录
        	if(type == 0) {
        		return selloutService.getWealthBuyInfo(request, sessionId);
        	}
        	//卖出财富值记录
        	if(type == 1) {
        		return selloutService.getWealthSellInfo(request, sessionId);
        	}
        	//卖出奖金记录
        	if(type == 2) {
        		return selloutService.getWealthBounsInfo(request, sessionId);
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return Result.newFailure("获取失败",0);
    }
}
