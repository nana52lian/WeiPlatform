package com.yidatec.weixin.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局常量定义
 * @author Lance
 *
 */
public class GlobalDef {

	/** 更新RSA公钥 */
	public final static String UPDATE_RSA_PUB_KEY = "9999";
	
    /**
	 * 数据库中日期比较方式的类型定义， 比较的最小单位是天
	 */
	public final static int DATE_COMPARE_TYPE_DAY = 0;
	
	/**
     * 数据库中日期比较方式的类型定义， 比较的最小单位是秒
     */
    public final static int DATE_COMPARE_TYPE_SECOND = 1;
    
    /**
     * 时间点
     */
    public final static List<String> SCHEDULE_TIME = new ArrayList<String>() {
    	{
    		add("5");
    		add("6");
    		add("7");
    		add("8");
    		add("9");
    		add("10");
    		add("11");
    		add("12");
    		add("13");
    		add("14");
    		add("15");
    		add("16");
    		add("17");
    		add("18");
    		add("19");
    		add("20");
    		add("21");    		
    	}
    };
}
