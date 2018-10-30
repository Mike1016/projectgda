package com.daka.util.sms;

import net.sf.json.JSONObject;

/**
 * 普通短信发送 
 */
public class SMSUtil {

	public static final String ACCOUNT = "N7623562";
	public static final String PASSWORD = "k05tXiHS2wb43a";
	public static final String URL = "https://smssh1.253.com/msg/send/json";
	public static final String REPORT = "true";

	/**
	 * Send Message
	 * @param phone 手机号
	 * @param message 消息
	 */
	public static void sendSms(String phone, String message) {
		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PASSWORD, message, phone, REPORT);
		String requestJson = JSONObject.fromObject(smsSingleRequest).toString();
		System.out.println("before request string is: " + requestJson);
		String response = SmsSendUtil.sendSmsByPost(URL, requestJson);
		System.out.println("response after request result is :" + response);
	}

	public static void main(String[] args) {
		String msg = "【GDA】您的验证码是, 两分钟内有效。若非本人操作请忽略此消息。";
		String phone = "15667284576";
		SmsSendRequest smsSingleRequest = new SmsSendRequest(ACCOUNT, PASSWORD, msg, phone, REPORT);
		String requestJson = JSONObject.fromObject(smsSingleRequest).toString();
		System.out.println("before request string is: " + requestJson);
		String response = SmsSendUtil.sendSmsByPost(URL, requestJson);
		System.out.println("response after request result is :" + response);
	}
}
