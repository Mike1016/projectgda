/**
 * Project Name:Bus
 * File Name:MD5.java
 * Package Name:com.bus.util
 * Date:2016��6��1������5:53:01
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.daka.util;

import java.security.MessageDigest;

/**
 * ClassName:MD5 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016��6��1�� ����5:53:01 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
public class MD5
{

	public static String md5(String str)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++)
			{
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (Exception e)
		{
			e.printStackTrace();

		}
		return str;
	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	public static String convertMD5(String inStr)
	{

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++)
		{
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}

	public static void main(String[] args)
	{
		String s = new String("tangfuqiang");
		System.out.println("原始：" + s);
		System.out.println("MD5后：" + md5(s));
		System.out.println("加密的：" + convertMD5(s));
		System.out.println("解密的：" + convertMD5(convertMD5(s)));
	}

}
