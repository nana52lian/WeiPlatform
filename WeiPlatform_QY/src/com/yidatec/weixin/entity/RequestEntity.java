package com.yidatec.weixin.entity;

import net.sf.json.JSONObject;

public class RequestEntity {
	
	// 客户端类型
	private String client_type = null;

	// RSA公钥版本
	private String pub_key_ver = null;

	// 3DES加密所用的KEY
	private String three_des_key = null;
	
	// 请求的JSON数据
	private String json_string = null;
	
	public String getClient_type() {
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}
	
	public String getPub_key_ver() {
		return pub_key_ver;
	}

	public void setPub_key_ver(String pub_key_ver) {
		this.pub_key_ver = pub_key_ver;
	}

	public String getThree_des_key() {
		return three_des_key;
	}

	public void setThree_des_key(String three_des_key) {
		this.three_des_key = three_des_key;
	}

	public String getJson_string() {
		return json_string;
	}

	public void setJson_string(String json_string) {
		this.json_string = json_string;
	}	
	
	/**
	 * 获取Json对象
	 * @return
	 */
	public JSONObject getJsonObject() {
		return JSONObject.fromObject(getJson_string());
	}
}
