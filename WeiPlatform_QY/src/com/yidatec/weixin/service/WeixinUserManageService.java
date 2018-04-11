package com.yidatec.weixin.service;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.common.EnumRes;
import com.yidatec.weixin.dao.WeixinUserManageDao;
import com.yidatec.weixin.entity.WeixinUsersEntity;

public class WeixinUserManageService {
	
	private static final Logger log = LogManager.getLogger(WeixinUserManageService.class);
	
	private WeixinUserManageDao weixinUserManageDao = null;

	public void setWeixinUserManageDao(WeixinUserManageDao weixinUserManageDao) {
		this.weixinUserManageDao = weixinUserManageDao;
	}

	/**
	 * 查询微信用户数
	 * @param qry_params
	 * @return
	 */
	public int queryWeixinUserCount(String qry_params) throws Exception {
		return weixinUserManageDao.queryWeixinUserCount(qry_params);
	}

	/**
	 * 查询微信用户列表
	 * @param qry_params
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List<WeixinUsersEntity> queryWeixinUser(String qry_params, int offset, int rows) throws Exception{
		return weixinUserManageDao.queryWeixinUser(qry_params, offset, rows);
	}

	/**
	 * 绑定K账号
	 * @param req_params
	 * @return
	 * @throws Exception
	 */
	public String bindKeyAccount(String req_params) throws Exception{
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		int res = weixinUserManageDao.bindKeyAccount(jsonObject);
		if (res > 0) {
			return EnumRes.SUCCESS.getDescription();
		} else {
			return EnumRes.FAILED.getDescription();
		}
	}
	
	
}
