/**
 * Project Name:Bus
 * File Name:Tools.java
 * Package Name:com.bus.util
 * Date:2016��4��11������11:02:47
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.daka.util;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.daka.enums.SystemEnum;
import com.daka.service.dictionaries.DictionariesService;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.comet4j.core.CometConnection;
import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;
import org.springframework.web.multipart.MultipartFile;

import com.daka.constants.PushMessageConstant;
import com.daka.constants.SystemConstant;
import com.daka.entry.SysUserVO;

/**
 * ClassName:Tools <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016��4��11�� ����11:02:47 <br/>
 *
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
public class Tools<T>
{
	public static final String UPLOAD_PATH = "/resource/images";

	public static final String UPLOAD_APK = "/resource/apk";

	public static final String DOWNLOAD_PATH = "/resource/downLoadFile";

	public static final String PATTEN = "yyyy-MM-dd";

	/**
	 * ��������λ����֤��
	 *
	 * @return
	 */
	public static int getRandomNum()
	{
		Random r = new Random();
		return r.nextInt(900000) + 100000;// (Math.random()*(999999-100000)+100000)
	}

	/**
	 * ����ַ��Ƿ�Ϊ��(null,"","null")
	 *
	 * @param s
	 * @return ��Ϊ���򷵻�true�����򷵻�false
	 */
	public static boolean notEmpty(String s)
	{
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	/**
	 * ����ַ��Ƿ�Ϊ��(null,"","null")
	 *
	 * @param s
	 * @return Ϊ���򷵻�true�������򷵻�false
	 */
	public static boolean isEmpty(String s)
	{
		return s == null || "".equals(s) || "null".equals(s);
	}

	/**
	 * �ַ�ת��Ϊ�ַ�����
	 *
	 * @param str
	 *            �ַ�
	 * @param splitRegex
	 *            �ָ���
	 * @return
	 */
	public static String[] str2StrArray(String str, String splitRegex)
	{
		if (isEmpty(str))
		{
			return null;
		}
		return str.split(splitRegex);
	}

	/**
	 * ��Ĭ�ϵķָ���(,)���ַ�ת��Ϊ�ַ�����
	 *
	 * @param str
	 *            �ַ�
	 * @return
	 */
	public static String[] str2StrArray(String str)
	{
		return str2StrArray(str, ",\\s*");
	}

	/**
	 * ����yyyy-MM-dd HH:mm:ss�ĸ�ʽ������ת�ַ�
	 *
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String date2Str(Date date)
	{
		return date2Str(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ����yyyy-MM-dd HH:mm:ss�ĸ�ʽ���ַ�ת����
	 *
	 * @param date
	 * @return
	 */
	public static Date str2Date(String date)
	{
		if (notEmpty(date))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try
			{
				return sdf.parse(date);
			} catch (ParseException e)
			{
				e.printStackTrace();
			}
			return new Date();
		} else
		{
			return null;
		}
	}

	/**
	 * ���ղ���format�ĸ�ʽ������ת�ַ�
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date, String format)
	{
		if (date != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} else
		{
			return "";
		}
	}

	/**
	 * ��ʱ����ʱ���֡���ת��Ϊʱ���
	 *
	 * @param StrDate
	 */
	public static String getTimes(String StrDate)
	{
		String resultTimes = "";

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date now;

		try
		{
			now = new Date();
			java.util.Date date = df.parse(StrDate);
			long times = now.getTime() - date.getTime();
			long day = times / (24 * 60 * 60 * 1000);
			long hour = (times / (60 * 60 * 1000) - day * 24);
			long min = ((times / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long sec = (times / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

			StringBuffer sb = new StringBuffer();
			// sb.append("�����ڣ�");
			if (hour > 0)
			{
				sb.append(hour + "Сʱǰ");
			} else if (min > 0)
			{
				sb.append(min + "����ǰ");
			} else
			{
				sb.append(sec + "��ǰ");
			}

			resultTimes = sb.toString();
		} catch (ParseException e)
		{
			e.printStackTrace();
		}

		return resultTimes;
	}

	/**
	 * дtxt��ĵ�������
	 *
	 * @param
	 *            �ļ�·��
	 * @param content
	 *            д�������
	 */
	public static void writeFile(String fileP, String content)
	{
		String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../"; // ��Ŀ·��
		filePath = (filePath.trim() + fileP.trim()).substring(6).trim();
		if (filePath.indexOf(":") != 1)
		{
			filePath = File.separator + filePath;
		}
		try
		{
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(content);
			writer.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * ��֤����
	 *
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email)
	{
		boolean flag = false;
		try
		{
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e)
		{
			flag = false;
		}
		return flag;
	}

	/**
	 * ��֤�ֻ����
	 *
	 * @param
	 * @return
	 */
	public static boolean checkMobileNumber(String mobileNumber)
	{
		boolean flag = false;
		try
		{
			Pattern regex = Pattern
					.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		} catch (Exception e)
		{
			flag = false;
		}
		return flag;
	}

	//
	// /**
	// * ���KEY�Ƿ���ȷ
	// *
	// * @param paraname �������
	// * @param FKEY ���յ� KEY
	// * @return Ϊ���򷵻�true�������򷵻�false
	// */
	// public static boolean checkKey(String paraname, String FKEY)
	// {
	// paraname = (null == paraname) ? "" : paraname;
	// return MD5.md5(paraname + DateUtil.getDays() + ",fh,").equals(FKEY);
	// }

	/**
	 * ��ȡtxt��ĵ�������
	 *
	 * @param
	 *            �ļ�·��
	 */
	public static String readTxtFile(String fileP)
	{
		try
		{

			String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource("")) + "../../"; // ��Ŀ·��
			filePath = filePath.replaceAll("file:/", "");
			filePath = filePath.replaceAll("%20", " ");
			filePath = filePath.trim() + fileP.trim();
			if (filePath.indexOf(":") != 1)
			{
				filePath = File.separator + filePath;
			}
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists())
			{ // �ж��ļ��Ƿ����
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding); // ���ǵ������ʽ
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null)
				{
					return lineTxt;
				}
				read.close();
			} else
			{
				System.out.println("�Ҳ���ָ�����ļ�,�鿴��·���Ƿ���ȷ:" + filePath);
			}
		} catch (Exception e)
		{
			System.out.println("��ȡ�ļ����ݳ���");
		}
		return "";
	}

	public static void uploadFile(List<MultipartFile> files, HttpServletRequest request, String prePath)
			throws IOException
	{
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
		String savePath = request.getServletContext().getRealPath(UPLOAD_PATH);
		savePath += "/" + prePath;
		File file = new File(savePath);
		if (!file.exists() && !file.isDirectory())
		{
			file.mkdirs();
		}
		for (MultipartFile multipartFile : files)
		{
			if (multipartFile.getSize() == 0)
			{
				continue;
			}

			String url = savePath + "/" + multipartFile.getOriginalFilename();
			;
			FileOutputStream fos = new FileOutputStream(url);
			// String contextPaht =
			// request.getSession().getServletContext().getContextPath();
			BufferedInputStream bis = new BufferedInputStream(multipartFile.getInputStream());
			byte[] buff = new byte[128];
			int count = -1;
			while ((count = bis.read(buff)) != -1)
			{
				fos.write(buff, 0, count);
			}
			bis.close();
			fos.close();
		}
	}

	public static void uploadFile(List<MultipartFile> files, String time, HttpServletRequest request, String prePath)
			throws IOException
	{
		String savePath = request.getServletContext().getRealPath(UPLOAD_PATH);
		savePath += "/" + prePath;
		File file = new File(savePath);
		if (!file.exists() && !file.isDirectory())
		{
			file.mkdirs();
		}
		for (MultipartFile multipartFile : files)
		{
			if (multipartFile.getSize() == 0)
			{
				continue;
			}
			String fileName = multipartFile.getOriginalFilename();
			// 获取图片的扩展名
			String extensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
			// 新的图片文件名 = 获取时间戳+"."图片扩展名
			String newFileName = time + "." + extensionName;

			String url = savePath + "/" + newFileName;
			;
			FileOutputStream fos = new FileOutputStream(url);
			// String contextPaht =
			// request.getSession().getServletContext().getContextPath();
			BufferedInputStream bis = new BufferedInputStream(multipartFile.getInputStream());
			byte[] buff = new byte[128];
			int count = -1;
			while ((count = bis.read(buff)) != -1)
			{
				fos.write(buff, 0, count);
			}
			bis.close();
			fos.close();
		}
	}

	public static void writeFile(HttpServletRequest request) throws IOException, InvalidFormatException
	{
		String fullFileName = request.getServletContext().getRealPath(DOWNLOAD_PATH + "/��Ȩ������·ģ��.xls");
		// ��ȡ�ļ�
		InputStream in = new FileInputStream(fullFileName);

		/* Workbook wb = WorkbookFactory.create(in); */
		POIFSFileSystem fs = new POIFSFileSystem(in);
		// ����ģ�幤����
		HSSFWorkbook wb = new HSSFWorkbook(fs);// excel�ļ�����
		// ��ȡ��һҳ
		HSSFSheet sheetlist = wb.getSheetAt(0);// ���������

		String[] textlist =
		{ "�б�1", "�б�2", "�б�3", "�б�4", "�б�5" };

		sheetlist = setHSSFValidation(sheetlist, textlist, 3, 500, 0, 0);
		/*
		 * DataValidationHelper helper = sheet.getDataValidationHelper();
		 * sheet.getLastRowNum();
		 */
		String[] poi =
		{ "1", "2" };
		// CellRangeAddressList(firstRow, lastRow, firstCol, lastCol)�������з�Χ
		/*
		 * CellRangeAddressList addressList = new CellRangeAddressList(3, 500,
		 * 17, 17); DataValidationConstraint constraint =
		 * helper.createExplicitListConstraint(poi); DataValidation
		 * dataValidation = helper.createValidation(constraint, addressList);
		 */
		// ����Excel����������
		/*
		 * if (dataValidation instanceof XSSFDataValidation) {
		 * dataValidation.setSuppressDropDownArrow(true);
		 * dataValidation.setShowErrorBox(true); } else {
		 * dataValidation.setSuppressDropDownArrow(false); }
		 * sheet.addValidationData(dataValidation);
		 */

		String url = request.getServletContext().getRealPath(DOWNLOAD_PATH) + "/"
				+ java.net.URLEncoder.encode("1111" + new Date().getTime() + ".xls", "UTF-8");
		;
		createExcel(wb, url);
		in.close();
	}

	public static void createExcel(Workbook workbook, String path)
	{
		FileOutputStream fileOut = null;
		try
		{
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
		} catch (Exception e)
		{
		} finally
		{
			try
			{
				if (fileOut != null)
				{
					fileOut.close();
				}

			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * ����ĳЩ�е�ֵֻ������Ԥ�Ƶ����,��ʾ������.
	 *
	 * @param sheetlist
	 *            Ҫ���õ�sheet.
	 * @param textlist
	 *            ��������ʾ������
	 * @param firstRow
	 *            ��ʼ��
	 * @param endRow
	 *            ������
	 * @param firstCol
	 *            ��ʼ��
	 * @param endCol
	 *            ������
	 * @return ���úõ�sheet.
	 */
	public static HSSFSheet setHSSFValidation(HSSFSheet sheetlist, String[] textlist, int firstRow, int endRow,
			int firstCol, int endCol)
	{
		// ���������б�����
		DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
		// ���������Ч�Լ������ĸ���Ԫ����,�ĸ�����ֱ��ǣ���ʼ�С���ֹ�С���ʼ�С���ֹ��
		CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
		// �����Ч�Զ���
		HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
		sheetlist.addValidationData(data_validation_list);
		return sheetlist;
	}

	public static void downLoadFile(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String fileName = "��Ȩ������Աģ��.xlsx";
		// �����ļ�MIME����
		/* response.setContentType("application/x-download"); */
		// ����Content-Disposition
		/*
		 * response.setHeader("Content-Disposition",
		 * "attachment;filename=��Ȩ������Աģ��.xlsx");
		 */
		// ��ȡĿ���ļ���ͨ��response��Ŀ���ļ�д���ͻ���

		response.setContentType("multipart/form-data");
		// 2.�����ļ�ͷ�����һ�����������������ļ���(�������ǽ�a.pdf)
		response.setHeader("Content-Disposition",
				"attachment;fileName=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
		// ��ȡĿ���ļ��ľ��·��
		String fullFileName = request.getServletContext().getRealPath(DOWNLOAD_PATH + "/��Ȩ������Աģ��.xlsx");
		// ��ȡ�ļ�
		InputStream in = new FileInputStream(fullFileName);
		OutputStream out = response.getOutputStream();

		// д�ļ�
		int b;
		while ((b = in.read()) != -1)
		{
			out.write(b);
		}

		in.close();
		out.close();

	}

	/**
	 * @author
	 * @param response
	 *            ��Ӧ����
	 * @param request
	 *            �������
	 * @param header
	 *            excelͷ����������
	 * @param keys
	 *            ÿһ�����Ӧ��map�е�key���������˳��һֱ
	 * @param sheetTitle
	 *            sheetҳ��name
	 * @param dataset
	 *            ���Դ
	 * @param modelName
	 *            ģ�����
	 * @throws Exception
	 *             �쳣
	 * @since JDK 1.6
	 */
	public static void exportExcel(HttpServletResponse response, HttpServletRequest request, String[] header,
			String[] keys, String sheetTitle, List<Map<String, String>> dataset, String modelName) throws Exception
	{
		String basepath = request.getServletContext().getRealPath(DOWNLOAD_PATH);
		String realpath = basepath + File.separator + modelName + File.separator + modelName + "_"
				+ new Date().getTime() + ".xls";
		File file = new File(realpath);
		if (!file.exists())
		{
			file.createNewFile();
		}
		OutputStream out = new FileOutputStream(file);
		createExcel(sheetTitle, header, keys, dataset, out, PATTEN);
		out.close();
		downLoadFile(realpath, response);
		File f = new File(realpath);
		if (f.exists())
		{
			f.delete();
		}
	}

	public static void downLoadFile(String path, HttpServletResponse response)
	{
		try
		{
			// path��ָ�����ص��ļ���·����
			File file = new File(path);
			// ȡ���ļ���
			String filename = file.getName().split("_")[0] + ".xls";
			// ��������ʽ�����ļ���
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// ���response
			response.reset();
			// ����response��Header
			response.addHeader("Content-Disposition",
					"attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public static void createExcel(String title, String[] headers, String[] keys,
			Collection<Map<String, String>> dataset, OutputStream out, String pattern) throws Exception
	{
		// ����һ��������
		HSSFWorkbook workbook = new HSSFWorkbook();
		// ���һ�����
		HSSFSheet sheet = workbook.createSheet(title);
		// ���ñ��Ĭ���п��Ϊ15���ֽ�
		sheet.setDefaultColumnWidth((short) 15);
		// ���һ����ʽ
		HSSFCellStyle style = workbook.createCellStyle();
		// ������Щ��ʽ
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// ���һ������
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// ������Ӧ�õ���ǰ����ʽ
		style.setFont(font);
		// ��ɲ�������һ����ʽ
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// �����һ������
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// ������Ӧ�õ���ǰ����ʽ
		style2.setFont(font2);

		// ����һ����ͼ�Ķ���������
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// ����ע�͵Ĵ�С��λ��,����ĵ�
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// ����ע������
		/* comment.setString(new HSSFRichTextString("������POI�����ע�ͣ�")); */
		// ����ע�����ߣ�������ƶ�����Ԫ�����ǿ�����״̬���п���������.
		comment.setAuthor("leno");

		// �����������
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++)
		{
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// �������ݣ����������
		Iterator<Map<String, String>> it = dataset.iterator();
		int index = 0;
		while (it.hasNext())
		{
			index++;
			row = sheet.createRow(index);
			Map<String, String> t = it.next();
			// ���÷��䣬���javabean���Ե��Ⱥ�˳�򣬶�̬����getXxx()�����õ�����ֵ
			int count = 0;
			for (int i = 0; i < t.keySet().size(); i++)
			{
				HSSFCell cell = row.createCell(count);
				count++;
				cell.setCellStyle(style2);
				Object value = t.get(keys[i]);
				// �ж�ֵ�����ͺ����ǿ������ת��
				String textValue = null;
				if (value instanceof Boolean)
				{
					boolean bValue = (Boolean) value;
					textValue = "��";
					if (!bValue)
					{
						textValue = "Ů";
					}
				} else if (value instanceof Date)
				{
					Date date = (Date) value;
					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
					textValue = sdf.format(date);
				} else if (value instanceof byte[])
				{
					// ��ͼƬʱ�������и�Ϊ60px;
					row.setHeightInPoints(60);
					// ����ͼƬ�����п��Ϊ80px,ע�����ﵥλ��һ������
					sheet.setColumnWidth(count, (short) (35.7 * 80));
					// sheet.autoSizeColumn(i);
					byte[] bsValue = (byte[]) value;
					HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
					anchor.setAnchorType(2);
					patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
				} else
				{
					// ����������Ͷ������ַ�򵥴���
					textValue = value.toString();
				}
				// �����ͼƬ��ݣ�������������ʽ�ж�textValue�Ƿ�ȫ�����������
				if (textValue != null)
				{
					Pattern p = Pattern.compile("^//d+(//.//d+)?$");
					Matcher matcher = p.matcher(textValue);
					if (matcher.matches())
					{
						// �����ֵ���double����
						cell.setCellValue(Double.parseDouble(textValue));
					} else
					{
						HSSFRichTextString richString = new HSSFRichTextString(textValue);
						HSSFFont font3 = workbook.createFont();
						font3.setColor(HSSFColor.BLUE.index);
						richString.applyFont(font3);
						cell.setCellValue(richString);
					}
				}
			}
		}
		workbook.write(out);
	}

	public static SysUserVO getUserInfo(HttpServletRequest request)
	{
		HttpSession httpSession = request.getSession();
		Object user = httpSession.getAttribute(SystemConstant.SYS_USER);
		if (null != user)
		{
			return (SysUserVO) user;
		}
		return null;
	}

	public static String buildInSQL(Collection<?> ins, String field, boolean needYinhao)
	{
		if (ins == null || ins.isEmpty())
		{
			return "1=1";
		}
		StringBuffer sb = new StringBuffer(field).append(" ").append("in( ");
		for (Object object : ins)
		{
			if (needYinhao)
			{
				sb.append("'").append(object.toString()).append("',");
			} else
			{
				sb.append(object.toString()).append(",");
			}
		}
		String result = sb.toString().substring(0, sb.length() - 1) + ")";
		return result;
	}

	public static String getChilds(String child,String filed)
	{
		StringBuffer childSQL = new StringBuffer();
		if (!child.isEmpty())
		{
			childSQL.append(" AND ("+filed+" IN(");
			String[] childs = child.split(",");
			for (int i = 0; i < childs.length; i++)
			{
				if (i > 0 && i % 100 == 0)
				{
					childSQL.append(") OR "+filed+" IN(" + childs[i] + ",");
				} else
				{
					childSQL.append(childs[i] + ",");
				}
			}
			childSQL.append(")) ");
		}
		return childSQL.toString().replaceAll(",\\)", "\\)");
	}
	
	public static Map<String, Object> buildField2VOMap(List<?> lst, String field)
	{
	    Map<String, Object> result = new HashMap<>();
	    for (Object object : lst) {
		try
		{
			Field f = object.getClass().getDeclaredField(field);
			f.setAccessible(true);
			String value = String.valueOf(f.get(object));
			result.put(value, object);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e )
		{
			return result;
		}
	    }
		return result;
	}

	public static boolean isMobileNO(String mobiles)
	{
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 向客户端的固定频道发送消息
	 * 
	 * @author yanhp
	 * @param userID
	 * @param data
	 * @param channel
	 * @since JDK 1.6
	 */
	public static void sendMessageToClient(List<String> userID, Object data, String channel)
	{
		List<CometConnection> conns = new ArrayList<CometConnection>();
		CometEngine engine = CometContext.getInstance().getEngine();
		for (String id : userID)
		{
			conns.add(engine.getConnection(PushMessageConstant.userID2ConnID.get(StringUtil.strToInt(id))));
		}
		engine.sendTo(channel, conns, data);
	}

	/**
	 * 想固定频道的所有用户发送消息
	 * 
	 * @author Administrator
	 * @param data
	 * @param channel
	 * @since JDK 1.6
	 */
	public static void sendMessageToAllClient(Object data, String channel)
	{
		CometEngine engine = CometContext.getInstance().getEngine();
		engine.sendToAll(channel, data);
	}

	/**
	 * 判断是否是倍数
	 * @param account
	 * @param systemEnum
	 * @return
	 */
	public static int isMultiple(BigDecimal account, SystemEnum systemEnum) {
		BigDecimal multiple = new BigDecimal(DictionariesService.dictionaries.get(systemEnum.getDesc()));
		BigDecimal temp = account.divide(multiple, 2, RoundingMode.HALF_UP);
		return new BigDecimal(temp.intValue()).compareTo(temp);
	}
}