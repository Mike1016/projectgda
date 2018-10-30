/**
 * File Name:BusinessLogAnnotation.java
 * Date:2016年3月30日上午10:31:38
 *yanhp
 */

package com.daka.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName:BusinessLogAnnotation <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年3月30日 上午10:31:38 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
@Target(
{ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Log
{

	public static String VALUESIGNTYPE_VO = "vo";

	public static String VALUESIGNTYPE_MAP = "map";

	public static String VALUESIGNTYPE_INDEX = "index";

	public static String VALUESIGNTYPE_LIST = "list";

	/**
	 * 
	 * modelName:模块名称. <br/>
	 * 模块名称
	 * 
	 * @author Administrator
	 * @return
	 * @since JDK 1.6
	 */
	String modelName() default "未知";

	/**
	 * 
	 * option:操作名称（eg：新增，删除...）. <br/>
	 * 操作类型
	 * 
	 * @author Administrator
	 * @return
	 * @since JDK 1.6
	 */
	String option() default "未知";

	/**
	 * 
	 * getValueSign:(这里用一句话描述这个方法的作用). <br/>
	 * 取值标志类型（支持vo，map,index,list）
	 * 
	 * @author Administrator
	 * @return
	 * @since JDK 1.6
	 */
	String getValueSignType() default "";

	/**
	 * 
	 * getValueSign:(这里用一句话描述这个方法的作用). <br/>
	 * 取值标志（如果入参是Vo，写是VO的get方法，如果是Map，则是key值，如果是多个参数，则是index，如果是List同理）
	 * 
	 * @author Administrator
	 * @return
	 * @since JDK 1.6
	 */
	String getValueSign() default "";

	/**
	 * 
	 * index:索引. <br/>
	 * 参数的index
	 * 
	 * @author Administrator
	 * @return
	 * @since JDK 1.6
	 */
	int index() default 0;

	/**
	 * 
	 * 取值的中文描述
	 * 
	 * @author Administrator
	 * @return
	 * @since JDK 1.6
	 */
	String valueSignCNDesc() default "无";

	boolean needControllerControl() default false;

}
