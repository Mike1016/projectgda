package com.daka.util.alipay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;

import net.sf.json.JSONObject;

public class AlipayConfig
{
	// **************手机网站支付信息***************************************************//
	// 商户appid
	public static String APPID = "2018091761449150";
	// 商户id
	public static String seller_id = "2088131142270201";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCFXkb8qKdNBeiyOIhLFSP+hKEtQ6SIcerQPjBkdVGxTbSba89UzrGDDsOoLlS6ZC4kK5xaJr8pJirzPyCDj7k67iHhpudKd743XWPi1FIVSqEIlH0KhGvLF+kBCYS8yqZpdwDFyJpoxr4VIZlTmF+kxw2y3r0Wx0+fquM9Pl0S2XDAf/2STBuX/iQqUwTh3EakIlKObsskehFUQP9/sEeyUG2tDhc9qZbM5IzcdX0R27+FxHBdeAO5hgKovMQqOmvuhHDpXbKrTXaIiSVUQfAuRAfqsKMv2SrkDHWaKf3r/Z5SGl7BihAHaOeDS/x2frrGt+U1hkC2X9WlwHWL4ls5AgMBAAECggEAQ+Xkc/sU+ZSZjCpV0QvZQFmbiOTYHhMI/a27i1ljFsY9vaacbkuwec2JdNTpk98u0oFVX1w8lCF3WlF4JyKDpenYW/5R4EqoD52/HZsEkwxkgW6/XhxemI36tOl4p3jwUd8hCJCBc/hCi5WIbtOzVbt1UOwvDiq4ccxZVjOqxM4sdE6r5A3Jcvy9S/o01JFrVG2kL+e3hUP80gICffCPPdcU4A/9herh96qy/wFxk/QC6P8lrmWORQgMSw4LWZBEu1OAQkx4K02gTiVLSvaN7C2Aa2FR7ljwz5jTaIGTNpYV9i8Hdq6Dwc0ffa1ZPzjnSuou+CDaDgd1eQS7UmpytQKBgQC9JgGdMzwZZqFqIAH+rJ/PhP/MW5m+GlVwiwvGQhYFbJs/cnKgSg2gEHLyHDcv3n7W3jd8KHKEqNfbFyiS0dTmCzrt6Yyx+Yy7Z6CqeDCrm0ZCAt/Kz6qPCU+IcU2/VIL1tZ23/vdp5VBjHMtlNmWlrG+3y5qEdZYGSqyEUBw2ewKBgQC0gVOB6yVL5guhtGLNq2LoCM632DAD7LD6BQaR1nsfIslodL6UAkTHmTKpAF5WnKyNdtn5nrAvoAT5+gRpJO5TPk6GciRTI63ISfu0UY+TsIcp6Zz7pFzdyiMUsc2Rmf5VYCoE3jm4B/yIVolgbKrn8UZ7jN0eENJ/0w/rURNA2wKBgQCAH8OOIYt/RUbnpUDUq0ghgNzpJ89Pt3TSpE1YgaF7ESNkafj9XMw39Yx3NeeoFFXk5Ge/QYK5G8oDzX8dXOeS4F6Nk9tLH1hWBY8OihWl2KtRwbKoJd1JwKvQJ7fbd7qyB9ELNrmPUNcQHAwmxh86h0Cxxkgnm6MBJgoQIlfKCwKBgEp43RpKCQ5CcGQcB3OxlVIEaD1jnZPQ8MeGkUpUQmH7OTu2hvaL5RKwuz0M4N89LtgCxDX/6dMMY2E7bNxwlz+TAlTn/OKsYsOy+n1P9TvK5kk9kD4mdwYuosCkBxtefJd+4Hc3tHqgGOmrQmydCPo/CoRo/sWrzR1d2ECPJ7FnAoGBALtMyQ2w/ueOy11D3KR4RdkXvzZTVd/FG3S9nbh6Fe27ff12srCbo/ysVBMih2yoDhRlUFnDVeqfVxLkparZp1keSioHEHBiETr9ApencxFlu+BXpsPuNih9nEXa+pQ5T2pX9h60BnkXyQbpMR7Jq7I7JDT/fBu9x51+8QNCScRQ";
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://154.222.142.152/GCR_Manage/app/alipay/payNotify.do";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	// 商户可以自定义同步跳转地址
	public static String return_url = "http://154.222.142.152/GCR_Manage/webapp/my.html";
	// 请求网关地址
	public static String URL = "https://openapi.alipay.com/gateway.do";
	// 编码
	public static String CHARSET = "UTF-8";
	// 返回格式
	public static String FORMAT = "json";

	// 调用的接口版本
	public static String version = "1.0";
	// 商品标题/订单标题
	public static String subject = "GCR商城充值";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmguJWaYRiYVs6r0JBXLKSjLwrPKBjxPHoLF9Pdsy\\/qoo+VdOmn+y0dcT7OnFvhYytacGBja3+kdisplxew4o5ohSC6zXVORjcAv33GUYyYgtXNe3QYXLlelTqwzfKE7e3KAJBBoMULxMP3Vx7vPaYzR7kt1eQgmKktNCdLGckXQUSPf9IvHF4++NFFCaABFokDUFGwduRSJT8ofrMjfOyKX5BpG74Qij72O95xhSR9UgEXTJeYUzi+NFD+LOoKn\\/3roc9ePUvVp3NfbTvHAdMl2IFSdmIgaH7RKSy2q1QbuZA8kknGkWYoO0lxjyqkEi9MskmGuZHQyPUZ1z5rQW\\/QIDAQAB";
	// 日志记录目录
	public static String log_path = "/log";

	public static String method = "alipay.trade.wap.pay";
	// RSA2
	public static String SIGNTYPE = "RSA2";
	// 超时关闭交易时间
	public static String timeoutExpress = "15m";

	public static String product_code = "QUICK_WAP_WAY";

	// ***************转账到支付宝账户**************************************************//
	// APPID
	public static String Transfer_appid = "2018091761417198";
	// 转账商户私钥
	public static String Transfer_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCpOw25ACWazrExG5EqXPTJR0XGmQ5o6pp03jrtq3ELSi2h6k63oDW0P2AxcPhU55WtxMDrwx+DkYf8gW2ZfjYVkY0j2+vYQwdPZbkpclgMtHM4g3LZVdNpJhNuIPVoc+xDCObQu4orjKwlqF2IsHjJHEXQ7vX8eLzycFFAS0xJm4Eg9klKZWjEzbFw+UUbKXZ2sstLqfhjviz/6llOwRlufTyK92zJdzrJRebrsRGiXh/mS4FSENDEFTvqd1IJjiijW5YFh4aZgLfCBmT9P+FgF+pAnAM00QH8Pd3FRitzbAkkaFy3/HIPg6POMEEidgkdKP4U+siu1P+OIcH+nglnAgMBAAECggEAUSYSJH4fiiZG7WdsqsW8U7Ark1ndgQ3OVvAhjcpCAMnnK9cxO/hFCFPDirHDQuNx8MuCPwtn1y036iseJRZSVPFgnqtcYm1x2e7LZUaBVkZJYfYWYoU3RRqPAqYnR5ke194y4DCtxshD3CLqBxuoL7ew+sk7h39WC2M0cwIoaLqyzYqSpI8C2Q2dsHNCJM9+tJwplB7PdTmSBtyOgpfwr1rdZbPcxlVGMUa1Wns1PMBcKTRAJdltC+RFCZ3px1UdWutz1yT/YBC+gau7dmh6MaPOL2kSgehDttW5duq+N74LssNuETvgrNw7BKmoOdQFwTTRqkSALtsNXy81IeSDMQKBgQDaaGDRcn5O2+nFc+1qvN59H6cj1sJh1WrVnSYjkIZMlS29T8kYsDgKUciJ5yP7wZ30hGXIGi/paWQ7PKyzcHVCSIcL/Lq9XIyEwuHQ/9Exu7Z7IfXaWsgbEPlyc+yZnQb2pMLeWKPyod/Xmew8aUnHwHE0TJXbTxPRkvpQqvGZgwKBgQDGW838A1tiPyuV2dCTtVQ3M2hGJAsG9LKXhlw4v26AWKNY149YTOi9CB/4mVf1cL6pw891ESfEhuoElwYCecKPxw2SeAlbPZJw64qXQ3kkDJoh6swB0uR5xLEaMYeJghi7OxRbB3c98ZmAgM0YGVgNYUI/tqaQpNLa1v8ywXQfTQKBgBIjpx7eFnVwOEso5Kf/xa64qUYFuSEs+3GAWsGLaEwF+8WEUdxWDmF1B2XEp61qGDdNo7Jp0l7dXPC6ilFg3qxBn820EMJXZGhGU0DrbjFmOLKjUGrxLTtqPn1t+VGMxB5J8tFKpVsqbI/YfdFKoVEQv5YrYirTt36paVa3y9cHAoGASWDkznmfcLTVttWg50OJJ/KIfIoQ2j3jY1J9AzEt+6TOqKr1iEDLSEKdMSXo+A8BOWQKdtAl2fne0FveCURpyS5lwu1M6MfaSJOZ7WM0iTVwP2PQnEwb4T/2FC6GaUQLShgM66/TWMLrejIrrOKTRD/adh9ndtLCfdpTBb+y6XECgYAGRPmIQrGmJzDCiG+tuUlLHjRK3fn6T/9WCaEgszSsXESKzbzreMGU3vAzBSc54xdpRjjtZbIUaB14FLCfG4gN4BSevXO8GDtlsarn5kvkJYBfQykrSRIZi907SqtFxw06C1yQN4P5Q0T7YazdX/PWrHQBGOUP6G8P5MAVvZ3Ppg==";
	// 转账支付宝公钥
	public static String Transfer_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsU1FVW3Zgl58uKme3j1L0Y9O+M7GCq0DoFJW6frl85jqrIYHHZSf3/Cff5ZgaIqVPhQk1qfROr1aS5rJGz3R21ARCkr2pcWv3xaPQel6vWZpbLlMuwzMIGrENFn9L+TiEXxL+tXlHJekaH8SoUq0RIuH7/ym78A4BnGFRbLOTrjvWOJWyBwjJnJmM5FahhNCAk0lVO9kPqhvNHqXLKlYjBZsKO3zgNRA7FliXppREtlzr9xpuXDoVwr3H3yZiZM6bWmMsZ5fTMuGHoRDDebSGSAvEUV19jqc8SFEDHm0s198mKHUnrCnR8XU9CmBOG67FAndNDookh5kg676IVr9iQIDAQAB";
	// 收款方支付宝信息类型 支付宝登录号
	public final static String Transfer_payee_type = "ALIPAY_LOGONID";

	public final static String payer_show_name = "GCR商城";

	public final static String remark = "会员提现";

	public static void main(String[] args) throws AlipayApiException, IOException
	{
		// //获得初始化的AlipayClient
		// AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL,
		// AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY,
		// AlipayConfig.FORMAT, AlipayConfig.CHARSET,
		// AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
		// AlipayTradeWapPayRequest alipayRequest=new
		// AlipayTradeWapPayRequest();
		// //设置同步回调地址
		// //alipayRequest.setReturnUrl(AlipayConfig.return_url);
		// //设置异步回调地址
		// alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		// //设置中途退出返回按钮
		// String quit_url = "https://docs.open.alipay.com/203/107090/";
		// String no=UUID.next()+"";
		// String money=0.01+"";
		//
		// alipayRequest.setBizContent("{\"out_trade_no\":"+no+","
		// + "\"total_amount\":"+money+","
		// + "\"subject\":\"手机网站支付测试\","
		// + "\"quit_url\":\""+ quit_url +"\","
		// + "\"product_code\":\"QUICK_WAP_WAY\"}");
		// AlipayTradeWapPayResponse response =
		// alipayClient.pageExecute(alipayRequest);
		// if(response.isSuccess()){
		// System.out.println("调用成功");
		// } else {
		// System.out.println("调用失败");
		// }
		// System.out.println(response.getBody());
		AlipayClient alipayClient = new DefaultAlipayClient(URL, Transfer_appid, Transfer_private_key, "json", "UTF-8",
				Transfer_public_key, "RSA2");
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		String orderno = UUID.next() + "";
		request.setBizContent("{" + "\"out_biz_no\":" + orderno + "," + "\"payee_type\":\"ALIPAY_LOGONID\","
				+ "\"payee_account\":\"18339795172\"," + "\"amount\":\"8\"," + "\"payer_show_name\":\"GCR商城\","
				+ "\"payee_real_name\":\"秦亚琦\"," + "\"remark\":\"会员提现\"" + "}");
		AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
		if (response.isSuccess())
		{

			// alipay_fund_trans_toaccount_transfer_response
			String body = response.getBody();
			System.out.println(body);
			JSONObject bodyJson = JSONObject.fromObject(body);
			Object transferResponse = bodyJson.get("alipay_fund_trans_toaccount_transfer_response");
			JSONObject transferResponseJson = JSONObject.fromObject(transferResponse);
			String code = String.valueOf(transferResponseJson.get("code"));
			String orderId = String.valueOf(transferResponseJson.get("order_id"));

			System.out.println("调用成功");
		} else
		{
			System.out.println("调用失败");
		}

	}

	// 转账到支付宝账户

	public static Map<String, String> TransferAlipay(String alipay, String amount, String out_pay_no,
			String payee_real_name) throws AlipayApiException
	{
		AlipayClient alipayClient = new DefaultAlipayClient(URL, Transfer_appid, Transfer_private_key, "json", "UTF-8",
				Transfer_public_key, "RSA2");
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizContent("{" + "\"out_biz_no\":" + out_pay_no + "," + "\"payee_type\":\"ALIPAY_LOGONID\","
				+ "\"payee_account\":\"" + alipay + "\"," + "\"amount\":\"" + amount + "\","
				+ "\"payer_show_name\":\"GCR商城\"," + "\"payee_real_name\":\"" + payee_real_name + "\","
				+ "\"remark\":\"会员提现\"" + "}");
		AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
		if (response.isSuccess())
		{
			System.out.println("调用成功");
			String body = response.getBody();
			System.out.println(body);
			JSONObject bodyJson = JSONObject.fromObject(body);
			Object transferResponse = bodyJson.get("alipay_fund_trans_toaccount_transfer_response");
			JSONObject transferResponseJson = JSONObject.fromObject(transferResponse);
			String code = String.valueOf(transferResponseJson.get("code"));
			String orderId = String.valueOf(transferResponseJson.get("order_id"));
			// String account = transferResponseJson.getString("amount");
			// String sign = transferResponseJson.getString("sign");
			Map<String, String> map = new HashMap<>();
			map.put("code", code);
			map.put("orderId", orderId);
			// map.put("account", account);
			// .put("sign", sign);
			return map;
		} else
		{
			System.out.println("调用失败");
		}
		return new HashMap<>();
	}
}
