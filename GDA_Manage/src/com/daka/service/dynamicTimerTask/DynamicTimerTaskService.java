package com.daka.service.dynamicTimerTask;

import com.daka.dao.dynamicTimerTask.IDynamicTimerTaskDAO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DynamicTimerTaskService {

    @Autowired
    IDynamicTimerTaskDAO dynamicTimerTaskDao;

    /**
     * 分页模糊查询
     *
     * @param page
     * @return
     */
    public List<PageData> queryDynamicTimerTasklistPage(Page page) throws Exception {
        return dynamicTimerTaskDao.queryDynamicTimerTasklistPage(page);
    }
}
