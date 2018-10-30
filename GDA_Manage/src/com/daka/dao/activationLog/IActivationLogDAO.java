package com.daka.dao.activationLog;

import com.daka.entry.ActivationLogVO;
 
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.entry.dto.IActivationLogDTO;

import java.util.List;
import java.util.Map;


/**
 * 激活日志 DAO
 */
public interface IActivationLogDAO {

    void saveBatch(List<ActivationLogVO> list) throws Exception;//批量添加

    List<PageData> queryActivationLoglistPage(Page page) throws Exception; // 后台 分页查询
    
    void updateActivationLog(ActivationLogVO activationLog) throws Exception; //修改激活日志信息
    
    List<ActivationLogVO> selectActivationLogById(int id) throws Exception; //根据用户id查询激活信息

    void delectActivationLogById(Integer id) throws Exception;//删除激活日志
    
    List<IActivationLogDTO>  selectActivationLogInfo(Map<String, Object> map) throws Exception; //根据激活码类型和用户id查询激活信息
    
    void saveActivatorLog(ActivationLogVO activationLog) throws Exception; //增加激活日志
}
