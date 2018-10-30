package com.daka.entry;

import org.apache.commons.lang.StringUtils;

public class JqGridResponseModel
{
    public static final String RESPONSE_DATA_ROOT = "rows";
    
    /**
     * 响应数据内容
     */
    private Object[] rows = null;
    
    /**
     * 当前页码
     */
    private String page = StringUtils.EMPTY;
    
    /**
     * 总页�?
     */
    private String total = StringUtils.EMPTY;
    
    /**
     * 总记录数
     */
    private String records = StringUtils.EMPTY;
    
    /**
     * 返回状�?
     */
    private String status = StringUtils.EMPTY;
    
    private Object postback = null;
    
    /**
     * 为false时根据colModel的name属�?获取对应数据
     */
    private Boolean repeatitems = false;
    
    public Object[] getRows()
    {
        return rows;
    }
    
    public void setRows(Object[] rows)
    {
        this.rows = rows;
    }
    
    public String getPage()
    {
        return page;
    }
    
    public void setPage(String page)
    {
        this.page = page;
    }
    
    public String getTotal()
    {
        return total;
    }
    
    public void setTotal(String total)
    {
        this.total = total;
    }
    
    public String getRecords()
    {
        return records;
    }
    
    public void setRecords(String records)
    {
        this.records = records;
    }
    
    public Boolean getRepeatitems()
    {
        return repeatitems;
    }
    
    public void setRepeatitems(Boolean repeatitems)
    {
        this.repeatitems = repeatitems;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public Object getPostback()
    {
        return postback;
    }
    
    public void setPostback(Object postback)
    {
        this.postback = postback;
    }
}
