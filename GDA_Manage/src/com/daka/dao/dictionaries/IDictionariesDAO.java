package com.daka.dao.dictionaries;

import com.daka.entry.DictionariesVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;

import java.util.List;

public interface IDictionariesDAO {

	List<PageData> queryDictionarieslistPage(Page page) throws Exception;//分页查询

	void updateDictionaries(DictionariesVO dictionariesVO) throws Exception;//修改

	DictionariesVO queryById(String id) throws Exception; //根据id查询

	DictionariesVO selectParameter(String type) throws Exception; //根据类型查询字典对象

	List<DictionariesVO> queryAll(); // 查询所有

	void saveDictionaries(DictionariesVO dictionariesVO) throws Exception;//添加

	String queryAboutUs() throws Exception; //查询关于我们
}
