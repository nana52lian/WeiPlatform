package com.yidatec.weixin.entity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.yidatec.weixin.common.DateFormatType;

/**
 * 实体基类
 * @author 
 *
 */
public class BaseEntity implements java.io.Serializable {
	
	/**
	 * 1: 新增
	 * 2: 修改
	 * 3: 删除
	 */
	private int action_no = 0;
	
	private String id = null;

	private String create_user = null;
	
	private String create_date = null;
	
	private String modify_user = null;
	
	private String modify_date = null;
	
    private DateFormat df_full = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private DateFormat df_short = new SimpleDateFormat("yyyy-MM-dd");

	public int getAction_no() {
		return action_no;
	}

	public void setAction_no(int action_no) {
		this.action_no = action_no;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getModify_user() {
		return modify_user;
	}

	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}

	public String getModify_date() {
		return modify_date;
	}

	public void setModify_date(String modify_date) {
		this.modify_date = modify_date;
	}
	
	protected String formatString(String str) {
		try {
			if (str == null || str.isEmpty()) {
				return null;
			}
			return str;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取格式化后的日期
	 * @param type
	 * @param date
	 * @return
	 */
	protected String getFormatDate(int type, Timestamp date) {
		String res = null;
		if (date == null) {
			return res;
		}
		try {
			switch(type) {
			case DateFormatType.FULL:
				res = df_full.format(date);
				break;
			case DateFormatType.SHORT:
				res = df_short.format(date);
				break;
			case DateFormatType.ORIGIN:
			default:
				res = date.toString();
				break;
			}
		} catch (Exception e) {
		}
		return res;
	}
	
}
