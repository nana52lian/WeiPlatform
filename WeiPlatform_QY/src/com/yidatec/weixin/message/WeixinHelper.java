package com.yidatec.weixin.message;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.action.AesException;
import com.yidatec.weixin.entity.message.ServiceMessage;
import com.yidatec.weixin.helper.MessageHelper;
import com.yidatec.weixin.util.HttpClientHelper;
import com.yidatec.weixin.util.MD5Util_YD;
import com.yidatec.weixin.util.PropertiesUtil;

/**
 * 处理微信消息帮助类
 * @author Lance
 *
 */
public class WeixinHelper {
	
	private static final Logger log = LogManager.getLogger(WeixinHelper.class);
	
	/** access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token */
	public static String ACCESS_TOKEN = "";
	
	public static String ACCESS_TOKEN_EN ="";
	
	/** 初始化token的时间 */
	public static Date INIT_TOKEN_TIME = null; 
	
	public static Date INIT_TOKEN_TIME_EN = null; 
	
	/*************************翼多接口************************************************/
	
	public final static String key = "620df6ec2d7b3fbc";
	
	public final static String  Secret="102CD6B4C23D60D29CBC2A546FE757D8";
	
	public final static String URL_GETUSERINFO = "http://eaopen.astrazeneca.com/api/getuserinfo";
	
	public final static String URL_SENDMESSAGE = "http://eaopen.astrazeneca.com/api/sendmessage";
	
	public final static String URL_SENDIMAGEMESSAGE = "";
	
	public final static String URL_GETMEDIA = "http://eaopen.astrazeneca.com/api/GetMedia";
	
	
	/*****************************************************************************/
	public final static String AgentID = PropertiesUtil.ppsConfig.getProperty("TAKEDA_AGENTID");
	
	/** 应用的APPID */
//	public final static String APP_ID = "wx5ce7d77750e6abc3";//PropertiesUtil.ppsConfig.getProperty("APP_ID");
	
	/** 应用的APPKEY */
//	public final static String APP_SECRET = "JrWUkGw_OpzW0WSFZzLG_tOW9gB8G50Qkfg9Rvgza-k5WrSfKUzWYtUURJo4whDk"; //PropertiesUtil.ppsConfig.getProperty("APP_SECRET");
	
	/** 应用的APPID */
	public final static String APP_ID = PropertiesUtil.ppsConfig.getProperty("APP_ID");
	
	/** 应用的APPKEY */
	public final static String APP_SECRET = PropertiesUtil.ppsConfig.getProperty("APP_SECRET");
	
	/** 英文应用的APPID */
//	public final static String APP_ID_EN = "wx5ce7d77750e6abc3";
	
	/** 英文应用的APPKEY */
//	public final static String APP_SECRET_EN = "JrWUkGw_OpzW0WSFZzLG_tOW9gB8G50Qkfg9Rvgza-k5WrSfKUzWYtUURJo4whDk";
	
	/** 获取access token的URL &appid=APPID&secret=APPSECRET */
	//public final static String URL_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
	public final static String URL_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?";

	/** 发送客服消息URL */
	//public final static String URL_SEND_MSG = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
	public final static String URL_SEND_MSG = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";
	
	/** 获取关注者列表URL  ACCESS_TOKEN&next_openid=NEXT_OPENID*/
	public final static String URL_GET_SUBSCRIBE = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=";
	
	/** 获取用户信息URL ACCESS_TOKEN&openid=OPENID */
	public final static String URL_GET_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=";
	
	/** 获取用户分组URL */
	public final static String URL_GET_GROUPS = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=";		
		
	/** 创建用户分组URL */
	public final static String URL_CREATE_GROUPS = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=";
	
	/** 修改用户分组URL */
	public final static String URL_UPDATE_GROUPS = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=";	
	
	/** 移动用户到分组URL */
	public final static String URL_MOVE_USER_GROUPS = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=";	
	
	/** 获取多媒体文件URL */
	//public final static String URL_MEDIA_GET = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=";
	public final static String URL_MEDIA_GET = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=";
	
	/**
	 * 获取access token
	 * @return
	 */
	public void checkToken() {
		try {
			if (INIT_TOKEN_TIME != null) {
				Date now = new Date();
				long tmp = now.getTime() - INIT_TOKEN_TIME.getTime();
				// 超时时间为7200秒
				if (tmp / 1000 < 7000) return;
			}
			String get_token_url = URL_TOKEN
							 	 + "&corpid="
							 	 + APP_ID
							 	 + "&corpsecret="			 
							 	 + APP_SECRET;
			HttpClientHelper helper = new HttpClientHelper();
			String resContent = helper.callTrustHttps(get_token_url);
			if (resContent != null) {				
				JSONObject jsonObj = JSONObject.fromObject(resContent);
				if (jsonObj.optInt("errcode") == 0) {
					ACCESS_TOKEN = jsonObj.optString("access_token");
					INIT_TOKEN_TIME = new Date();
				} else {
					log.error(resContent);
				}	
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取access token (中英文)
	 * @return
	 */
//	public void checkToken(String lang) {
//		try {
//			String get_token_url = "";
//			if(lang.equals("cn") || lang.equals("az_cn")){
//				if (INIT_TOKEN_TIME != null) {
//					Date now = new Date();
//					long tmp = now.getTime() - INIT_TOKEN_TIME.getTime();
//					// 超时时间为7200秒
//					if (tmp / 1000 < 7000) return;
//				}
//				get_token_url = URL_TOKEN
//					 	 + "&corpid="
//					 	 + APP_ID
//					 	 + "&corpsecret="			 
//					 	 + APP_SECRET;
//			} else {
//				if (INIT_TOKEN_TIME_EN != null) {
//					Date now = new Date();
//					long tmp = now.getTime() - INIT_TOKEN_TIME_EN.getTime();
//					// 超时时间为7200秒
//					if (tmp / 1000 < 7000) return;
//				}
//				get_token_url = URL_TOKEN
//					 	 + "&corpid="
//					 	 + APP_ID_EN
//					 	 + "&corpsecret="			 
//					 	 + APP_SECRET_EN;
//			}
			
//			HttpClientHelper helper = new HttpClientHelper();
//			String resContent = helper.callTrustHttps(get_token_url);
//			if (resContent != null) {				
//				JSONObject jsonObj = JSONObject.fromObject(resContent);
//				if (jsonObj.optInt("errcode") == 0) {
//					if(lang.equals("cn") || lang.equals("az_cn")){
//						ACCESS_TOKEN = jsonObj.optString("access_token");
//					    INIT_TOKEN_TIME = new Date();
//					} else {
//						ACCESS_TOKEN_EN = jsonObj.optString("access_token");
//						INIT_TOKEN_TIME_EN = new Date();
//					}
//				} else {
//					log.error(resContent);
//				}	
//			}
//		} catch(Exception e) {
//			log.error(e.getMessage(), e);
//		}
//	}
	
	/**
	 * 获取access token
	 * @return
	 */
	public String getAccessToken() {
		try {
			String get_token_url = URL_TOKEN
							 	 + "&appid="
							 	 + APP_ID
							 	 + "&secret="			 
							 	 + APP_SECRET;
			HttpClientHelper helper = new HttpClientHelper();
			String resContent = helper.callTrustHttps(get_token_url);
			if (resContent != null) {
				// {"access_token":"ACCESS_TOKEN","expires_in":7200}			
				JSONObject jsonObj = JSONObject.fromObject(resContent);
				return jsonObj.optString("access_token");
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取access token
	 * @param appid
	 * @param appsecret
	 * @return
	 */
	public String getAccessToken(String appid, String appsecret) {
		if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(appsecret)) return null;
		try {
			String get_token_url = URL_TOKEN
							 	 + "&appid="
							 	 + appid
							 	 + "&secret="			 
							 	 + appsecret;
			HttpClientHelper helper = new HttpClientHelper();
			String resContent = helper.callTrustHttps(get_token_url);
			if (resContent != null) {
				// {"access_token":"ACCESS_TOKEN","expires_in":7200}			
				JSONObject jsonObj = JSONObject.fromObject(resContent);
				return jsonObj.optString("access_token");
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 发送消息
	 * @param message
	 */
	public boolean sendMessage11111111111111(ServiceMessage message) {
		try {
			checkToken();
//			String send_message_url = URL_SEND_MSG + ACCESS_TOKEN;
//			HttpClientHelper helper = new HttpClientHelper();
//			String resContent = helper.callTrustHttps(send_message_url, message.toJson());
//			if (resContent != null) {
//				JSONObject jsonObj = JSONObject.fromObject(resContent);
//				if (jsonObj.optInt("errcode") != 0) {
//					log.error(resContent);
//					if (40001 == jsonObj.optInt("errcode")) {
//						log.error("Token timeout...");
//						WeixinHelper.ACCESS_TOKEN = this.getAccessToken();
//						log.error("New token::" + WeixinHelper.ACCESS_TOKEN);
//						sendMessage(message);
//					}
//				}
//			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
	
	/**
	 * 发送消息
	 * @param message
	 */
	public boolean sendMessageByLandOld(ServiceMessage message,String lang) {
		try {
			message.setAgentid(AgentID);
//			checkToken(lang);
			String send_message_url = "";
			if(lang.equals("cn") || lang.equals("az_cn")){
			    send_message_url = URL_SEND_MSG + ACCESS_TOKEN;
			} else {
				send_message_url = URL_SEND_MSG + ACCESS_TOKEN_EN;
			}
			HttpClientHelper helper = new HttpClientHelper();
			String resContent = helper.callTrustHttps(send_message_url, message.toJson());
			if (resContent != null) {
				JSONObject jsonObj = JSONObject.fromObject(resContent);
				if (jsonObj.optInt("errcode") != 0) {
					log.error(resContent);
					if (40001 == jsonObj.optInt("errcode")) {
						log.error("Token timeout...");
						if(lang.equals("cn") || lang.equals("az_cn")){
							WeixinHelper.ACCESS_TOKEN = this.getAccessToken();
							log.error("New token::" + WeixinHelper.ACCESS_TOKEN);
						} else {
							WeixinHelper.ACCESS_TOKEN_EN = this.getAccessToken();
							log.error("New token::" + WeixinHelper.ACCESS_TOKEN_EN);
						}
						sendMessageByLandOld(message,lang);
					}
				}
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
	
	/**
	 * 获取关注列表	
	 * @return
	 */
	public JSONObject getSubscribeList() {
		return getSubscribeList(null);
	}
	
	/**
	 * 获取关注列表
	 * @param next_openid 关注人超1万用这个参数
	 * @return
	 */
	public JSONObject getSubscribeList(String next_openid) {
		try {
			checkToken();
			String get_subscribe_url = URL_GET_SUBSCRIBE + ACCESS_TOKEN;
			if (next_openid != null) get_subscribe_url = get_subscribe_url + "&next_openid=" + next_openid;
			HttpClientHelper helper = new HttpClientHelper();
			String resContent = helper.callTrustHttps(get_subscribe_url);
			if (resContent != null) {
				log.info("Send message result::" + resContent);
				JSONObject jsonObj = JSONObject.fromObject(resContent);
				return jsonObj;
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取用户信息
	 * @param open_id
	 * @return
	 */
	public JSONObject getUserInfo(String open_id) {
		try {
			checkToken();
			String get_userinfo_url = URL_GET_USERINFO 
									+ ACCESS_TOKEN 
									+ "&openid="
									+ open_id;			
			HttpClientHelper helper = new HttpClientHelper();
			String resContent = helper.callTrustHttps(get_userinfo_url);
			if (resContent != null) {
				log.info("Send message result::" + resContent);
				JSONObject jsonObj = JSONObject.fromObject(resContent);
				return jsonObj;
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取用户信息(根据token)
	 * @param open_id
	 * @return
	 */
	public JSONObject getUserInfoByToken(String open_id,String lang) {
		try {
//			checkToken(lang);
			String get_userinfo_url = "";
			if(lang.equals("cn") || lang.equals("az_cn")){
			   get_userinfo_url = URL_GET_USERINFO 
									+ ACCESS_TOKEN 
									+ "&openid="
									+ open_id;			
			} else {
				get_userinfo_url = URL_GET_USERINFO 
						+ ACCESS_TOKEN_EN 
						+ "&openid="
						+ open_id;	
			}
			HttpClientHelper helper = new HttpClientHelper();
			String resContent = helper.callTrustHttps(get_userinfo_url);
			if (resContent != null) {
				log.info("Send message result::" + resContent);
				JSONObject jsonObj = JSONObject.fromObject(resContent);
				return jsonObj;
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 获取用户分组
	 * @return
	 */
	public JSONObject getGroups() {
		try {
			checkToken();
			String get_groups_url = URL_GET_GROUPS + ACCESS_TOKEN;									
			HttpClientHelper helper = new HttpClientHelper();
			String resContent = helper.callTrustHttps(get_groups_url);
			if (resContent != null) {
				log.info("Send message result::" + resContent);
				JSONObject jsonObj = JSONObject.fromObject(resContent);
				return jsonObj;
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 创建用户分组
	 * @return
	 */
	public JSONObject createGroups(JSONObject jsonGroups) {
		try {
			checkToken();
			String create_groups_url = URL_CREATE_GROUPS + ACCESS_TOKEN;									
			HttpClientHelper helper = new HttpClientHelper();			
			String resContent = helper.callTrustHttps(create_groups_url, jsonGroups.toString());
			if (resContent != null) {
				log.info("Send message result::" + resContent);
				JSONObject jsonObj = JSONObject.fromObject(resContent);
				return jsonObj;
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 修改用户分组
	 * @return
	 */
	public boolean updateGroups(JSONObject jsonGroups) {
		try {
			checkToken();
			String update_groups_url = URL_UPDATE_GROUPS + ACCESS_TOKEN;									
			HttpClientHelper helper = new HttpClientHelper();			
			String resContent = helper.callTrustHttps(update_groups_url, jsonGroups.toString());
			if (resContent != null) {				
				log.info("Send message result::" + resContent);
				JSONObject jsonObj = JSONObject.fromObject(resContent);
				return jsonObj.optInt("errcode") == 0;
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}
	
	/**
	 * 修改用户分组
	 * @return
	 */
	public boolean moveUserToGroup(JSONObject jsonGroups) {
		try {
			checkToken();
			String move_user_group_url = URL_MOVE_USER_GROUPS + ACCESS_TOKEN;									
			HttpClientHelper helper = new HttpClientHelper();			
			String resContent = helper.callTrustHttps(move_user_group_url, jsonGroups.toString());
			if (resContent != null) {				
				log.info("Send message result::" + resContent);
				JSONObject jsonObj = JSONObject.fromObject(resContent);
				return jsonObj.optInt("errcode") == 0;
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 上传媒体文件
	 * @param uploadUrl
	 * @param uploadFile
	 * @param type
	 * @return
	 */
	public String uploadMediaFile(String uploadUrl, File uploadFile, String type) {
		checkToken();
		if (!uploadFile.exists() || !uploadFile.isFile()) {
			return null;
		}
		String returnStr = "";
		uploadUrl += "access_token=" + WeixinHelper.ACCESS_TOKEN + "&type=" + type;
		try {
			URL urlObj = new URL(uploadUrl);
			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);

			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");

			String BOUNDARY = "----------" + System.currentTimeMillis();
			con.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"media\";filename=\""
					+ uploadFile.getName() + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] head = sb.toString().getBytes("utf-8");
			OutputStream out = new DataOutputStream(con.getOutputStream());
			out.write(head);
			DataInputStream in = new DataInputStream(new FileInputStream(
					uploadFile));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
			out.write(foot);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				returnStr += line;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return returnStr;
	}
	
	/**
	 * 获取图片文件
	 * @param mediaId
	 * @param target_path
	 * @return
	 */
	/*public String getMediaFile(String mediaId, String target_path) {
		try {
			checkToken();
			String get_media_url = URL_MEDIA_GET + ACCESS_TOKEN + "&media_id=" + mediaId;			
			int BUFFER_SIZE = 8096; //缓冲区大小
			FileOutputStream fos = null;
	        BufferedInputStream bis = null;
	        HttpURLConnection httpUrl = null;
	        URL url = null;
	        byte[] buf = new byte[BUFFER_SIZE];
	        int size = 0;	        
	        //建立链接
	        url = new URL(get_media_url);
	        httpUrl = (HttpURLConnection) url.openConnection();
	        //连接指定的资源
	        httpUrl.connect();
	        Map<String, List<String>> headerFields = httpUrl.getHeaderFields();
	        String filename = headerFields.get("Content-disposition").get(0);
			filename = filename.substring(filename.indexOf("=\"") + 2, filename.lastIndexOf("\""));
	        //获取网络输入流
	        bis = new BufferedInputStream(httpUrl.getInputStream());
	        //建立文件
	        fos = new FileOutputStream(target_path + "/"+filename);	       
	        //保存文件
	        while ((size = bis.read(buf)) != -1)
	            fos.write(buf, 0, size);

	        fos.close();
	        bis.close();
	        httpUrl.disconnect();
	        return filename;	        
			*//** 
			HttpClientHelper helper = new HttpClientHelper();
			helper.setReqContent(get_media_url);
			if (helper.call()) {
				// {"errcode":42001,"errmsg":"access_token expired"}
				String res = helper.getResContent();
				if (res.indexOf("errcode") != -1) return null;
				// Content-disposition: attachment;
				// filename="y-uf6GxoLOKMMqIbUlRw6YHYczs3WsdD-eSyOJpHTJOR0QvPnAAp-fdjRSolUB5x.jpg"
				Map<String, List<String>> headerFields = helper.getResponseHeaderFields();
				String filename = headerFields.get("Content-disposition").get(0);
				filename = filename.substring(filename.indexOf("=\"") + 2, filename.lastIndexOf("\""));
				File file = new File(target_path + "/"+filename);
				FileOutputStream fops = new FileOutputStream(file);
				fops.write(readInputStream(helper.getInputStream()));
				fops.flush();
				fops.close();
				return filename;
			}*//*
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}*/
	
	/**
	 * 获取图片文件
	 * @param mediaId
	 * @param target_path
	 * @return
	 */
	public String getMediaFile(String mediaId, String target_path,String lang,  String openid) {
		try {
//			checkToken(lang);
			String get_media_url = "";
//			if(lang.equals("cn") || lang.equals("az_cn")){
			   get_media_url = URL_MEDIA_GET + ACCESS_TOKEN + "&media_id=" + mediaId;	
//			} else {
//			   get_media_url = URL_MEDIA_GET + ACCESS_TOKEN_EN + "&media_id=" + mediaId;	
//			}
			int BUFFER_SIZE = 8096; //缓冲区大小
			FileOutputStream fos = null;
	        BufferedInputStream bis = null;
	        HttpURLConnection httpUrl = null;
	        URL url = null;
	        byte[] buf = new byte[BUFFER_SIZE];
	        int size = 0;	        
	        //建立链接
	        url = new URL(get_media_url);
	        httpUrl = (HttpURLConnection) url.openConnection();
	        //连接指定的资源
	        httpUrl.connect();
	        Map<String, List<String>> headerFields = httpUrl.getHeaderFields(); 
	        String filename = headerFields.get("Content-disposition").get(0);
			filename = filename.substring(filename.indexOf("=\"") + 2, filename.lastIndexOf("\""));
	        //String filename = null;
	        /*if(StringUtils.isBlank(openid)){
	        	filename = new Date().getTime() +  ".jpg";
	        } else{
	        	filename = new Date().getTime() + "_" + openid + ".jpg";
	        }*/
	        //获取网络输入流
	        bis = new BufferedInputStream(httpUrl.getInputStream());
	        //建立文件
	        fos = new FileOutputStream(target_path + "/"+filename);	       
	        //保存文件
	        while ((size = bis.read(buf)) != -1)
	            fos.write(buf, 0, size);

	        fos.close();
	        bis.close();
	        httpUrl.disconnect();
	        return filename;	        
			/** 
			HttpClientHelper helper = new HttpClientHelper();
			helper.setReqContent(get_media_url);
			if (helper.call()) {
				// {"errcode":42001,"errmsg":"access_token expired"}
				String res = helper.getResContent();
				if (res.indexOf("errcode") != -1) return null;
				// Content-disposition: attachment;
				// filename="y-uf6GxoLOKMMqIbUlRw6YHYczs3WsdD-eSyOJpHTJOR0QvPnAAp-fdjRSolUB5x.jpg"
				Map<String, List<String>> headerFields = helper.getResponseHeaderFields();
				String filename = headerFields.get("Content-disposition").get(0);
				filename = filename.substring(filename.indexOf("=\"") + 2, filename.lastIndexOf("\""));
				File file = new File(target_path + "/"+filename);
				FileOutputStream fops = new FileOutputStream(file);
				fops.write(readInputStream(helper.getInputStream()));
				fops.flush();
				fops.close();
				return filename;
			}*/
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/** 
     * 从输入流中获取数据 
     * @param inStream 输入流 
     * @return 
     * @throws Exception 
     */  
	private static byte[] readInputStream(InputStream inStream)
			throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	} 
	
	/**
	 * 解析表情
	 * @param message
	 * @return
	 */
	public static String parseEmotion(String message) {
		try {
			String qqfaceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::\\$|/::X|/::Z|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@\\!|/:\\!\\!\\!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";
			Pattern p = Pattern.compile(qqfaceRegex);
			Matcher m = p.matcher(message);
			String emotion_key = null;
			while (m.find()) {
				emotion_key = m.group();
				if ("".equals(emotion_key)) continue;
				//System.out.println(emotion_key);
				String ipAndPort = PropertiesUtil.ppsConfig.getProperty("SERVER_IP") + ":" + PropertiesUtil.ppsConfig.getProperty("SERVER_PORT");
				message = message.replace(emotion_key, MessageHelper.getInstance().getMessage(emotion_key,new String[] { ipAndPort +"/WeiPlatform_QY" }));					
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}		
		return message;
	}
		
	public static void main(String[] args) throws IOException {

		
		//getUserInfoForAZ("hongtu.chi");
		//sendMessageForAZ("hongtu.chi","咨询已经结束，感谢您的配合，可以告诉我们您的咨询感受，我们会认真倾听。本次咨询的ServiceNowID是006，您可以通过该ID查询本次咨询情况。");

		sendMessageForAZ("hongtu.chi","咨询已经结束，感谢您的配合，<a href=\"http://139.219.134.90:80/WeiPlatform_QY/admin/weixin/appraise.jsp?task_queue_id=201508250000015445hongtu.chi\">点击这里</a>可以告诉我们您的咨询感受，我们会认真倾听。本次咨询的ServiceNowID是006，您可以通过该ID查询本次咨询情况。");
		
		//SendImageMessage("hongtu.chi","http://121.40.92.251/WeiPlatform_QY/images/name.png");
		
//		WeixinHelper helper = new WeixinHelper();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String strDate = "2013-11-29 12:00:00";
//		Date before;
//		try {
//			before = format.parse(strDate);
//			Date now = new Date();
//			long tmp = now.getTime() - before.getTime();
//			System.out.println(tmp);
//		} catch (ParseException e) {}	
		//////////////////////////////////////////////
//		helper.getAccessToken();
		//////////////////////////////////////////////
//		ServiceMessage message = new ServiceMessage();
//		message.setTouser("oNkX-jhvFxMpayS0vd2VSQd2GFpo");
//		message.setText("hello");
//		helper.sendMessage(message);
		//////////////////////////////////////////////
//		helper.getSubscribeList();
		//////////////////////////////////////////////
//		helper.getUserInfo("oNkX-jhvFxMpayS0vd2VSQd2GFpo");
		//////////////////////////////////////////////
//		helper.getGroups();
		//////////////////////////////////////////////
//		JSONObject jsonGroups = new JSONObject();
//		JSONObject group = new JSONObject();
//		group.put("name", "test_group_001");
//		jsonGroups.put("group", group);
//		helper.createGroups(jsonGroups);
		//////////////////////////////////////////////
//		JSONObject jsonGroups = new JSONObject();
//		JSONObject group = new JSONObject();
//		group.put("id", "101");
//		group.put("name", "test_group_002");
//		jsonGroups.put("group", group);
//		helper.updateGroups(jsonGroups);
		//////////////////////////////////////////////
//		JSONObject jsonGroups = new JSONObject();
//		jsonGroups.put("openid", "oNkX-jhvFxMpayS0vd2VSQd2GFpo");
//		jsonGroups.put("to_groupid", "101");
//		helper.moveUserToGroup(jsonGroups);
		//////////////////////////////////////////////
//		helper.getMediaFile("y-uf6GxoLOKMMqIbUlRw6YHYczs3WsdD-eSyOJpHTJOR0QvPnAAp-fdjRSolUB5x", "D:\\");
//		helper.getMediaFile("n5xuVm29GbbMmCECotdaByNH6hacuelJhoNX7LV2DM-jkDCBSANxtg1eBtwYeB2D", "D:\\");
		//////////////////////////////////////////////
		
		
	}
	
	/**
	 * 获取用户信息
	 * @param kid
	 */
	public static String getUserInfoForAZ(String kid){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd"); 
		Date d=new Date();
		String dTime=f.format(d);
		String signStr=key+dTime+Secret;
		String sign=MD5Util_YD.MD5(signStr);
		String sss="?key="+key+"&date="+dTime+"&sign="+sign+"&userid="+kid;
		String url=URL_GETUSERINFO+sss;
		String str=new HttpClientHelper().callTrustHttp(url, null);
		System.out.println(str);
		return str;
	}
	
	/**
	 * 发送文本信息
	 * @param kid
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static boolean sendMessageForAZ(String kid,String content) throws IOException{
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd"); 
		Date d=new Date();
		String dTime=f.format(d);  
		String signStr=key+dTime+Secret;
		String sign=MD5Util_YD.MD5(signStr);  
		String sss="key="+key+"&date="+dTime+"&sign="+sign+"&userid="+kid+"&content="+content;
		byte[] postData = sss.getBytes("UTF-8");
		boolean ret = new HttpClientHelper().httpPostMethod(URL_SENDMESSAGE, postData);
		System.out.println(ret);
		return ret;
	}
	
	/**
	 * 发送图片消息
	 * @param kid
	 * @param imageurl
	 * @return
	 * @throws IOException
	 */
	public static boolean SendImageMessage(String kid,String imageurl) throws IOException{
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd"); 
		Date d=new Date();
		String dTime=f.format(d);  
		String signStr=key+dTime+Secret;
		String sign=MD5Util_YD.MD5(signStr);  
		String sss="key="+key+"&date="+dTime+"&sign="+sign+"&userid="+kid+"&imageurl="+imageurl;
		byte[] postData = sss.getBytes("UTF-8");
		boolean ret = new HttpClientHelper().httpPostMethod(URL_SENDIMAGEMESSAGE, postData);
		System.out.println(ret);
		return ret;
	}
	
	/**
	 * 获取MEDIAID素材
	 * @param mediaid
	 */
	public String GetMedia(String mediaid) {
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd"); 
		Date d=new Date();
		String dTime=f.format(d);
		String signStr=key+dTime+Secret;
		String sign=MD5Util_YD.MD5(signStr);
		String sss="?key="+key+"&date="+dTime+"&sign="+sign+"&mediaid="+mediaid;
		String url=URL_GETMEDIA+sss;
		String str=new HttpClientHelper().callTrustHttp(url, null);
		System.out.println(str);
		return str;
	}

}
