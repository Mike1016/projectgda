package com.daka.dao.tray;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.entry.TrayVO;

public interface ITrayDAO {
	
	TrayVO findByNo(@Param("trayNo")int trayNo) throws Exception;//根据奖品编号查询奖品
	
	List<TrayVO> findByGrade(@Param("trayGrade")int trayGrade) throws Exception;//根据档数查询该档数的转盘信息
	
	List<PageData> queryTraylistPage(Page page) throws Exception; // 后台 分页查询
	
	void update(TrayVO trayvo) throws Exception;//修改转盘的奖品信息
	
	BigDecimal sumProbability(TrayVO vo) throws Exception;//计算此奖品所在的档数其他奖品的概率之和
	
	TrayVO findTrayById(@Param("id")Integer id);//根据id查询奖品信息
	
	BigDecimal sumProbabilitycount(int trayGrade)throws Exception;//计算档数奖品概率之和
}
