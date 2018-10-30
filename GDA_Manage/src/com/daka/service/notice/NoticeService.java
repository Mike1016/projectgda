package com.daka.service.notice;

import com.daka.dao.notice.INoticeDAO;
import com.daka.entry.NoticeVO;
import com.daka.entry.Page;
import com.daka.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoticeService {

    @Autowired
    private INoticeDAO iNoticeDAO;

    public List<NoticeVO> queryNotice() throws Exception{
        return iNoticeDAO.queryNotice();
    }

    /**
     * 分页查询
     * @param page
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> queryDatalistPage(Page page) throws Exception{
        return iNoticeDAO.queryDatalistPage(page);
    }

    /**
     * 删除
     * @param id
     * @throws Exception
     */
    public void deleteNoticeById(String id) throws Exception{
        iNoticeDAO.deleteNoticeById(id);
    }

    /**
     * 根据id 查询
     * @param id
     * @return
     * @throws Exception
     */
    public Map<String,Object> queryNoticeById(String id) throws Exception{
        return iNoticeDAO.queryNoticeById(id);
    }

    /**
     * 修改
     * @param id
     * @param title
     * @param content
     * @param create_time
     * @throws Exception
     */
    public void updateNoticeById(String id,String title,String content,String create_time) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("title", title);
        map.put("content", content);
        map.put("create_time", create_time);
        iNoticeDAO.updateNoticeById(map);
    }

    /**
     * 添加
     * @param noticeVO
     * @throws Exception
     */
    public void insertNotice(NoticeVO noticeVO) throws Exception{
        noticeVO.setCreateTime(DateUtil.getDay());
        iNoticeDAO.insertNotice(noticeVO);
    }
}
