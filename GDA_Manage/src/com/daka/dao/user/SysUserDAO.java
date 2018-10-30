package com.daka.dao.user;

import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.entry.SysUserVO;

import java.util.List;
import java.util.Map;

public interface SysUserDAO {
	
	 List<PageData> queryUserlistPage(Page page); //分页查询
	 
	 SysUserVO queryUser(SysUserVO sysUserVO); //查询账号
	 
	 SysUserVO queryUserById(Integer id); //根据id查询账号
	 
	 Map<String,Object> selectUserById(Integer id);
	 
	 SysUserVO queryUserByName(String name); //根据账号查询
	 
	 void saveUser(SysUserVO sysUserVO); //添加
	 
	 void delUserById(Integer id); //删除
	 
	 void updateUser(SysUserVO sysUserVO); //修改
	 
}
