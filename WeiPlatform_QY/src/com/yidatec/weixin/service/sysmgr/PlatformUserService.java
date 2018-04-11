package com.yidatec.weixin.service.sysmgr;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.yidatec.weixin.common.EnumRes;
import com.yidatec.weixin.dao.sysmgr.PlatformUserDao;
import com.yidatec.weixin.entity.PlatformUserEntity;

public class PlatformUserService {

	private PlatformUserDao platformUserDao = null;
	
	public void setPlatformUserDao(PlatformUserDao platformUserDao) {
		this.platformUserDao = platformUserDao;
	}

	/**
	 * 查询平台账户数量
	 * @param qry_params
	 * @return
	 */
	public int queryPlatformUserCount(String qry_params) throws Exception {
		return platformUserDao.queryPlatformUserCount(qry_params);
	}
	
	/**
	 * 查询平台账户
	 * @param qry_params
	 * @return
	 */
	public List<PlatformUserEntity> queryPlatformUser(String qry_params, int offset, int rows) throws Exception {
		return platformUserDao.queryPlatformUser(qry_params, offset, rows);
	}
	
	/**
	 * 加载平台账户数量
	 * @param qry_params
	 * @return
	 */
	public int loadPlatformUserCount() throws Exception {
		return platformUserDao.loadPlatformUserCount();
	}
	
	/**
	 * 加载所有平台账户
	 * @param qry_params
	 * @return
	 */
	public List<PlatformUserEntity> loadPlatformUsers(int offset, int rows) throws Exception {
		return platformUserDao.loadPlatformUsers(offset, rows);
	}
	
	/**
	 * 加载座席数量
	 * @param qry_params
	 * @return
	 */
	public int loadSeatUserCount() throws Exception {
		return platformUserDao.loadSeatUserCount();
	}
	
	/**
	 * 加载所有座席
	 * @param qry_params
	 * @return
	 */
	public List<PlatformUserEntity> loadSeatUsers(int offset, int rows) throws Exception {
		return platformUserDao.loadSeatUsers(offset, rows);
	}
	
	/**
	 * 加载所有座席
	 * @param qry_params
	 * @return
	 */
	public List<Map<String,String>> loadLeader() throws Exception {
		return platformUserDao.loadLeader();
	}
	
	/**
	 * 处理管理账户的新增、修改、删除
	 * @param req_params
	 * @return
	 * @throws Exception
	 */
	public String platformUserAction(String req_params) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		int action_no = jsonObject.optInt("action_no");
		int res = 0;
		switch (action_no) {
			case 1: //新增
				res = platformUserDao.addPlatformUser(jsonObject);
				break;
			case 2: //修改
				res = platformUserDao.updatePlatformUser(jsonObject);
				break;
			case 3: //删除
				res = platformUserDao.deletePlatformUser(jsonObject);
				break;
			case 4: //启用
				res = platformUserDao.updatePlatformUserForStart(jsonObject);
				break;
			case 5: //停用
				res = platformUserDao.updatePlatformUserForStop(jsonObject);
				break;
		}
		if (res > 0) {
			return EnumRes.SUCCESS.getDescription();
		} else {
			return EnumRes.FAILED.getDescription();
		}
	}

	/**
	 * 修改密码时，验证输入的原始密码是否正确
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PlatformUserEntity validateOldPass(String id) throws Exception {
		return platformUserDao.validateOldPass(id);
	}

	/**
	 * 更改账户密码
	 * @param platformUser
	 * @throws Exception
	 */
	public int changePassword(String newPassword) throws Exception {
		return platformUserDao.changePassword(newPassword);
		
	}

	/**
	 * 重新分配
	 * @param id
	 */
	public int reAllot(String id) {
		return platformUserDao.reAllot(id);
	}
}
