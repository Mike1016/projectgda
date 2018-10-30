package com.daka.api.platoon;

import com.daka.api.base.Result;
import com.daka.entry.CustomerVO;
import com.daka.entry.dto.PlatoonDTO;
import com.daka.enums.SystemEnum;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.service.customer.CustomerService;
import com.daka.service.dictionaries.DictionariesService;
import com.daka.service.platoon.PlatoonService;
import com.daka.util.MD5;
import com.daka.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RequestMapping("/app/platoon")
@RestController
public class PlatoonProvider {

	@Autowired
	private PlatoonService platoonService; //排单
	@Autowired
	private CustomerService customerService;

	/**
	 * 买入财富
	 *
	 * @param request          请求参数
	 * @param sessionId        sessionId
	 * @param securityPassword 安全密码
	 * @param account          财富值
	 * @return
	 */
	@RequestMapping("/saveWealth")
	public Result saveWealth(HttpServletRequest request, String sessionId, String securityPassword, BigDecimal account) {
		try {
			if (null == account) {
				return Result.newFailure("财富值不能为空");
			}
			if (null == securityPassword) {
				return Result.newFailure("安全密码不能为空");
			}
			if (Tools.isMultiple(account, SystemEnum.PLATOONACCOUNTMULTIPLE) != 0) {
				return Result.newFailure("财富值为500的倍数");
			}
			if (account.compareTo(new BigDecimal(DictionariesService.dictionaries.get(SystemEnum.PLATOONACCOUNTMIN.getDesc()))) < 0) {
				return Result.newFailure("财富值最低2000");
			}
			if (platoonService.matchingTimeControl()) {
				return Result.newFailure("禁止买入，请稍后再试");
			}
			CustomerVO customerVO = SessionContext.getConstomerInfoBySessionId(request, sessionId);
			customerVO = customerService.queryById(customerVO.getId());
			if (!platoonService.perfectPersonalInfo(customerVO.getId())) {
				return Result.newFailure("请完善个人信息");
			}
			if (!MD5.md5(securityPassword).equals(customerVO.getSecurityPassword())) {
				return Result.newFailure("安全密码不正确");
			}
			return platoonService.saveOrUpdatePlatoon(customerVO.getId(), account); //买入订单
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("请联系管理员");
		}
	}

	/**
	 * 查询买入财富的订单
	 * @param request
	 * @param sessionId
	 * @param state
	 * @return
	 */
	@RequestMapping("/showPlatoonOrder")
	public Result showPlatoonOrder(HttpServletRequest request,String sessionId,Integer state) {
		try {
			CustomerVO customerVO = SessionContext.getConstomerInfoBySessionId(request, sessionId);
			List<Map<String,Object>> data = platoonService.showPlatoonOrder(customerVO.getId(),state);
			return Result.newSuccess(data,"成功",1);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("请联系管理员",0);
		}
	}

    /**
     * 根据订单id查询订单详情
     *
     * @param request
     * @param sessionId
     * @param id
     * @return
     */
	@RequestMapping("/showOrderByPlatoonId")
	public Result showOrderByPlatoonId(HttpServletRequest request,String sessionId,Integer id) {
	    try {
	        PlatoonDTO platoonDTO = platoonService.showOrderByPlatoonId(id);
	        return Result.newSuccess(platoonDTO,"成功",1);
        } catch (Exception e) {
	        e.printStackTrace();
	        return Result.newFailure("请联系管理员");
        }
    }

	/**
	 * 查询卖方信息
	 *
	 * @param request
	 * @param sessionId
	 * @param id
	 * @return
	 */
	@RequestMapping("/showOtherInfo")
	public Result showOtherInfo(HttpServletRequest request, String sessionId, Integer id) {
		try {
			Map<String,Object> map = platoonService.showOtherInfo(id);
			return Result.newSuccess(map,"成功",1);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("请联系管理员",0);
		}
	}

	/**
	 * 上传截图
	 *
	 * @param request
	 * @param id 卖出订单ID
	 * @param sessionId
	 * @return
	 */
	@RequestMapping("/upLoadImg")
	public Result upLoadImg(HttpServletRequest request, Integer id, String sessionId, MultipartFile paymentImg) {
		try {
			if (null == id){
				return Result.newFailure("请求参数异常", 0);
			}
			if (null == paymentImg){
				return Result.newFailure("请选择要上传的图片", 0);
			}
			platoonService.uploadMap(request, id, paymentImg);
			return Result.newSuccess("上传成功", 1);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("请联系管理员", 0);
		}
	}

	/**
	 * 确定已付款
	 *
	 * @param request   请求
	 * @param id   订单ID
	 * @param sessionId sessionId
	 * @return Json
	 */
	@RequestMapping("/platoonPayment")
	public Result platoonPayment(HttpServletRequest request, Integer id, String sessionId) {
		try {
			CustomerVO customerVO = SessionContext.getConstomerInfoBySessionId(request, sessionId);
			return platoonService.determinePayment(id, customerVO.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("请联系管理员", 0);
		}
	}

	/**
	 * 成长中（已收益）财富
	 *
	 * @param request 请求参数
	 * @param sessionId
	 * @return
	 */
	@RequestMapping("/getWealthIncome")
	public Result getWealthIncome(HttpServletRequest request, String sessionId, int type) {

		if (sessionId == null) {
			return Result.newFailure("sessionId不能为空");
		}
		try {
			// 成长中财富
			if (type == 0) {
				return platoonService.getWealthGrowUp(request, sessionId);
			}
			// 已收益财富
			if (type == 1) {
				return platoonService.getWealthGain(request, sessionId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.newFailure("获取失败", 0);
	}

	/**
	 * 确认已完成
	 *
	 * @param request   请求参数
	 * @param sessionId sessionId
	 * @param id        订单ID
	 * @return Json
	 */
	@RequestMapping("/confirmCompleted")
	public Result confirmCompleted(HttpServletRequest request, String sessionId, Integer id) {
		try {
			SessionContext.getConstomerInfoBySessionId(request, sessionId);
			if (null == id) {
				return Result.newFailure("单号不能为空");
			}
			return platoonService.confirmCompleted(id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.newFailure("请联系管理员");
		}
	}

}
