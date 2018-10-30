package com.daka.dao.recommendLog;

import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.entry.RecommendLogVO;
import com.daka.entry.dto.CustomerDTO;

import java.util.List;

public interface IRecommendLogDAO {

    List<PageData> queryRecommendLoglistPage(Page page) throws Exception; //后台 分页查询

    List<CustomerDTO> queryExtensionAward(Integer customerId) throws Exception; //查询推荐奖励

    void saveRecommendList(List<RecommendLogVO> list) throws Exception; //批量新增推荐奖日志

	void updateBatchCustomerBonus(List<RecommendLogVO> list) throws Exception; //批量更新推荐奖
}
