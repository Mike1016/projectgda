package com.daka.dao.vitalityLog;

import java.util.List;
import java.util.Map;
import com.daka.entry.VitalityLogVO;

public interface IVitalityLogDAO {
	
	void insert(VitalityLogVO vo) throws Exception;//新增记录
	
	List<VitalityLogVO> findAll(Map<String, Object> map) throws Exception;//查询用户的生命值记录
	
	void insertVitalityLog() throws Exception;//添加每日根据不同等级的用户扣除用户生命力的记录
	
	void insertBatch(List<VitalityLogVO> list) throws Exception;//添加 后台给单个用户或者所有用户增加生命 记录
	
}
