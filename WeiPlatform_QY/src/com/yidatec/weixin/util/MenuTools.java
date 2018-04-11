package com.yidatec.weixin.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import com.yidatec.weixin.message.WeixinHelper;


public class MenuTools {
	
	// 获取TOKEN
	// https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx1312d09e60ea91d5&secret=6755df466340ec2fff8e1c7a480db211
	//public final static String TOKEN = "OOTMw8mNPpE2Z1d7PS4STt7RBVvu69vWRRs9Dry9WXn5tP7fbh5MX8nEgZ574cTyFPOHJ0pb9wed-AGYcPPI1y9udrnVT3aLyGn9knQfXlEJiMiN-TD0MoaHlY8trH2kChMzo3Znig6e5o6hHzVrBw";
	
	// 菜单创建
	// {"errcode":0,"errmsg":"ok"}
	// {"errcode":40018,"errmsg":"invalid button name size"}
	public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
	
	// 菜单查询	
	//public final static String MENU_SEARCH_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + TOKEN;
	
	// 菜单删除	
	// {"errcode":0,"errmsg":"ok"}
	public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=";
	
	
	private String createMenuJson() {		
		Map<String, Object> menus = new HashMap<String, Object>();
		List<Map<String, Object>> buttons = new ArrayList<Map<String, Object>>();
		Map<String, Object> button = new HashMap<String, Object>();
		Map<String, Object> sub_button = new HashMap<String, Object>();
		List<Map<String, Object>> list_sub_button = new ArrayList<Map<String, Object>>();
		/** 公司介绍 */
/*		button.put("name", "公司介绍");
		button.put("type", "view");
		button.put("url", "http://www.astrazeneca.com.cn/");
		buttons.add(button);*/		
		/** 心里咨询顾问 */
		button = new HashMap<String, Object>();		
//		list_sub_button = new ArrayList<Map<String, Object>>();
		button.put("name", "IT培训");
		button.put("type", "click");
		button.put("key", "it_training");
		
		sub_button = new HashMap<String, Object>();
		sub_button.put("type", "view");
		sub_button.put("name", "IPAD相关支持");
		sub_button.put("url", "http://114.215.129.174/WeiPlatform_QY/weixin!getTwoLevelMenuList.action?title_code=001");
		list_sub_button.add(sub_button);
		
		sub_button = new HashMap<String, Object>();
		sub_button.put("type", "view");
		sub_button.put("name", "账号和Token");
		sub_button.put("url", "http://114.215.129.174/WeiPlatform_QY/weixin!getTwoLevelMenuList.action?title_code=002");
		list_sub_button.add(sub_button);
		
		sub_button = new HashMap<String, Object>();
		sub_button.put("type", "view");
		sub_button.put("name", "VDI虚拟桌面/AZCC");
		sub_button.put("url", "http://114.215.129.174/WeiPlatform_QY/weixin!getTwoLevelMenuList.action?title_code=003");
		list_sub_button.add(sub_button);
		
		sub_button = new HashMap<String, Object>();
		sub_button.put("type", "view");
		sub_button.put("name", "APP应用软件");
		sub_button.put("url", "http://114.215.129.174/WeiPlatform_QY/weixin!getTwoLevelMenuList.action?title_code=004");
		list_sub_button.add(sub_button);	

		sub_button = new HashMap<String, Object>();
		sub_button.put("type", "view");
		sub_button.put("name", "办公室用户");
		sub_button.put("url", "http://114.215.129.174/WeiPlatform_QY/weixin!getTwoLevelMenuList.action?title_code=005");
		list_sub_button.add(sub_button);
		
		button.put("sub_button", list_sub_button);
		buttons.add(button);
		/** 其他 */
/*		button = new HashMap<String, Object>();		*/
//		list_sub_button = new ArrayList<Map<String, Object>>();
//		button.put("name", "其他");
//		sub_button = new HashMap<String, Object>();
//		sub_button.put("type", "click");
//		sub_button.put("name", "咨询使用说明");
//		sub_button.put("key", "user_guide");
//		list_sub_button.add(sub_button);
//		button.put("sub_button", list_sub_button);
/*		button.put("name", "帮助");
		button.put("type", "view");
		button.put("url", "http://www.astrazeneca.com.cn/");
		buttons.add(button);*/
		menus.put("button", buttons);		
		String jsonMenu = JSONObject.fromObject(menus).toString();
		System.out.println(jsonMenu);
		return jsonMenu;
	}
	
	/**
	 * 创建按钮
	 */
	public void createMenu(String TOKEN) {
		try {
			String jsonMenu = this.createMenuJson();	
			
		    HttpClient client = new HttpClient();
		    Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
		    Protocol.registerProtocol("https", myhttps);
		    PostMethod post = new PostMethod(MENU_CREATE_URL+TOKEN);
		    post.setRequestBody(jsonMenu);
		    post.getParams().setContentCharset("utf-8");
		    //发送http请求
		    String respStr = "";
		    try {
		        client.executeMethod(post);
		        respStr = post.getResponseBodyAsString();
		    } catch (HttpException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }		   
		    System.out.println(respStr);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 信任HTTPS请求
	 * @param url
	 * @return
	 */
	public String deleteMenu(String TOKEN) {
		try {			
		    HttpClient client = new HttpClient();
		    Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
		    Protocol.registerProtocol("https", myhttps);
		    GetMethod get = new GetMethod(MENU_DELETE_URL+TOKEN);
		    get.getParams().setContentCharset("UTF-8");
		    //发送http请求
		    String respStr = "";
		    try {
		        client.executeMethod(get);
		        //返回成功
		        if(get.getStatusCode() == 200){
		        	respStr = get.getResponseBodyAsString();
		        } else {
		        	respStr = "";
		        }
		    } catch (HttpException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }   
		    System.out.println(respStr);
		    return respStr;			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("static-access")
	public static void main(String args[]) {
		WeixinHelper weixinHelper = new WeixinHelper();
//		weixinHelper.checkToken("az_cn");
		//创建菜单
		new MenuTools().createMenu(weixinHelper.ACCESS_TOKEN);
		//删除菜单
		//new MenuTools().deleteMenu(weixinHelper.ACCESS_TOKEN);
	}
}
