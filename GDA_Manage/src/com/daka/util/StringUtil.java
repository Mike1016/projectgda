package com.daka.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

/**
 * 字符串工具类
 * 
 * @author 刘新
 */
public class StringUtil
{

	public static final String NULL_STRING = "NULL".intern();

	public static final String COLON = ":".intern();

	public static final String SEMICOLON = ";".intern();

	public static final String UNDERLINE = "_".intern();

	public static final String MIDLINE = "-".intern();

	public static final String BLANK = " ".intern();

	public static final String STAR = "*".intern();

	public static final String LEFT_ANGLE_BRACKET = "<".intern();

	public static final String RIGHT_ANGLE_BRACKET = ">".intern();

	public static final String BRACE_LEFT = "{".intern();

	public static final String BRACE_RIGHT = "}".intern();

	public static final String BRACKET_LEFT = "[".intern();

	public static final String BRACKET_RIGHT = "]".intern();

	public static final String EQUAL = "=".intern();

	public static final String TAB = "\t".intern();

	public static final String DOUBLE_QUOTATION = "\"".intern();

	public static final String SINGLE_QUOTATION = "'".intern();

	public static final String BACK_SLANT = "/".intern();

	public static final String WRAP = "\n".intern();

	public static final String ZERO = "0".intern();

	public static final String DOT = ".".intern();

	public static final String COMMA = ",".intern();

	public static final String CHARSET_UTF8 = "UTF-8";

	public static final String CHARSET_GBK = "GBK";
	/**
	 * 正则表达式：验证用户名
	 */
	public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,20}$";

	/**
	 * 正则表达式：验证密码
	 */
	public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";

	/**
	 * 正则表达式：验证手机号
	 */
	public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

	/**
	 * 正则表达式：验证邮箱
	 */
	public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * 正则表达式：验证汉字
	 */
	public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

	/**
	 * 正则表达式：验证身份证
	 */
	public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

	/**
	 * 正则表达式：验证URL
	 */
	public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

	/**
	 * 正则表达式：验证IP地址
	 */
	public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

	/**
	 * 校验用户名
	 * 
	 * @param username
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isUsername(String username)
	{
		return Pattern.matches(REGEX_USERNAME, username);
	}

	/**
	 * 校验密码
	 * 
	 * @param password
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isPassword(String password)
	{
		return Pattern.matches(REGEX_PASSWORD, password);
	}

	/**
	 * 校验手机号
	 * 
	 * @param mobile
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isMobile(String mobile)
	{
		return Pattern.matches(REGEX_MOBILE, mobile);
	}

	/**
	 * 校验邮箱
	 * 
	 * @param email
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isEmail(String email)
	{
		return Pattern.matches(REGEX_EMAIL, email);
	}

	/**
	 * 校验汉字
	 * 
	 * @param chinese
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isChinese(String chinese)
	{
		return Pattern.matches(REGEX_CHINESE, chinese);
	}

	/**
	 * 校验身份证
	 * 
	 * @param idCard
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isIDCard(String idCard)
	{
		return Pattern.matches(REGEX_ID_CARD, idCard);
	}

	/**
	 * 校验URL
	 * 
	 * @param url
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isUrl(String url)
	{
		return Pattern.matches(REGEX_URL, url);
	}

	/**
	 * 校验IP地址
	 * 
	 * @param ipAddr
	 * @return
	 */
	public static boolean isIPAddr(String ipAddr)
	{
		return Pattern.matches(REGEX_IP_ADDR, ipAddr);
	}

	/**
	 * 给定的字符串是否是一个整数或百分数
	 * 
	 * @author 刘新 2015年10月29日
	 * @param str
	 * @return
	 */
	public static boolean isValidIntOrPercent(String str)
	{
		boolean isValid = true;
		if (str == null || str.isEmpty())
		{
			isValid = false;
		} else
		{
			try
			{
				String reg = "^(?:[\\d]+)|(?:100|[1-9]?[0-9])%$";
				if (!str.matches(reg))
				{
					isValid = false;
				}
			} catch (PatternSyntaxException e)
			{
				isValid = false;
			}
		}
		return isValid;
	}

	/**
	 * 字符串转整形
	 * 
	 * strToInt:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @author Administrator
	 * @param str
	 * @return
	 * @since JDK 1.6
	 */
	public static int strToInt(String str)
	{
		if (null == str || str.isEmpty())
		{
			return 0;
		}

		int result = 0;
		try
		{
			result = Integer.valueOf(str);
		} catch (NumberFormatException e)
		{
			result = 0;
		}
		return result;
	}

	/**
	 * 字符串转long
	 * 
	 * strToInt:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @author Administrator
	 * @param str
	 * @return
	 * @since JDK 1.6
	 */
	public static long strToLong(String str)
	{
		if (null == str || str.isEmpty())
		{
			return 0;
		}

		long result = 0;
		try
		{
			result = Long.valueOf(str);
		} catch (NumberFormatException e)
		{
			result = 0;
		}
		return result;
	}

	/**
	 * 对象转整形
	 * 
	 * strToInt:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @author Administrator
	 * @param str
	 * @return
	 * @since JDK 1.6
	 */
	public static int objToInt(Object obj)
	{
		if (null == obj)
		{
			return 0;
		}

		int result = 0;
		try
		{
			result = Integer.valueOf(String.valueOf(obj));
		} catch (NumberFormatException e)
		{
			result = 0;
		}
		return result;
	}

	/**
	 * 字符串转浮点
	 * 
	 * strToInt:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @author Administrator
	 * @param str
	 * @return
	 * @since JDK 1.6
	 */
	public static float strToFloat(String str)
	{
		if (null == str || str.isEmpty())
		{
			return 0.0f;
		}

		float result = 0;
		try
		{
			result = Float.valueOf(str);
		} catch (NumberFormatException e)
		{
			result = 0.0f;
		}
		return result;
	}

	/**
	 * 对象转浮点
	 * 
	 * strToInt:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @author Administrator
	 * @param str
	 * @return
	 * @since JDK 1.6
	 */
	public static float objToFloat(Object obj)
	{
		if (null == obj)
		{
			return 0.0f;
		}

		float result = 0;
		try
		{
			result = Float.valueOf(String.valueOf(obj));
		} catch (Exception e)
		{
			result = 0.0f;
		}
		return result;
	}

	/**
	 * 对象转double
	 * 
	 * strToInt:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @author Administrator
	 * @param str
	 * @return
	 * @since JDK 1.6
	 */
	public static double objToDouble(Object obj)
	{
		if (null == obj)
		{
			return 0.0;
		}

		double result = 0;
		try
		{
			result = Double.valueOf(String.valueOf(obj));
		} catch (Exception e)
		{
			result = 0.0;
		}
		return result;
	}

	/**
	 * 字符串转double
	 * 
	 * strToInt:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @author Administrator
	 * @param str
	 * @return
	 * @since JDK 1.6
	 */
	public static double strToDouble(String str)
	{
		if (null == str || str.isEmpty())
		{
			return 0.0f;
		}

		double result = 0;
		try
		{
			result = Double.valueOf(str);
		} catch (NumberFormatException e)
		{
			result = 0.0f;
		}
		return result;
	}

	/**
	 * 是否是空字符串对象
	 * 
	 * @author Administrator
	 * @param str
	 * @return
	 * @since JDK 1.6
	 */
	public static boolean isEmptyString(String str)
	{
		return null == str || str.isEmpty();
	}

	public static void sendDataToResponse(HttpServletResponse response, String data)
	{

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try
		{
			out = response.getWriter();
			out.write(data);
			out.flush();
		} catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				response.sendError(500);
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	public static String buildInSqlByStr(String str)
	{
		String[] strArr = str.split(",");
		StringBuffer result = new StringBuffer();
		for (String string : strArr)
		{
			result.append("'").append(string).append("',");
		}
		result.delete(result.lastIndexOf(","), result.length());
		return result.toString();
	}
}
