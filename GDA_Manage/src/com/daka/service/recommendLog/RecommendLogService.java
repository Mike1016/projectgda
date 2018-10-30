package com.daka.service.recommendLog;

import com.daka.dao.customer.ICustomerDAO;
import com.daka.dao.recommendLog.IRecommendLogDAO;
import com.daka.entry.CustomerVO;
import com.daka.entry.Page;
import com.daka.entry.PageData;
import com.daka.entry.RecommendLogVO;
import com.daka.entry.dto.CustomerDTO;
import com.daka.enums.CustomerLevelEnum;
import com.daka.service.dictionaries.DictionariesService;
import com.daka.util.DateUtils;
import com.daka.util.Tools;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class RecommendLogService {

	@Autowired
	IRecommendLogDAO recommendLogDAO;
	@Autowired
	ICustomerDAO customerDAO;

	private static Lock recommend_lock = new ReentrantLock();

	/**
	 * 分页模糊查询
	 *
	 * @param page
	 * @return
	 */
	public List<PageData> queryRecommendLoglistPage(Page page) throws Exception {
		return recommendLogDAO.queryRecommendLoglistPage(page);
	}

	/**
	 * 推荐奖及代数
	 *
	 * @param customerId 用户ID
	 * @param account    金额
	 * @throws Exception 异常
	 */
	@Transactional(rollbackFor = {Exception.class, RuntimeException.class, Throwable.class})
	public void saveOrUpdateRecommend(Integer customerId, BigDecimal account) throws Exception {
		try {
			recommend_lock.lock();
			CustomerVO customerVO = customerDAO.queryCustomerById(customerId);
			if (null == customerVO) {
				return;
			}
			//获取树形父级成员
			String agentIds = customerDAO.selectAgentIdListById(customerVO.getId())
					.substring((String.valueOf(customerVO.getId()).length() + 2));
			String[] agentIdList = agentIds.split(","); //父级ID
			//获取树形父级的有效成员的最近一单金额(封号不收益，其他收益)
			List<CustomerDTO> agentDTOList = customerDAO.selectLastAccountList(
					Tools.getChilds(agentIds, "customer_id"), Tools.getChilds(agentIds, "b.id"));
			List<RecommendLogVO> recommendList = new ArrayList<>();
			String level = customerVO.getLevel();
			//烧伤
			for (CustomerDTO parentDTO : agentDTOList) {
				Integer parentLevel = CustomerLevelEnum.getLevel(parentDTO.getLevel()); //父级等级
				Integer currentLevel = CustomerLevelEnum.getLevel(level); //用户等级

				//越级 上级如果级别低于直属下级别，则不参与奖励计算
				if (currentLevel.compareTo(parentLevel) <= 0) {
					//按照树成员的最近一单排单金额比较，获取较小值计算奖励
					BigDecimal amallAccount = this.getSmallAccount(account, parentDTO.getAccount());
					int algebra = ArrayUtils.indexOf(agentIdList, String.valueOf(parentDTO.getId())); //代数
					BigDecimal ratio = this.getRecommend(algebra); //比率
					BigDecimal recoumenAccount = amallAccount.multiply(ratio); //推荐奖金额
					RecommendLogVO recommendLogVO = new RecommendLogVO();
					recommendLogVO.setCustomerId(customerVO.getId());
					recommendLogVO.setRefereeId(parentDTO.getId());
					recommendLogVO.setLevel(parentDTO.getLevel());
					recommendLogVO.setSelfLastAccount(parentDTO.getAccount());
					recommendLogVO.setChildAccount(account);
					recommendLogVO.setRealAccount(amallAccount);
					recommendLogVO.setAccount(recoumenAccount);
					recommendLogVO.setRatio(ratio);
					recommendLogVO.setCreateTime(DateUtils.getCurrentTimeYMDHMS());
					recommendList.add(recommendLogVO); //添加推荐奖奖励
				}
				level = parentDTO.getLevel();
			}

			if (!recommendList.isEmpty()) {
				recommendLogDAO.updateBatchCustomerBonus(recommendList); //批量更新推荐奖
				recommendLogDAO.saveRecommendList(recommendList); //记录日志
			}
		} catch (Exception e) {
			throw e;
		} finally {
			recommend_lock.unlock();
		}
	}

	//获取推荐奖比率
	private BigDecimal getRecommend(int algebra) {
		if (algebra > 6) {
			return new BigDecimal(DictionariesService.dictionaries.get("RECOMMEND_7"));
		}
		return new BigDecimal(DictionariesService.dictionaries.get("RECOMMEND_" + algebra)); //推荐奖比率
	}

	//获取推荐奖最金额
	private BigDecimal getSmallAccount(BigDecimal... account) {
		return null == account[1] ? account[0] : account[0].compareTo(account[1]) < 0 ? account[0] : account[1];
	}

	/**
	 * 根据用户id查询被推荐人的信息
	 *
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	public List<CustomerDTO> queryExtensionAward(Integer customerId) throws Exception {
		return recommendLogDAO.queryExtensionAward(customerId);
	}
}
