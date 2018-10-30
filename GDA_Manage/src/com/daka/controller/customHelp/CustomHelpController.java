package com.daka.controller.customHelp;

import com.daka.api.base.Result;
import com.daka.constants.SystemConstant;
import com.daka.controller.CommonController;
import com.daka.entry.CustomHelpVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.enums.SystemEnum;
import com.daka.service.customHelp.CustomHelpService;
import com.daka.util.DateUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/customHelp")
public class CustomHelpController extends CommonController {
    @Autowired
    CustomHelpService customHelpService;

    /**
     * 跳转到list.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toList")
    public Object toList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/customHelp/list");
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
        List<PageData> customHelpList = null;
        try {
            customHelpList = customHelpService.queryCustomHelplistPage(page);
            JSONObject result = new JSONObject();
            result.put("status", "200");
            result.put("message", "");
            result.put("count", page.getTotalResult());
            result.put("data", customHelpList);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("",0);
        }
    }

    /**
     * 跳转到add.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toAdd")
    public Object toUpdateAndSave(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/customHelp/add");
        CustomHelpVO customHelpVO = (CustomHelpVO)getParam(request,CustomHelpVO.class);
        try {
            if (customHelpVO != null){
                customHelpVO.setImgPath(customHelpVO.getImgPath().replaceAll("wximg","/wximg"));
                mv.addObject("result", customHelpVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    /**
     * 添加
     * @return
     */
    @RequestMapping("/save")
    public Result save(String imgPath){
        try {
            if (null == imgPath || imgPath == ""){
                return Result.newSuccess("请选择需要上传的图片");
            }
            CustomHelpVO customHelpVO = new CustomHelpVO();
            customHelpVO.setImgPath(imgPath);
            customHelpVO.setCreateTime(DateUtil.getTime());
            customHelpService.saveCustomHelp(customHelpVO);
            return Result.newSuccess("添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return Result.newSuccess("添加失败");
        }
    }

    /**
     * 修改
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(Integer id ,String imgPath){
        try {
            if (null == imgPath || imgPath == ""){
                return Result.newSuccess("请选择需要上传的图片");
            }
            if (null == id){
                return Result.newSuccess("未接收到参数");
            }
            CustomHelpVO customHelpVO = new CustomHelpVO();
            customHelpVO.setId(id);
            customHelpVO.setImgPath(imgPath);
            customHelpVO.setCreateTime(DateUtil.getTime());
            customHelpService.updateCustomHelpById(customHelpVO);
            return Result.newSuccess("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return Result.newSuccess("修改失败");
        }
    }

    /**
     * 上传图片
     * @param file
     */
    @RequestMapping("/upLoad")
    public Result upLoad(MultipartFile file) throws Exception{
        try {
            if (null == file){
                return Result.newSuccess("请选择图片");
            }
            String fileName = file.getOriginalFilename();
            // 获取图片的扩展名
            String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
            // 新的图片文件名 = 获取时间戳+"."图片扩展名
            String time = String.valueOf(SystemEnum.WXIMG.getDesc() + System.currentTimeMillis());
            String newFileName = time + "." + extensionName;
            String location = SystemConstant.LOCAL_PATH + SystemEnum.WXIMG.getDesc() + File.separator;
            File filePath = new File(location);
            if (!filePath.exists() && !filePath.isDirectory())
            {
                filePath.mkdirs();
            }
            Files.write(Paths.get(location + newFileName), file.getBytes());
            String path = SystemConstant.LOCAL_IMAGES + File.separator + SystemEnum.WXIMG.getDesc() +  File.separator +newFileName;
            return Result.newSuccess(path,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return Result.newSuccess("修改失败");
        }

    }
}
