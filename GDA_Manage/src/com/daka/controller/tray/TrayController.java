package com.daka.controller.tray;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.daka.constants.SystemConstant;
import com.daka.entry.DictionariesVO;
import com.daka.enums.SystemEnum;
import com.daka.util.Tools;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.daka.api.base.Result;
import com.daka.entry.TrayVO;
import com.daka.service.tray.TrayService;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tray")
public class TrayController {
    @Autowired
    private TrayService trayservice;

    /**
     * 跳转页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/toList")
    public Object toList(HttpServletRequest request) {
        ModelAndView model = new ModelAndView("tray/list");
        return model;
    }

    /**
     * 分页查询转盘信息   可以根据档数查询不同档数的信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/querydata")
    @ResponseBody
    public Object querydata(HttpServletRequest request) {
        try {
            return trayservice.queryTraylistPage(request);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Result.newFailure("系统错误，请联系管理员", 0);
        }

    }

    /**
     * 跳转到修改页面
     *
     * @param request
     * @return
     */
    @RequestMapping("/updateTray")
    public Object updateTray(HttpServletRequest request) {
        ModelAndView model = new ModelAndView("tray/update");
        return model;
    }

    /**
     * 返回被修改的奖品信息
     *
     * @param request
     * @param
     * @return
     */
    @RequestMapping("/queryDataById")
    public Object queryDataById(HttpServletRequest request) {
        String id = request.getParameter("id");
        ModelAndView mv = new ModelAndView("/tray/review");
        Result result = null;
        try {
            result = trayservice.queryTrayById(Integer.valueOf(id));
            mv.addObject("result", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 图片上传和下载
     *
     * @param: [uploadFile, session]
     * @return: java.lang.String
     */
    @RequestMapping("/upload")
    @ResponseBody
    public Object upload(MultipartFile file, HttpServletRequest request) {
        String fileName = file.getOriginalFilename();
        // 获取图片的扩展名
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 新的图片文件名 = 获取时间戳+"."图片扩展名
        String time = String.valueOf("prize"+System.currentTimeMillis());
        String newFileName = time + "." + extensionName;

        List<MultipartFile> files = new ArrayList<MultipartFile>();
        files.add(file);
        try {
            Tools.uploadFile(files, time, request, "prize");
            String src = Tools.UPLOAD_PATH + File.separator + "prize" + File.separator + newFileName;
            //上传成功返回信息
            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("message", "上传成功");
            result.put("src",".."+src+"");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("上传失败", 0);
        }
    }

    /**
     * 修改转盘 中的一项奖品信息
     *
     * @param request
     * @param vo      id,type,account,probability,prizes
     * @param
     * @return
     */
    @RequestMapping("/updateTurntable")
    @ResponseBody
    public Object updateTurntable(HttpServletRequest request, TrayVO vo, @RequestParam("contextImg") MultipartFile file) {
        if (vo == null) {
            return Result.newFailure("信息不全，请重试", 0);
        }
        if (file == null) {
            return Result.newFailure("图片错误，请重新选择", 0);
        }
        try {
            return trayservice.updateByGrade(vo, file);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newFailure("系统错误，请联系管理员", 0);
        }
    }

    /**
     * 查询一个档数的奖品信息
     *
     * @param: [trayGrade] 转盘档数
     * @return: com.daka.api.base.Result
     */
    @RequestMapping("/queryTrayByGrade")
    @ResponseBody
    public Result queryTrayByGradet(HttpServletRequest request) {
        String trayGrade = request.getParameter("trayGrade");
        Result result = null;
        try {
            result = trayservice.queryTurntableByTrayGradet(Integer.valueOf(trayGrade));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("查询失败", 0);
        }
    }

    /**
     * 修改奖品信息
     *
     * @param: [trayVO]
     * @return: void
     */
    @RequestMapping("/modifyTray")
    @ResponseBody
    public Result modifyTray(HttpServletRequest request, TrayVO trayVO) {
        try {
           return trayservice.updatePrize(trayVO);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("操作失败", 0);
        }


    }


}
