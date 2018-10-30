package com.daka.controller.bank;

import com.daka.api.base.Result;
import com.daka.controller.CommonController;
import com.daka.entry.BankVO;
import com.daka.entry.CustomerVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.service.bank.BankService;
import com.daka.service.sellout.SelloutService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/bank")
public class BankController extends CommonController {

    @Autowired
    BankService bankService;

    @Autowired
    SelloutService selloutService;

    /**
     * 跳转到list.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toList")
    public Object toList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/bank/list");
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
        List<PageData> bankList = null;
        try {
            bankList = bankService.queryBanklistPage(page);
            JSONObject result = new JSONObject();
            result.put("status", "200");
            result.put("message", "");
            result.put("count", page.getTotalResult());
            result.put("data", bankList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("",0);
        }
    }

    /**
     * 删除
     *
     * @param request
     * @return
     */
    @RequestMapping("/deleteData")
    public Result deleteData(HttpServletRequest request) {
        try {
            String id = request.getParameter("id");
            if (null == id || "".equals(id)){
                return Result.newSuccess("删除失败", 1);
            }
            bankService.deleteById(Integer.valueOf(id));
            return Result.newSuccess("删除成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("删除失败", 1);
        }
    }
    /**
     * 跳转到add.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toAdd")
    public Object toUpdateAndSave(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/bank/add");
        return mv;
    }

    /**
     * 添加/修改
     * @param request
     * @return
     */
    @RequestMapping(value = "/save")
    public Result save(HttpServletRequest request,String userName) {
        BankVO bankVO = (BankVO) getParam(request, BankVO.class);
        try {
            if (null == userName || "".equals(userName)){
                return Result.newSuccess("请输入用户名");
            }
            Result result = selloutService.checkUserNameInfo(userName);
            if (null == result.getData() || "".equals(result.getData())){
                return Result.newSuccess("此用户不存在");
            }
            CustomerVO vo = (CustomerVO) result.getData();
            bankVO.setCustomerId(vo.getId());
            return bankService.saveBank(bankVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("添加失败");
        }
    }
}
