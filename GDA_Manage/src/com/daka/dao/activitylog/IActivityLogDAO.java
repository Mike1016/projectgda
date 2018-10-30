package com.daka.dao.activitylog;

import java.util.List;
import java.util.Map;
import com.daka.entry.ActivityLogVO;

public interface IActivityLogDAO {
	
	void insert(ActivityLogVO vo) throws Exception;//添加活力值变更记录
	
	List<ActivityLogVO> findBycustomer(Map<String, Object> map) throws Exception;//根据id查询发货记录
	
}
