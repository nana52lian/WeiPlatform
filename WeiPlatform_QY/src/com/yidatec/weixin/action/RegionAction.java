package com.yidatec.weixin.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.entity.RegionEntity;
import com.yidatec.weixin.service.RegionService;

public class RegionAction extends BaseAction {
	
	private static final Logger log = LogManager.getLogger(RegionAction.class);
	
	private RegionService regionService = null;

	public RegionService getRegionService() {
		return regionService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	private int parentId;

	private int type;
	
	/**
	 * 获取指定的区域信息
	 */
	public void getRegion() {
		try {
			List<RegionEntity> regions = regionService.getRegion(parentId, type);
			if (regions == null) {
				regions = new ArrayList<RegionEntity>();
			}
			RegionEntity re = new RegionEntity();
			re.setRegion_id(0);
			re.setRegion_name("-请选择-");
			if (regions.size() == 0) { 
				regions.add(re);
			} else {
				regions.add(0, re);
			}
			sendJson(JSONArray.fromObject(regions).toString());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
	

}
