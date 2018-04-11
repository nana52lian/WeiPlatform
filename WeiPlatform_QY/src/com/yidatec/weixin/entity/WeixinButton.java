package com.yidatec.weixin.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信平台的按钮
 * @author Lance
 *
 */
public class WeixinButton {

	private String type = null;
	
	private String name = null;
	
	private String key = null;
	
	private List<WeixinButton> sub_button = new ArrayList<WeixinButton>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<WeixinButton> getSub_button() {
		return sub_button;
	}

	public void setSub_button(List<WeixinButton> sub_button) {
		this.sub_button = sub_button;
	}
	
	
}
