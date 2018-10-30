package com.daka.dao.customHelp;

import com.daka.entry.CustomHelpVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;

import java.util.List;

public interface ICustomHelpDAO {

    List<PageData> queryCustomHelplistPage(Page page) throws Exception;//分页查询

    void saveCustomHelp(CustomHelpVO customHelpVO) throws Exception;//添加

    List<CustomHelpVO> queryAll() throws Exception; //查询

    void updateCustomHelpById(CustomHelpVO customHelpVO) throws Exception;//修改
}
