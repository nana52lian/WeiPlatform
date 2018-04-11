package com.yidatec.weixin.common;

import java.util.HashMap;
import java.util.Map;

import com.yidatec.weixin.entity.ParamEntity;

/**
 * 参数配置
 * @author Lance
 *
 */
public class ParamsConfig {
	
	private static Map<String, ParamEntity> params = new HashMap<String, ParamEntity>();

	public static Map<String, ParamEntity> getParams() {
		return params;
	}

	public static void setParams(Map<String, ParamEntity> params) {
		ParamsConfig.params = params;
	}
	
}
