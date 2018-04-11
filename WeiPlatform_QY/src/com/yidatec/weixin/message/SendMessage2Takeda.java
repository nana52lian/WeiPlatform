package com.yidatec.weixin.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import com.yidatec.weixin.action.AesException;
import com.yidatec.weixin.entity.message.ServiceMessage;
import com.yidatec.weixin.util.PropertiesUtil;

public class SendMessage2Takeda {
	/**
	 * POST XML
	 * 
	 * @param urlStr
	 * @param message
	 */
	public static boolean testPost(String urlStr, ServiceMessage outMsg) {
		try {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
//			con.setRequestProperty("Connection", "close");
		    con.setRequestProperty("accept", "*/*");
		    con.setRequestProperty("Accept-Charset", "UTF-8");
		    con.setRequestProperty("contentType", "UTF-8");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");

			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			String xmlInfo = getXmlInfo(outMsg);
//			String newstr = new String(xmlInfo.getBytes("UTF-8"));导致乱码
//			xmlInfo = urlEncode(newstr, "UTF-8");
			out.write(xmlInfo);// ISO-8859-1
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String line = "";
			for (line = br.readLine(); line != null; line = br.readLine()) {
				System.out.println(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (AesException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static String getXmlInfo(ServiceMessage outMsg) throws AesException {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("    <ToUserName>" + outMsg.getTouser() + "</ToUserName>");
		sb.append("    <FromUserName>" + PropertiesUtil.ppsConfig.getProperty("TAKEDA_CORPID") + "</FromUserName>");
		sb.append("    <CreateTime>" + System.currentTimeMillis() + "</CreateTime>");
		sb.append("    <MsgType>" + outMsg.getMsgtype() + "</MsgType>");
		sb.append("    <AgentID>" + PropertiesUtil.ppsConfig.getProperty("TAKEDA_AGENTID") + "</AgentID>");
		sb.append("    <Content>" + outMsg.getText().get("content") + "</Content>");
		sb.append("</xml>");
		return sb.toString();
	}
	public static String urlEncode(String source, String encode) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, encode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "0";
		}
		return result;
	}

}
