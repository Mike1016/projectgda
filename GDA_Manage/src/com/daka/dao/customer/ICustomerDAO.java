package com.daka.dao.customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.daka.entry.CustomerVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.entry.dto.CustomerDTO;
import org.apache.ibatis.annotations.Param;

public interface ICustomerDAO
{

	List<PageData> queryCustomerlistPage(Page page) throws Exception; // 后台 分页查询

	void updateDaliyLife(Map<String, Integer> map) throws Exception;// 每日减少相应等级的激活用户的生命力

	void updateVitalityLower() throws Exception;// 生命值低于0的修改为0

	void updateVitalityUpper() throws Exception;// 生命值高于100的修改为100

	void updateMessage(CustomerVO customerVO) throws Exception; // 完善个人信息

	CustomerVO queryCustomerById(Integer id) throws Exception; // 根据用户id查询用户信息

	int deleteById(Integer id) throws Exception;// 后台 删除

	int saveCustomer(CustomerVO customerVO) throws Exception;// 后台 添加

	CustomerVO queryById(Integer id) throws Exception; // 根据id查询

	void updateBatch(List<CustomerVO> list) throws Exception; // 批量修改用户的激活数、生命力

	List<CustomerVO> queryAll(CustomerVO customerVO) throws Exception; // 查询所有用户

	int saveRegister(CustomerVO vo) throws Exception; // 注册

	CustomerVO login(CustomerVO vo) throws Exception; // 登录

	int getUserNameCount(String phone) throws Exception; // 根据手机号关联几个用户

	CustomerVO getExtensionCodInfo(String extensionCode) throws Exception; // 根据二维码查询父id信息

	CustomerVO checkUserNameInfo(String userName) throws Exception; // 根据用户名查询信息

	int selectIdCount() throws Exception; // 查询id总数

	List<Map<String, String>> queryAllCustomerWithRelation(); // 获取所有的用户信息（子ID，父ID），

	void updateCustomerWealth() throws Exception; // 更新用户每日利息财富值

	CustomerVO getCustomerById(int customerId) throws Exception; //根据用户id查询用户对象

	int selectPushCount(Integer customerId) throws Exception; //获取直推人数

	List<CustomerVO> selectEffectiveTeamList(@Param("ids") String ids) throws Exception; //获取有效的团队

	String selectAgentIdListById(int id) throws Exception; //根据用户id查询所有向上父id信息
	
	List<CustomerVO> selectAgentIdList(int id) throws Exception; //根据id查询推荐用户信息

	List<String> queryLevelByAgentId(Integer agentId) throws Exception; //根据父级id查询直属下级等级

	int getCustomerCountByAgentId(int agentId) throws Exception; //根据父id查询直推人数

	List<CustomerDTO> selectLastAccountList(@Param("ids") String ids, @Param("idList") String id) throws Exception; //获取树形父级成员的最近一单金额

	void updateCustomerStateByPlatoonId(Integer platoonId) throws Exception; //根据排单Id修改用户状态

	void updateCustomerWealthById(@Param("wealth") BigDecimal wealth, @Param("id") Integer id) throws Exception; //根据Id修改财富值
}
