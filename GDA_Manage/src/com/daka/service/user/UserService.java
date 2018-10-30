package com.daka.service.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.daka.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.entry.SysUserVO;
import com.daka.dao.user.SysUserDAO;

@Service
public class UserService {
	@Autowired
	SysUserDAO sysUserDao;
	
	/**
	 * 根据账号和密码查询账号
	 * @return
	 */
	public SysUserVO queryUser(SysUserVO sysUserVO) {
		return sysUserDao.queryUser(sysUserVO);
	}
	
	/**
	 * 根据id查询账号
	 * @return
	 */
	public SysUserVO queryUserById(Integer id) {
		return sysUserDao.queryUserById(id);
	}
	
	/**
	 * 根据账号查询
	 * @return
	 */
	public SysUserVO queryUserByName(String name) {
		return sysUserDao.queryUserByName(name);
	}
	
	/**
	 * 查询所有(分页)
	 * @param page
	 * @return
	 */
	public List<PageData> queryCustomerlistPage(Page page) {
		return sysUserDao.queryUserlistPage(page);
	}
	
	/**
	 * 添加
	 * @param sysUserVO
	 * @return
	 */
	public void saveUser(SysUserVO sysUserVO) {
		sysUserVO.setCreateTime(DateUtil.getTime());
		sysUserDao.saveUser(sysUserVO);
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public void delUserById(Integer id) {
		sysUserDao.delUserById(id);
	}
	
	public void updateUser(SysUserVO sysUserVO) {
		sysUserDao.updateUser(sysUserVO);
	}
	
	public Map<String,Object> selectUserById(Integer id) {
		return sysUserDao.selectUserById(id);
	}
}
