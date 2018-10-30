/**
 * Project Name:Bus
 * File Name:PageData.java
 * Package Name:com.bus.entry
 * Date:2016��4��11������10:57:46
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.daka.entry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName:PageData <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016��4��11�� ����10:57:46 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
public class PageData extends HashMap implements Map
{

	private static final long serialVersionUID = 1L;

	Map map = null;

	HttpServletRequest request;

	public PageData(HttpServletRequest request)
	{
		this.request = request;
		Map properties = request.getParameterMap();
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext())
		{
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj)
			{
				value = "";
			} else if (valueObj instanceof String[])
			{
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++)
				{
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else
			{
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		// returnMap.put("offset", returnMap.get("page")*returnMap.get("page"))
		map = returnMap;
	}

	public PageData()
	{
		map = new HashMap();
	}

	@Override
	public Object get(Object key)
	{
		Object obj = null;
		if (map.get(key) instanceof Object[])
		{
			Object[] arr = (Object[]) map.get(key);
			obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
		} else
		{
			obj = map.get(key);
		}
		return obj;
	}

	public String getString(Object key)
	{
		return String.valueOf(get(key));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value)
	{
		return map.put(key, value);
	}

	@Override
	public Object remove(Object key)
	{
		return map.remove(key);
	}

	@Override
	public void clear()
	{
		map.clear();
	}

	@Override
	public boolean containsKey(Object key)
	{
		// TODO Auto-generated method stub
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		// TODO Auto-generated method stub
		return map.containsValue(value);
	}

	@Override
	public Set entrySet()
	{
		// TODO Auto-generated method stub
		return map.entrySet();
	}

	@Override
	public boolean isEmpty()
	{
		// TODO Auto-generated method stub
		return map.isEmpty();
	}

	@Override
	public Set keySet()
	{
		// TODO Auto-generated method stub
		return map.keySet();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void putAll(Map t)
	{
		// TODO Auto-generated method stub
		map.putAll(t);
	}

	@Override
	public int size()
	{
		// TODO Auto-generated method stub
		return map.size();
	}

	@Override
	public Collection values()
	{
		// TODO Auto-generated method stub
		return map.values();
	}

}
