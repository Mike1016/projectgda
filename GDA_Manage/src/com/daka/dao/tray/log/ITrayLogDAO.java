package com.daka.dao.tray.log;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.daka.entry.TrayLogVO;
import com.daka.entry.dto.TrayLogDTO;

public interface ITrayLogDAO {
	
	void insert(TrayLogVO vo) throws Exception;//添加领奖记录
	
	TrayLogVO findById(@Param("id")int id) throws Exception;//根据id查询发货记录
	
	void update(TrayLogVO vo)throws Exception;//发货
	
	List<TrayLogVO> findByCustomer(Map<String, Object> map)throws Exception;//查询用户的抽奖记录
	
	List<TrayLogVO> trayLogList(int id)throws Exception; //根据用户id查询中奖信息
	
	List<TrayLogDTO> findAll() throws Exception;//分页查询所有用户的中奖记录
	
}
