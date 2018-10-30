package com.daka.service.customHelp;

import com.daka.dao.customHelp.ICustomHelpDAO;
import com.daka.entry.CustomHelpVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CustomHelpService {
    @Autowired
    private ICustomHelpDAO customHelpDAO;

    /**
     * 后台查询
     * @return
     * @throws Exception
     */
    public List<PageData> queryCustomHelplistPage(Page page) throws Exception{
        return customHelpDAO.queryCustomHelplistPage(page);
    }

    /**
     * 查询所有
     * @return
     * @throws Exception
     */
    public List<CustomHelpVO> queryAll() throws Exception {
        return customHelpDAO.queryAll();
    }

    /**
     * 添加客服信息
     * @param customHelpVO
     * @throws Exception
     */
    public void saveCustomHelp(CustomHelpVO customHelpVO) throws Exception{
        customHelpDAO.saveCustomHelp(customHelpVO);
    }

    /**
     * 修改客服信息
     * @param customHelpVO
     * @throws Exception
     */
    public void updateCustomHelpById(CustomHelpVO customHelpVO) throws Exception{
        customHelpDAO.updateCustomHelpById(customHelpVO);
    }
}
