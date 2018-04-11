package com.yidatec.weixin.service;

import java.util.List;

import com.yidatec.weixin.dao.RegionDao;
import com.yidatec.weixin.entity.RegionEntity;

public class RegionService {

	private RegionDao regionDao = null;

	public RegionDao getRegionDao() {
		return regionDao;
	}

	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
	}

	public List<RegionEntity> getRegion(int parentId, int type) throws Exception {		
		return regionDao.getRegion(parentId, type);
	}

}
