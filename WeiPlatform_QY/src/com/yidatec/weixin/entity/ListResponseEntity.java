package com.yidatec.weixin.entity;

import java.util.List;

public class ListResponseEntity<T> extends ResponseEntity {

	List<T> list = null;

	public List<T> getList() {
		return list;
	}

	public void setListData(List<T> list) {
		this.list = list;
	}	
	
}
