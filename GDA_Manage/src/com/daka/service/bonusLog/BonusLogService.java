package com.daka.service.bonusLog;

import com.daka.dao.bonusLog.IBonusLogDAO;
import com.daka.entry.BonusLogVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BonusLogService {

    @Autowired
    IBonusLogDAO bonusLogDAO;

    /**
     * 分页模糊查询
     *
     * @param page
     * @return
     */
    public List<PageData> queryBonusLoglistPage(Page page) throws Exception {
        return bonusLogDAO.queryBonusLoglistPage(page);
    }

    /**
     * 获取每日利息
     * @param customerId 用户ID
     * @return List BonusLogVO
     * @throws Exception 异常
     */
    public List<BonusLogVO> selectBonusLogList(Integer customerId) throws Exception {
        return bonusLogDAO.selectBonusLogList(customerId);
    }

    /**
     * 删除
     * @param id
     * @throws Exception
     */
    public void delectBonusLogById(Integer id) throws Exception{
        bonusLogDAO.delectBonusLogById(id);
    }
}
