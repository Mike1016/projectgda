package com.daka.service.flashbackLog;

import com.daka.dao.flashbackLog.IFlashbackLogDAO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 回溯日志 Service
 */
@Service
public class FlashbackLogService {
	@Autowired
	IFlashbackLogDAO flashbackLogDAO;

	/**
	 * 分页模糊查询
	 *
	 * @param page
	 * @return
	 */
	public List<PageData> queryFlashbackLoglistPage(Page page) throws Exception {
		return flashbackLogDAO.queryFlashbackLoglistPage(page);
	}

	/**
	 * 删除
	 * @param id
	 * @throws Exception
	 */
	public void delectFlashbackLogById(Integer id) throws Exception{
		flashbackLogDAO.delectFlashbackLogById(id);
	}
}
