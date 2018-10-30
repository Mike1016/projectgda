package com.daka.controller.customer;

import com.daka.api.base.Result;
import com.daka.controller.CommonController;
import com.daka.entry.CustomerVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.enums.CustomerCustomEnum;
import com.daka.service.customer.CustomerService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController extends CommonController {
    @Autowired
    CustomerService customerService;

    /**
     * 跳转到list.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toList")
    public Object toList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/customer/list");
        return mv;
    }

    /**
     * 查询
     * @param request
     * @return
     */
    @RequestMapping("/queryData")
    public Object queryData(HttpServletRequest request) {
        PageData pd = new PageData(request);
        Page page = new Page();
        page.setPd(pd);
        List<PageData> customerList = null;
        try {
            customerList = customerService.queryCustomerlistPage(page);
            JSONObject result = new JSONObject();
            result.put("status", "200");
            result.put("message", "");
            result.put("count", page.getTotalResult());
            result.put("data", customerList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("",0);
        }
    }

    /**
     * 删除
     * @param request
     * @return
     */
    @RequestMapping("/deleteData")
    public Result delete(HttpServletRequest request) {
        String id = request.getParameter("id");
        int row = 0;
        try {
            row = customerService.deleteById(Integer.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("删除失败",0);
        }
        if (row > 0){
            return Result.newSuccess("删除成功",1);
        }else {
            return Result.newSuccess("删除失败",0);
        }
    }

    /**
     * 跳转到add.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toAdd")
    public Object toUpdateAndSave(HttpServletRequest request) {
        CustomerVO customerVO = null;
        ModelAndView mv = new ModelAndView("/customer/add");
        String btnType=request.getParameter("btnType");
        if("add".equals(request.getParameter("btnType"))){
            return mv;
        }else{
            String id=request.getParameter("id");
            try {
                customerVO=customerService.queryById(Integer.parseInt(id));
            } catch (Exception e) {
                e.printStackTrace();
            }
            mv.addObject("result", customerVO);
        }
        return mv;
    }

    /**
     * 添加/修改
     * @param request
     * @return
     */
    @RequestMapping(value = "/toAddEdit")
    public Result addEdit(HttpServletRequest request) {
        CustomerVO customerVO = (CustomerVO) getParam(request, CustomerVO.class);
        String type = request.getParameter("btnType");
        try {
            if (CustomerCustomEnum.ADD.getValue().equals(type)){
                if (null == customerVO.getUserName() || customerVO.getUserName() == "") {
                    return Result.newSuccess("请输入用户名", 0);
                }
                if (customerVO.getUserName().length() > 6) {
                    return Result.newSuccess("用户名长度必须小于6位", 0);
                }
                if (null == customerVO.getPassword() || customerVO.getPassword() == "") {
                    return Result.newSuccess("请输入密码", 0);
                }
                if (null == customerVO.getPhone() || customerVO.getPhone() == "") {
                    return Result.newSuccess("请输入电话号码", 0);
                }
            }
            customerService.saveOrUpadteByType(customerVO,type);
            return Result.newSuccess("操作成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("用户已存在", 0);
        }
    }

    /**
     * 跳转到activatingLife.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toActivatingLife")
    public Object toActivatingLife(HttpServletRequest request){
        String id = request.getParameter("id");
        ModelAndView mv = new ModelAndView("customer/vitalityLife");
        if (id != null){
            mv.addObject("id", id);
        }
        return mv;
    }

    /**
     * 配送激活数或者生命力
     * @param request
     * @return
     */
    @RequestMapping("/distribution")
    public Result distribution(HttpServletRequest request){
        try {
            CustomerVO customerVO = (CustomerVO) getParam(request, CustomerVO.class);
            customerService.batchUpdate(customerVO);
            return Result.newSuccess("操作成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("操作失败", 0);
        }
    }
}
