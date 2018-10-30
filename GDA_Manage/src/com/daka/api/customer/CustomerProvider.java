package com.daka.api.customer;

import com.daka.api.base.Result;
import com.daka.constants.SystemConstant;
import com.daka.entry.BonusLogVO;
import com.daka.entry.CustomHelpVO;
import com.daka.entry.CustomerVO;
import com.daka.entry.dto.CustomerDTO;
import com.daka.enums.CustomerStateEnum;
import com.daka.enums.SystemEnum;
import com.daka.interceptor.appsession.SessionContext;
import com.daka.service.bonusLog.BonusLogService;
import com.daka.service.customHelp.CustomHelpService;
import com.daka.service.customer.CustomerService;
import com.daka.service.dictionaries.DictionariesService;
import com.daka.service.recommendLog.RecommendLogService;
import com.daka.util.ExpiryMap;
import com.daka.util.MD5;
import com.daka.util.sms.SMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RequestMapping("/app/customer")
@RestController
public class CustomerProvider {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private BonusLogService bonusLogService;
    @Autowired
    private RecommendLogService recommendLogService;
    @Autowired
    private DictionariesService dictionariesService;
    @Autowired
    private CustomHelpService customHelpService;

	public static ExpiryMap<String, String> SMSMap = new ExpiryMap<>(1000 * 60 * 2);


	/**
     * 通过sessionId查询用户的电话号码
     *
     * @param request
     * @param sessionId
     * @return
     */
    @RequestMapping("/showPhone")
    public Result showPhone(HttpServletRequest request, String sessionId) {
        try {
            CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            String phone = cus.getPhone();
            String data = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
            return Result.newSuccess(data, "成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员！", 0);
        }
    }

    /**
     * 展示个人信息
     *
     * @param request
     * @param sessionId
     * @return
     */
    @RequestMapping("/showPersonalInfo")
    public Result showPersonalInfo(HttpServletRequest request, String sessionId) {
        try {
            CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            CustomerVO data = customerService.queryCustomerById(cus.getId());
            return Result.newSuccess(data, "获取成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员！", 0);
        }
    }

    /**
     * 修改/完善个人信息
     *
     * @param request
     * @param sessionId
     * @param customerVO
     * @return
     */
    @RequestMapping("/updateMessage")
    public Result updateMessage(HttpServletRequest request, String sessionId, CustomerVO customerVO) {
        try {
            CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            CustomerVO vo = customerService.queryCustomerById(cus.getId());
            if (vo.getState() == CustomerStateEnum.IS_ACTIVE_NO.getValue() || vo.getState() == CustomerStateEnum.IS_ACTIVE_BAN.getValue()) {
				return Result.newFailure("账号未激活,请先去激活！");
			}
            customerVO.setId(cus.getId());
            return customerService.updateMessage(customerVO); //完善个人信息
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员！", 0);
        }
    }

    /**
     * 修改登录密码
     *
     * @param request
     * @param sessionId
     * @param oldPassword
     * @param newPassword
     * @param authCode
     * @return
     */
    @RequestMapping("/updateLoginPassword")
    public Result updateLoginPassword(HttpServletRequest request, String sessionId, String oldPassword, String newPassword, String authCode) {
        try {
            CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            CustomerVO vo = customerService.queryCustomerById(cus.getId());
            if (!(MD5.md5(oldPassword).equals(vo.getPassword()))) {
                return Result.newFailure("原始密码不正确", 0);
            }
            if (!(authCode.equals(SMSMap.get(cus.getPhone())))) {
                return Result.newFailure("验证码不正确", 0);
            }
            vo.setPassword(MD5.md5(newPassword));
            customerService.updateMessage(vo);
            return Result.newSuccess("修改成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员", 0);
        }
    }

    /**
     * 设置安全密码
     *
     * @param request
     * @param sessionId
     * @param password
     * @param authCode
     * @return
     */
    @RequestMapping("/saveSecurityPassword")
    public Result saveSecurityPassword(HttpServletRequest request, String sessionId, String password, String authCode) {
        try {
            CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            CustomerVO vo = customerService.queryCustomerById(cus.getId());
            if (!(authCode.equals(SMSMap.get(vo.getPhone())))) {
                return Result.newFailure("验证码不正确", 0);
            }
            vo.setSecurityPassword(MD5.md5(password));
            customerService.updateMessage(vo);
            return Result.newSuccess("设置成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员", 0);
        }
    }

    /**
     * 注冊
     *
     * @param request  请求
     * @param vo       参数
     * @param authCode 验证码
     * @return Json
     */
    @RequestMapping("/register")
    public Result register(HttpServletRequest request, CustomerVO vo, String authCode) {
        try {
			//校验
			if (vo.getUserName().equals("") || vo.getPhone().equals("") || vo.getPassword().equals("")) {
				return Result.newFailure("用户名或密码错误");
			}

			//注册验证码
			if (!(authCode.equals(SMSMap.get(vo.getPhone())))) {
				return Result.newFailure("验证码不正确");
			}
            return customerService.insertRegister(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("注册失败");
        }
    }

    /**
     * 登录
     *
     * @param request
     * @param vo
     * @return
     */
    @RequestMapping("/login")
    public Result login(HttpServletRequest request, CustomerVO vo) {
        try {
			//校验
			if (vo.getUserName().equals("") || vo.getPassword().equals("")) {
				return Result.newFailure("用户名或密码错误", 0);
			}
            return customerService.login(request, vo);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("登录失敗", 0);
        }
    }

    /**
     * 忘记密码
     *
     * @param
     * @param authCode
     * @return
     */
    @RequestMapping("/retrievePass")
    public Result retrievePass(HttpServletRequest request, CustomerVO vo, String authCode) {
        try {
			//校验
			if (vo.getUserName().equals("") || vo.getPassword().equals("") || vo.getPhone().equals("")) {
				return Result.newFailure("用户名，手机号或者密码错误", 0);
			}
			if(!(authCode.equals(SMSMap.get(vo.getPhone())))) {
				return Result.newFailure("验证码不正确");
			}
            return customerService.retrievePass(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("密码修改失敗", 0);
        }
    }

    /**
     * 退出登录
     *
     * @param request
     * @param sessionId
     * @return
     */
    @RequestMapping("/quit")
    public Result quit(HttpServletRequest request, String sessionId) {
        try {
            HttpSession session = SessionContext.getSession(sessionId);
            if (session != null) {
                session.removeAttribute(SystemConstant.APP_USER);
                SessionContext.DelSession(session);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return Result.newSuccess("退出成功", 1);
        }
    }

    /**
     * 获取验证码（注册）
     *
     * @param phone 电话
     * @return Json
     */
    @RequestMapping("/getRegisterAuthCode")
    public Result getRegisterAuthCode(String phone) {
        try {
			//校验
			if (phone.equals("")) {
				return Result.newFailure("手机号不正确");
			}
			String number=((int)((Math.random()*9+1)*100000))+"";
			String message = SystemEnum.MESSAGE_1.getMessage(number);
 
            SMSUtil.sendSms(phone, message);
            SMSMap.put(phone, number);
			return Result.newSuccess("获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("获取失败");
        }
    }

    /**
     * 每日利息
     *
     * @param request   请求
     * @param sessionId sessionId
     * @return result
     */
    @RequestMapping(value = "/dailyInterest", method = RequestMethod.GET)
    public Result dailyInterest(HttpServletRequest request, String sessionId) {
        List<BonusLogVO> list = null;
        try {
            CustomerVO cus = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            if (null == cus) {
                return Result.newFailure("信息不正确");
            }
            list = bonusLogService.selectBonusLogList(cus.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("获取每日利息失败");
        }
        return Result.newSuccess(list, "成功");
    }

    /**
     * 获取验证码（除注册以外）
     *
     * @param request
     * @param sessionId
     * @return
     */
    @RequestMapping("/getAuthCode")
    public Result getAuthCode(HttpServletRequest request, String sessionId) {
        try {
			CustomerVO customerVo = SessionContext.getConstomerInfoBySessionId(request, sessionId);
			CustomerVO customer = customerService.queryCustomerById(customerVo.getId());
			String number=((int)((Math.random()*9+1)*100000))+"";
			String message = SystemEnum.MESSAGE_1.getMessage(number);

			SMSUtil.sendSms(customer.getPhone(), message);
			SMSMap.put(customer.getPhone(), number);

			return Result.newSuccess("获取成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("获取失败", 0);
        }

    }

    /**
     * 团队中心
     *
     * @param request   请求信息
     * @param sessionId 登录用户
     * @return JSON
     */
    @RequestMapping(value = "/teamCenter", method = RequestMethod.GET)
    public Result teamCenter(HttpServletRequest request, String sessionId) {
        try {
            CustomerVO customerVO = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            CustomerDTO cusetomerDTO = customerService.getTeamCenter(customerVO.getId());
            return Result.newSuccess(cusetomerDTO, "获取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("获取失败");
        }
    }

    /**
     * 激活码转增
     *
     * @param request   请求信息
     * @param sessionId 登录用户
     * @param vo        转增信息
     * @param account   转增数量
     * @return JSON
     */
    @RequestMapping("/giveActivation")
    public Result giveActivation(HttpServletRequest request, String sessionId, CustomerVO vo, int account) {
        if (sessionId == null) {
            Result.newFailure("sessionId不能为空");
        }
        try {
            return customerService.savaActivation(request, sessionId, vo, account);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("激活码转增失败", 0);
        }
    }

    /**
     * 根据用户id查询被推荐人的信息
     *
     * @param request
     * @param sessionId
     * @return
     */
    @RequestMapping("/queryExtensionAward")
    public Result queryExtensionAward(HttpServletRequest request, String sessionId) {
        try {
            CustomerVO customerVO = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            List<CustomerDTO> data = recommendLogService.queryExtensionAward(customerVO.getId());
            if (null == data || data.size() == 0) {
                return Result.newFailure("暂无数据", 0);
            }
            return Result.newSuccess(data, "获取成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("获取失败");
        }
    }

    /**
     * 直推人数
     *
     * @param request   请求信息
     * @param sessionId 登录用户
     * @return JSON
     */
    @RequestMapping("/agentNum")
    public Result agentNum(HttpServletRequest request, String sessionId) {

        if (sessionId == null) {
            Result.newFailure("sessionId不能为空");
        }
        try {
            return customerService.agentNum(request, sessionId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("获取失败", 0);
        }
    }

    /**
     * 显示logo图标
     *
     * @param request
     * @return
     */
    @RequestMapping("/showLogo")
    public Result showLogo(HttpServletRequest request) {
        try {
            String logo = SystemConstant.LOCAL_IMAGES + DictionariesService.dictionaries.get(SystemEnum.LOGO.getDesc());
            return Result.newSuccess(logo, "成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("", 0);
        }
    }

    /**
     * 财务中心的所有金额显示
     *
     * @param request
     * @param sessionId
     * @return
     */
    @RequestMapping("/financialCenter")
    public Result financialCenter(HttpServletRequest request, String sessionId) {
        try {
            CustomerVO customerVO = SessionContext.getConstomerInfoBySessionId(request, sessionId);
            return customerService.financialCenter(customerVO.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("失败", 0);
        }
    }

    /**
     * 修改头像
     *
     * @return
     */
    @RequestMapping("/updatePersonalImage")
    public Result updatePersonalImage(HttpServletRequest request, String sessionId, @RequestParam("headImg") MultipartFile file) {
        CustomerVO customerVO = SessionContext.getConstomerInfoBySessionId(request, sessionId);
        try {
            if (customerVO == null) {
                return Result.newFailure("请重新登录", 5000);
            }
            CustomerVO vo = customerService.queryCustomerById(customerVO.getId());
            if (file != null) {
                String location = SystemConstant.LOCAL_PATH + customerVO.getId() + File.separator;
                File filePath = new File(location);
                if (!filePath.exists() && !filePath.isDirectory()) {
                    filePath.mkdirs();
                }
                Files.write(Paths.get(location + file.getOriginalFilename()), file.getBytes());
            }
            CustomerVO customer = new CustomerVO();
            customer.setId(vo.getId());
            customer.setHeadImg(SystemConstant.LOCAL_IMAGES + File.separator + customerVO.getId() + File.separator + file.getOriginalFilename());
            customerService.updatePersonalImage(customer);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员", 0);
        }
        return Result.newSuccess("修改成功", 1);
    }

    /**
     * 查询关于我们
     *
     * @param request
     * @param sessionId
     * @return
     */
    @RequestMapping("/queryAboutUs")
    public Result queryAboutUs(HttpServletRequest request,String sessionId) {
        try {
            String data = dictionariesService.queryAboutUs();
            if (null == data) {
                return Result.newFailure("暂无数据", 0);
            }
            return Result.newSuccess(data,"成功",1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员", 0);
        }
    }

    /**
     * 查询所有客服帮助
     *
     * @param request
     * @param sessionId
     * @return
     */
    @RequestMapping("/queryAllCustomerHelp")
    public Result queryAll(HttpServletRequest request,String sessionId) {
        try {
            List<CustomHelpVO> data = customHelpService.queryAll();
            if (null == data) {
                return Result.newFailure("暂无数据",0);
            }
            return Result.newSuccess(data,"成功",1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("请联系管理员", 0);
        }
    }
}
