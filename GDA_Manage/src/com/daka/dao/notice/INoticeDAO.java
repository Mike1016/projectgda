package com.daka.dao.notice;

import com.daka.entry.NoticeVO;
import com.daka.entry.Page;

import java.util.List;
import java.util.Map;

public interface INoticeDAO {

    List<NoticeVO> queryNotice() throws Exception; // 显示所有公告

    /**
     * 分页查询
     * @param page
     * @return
     */
    List<Map<String,Object>> queryDatalistPage(Page page) throws Exception;

    /**
     * 根据id删除公告信息
     * @param id
     */
    void deleteNoticeById(String id) throws Exception;

    /**
     * 根据id查询数据
     * @return
     */
    Map<String,Object> queryNoticeById(String id) throws Exception;

    /**
     * 根据id修改信息
     * @param id
     */
    void updateNoticeById(Map<String,Object> map) throws Exception;

    /**
     * 插入公告数据
     */
    void insertNotice(NoticeVO noticeVO) throws Exception;

}
