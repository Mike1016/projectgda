package com.daka.controller.user;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.daka.constants.SystemConstant;
import com.daka.controller.CommonController;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.entry.SysUserVO;
import com.daka.service.user.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController extends CommonController
{
	@Autowired
	private UserService userService;

	/**
	 * 登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Object login(HttpServletRequest request)
	{
		SysUserVO user = (SysUserVO) getParam(request, SysUserVO.class);
		SysUserVO loginList = userService.queryUser(user);
		if (null == loginList)
		{
			return SystemConstant.FAIL_CODE;
		}
		request.getSession().setAttribute(SystemConstant.SYS_USER, loginList);
		return SystemConstant.SUCCESS_CODE;
	}

	/**
	 * 退出登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public Object logout(HttpServletRequest request)
	{
		request.getSession().removeAttribute(SystemConstant.SYS_USER);
		return "login";
	}

	/**
	 * 跳转页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toList")
	public Object toList(HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView("user/list");
		return mv;
	}

	/**
	 * 跳转页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toAdd_update")
	public Object toAddUpdate(HttpServletRequest request)
	{
		SysUserVO sysUserVO;
		ModelAndView mv = new ModelAndView("user/edit_add");
		if("add".equals(request.getParameter("btnType"))){
			return mv;
		}else{
			String id=request.getParameter("id");
			sysUserVO=userService.queryUserById(Integer.parseInt(id));
			mv.addObject("result", sysUserVO);
		}
		return mv;
	}

	@RequestMapping(value = "/add")
	public Object Add(HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView("user/add");
		return mv;
	} 
	
	/**
	 * 添加用户
	 * @param request
	 * @param sysUserVO
	 * @return
	 */
	@RequestMapping("/insertUser")
	@ResponseBody
	public Object insertUser(HttpServletRequest request,SysUserVO sysUserVO) {
		userService.saveUser(sysUserVO);
		JSONObject result = new JSONObject();
		result.put("status", "200");
		result.put("message", "OK");
		return result;
	}
	
	/**
	 * 修改信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/toReview")
	public Object toReview(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map<String, Object> map = userService.selectUserById(Integer.parseInt(id));
		ModelAndView mv = new ModelAndView("/user/review");
		mv.addObject("result", map);
		return mv;
	}

	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryData")
	@ResponseBody
	public Object queryData(HttpServletRequest request)
	{
		PageData pd = new PageData(request);
		Page page = new Page();
		page.setPd(pd);
		List<PageData> queryRole = userService.queryCustomerlistPage(page);
		JSONObject result = new JSONObject();
		result.put("code", "200");
		result.put("status", 200);
		result.put("msg", "");
		result.put("count", page.getTotalResult());
		result.put("data", JSONArray.fromObject(queryRole));
		return result;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Object deleteNoticeById(HttpServletRequest request,Integer id) {
		userService.delUserById(id);
		JSONObject result = new JSONObject();
		result.put("status", "200");
		result.put("message", "OK");
		return result;
	}
	
	@RequestMapping("/updateUser")
	@ResponseBody
	public Object updateUser(HttpServletRequest request,SysUserVO sysUserVO) {
		userService.updateUser(sysUserVO);
		JSONObject result = new JSONObject();
		result.put("status", "200");
		result.put("message", "OK");
		return result;
	}

}
