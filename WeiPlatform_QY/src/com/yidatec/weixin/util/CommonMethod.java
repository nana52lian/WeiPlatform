package com.yidatec.weixin.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

public class CommonMethod {

	public static final int BUFFER_SIZE = 2 * 1024; // 2M
	public final static String UPLOAD_IMAGE_PATH = "upload_files/images/";
	public final static String UPLOAD_IMAGE_TEMP_PATH = "temp_files/";

	// 格式化内容，再存到数据库
	public static String formatContext(String context) {
		while (context.indexOf("\n") != -1) {
			context = context.substring(0, context.indexOf("\n")) // + "<br>"
					+ context.substring(context.indexOf("\n") + 1);
		}
		while (context.indexOf(" ") != -1) {
			context = context.substring(0, context.indexOf(" ")) + "&nbsp;"
					+ context.substring(context.indexOf(" ") + 1);
		}
		return context;
	}

	/**
	 * 根据日期字符串获取Date型日期
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date getDateFromString(String dateStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得当前系统时间，以Date类型返回
	 * 
	 * @return
	 */
	public static Date getNowDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(0);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * 获得当前系统时间，以String类型返回
	 * 
	 * @return
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获得当前系统时间，以String类型返回
	 * 
	 * @return
	 */
	public static String getStringDateLong() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取过期时间 从当前时间加上多少天过期
	 * 
	 * @param day
	 *            多少天后过期
	 * @return
	 */
	public static String getExpiredTime(int day) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Date currentDate = new Date();
		cal.setTime(currentDate);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.add(Calendar.DAY_OF_YEAR, day);
		return formatter.format(cal.getTime());
	}

	/**
	 * 获取当前年，格式�?08
	 * 
	 * @return
	 */
	public static String getStringYear() {
		String year = getStringDateShort().split("-")[0];
		return year.substring(2, year.length());
	}

	/**
	 * 获取当前月，格式�?05
	 * 
	 * @return
	 */
	public static String getStringMonth() {
		String month = getStringDateShort().split("-")[1];
		return month;
	}

	/**
	 * 格式化日期到日
	 * 
	 * @param strDate
	 * @return
	 */
	public static String strToDate(String strDate) {
		if (strDate == null) {
			return "";
		}
		String tmpDate = strDate.split(" ")[0];
		if ("1900-01-01".equals(tmpDate)) {
			tmpDate = "";
		}
		return tmpDate;
	}

	/**
	 * 格式化日期到分
	 * 
	 * @param strDate
	 * @return
	 */
	public static String strToDateTime(String strDate) {
		if (strDate == null) {
			return "";
		}
		return strDate.substring(0, strDate.lastIndexOf(":"));
	}

	/**
	 * 格式化日期到秒
	 * 
	 * @param strDate
	 * @return
	 */
	public static String strToDateTimeSecond(String strDate) {
		if (strDate == null) {
			return "";
		}
		if (strDate.lastIndexOf(".") != -1)
			return strDate.substring(0, strDate.lastIndexOf("."));
		return null;
	}

	/**
	 * 格式化日期的时间
	 * 
	 * @param strDate
	 * @return
	 */
	public static String strToTime(String strDate) {
		if (strDate == null) {
			return "";
		}
		return strDate.substring(strDate.lastIndexOf(" ") + 1,
				strDate.lastIndexOf(":"));
	}

	public static String getStringCode() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 用时间作为文件名 yyyyMMddHHmmssSSS
	 * 
	 * @return
	 */
	public static String getFileNameByDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 从缓存区域将文件复制到图片文件夹
	 * 
	 * @param src
	 * @param dst
	 */
	public static boolean copyFile(File src, File dst) throws Exception {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src),
						BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// m_logger.error("CommonMethod::copyFile:" + e.getMessage());
		}
		return true;
	}

	/**
	 * 将图片从一个文件夹移至另一个文件夹
	 * 
	 * @param src
	 * @param dst
	 */
	public static boolean copyFile(String src, String dst) throws Exception {
		try {
			File srcFile = new File(src);
			File dstFile = new File(dst);
			if (CommonMethod.copyFile(srcFile, dstFile)) {
				CommonMethod.deleteFile(srcFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 获取扩展 名
	 * 
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}

	/**
	 * 创建日期文件夹
	 * 
	 * @return
	 */
	public static String newFolder(String path) throws Exception {
		String str = "";
		try {
			File myFilePath = new File(ServletActionContext.getServletContext()
					.getRealPath(path)
					+ "/"
					+ new SimpleDateFormat("yyyyMMdd").format(new Date()));
			if (!myFilePath.exists()) {
				myFilePath.mkdir();
			}
			str = myFilePath.getName();
		} catch (Exception e) {
			// m_logger.error("CommonMethod::copyFile:" + e.getMessage());
			throw e;
		}
		return str;
	}

	public static void deleteFile(File tmpFile) {
		try {
			if (tmpFile.exists()) {
				if (tmpFile.delete()) {
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void deleteFile(String uploadFile) {
		try {
			String uploadUrl = UPLOAD_IMAGE_PATH + uploadFile;

			File uploadPath = new File(ServletActionContext.getServletContext()
					.getRealPath(uploadUrl));
			if (uploadPath.exists()) {
				if (uploadPath.delete()) {
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void deleteFileTemp(String uploadFile) {
		try {
			String uploadUrl = UPLOAD_IMAGE_TEMP_PATH + uploadFile;
			// 将上传成功的图片移到正式文件夹，并删除临时文件夹中的文件
			File uploadPath = new File(ServletActionContext.getServletContext()
					.getRealPath(uploadUrl));
			if (uploadPath.exists()) {
				if (uploadPath.delete()) {
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 判断String是否为null, "","null" 或空格
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		boolean bool = false;
		if (obj == null || "".equals(obj) || "null".equals(obj)) {
			return true;
		}

		if (obj instanceof String && "".equals(((String) obj).trim())) {
			return true;

		}

		if (obj instanceof Collection && ((Collection) obj).size() <= 0) {
			return true;
		}

		return bool;
	}

	/**
	 * 判断String是否不为null "" 或空格
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * 将非空的字符串trim
	 * 
	 * @param str
	 * @return
	 */
	public static String stringTrim(String str) {
		String ret = str;
		if (ret != null) {
			ret = ret.trim();
		}

		return ret;
	}

	/**
	 * 合并数组并返回新数组
	 * 
	 * @param objArr1
	 * @param objArr2
	 * @return
	 */
	public static Object[] mergeArray(Object[] objArr1, Object[] objArr2) {
		Object[] totalArr = null;
		if (objArr1 == null) {
			return objArr2;
		}

		if (objArr2 == null) {
			return objArr1;
		}

		int arr1Length = objArr1.length;
		int arr2Length = objArr2.length;

		totalArr = new Object[objArr1.length + objArr2.length];
		System.arraycopy(objArr1, 0, totalArr, 0, arr1Length);
		System.arraycopy(objArr2, 0, totalArr, arr1Length, arr2Length);

		return totalArr;
	}

	/**
	 * 判断批量数据库操作是否成功
	 * 
	 * @param list_res
	 * @return
	 */
	public static boolean updateSuccess(int[] list_res) {
		if (list_res == null) {
			return false;
		}
		int sum = 0;
		for (int i = 0; i < list_res.length; i++) {
			sum = sum + list_res[0];
		}
		return sum == list_res.length;
	}

	/**
	 * 判断批量数据库操作是否成功
	 * 
	 * @param list_res
	 * @return
	 */
	public static boolean batchUpdateSuccess(int[] list_res, int count) {
		if (list_res == null) {
			return false;
		}
		int sum = 0;
		for (int i = 0; i < list_res.length; i++) {
			sum = sum + list_res[i];
		}
		return sum == count;
	}

	/**
	 * 获取6位随机密码
	 * 
	 * @return
	 */
	public static String randomPass() {
		return String.valueOf((Math.random() + 1) * 1000000).substring(1, 7);
	}

	/**
	 * 是否小于当前日期
	 * 
	 * @param date
	 * @return
	 */
	public static boolean lessThanCurrentDate(String date) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate = new Date();
		Date compareDate = formatter.parse(date);
		return compareDate.before(currentDate);
	}

	/**
	 * 是否大于当前日期
	 * 
	 * @param date
	 * @return
	 */
	public static boolean moreThanCurrentDate(String date) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate = new Date();
		Date compareDate = formatter.parse(date);
		return compareDate.after(currentDate);
	}
	
	/**
	 * 获得当前系统时间，以String类型返回
	 * 
	 * @return
	 */
	public static String getStringDatePrefix() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	// readContentFromPost
	public static String readContentFromPost(String content) {
		URL url;
		String xmlData = "";
		try {
			url = new URL(
					"https://wap.tenpay.com/cgi-bin/wappayv2.0/wappay_init.cgi");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());

			// String content =
			// "ver=2.0&charset=1&bank_type=0&desc=测试商品&purchaser_id=6766724&bargainor_id=111111111111&sp_billno=0001&total_fee=10&notify_url=http://www.baidu.com&callback_url=http://www.163.com&sign=5E2231E3166938D4C577CDF6D915E21A";

			out.writeBytes(content);
			out.flush();
			out.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			System.out.println("=============================");
			System.out.println("Contents of post request");
			System.out.println("=============================");
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				xmlData += line;
			}
			System.out.println("=============================");
			System.out.println("Contents of post request ends");
			System.out.println("=============================");
			reader.close();
			connection.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xmlData;
	}

	public static List<String> subArray(String[] arrayA, String[] arrayB) {
		List<String> listA = Arrays.asList(arrayA);
		List<String> listB = Arrays.asList(arrayB);

		Set<String> setA = new HashSet<String>(listA);
		setA.removeAll(listB);
		System.out.println(setA);
		List<String> returnList = new ArrayList<String>();
		for (String url : setA) {
			returnList.add(url);
		}
		return returnList;
	}

	// 删除指定文件夹下所有文件
	public static boolean delAllFile(String path) {
		if (path == null) {
			String pathClass = CommonMethod.class.getResource("/").getPath();
			path = pathClass.substring(0, pathClass.lastIndexOf("WEB-INF"))
					+ UPLOAD_IMAGE_TEMP_PATH;
		}
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	// 删除文件夹
	// param folderPath 文件夹完整绝对路径

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getTextFromHtml(String htmlStr) {
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签
		return htmlStr.trim(); // 返回文本字符串
	}

	public static int getDaysBetween(Calendar d1, Calendar d2) {
		if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
		int y2 = d2.get(Calendar.YEAR);
		if (d1.get(Calendar.YEAR) != y2) {
			d1 = (Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
				d1.add(Calendar.YEAR, 1);
			} while (d1.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	/**
	 * 计算2个日期之间的相隔天数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public int getWorkingDay(Calendar d1, Calendar d2) {
		int result = -1;
		if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}

		int betweendays = getDaysBetween(d1, d2);

		int charge_date = 0;
		int charge_start_date = 0;// 开始日期的日期偏移量
		int charge_end_date = 0;// 结束日期的日期偏移量
		// 日期不在同一个日期内
		int stmp;
		int etmp;
		stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
		etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
		if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
			charge_start_date = stmp - 1;
		}
		if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
			charge_end_date = etmp - 1;
		}
		// }
		result = (getDaysBetween(this.getNextMonday(d1), this.getNextMonday(d2)) / 7)
				* 5 + charge_start_date - charge_end_date;
		// System.out.println("charge_start_date>" + charge_start_date);
		// System.out.println("charge_end_date>" + charge_end_date);
		// System.out.println("between day is-->" + betweendays);
		return result;
	}

	/**
	 * 获得日期的下一个星期一的日期
	 * 
	 * @param date
	 * @return
	 */
	public Calendar getNextMonday(Calendar date) {
		Calendar result = null;
		result = date;
		do {
			result = (Calendar) result.clone();
			result.add(Calendar.DATE, 1);
		} while (result.get(Calendar.DAY_OF_WEEK) != 2);
		return result;
	}

	public String getChineseWeek(Calendar date) {
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六" };

		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);

		// System.out.println(dayNames[dayOfWeek - 1]);
		return dayNames[dayOfWeek - 1];

	}

	public static int getDaysOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		System.out.print(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static int getMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH)+1;
	}

	/** 
	 * 判断是否是QQ表情 
	 *  
	 * @param content 
	 * @return 
	 */
	public static boolean isQqFace(String content) {
		// 判断QQ表情的正则表达式
		String qqfaceRegex = "/::\\)|/::~";
		Pattern p = Pattern.compile(qqfaceRegex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			System.out.println(m.group());
			//return true;
		}
		return false;
	}
	
	/** 
	 * 将微信消息中的CreateTime转换成标准格式的时间（yyyy-MM-dd HH:mm:ss） 
	 *  
	 * @param createTime 消息创建时间 
	 * @return 
	 */
	public static String formatTime(String createTime) {
		// 将微信传入的CreateTime转换成long类型，再乘以1000
		long msgCreateTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(msgCreateTime));
	}
	
	
	/**
	 * 时间相减(小时)
	 * @param createTime
	 * @return
	 */
	public static int dateSubtract(String createDate) {
		//当前系统时间
		Date currentTime = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date createTime;
		int hh = 0;
		try {
			createTime = format.parse(createDate);
			long e = currentTime.getTime() - createTime.getTime(); 
			long ss= e/(1000); //共计秒数
			//int MM = (int)ss/60;   //共计分钟数
			hh=(int)ss/3600;  //共计小时数
			//int dd=(int)hh/24;   //共计天数
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return hh;
	}
	
	/**
	 * 时间相减(分钟)
	 * @param createTime
	 * @return
	 */
	public static int dateSubtractForMinute(String createDate) {
		//当前系统时间
		Date currentTime = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date createTime;
		int MM = 0;
		try {
			createTime = format.parse(createDate);
			long e = currentTime.getTime() - createTime.getTime(); 
			long ss= e/(1000); //共计秒数
			MM = (int)ss/60;   //共计分钟数
			//hh=(int)ss/3600;  //共计小时数
			//int dd=(int)hh/24;   //共计天数
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return MM;
	}
	
	
	public static void main(String[] args) {
//		String msg = "你好/::),再见吧/::~";
//		System.out.println(CommonMethod.isQqFace(msg));
//		
//		getDaysOfMonth(new Date());
		
		
		dateSubtract("2014-01-10 08:00:57");

//		int n = 1;
//		String str1 = String.format("%02d", n);
//		System.out.println(str1);

		// CommonMethod.getExpiredTime(5);
		// String htmlStr =
		// "<img src='http://192.168.70.12:8080/o2obuy/js/fckeditor/editor/images/smiley/msn/devil_smile.gif' alt='' />&nbsp;你好啊，我是XXXXX，有图有真相！<a href='http://www.baidu.com'>www.baidu.com</a><br /><input type='image' src='http://192.168.70.12:8080/o2obuy/upload_files/image/u=3252615714,1090610325&amp;fm=52&amp;gp=0.jpg' width='161' height='220' />";
		//
		// String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>";
		// //定义script的正则表达式
		// String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>";
		// //定义style的正则表达式
		// String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

		// Pattern
		// p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
		// Matcher m_script=p_script.matcher(htmlStr);
		// htmlStr=m_script.replaceAll(""); //过滤script标签
		//
		// Pattern
		// p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
		// Matcher m_style=p_style.matcher(htmlStr);
		// htmlStr=m_style.replaceAll(""); //过滤style标签

		// Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
		// Matcher m_html=p_html.matcher(htmlStr);
		// htmlStr=m_html.replaceAll(""); //过滤html标签
		//
		// System.out.println(htmlStr);
		// return htmlStr.trim(); //返回文本字符串
	}

}
