package com.daka.constants;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public interface SystemConstant {
	String SYS_USER = "user";

	String APP_USER = "app_user";

	String SUCCESS_CODE = "200";

	String FAIL_CODE = "500";

	String STATUS = "status";

	String MESSAGE = "msg";

	String PUBLIC_USER = "public_user";

	String LOCAL_PATH = "E:/resource/";

	String LOCAL_IMAGES = "/app/images";

	String DATE_FORMART_PATTEN_1 = "yyyyMMddHHmmss";

	String DATE_FORMART_PATTEN_2 = "yyyy-MM-dd HH:mm:ss";

	SimpleDateFormat DATE_FORMAT_1 = new SimpleDateFormat(DATE_FORMART_PATTEN_1);

	SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat(DATE_FORMART_PATTEN_2);

	String TIGGER_PARAM = "parameterList";

	int THANK_PATRONAGE = 0;//谢谢惠顾的奖品id

	String DICTIONARIES_PHONE = "phone"; //每个手机号注册限制人数

	Integer CONSUMPTION_MULTIPLE = 3;//抽奖消耗的活力值与档数的倍数

	Integer TRAY_GRADE_MIN = 1;//最低抽奖档数

	Integer TRAY_GRADE_MAX = 3;//最高抽奖档数

	int APP_PAGE_NUMBER = 20;//APP分页每页显示的数量

	Integer IMAGE_WIDTH = 120;//奖品图片统一的宽度

	Integer IMAGE_HEIGHT = 60;//奖品图片统一的高度

	int ZERO = 0;

	int CUSTOMER_AGENTID_NUM = 5; //父id个数临界对比值

	Integer VITALITY_UPPER_LIMIT = 100;//用户生命力上限

	int ACTIVITY_RATIO = 10000;//排单每10000财富值获取一点活动值，不累计

	BigDecimal TRAY_UPPER = new BigDecimal(1);//用户抽奖获得该档次第八种奖品
}
