/**
 * Project Name:Bus
 * File Name:Page.java
 * Package Name:com.bus.entry
 * Date:2016��4��11������10:58:28
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.daka.entry;

/**
 * ClassName:Page <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016��4��11�� ����10:58:28 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
public class Page
{
	/**
	 * ÿҳ��ʾ��¼��
	 */
	private int showCount;

	/**
	 * ��ҳ��
	 */
	private int totalPage;

	/**
	 * �ܼ�¼��
	 */
	private int totalResult;

	/**
	 * ��ǰҳ
	 */
	private int currentPage;

	/**
	 * ��ǰ��¼��ʼ����
	 */
	private int currentResult;

	/**
	 * true:��Ҫ��ҳ�ĵط�������Ĳ������Pageʵ�壻false:��Ҫ��ҳ�ĵط�������Ĳ��������ʵ��ӵ��
	 * Page����
	 */
	private boolean entityOrField;

	private PageData pageData = new PageData();

	/**
	 * �������
	 */
	private int draw;

	private int start;

	private int length;

	/**
	 * ��������
	 */
	private String searchValue;

	private String orderColumnValue = "";

	private String orderDirValue = "";

	public String getSearchValue()
	{
		return pageData.getString("search[value]");
	}

	public String getOrderColumnValue()
	{
		return pageData.getString("sort");
		// String columnIndex = pd.getString("order[0][column]");
		// if ("".equals(columnIndex) || null == columnIndex)
		// {
		// return "";
		// }
		// else
		// {
		// return pd.getString("columns[" + columnIndex + "][data]");
		// }

	}

	public String getOrderDirValue()
	{
		return pageData.getString("sord");
	}

	public int getStart()
	{
		return start;
	}

	public void setStart(int start)
	{
		this.start = start;
		this.currentResult = start;
		this.currentPage = (currentResult / getShowCount()) + 1;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
		this.showCount = length;
	}

	public int getDraw()
	{
		return draw;
	}

	public void setDraw(int draw)
	{
		this.draw = draw;
	}

	public Page()
	{
		try
		{
			this.showCount = Integer.parseInt("20");
		} catch (Exception e)
		{
			this.showCount = 15;
		}
	}

	public int getTotalPage()
	{
		if (totalResult % showCount == 0)
			totalPage = totalResult / showCount;
		else
			totalPage = totalResult / showCount + 1;
		return totalPage;
	}

	public void setTotalPage(int totalPage)
	{
		this.totalPage = totalPage;
	}

	public int getTotalResult()
	{
		return totalResult;
	}

	public void setTotalResult(int totalResult)
	{
		this.totalResult = totalResult;
	}

	public int getCurrentPage()
	{
		if (currentPage <= 0)
			currentPage = 1;
		if (currentPage > getTotalPage())
			currentPage = getTotalPage();
		return currentPage;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	public int getShowCount()
	{
		return showCount;
	}

	public void setShowCount(int showCount)
	{

		this.showCount = showCount;
	}

	public int getCurrentResult()
	{
		currentResult = (getCurrentPage() - 1) * getShowCount();
		if (currentResult < 0)
			currentResult = 0;
		return currentResult;
	}

	public void setCurrentResult(int currentResult)
	{
		this.currentResult = currentResult;
	}

	public boolean isEntityOrField()
	{
		return entityOrField;
	}

	public void setEntityOrField(boolean entityOrField)
	{
		this.entityOrField = entityOrField;
	}

	public PageData getPd()
	{
		return pageData;
	}

	public void setPd(PageData pageData)
	{
		// {sord=asc, sort=userId, page=1, nd=1456211731473, companyId=1,
		// size=10, _search=false}
		this.pageData = pageData;
		this.searchValue = this.getSearchValue();
		this.orderColumnValue = this.getOrderColumnValue();
		this.orderDirValue = this.getOrderDirValue();
		this.showCount = Integer.parseInt(pageData.get("limit").toString());
		this.currentPage = Integer.parseInt(pageData.get("page").toString());
		this.pageData.put("searchValue", searchValue);
		this.pageData.put("orderColumnValue", orderColumnValue);
		this.pageData.put("orderDirValue", orderDirValue);
	}

	public void setGridPd(PageData pageData)
	{
		this.pageData = pageData;
		this.searchValue = this.getSearchValue();
		this.orderColumnValue = this.getOrderColumnValue();
		this.orderDirValue = this.getOrderDirValue();
		this.showCount = Integer.parseInt(this.pageData.getString("rows"));
		this.currentPage = Integer.parseInt(this.pageData.getString("page"));
		this.pageData.put("orderColumnValue", this.pageData.get("sidx"));
		this.pageData.put("orderDirValue", this.pageData.get("sord"));
	}

}
