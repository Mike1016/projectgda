/**
 * Project Name:SpringMVC_Frame
 * File Name:PushMessageConstant.java
 * Package Name:com.bus.constants
 * Date:2016年10月25日下午12:40:45
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.daka.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:PushMessageConstant <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年10月25日 下午12:40:45 <br/>
 *
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
public interface PushMessageConstant
{

	String ARTICLE_NOTICE = "1";

	String APPLY_NOTICE = "2";

	// Userid对应的链接ID
	public static Map<Object, String> userID2ConnID = new HashMap<Object, String>();
}
