package com.yidatec.weixin.service.sysmgr;

import java.util.HashMap;
import java.util.List;

import org.apache.axis.utils.StringUtils;

import com.yidatec.weixin.common.ParamsConfig;
import com.yidatec.weixin.dao.sysmgr.ResourceDao;
import com.yidatec.weixin.entity.ParamEntity;
import com.yidatec.weixin.entity.ResourcesEntity;

public class ResourceService {

	private ResourceDao resourceDao = null;

	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}

	public int addResource(ResourcesEntity resourcesEntity) throws Exception {
		return resourceDao.addResource(resourcesEntity);
	}

	public List<ResourcesEntity> loadResources(boolean isLoadParent)
			throws Exception {
		return resourceDao.loadResources(isLoadParent);
	}

	public List<ResourcesEntity> loadResources(boolean isLoadParent,
			int offset, int rows) throws Exception {
		return resourceDao.loadResources(isLoadParent, offset, rows);
	}

	public int[] delResources(String[] ids) throws Exception {
		return resourceDao.delResources(ids);
	}

	public int getResourcesCount() throws Exception {
		return resourceDao.getResourcesCount();
	}

	/**
	 * 加载参数信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ParamEntity> loadParams() throws Exception {
		return resourceDao.loadParams();
	}

	/**
	 * 修改参数信息
	 * 
	 * @param id
	 * @param param_name
	 * @param param_value
	 * @throws Exception
	 */
	public void updateParams(String id, String param_name, String param_value)
			throws Exception {
		int result = resourceDao.updateParams(id, param_value);
		if (result > 0) {
			ParamsConfig.getParams().get(param_name)
					.setParam_value(param_value);
		}
	}

	/**
	 * 加载请求类型列表
	 * 
	 * @param offset
	 * @param rows
	 * @return
	 */
	public List<HashMap<String, String>> loadRequestType(int offset, int rows)
			throws Exception {
		return resourceDao.loadRequestType(offset, rows);
	}

	/**
	 * 请求类型总数
	 * 
	 * @return
	 */
	public int getRequestTypeCount() throws Exception {
		return resourceDao.getRequestTypeCount();
	}

	/**
	 * 创建或者删除请求类型
	 * 
	 * @param requestTypeId
	 * @param requestTypeDescription
	 */
	public void addOrUpdateRequestType(String requestTypeId,
			String requestTypeDescription) throws Exception {
		if (StringUtils.isEmpty(requestTypeId))
			resourceDao.createRequestType(requestTypeDescription);
		else
			resourceDao
					.updateRequestType(requestTypeId, requestTypeDescription);
	}

	/**
	 * 删除请求类型
	 * 
	 * @param requestTypeId
	 */
	public void delRequestType(String requestTypeId) throws Exception {
		resourceDao.delRequestType(requestTypeId);
	}

	/**
	 * 添加子请求类型
	 * 
	 * @param requestTypeId
	 * @param requestTypeDescription
	 */
	public void saveSubRequestType(String requestTypeId,
			String requestTypeDescription) throws Exception {
		resourceDao.saveSubRequestType(requestTypeId, requestTypeDescription);
	}

}
