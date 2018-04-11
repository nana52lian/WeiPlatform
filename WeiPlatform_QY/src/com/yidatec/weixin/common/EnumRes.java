package com.yidatec.weixin.common;

public enum EnumRes {
	SUCCESS("200", "Operation Success"),
	SESSION_TIMEOUT("300", "Time Out"),
	EMPTY("400", "No data"),	
	FAILED("500", "Operation Fail"),
	UPDATESUCCESS("600", "Update Success"),
	UPDATEFAILED("700", "Update Fail"),
	;
	
	private String id;

	private String code;
    
    private String description;
    

    private EnumRes(String code, String description) {   
        this.code = code;   
        this.description = description;   
    }
        
    // 普�1�7�方泄1�7   
    public static String getDescription(String code) {   
        for (EnumRes r : EnumRes.values()) {   
            if (r.getCode() == code) {   
                return r.description;   
            }   
        }   
        return null;   
    }
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
    
    public String getCode() {
		return code;
	}
    
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
