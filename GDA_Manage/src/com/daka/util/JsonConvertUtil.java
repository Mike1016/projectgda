/**
 * Project Name:donkeyBus
 * File Name:JsonUtil.java
 * Package Name:com.bus.utils
 * Date:2016年4月28日下午4:58:38
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.daka.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * json转换帮助类
 * 
 * ClassName:JsonUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年4月28日 下午4:58:38 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
public class JsonConvertUtil
{
    private static JsonConvertUtil jsonConvertUtil;

    private JsonConvertUtil()
    {

    }

    public synchronized static JsonConvertUtil getInstance()
    {
        if (null == jsonConvertUtil)
        {
            jsonConvertUtil = new JsonConvertUtil();
        }
        return jsonConvertUtil;
    }

    /**
     * json转换为整型List
     * 
     * @author Administrator
     * @param jsonStr
     * @return
     * @since JDK 1.6
     */
    public List<Integer> ConvertIntList(String jsonStr)
    {
        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
        List<Integer> intList = new ArrayList<Integer>();
        for (Object jobj : jsonArray)
        {
            intList.add(StringUtil.strToInt(String.valueOf(jobj)));
        }
        return intList;
    }

    /**
     * json转换为字符串List
     * 
     * @author Administrator
     * @param jsonStr
     * @return
     * @since JDK 1.6
     */
    public List<String> ConvertStringList(String jsonStr)
    {
        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
        List<String> stringList = new ArrayList<String>();
        for (Object jobj : jsonArray)
        {
            stringList.add(String.valueOf(jobj));
        }
        return stringList;
    }

    /**
     * 转换经纬坐标list
     * 
     * @author Administrator
     * @param jsonStr
     * @return
     * @since JDK 1.6
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Double>> ConvertPointMapList(String jsonStr)
    {
        Object convertedObj = null;
        // 得到折点坐标列表
        JSONArray pointJsonArray = JSONArray.fromObject(jsonStr);
        List<Map<String, Double>> pointMapList = new ArrayList<Map<String, Double>>();
        for (Object jobj : pointJsonArray)
        {
            if (jobj instanceof JSONObject)
            {
                convertedObj = JSONObject.toBean((JSONObject)jobj, Map.class);
                pointMapList.add((Map<String, Double>)convertedObj);
            }
        }
        return pointMapList;
    }

}
