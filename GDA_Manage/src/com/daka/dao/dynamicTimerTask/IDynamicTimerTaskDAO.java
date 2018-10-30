package com.daka.dao.dynamicTimerTask;

import java.util.List;

import com.daka.entry.Page;
import com.daka.entry.PageData;
import org.springframework.stereotype.Repository;

import com.daka.entry.DynamicTimerTaskVO;

@Repository
public interface IDynamicTimerTaskDAO
{

	/**
	 * 新增
	 * 
	 * @param param
	 */
	void saveDynamicTimerTask(DynamicTimerTaskVO param);

	/**
	 * 根据任务名称删除
	 * 
	 * @param taskName
	 */
	void delDynamicTimerTaskByTaskName(String taskName);

	/**
	 * 查询所有的动态任务
	 */
	List<DynamicTimerTaskVO> queryDynamicTimerTask();

	List<PageData> queryDynamicTimerTasklistPage(Page page) throws Exception; //后台 分页查询

	DynamicTimerTaskVO queryDynamicTimerTaskByTaskName(String taskName) throws Exception;//根据task_name查询正在执行的定时任务
}
