package com.daka.util.base64;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.daka.util.DateUtils;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class TypeConverter {
	public static String GetImageStr(String path){//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		String imgFile=path;
		InputStream in=null;
		byte[] data=null;
		try {
			in=new FileInputStream(imgFile);
			data=new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BASE64Encoder encoder=new BASE64Encoder();
		return encoder.encode(data);
	}
	
	public static String GenerateImage(String imgstr){    //base64字符串转化成图片
		if(imgstr==null){
			return null;
			}
		BASE64Decoder decoder=new BASE64Decoder();
		try {
			byte[] b=decoder.decodeBuffer(imgstr);
			for (int i = 0; i < b.length; i++) {
				if(b[i]<0){//数据异常
					b[i]+=256;
				}
			}
		String date=DateUtils.getCurrentTimeYMDHMS();
		String imgFilePath="img/"+date+".jpg";
		OutputStream out=new FileOutputStream(imgFilePath);
		out.write(b);
		out.flush();out.close();
		return imgFilePath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
