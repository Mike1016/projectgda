package com.daka.service.dictionaries;

import com.daka.dao.dictionaries.IDictionariesDAO;
import com.daka.entry.DictionariesVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictionariesService {

    public static Map<String,String> dictionaries = new HashMap<>();

    @Autowired
    IDictionariesDAO iDictionariesDAO;

    public void init() {
        List<DictionariesVO> list = iDictionariesDAO.queryAll();
        for (DictionariesVO vo : list) {
            dictionaries.put(vo.getType(), vo.getParameter());
        }
    }

    /**
     * 分页查询
     *
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> queryDictionarieslistPage(Page page) throws Exception {
        return iDictionariesDAO.queryDictionarieslistPage(page);
    }

    /**
     * 修改
     *
     * @param dictionariesVO
     * @throws Exception
     */
    public void updateDictionaries(DictionariesVO dictionariesVO) throws Exception {
        if (dictionariesVO == null){
            throw new Exception("请输入完整信息");
        }
        iDictionariesDAO.updateDictionaries(dictionariesVO);
        dictionaries.clear();
        init();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     * @throws Exception
     */
    public DictionariesVO queryById(String id) throws Exception{
        return iDictionariesDAO.queryById(id);
    }

    /**
     * 添加
     * @param dictionariesVO
     * @throws Exception
     */
    public void saveDictionaries(DictionariesVO dictionariesVO) throws Exception{
        if (dictionariesVO == null){
            throw new Exception("请输入完整信息");
        }
        dictionariesVO.setId(String.valueOf(System.currentTimeMillis()));
        iDictionariesDAO.saveDictionaries(dictionariesVO);
        dictionaries.clear();
        init();
    }

    /**
     * 根据类型查询字典对象
     * @param type
     * @return
     * @throws Exception
     */
    public DictionariesVO selectParameter(String type) throws Exception{
        return iDictionariesDAO.selectParameter(type);
    }

    /**
     * 查询关于我们
     * @return
     * @throws Exception
     */
    public String queryAboutUs() throws Exception {
        return iDictionariesDAO.queryAboutUs();
    }
}
