package com.daka.controller.dictionaries;

import com.daka.api.base.Result;
import com.daka.constants.SystemConstant;
import com.daka.controller.CommonController;
import com.daka.entry.DictionariesVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.enums.SystemEnum;
import com.daka.service.dictionaries.DictionariesService;
import com.daka.util.Tools;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dictionaries")
public class DictionariesController extends CommonController {

    @Autowired
    DictionariesService dictionariesService;

    /**
     * 跳转到list.jsp
     * @param request
     * @return
     */
    @RequestMapping("/toList")
    public Object toList(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/dictionaries/list");
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
        List<PageData> dictionariesList = null;
        try {
            dictionariesList = dictionariesService.queryDictionarieslistPage(page);
            JSONObject result = new JSONObject();
            result.put("status", "200");
            result.put("message", "");
            result.put("count", page.getTotalResult());
            result.put("data", dictionariesList);
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
        DictionariesVO dictionariesVO = null;
        ModelAndView mv = new ModelAndView("/dictionaries/add");
        String id=request.getParameter("id");
        try {
            dictionariesVO=dictionariesService.queryById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.addObject("result", dictionariesVO);
        return mv;
    }

    /**
     * 修改
     *
     * @param request
     * @return
     */
    @RequestMapping("/updateData")
    public Result updateData(HttpServletRequest request) {
        try {
            DictionariesVO dictionariesVO = (DictionariesVO) getParam(request, DictionariesVO.class);
            dictionariesService.updateDictionaries(dictionariesVO);
            return Result.newSuccess("修改成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("修改失败", 0);
        }
    }

    /**
     * 添加
     *
     * @param request
     * @return
     */
    @RequestMapping("/save")
    public Result save(HttpServletRequest request) {
        try {
            DictionariesVO dictionariesVO = (DictionariesVO) getParam(request, DictionariesVO.class);
            if (dictionariesVO != null){
                DictionariesVO vo = dictionariesService.selectParameter(dictionariesVO.getType());
                if (null == vo){
                    dictionariesService.saveDictionaries(dictionariesVO);
                }else{
                    return Result.newSuccess("已存在该类型的数据", 1);
                }
            }
            return Result.newSuccess("添加成功", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("添加失败", 0);
        }
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile file, HttpServletRequest request) {
        String fileName = file.getOriginalFilename();
        // 获取图片的扩展名
        String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 新的图片文件名 = 获取时间戳+"."图片扩展名
        String time = String.valueOf(SystemEnum.LOGO.getDesc() + System.currentTimeMillis());
        String newFileName = time + "." + extensionName;
        try {
            if(file != null) {
                String location = SystemConstant.LOCAL_PATH + SystemEnum.LOGO.getDesc() + File.separator;
                File filePath = new File(location);
                if (!filePath.exists() && !filePath.isDirectory())
                {
                    filePath.mkdirs();
                }
                Files.write(Paths.get(location + newFileName), file.getBytes());
            }
            DictionariesVO dictionariesVO = new DictionariesVO();
            dictionariesVO.setType(SystemEnum.LOGO.getDesc());
            dictionariesVO.setParameter(File.separator + SystemEnum.LOGO.getDesc() + File.separator + newFileName);
            dictionariesService.updateDictionaries(dictionariesVO);
            return Result.newSuccess("上传成功", 0);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.newSuccess("上传失败", 0);
        }
    }
    
    /**
     * 关于我们
     *
     * @param request
     * @return
     */
    @RequestMapping("/select")
    public Result select(HttpServletRequest request) {   	
    	try {
    		DictionariesVO dictionariesVO = (DictionariesVO) getParam(request, DictionariesVO.class);
    		dictionariesService.selectParameter(dictionariesVO.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	 return Result.newSuccess("获取失败", 0);
    }
}
