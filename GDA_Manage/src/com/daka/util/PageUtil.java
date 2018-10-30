/**
 * File Name:PageUtil.java
 * Package Name:com.qianye.util
 * Date:2016骞�鏈�3鏃ヤ笂鍗�0:42:11
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.daka.util;

import java.util.List;

import com.daka.entry.JqGridResponseModel;
import com.daka.entry.Page;

/**
 * ClassName:PageUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016骞�鏈�3鏃�涓婂崍10:42:11 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
public class PageUtil
{
    private static PageUtil pageUtil;

    private PageUtil()
    {

    }

    public synchronized static PageUtil getInstance()
    {
        if (null == pageUtil)
        {
            pageUtil = new PageUtil();
        }

        return pageUtil;
    }

    /**
     * 缁勮JqGridResponseModel returnGridPageObject:(杩欓噷鐢ㄤ竴鍙ヨ瘽鎻忚堪杩欎釜鏂规硶鐨勪綔鐢�. <br/>
     * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶閫傜敤鏉′欢 鈥�鍙�).<br/>
     * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬墽琛屾祦绋�鈥�鍙�).<br/>
     * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勪娇鐢ㄦ柟娉�鈥�鍙�).<br/>
     * TODO(杩欓噷鎻忚堪杩欎釜鏂规硶鐨勬敞鎰忎簨椤�鈥�鍙�).<br/>
     *
     * @author Administrator
     * @param page
     * @param ListDate
     * @return
     * @since JDK 1.6
     */
    public com.daka.entry.JqGridResponseModel returnGridPageObject(Page page, List ListDate)
    {
        JqGridResponseModel pageData = new JqGridResponseModel();
        pageData.setTotal(page.getTotalPage() + "");
        pageData.setPage(page.getCurrentPage() + "");
        pageData.setRecords(page.getTotalResult() + "");
        pageData.setRows(ListDate.toArray());
        return pageData;
    }
}
