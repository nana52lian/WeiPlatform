package com.yidatec.weixin.entity;

import com.yidatec.weixin.common.EnumRes;

public class ResponseEntity {

	// 返回状态码
	private String resCode = null;
	
	// 返回状态码对应的描述信息
	private String resDesc = null;

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResDesc() {
		return resDesc;
	}

	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}
	
	public void setRes(EnumRes res) {
		this.resCode = res.getCode();
		this.resDesc = res.getDescription();
	}	
}
