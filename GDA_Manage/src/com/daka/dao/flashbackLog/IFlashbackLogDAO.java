package com.daka.dao.flashbackLog;

import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.entry.TlashbackLogVO;

import java.util.List;

/**
 * 回溯日志 Interface
 */
public interface IFlashbackLogDAO {

    List<PageData> queryFlashbackLoglistPage(Page page) throws Exception;//分页查询

    void saveFlashbackLog(List<TlashbackLogVO> list) throws Exception;//添加交易回溯日志

    void delectFlashbackLogById(Integer id) throws Exception;//删除回溯日志
}
