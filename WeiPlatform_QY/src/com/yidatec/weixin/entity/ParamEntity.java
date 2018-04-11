package com.yidatec.weixin.entity;

public class ParamEntity extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	private String id = null;
	
	private String type;
	
	private String param_name = null;	

	private String param_description = null;

	private String param_value = null;	
	
	private String param_value_hidden = null;
	
	public ParamEntity() {
    	
    }
    
    public ParamEntity(String id, 
    		String type,
    				  String param_name,
    				  String param_description,
    				  String param_value,
    				  String param_value_hidden,
    				  String create_user,
    				  String create_date,
    				  String modify_user,
    				  String modify_date
    				  ) {
    	this.setId(id);
    	this.type = type;
    	this.param_name = param_name;
    	this.param_description = param_description;
    	this.param_value = param_value;
    	this.param_value_hidden = param_value_hidden;
    	this.setCreate_user(create_user);
    	this.setCreate_date(create_date);
    	this.setModify_user(modify_user);
    	this.setModify_date(modify_date);
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParam_name() {
		return param_name;
	}

	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}

	public String getParam_description() {
		return param_description;
	}

	public void setParam_description(String param_description) {
		this.param_description = param_description;
	}

	public String getParam_value() {
		return param_value;
	}

	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}
	
	public String getParam_value_hidden() {
		return param_value_hidden;
	}

	public void setParam_value_hidden(String param_value_hidden) {
		this.param_value_hidden = param_value_hidden;
	}


}
