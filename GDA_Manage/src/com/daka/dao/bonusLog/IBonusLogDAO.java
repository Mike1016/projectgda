package com.daka.dao.bonusLog;

import com.daka.entry.BonusLogVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IBonusLogDAO {

    List<PageData> queryBonusLoglistPage(Page page) throws Exception; //后台 分页查询

    void insertBonusLog() throws Exception; //新增日利息

    List<BonusLogVO> selectBonusLogList(@Param("customerId") Integer customerId) throws Exception; //获取每日利息记录

    void delectBonusLogById(Integer id) throws Exception;//删除每日利息日志
}
