/**
 * Project Name:Bus
 * File Name:ReflectHilper.java
 * Package Name:com.bus.util
 * Date:2016��4��11������11:04:08
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.daka.util;

import java.lang.reflect.Field;

/**
 * ClassName:ReflectHilper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016��4��11�� ����11:04:08 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
public class ReflectHelper
{
    /**
     * ��ȡobj����fieldName��Field
     * 
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field getFieldByFieldName(Object obj, String fieldName)
    {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
        {
            try
            {
                return superClass.getDeclaredField(fieldName);
            }
            catch (NoSuchFieldException e)
            {
            }
        }
        return null;
    }

    /**
     * ��ȡobj����fieldName������ֵ
     * 
     * @param obj
     * @param fieldName
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Object getValueByFieldName(Object obj, String fieldName)
        throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if (field != null)
        {
            if (field.isAccessible())
            {
                value = field.get(obj);
            }
            else
            {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * ����obj����fieldName������ֵ
     * 
     * @param obj
     * @param fieldName
     * @param value
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value)
        throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (field.isAccessible())
        {
            field.set(obj, value);
        }
        else
        {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }

}
