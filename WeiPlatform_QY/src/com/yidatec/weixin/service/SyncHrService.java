package com.yidatec.weixin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.yidatec.weixin.dao.SyncHrDao;
import com.yidatec.weixin.util.ExcelUtil;

public class SyncHrService {

	private SyncHrDao syncHrDao;

	public void setSyncHrDao(SyncHrDao syncHrDao) {
		this.syncHrDao = syncHrDao;
	}
	
	public void addKData(Sheet sheet) throws Exception {
		int rowNum = sheet.getLastRowNum();
		Row row;
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PRID", ExcelUtil.getCellStringValue(row.getCell(1)));
			map.put("CHINESE_NAME", ExcelUtil.getCellStringValue(row.getCell(2)));
			map.put("GENDER", ExcelUtil.getCellStringValue(row.getCell(3)));
			map.put("ESM_USER_TYPE", ExcelUtil.getCellStringValue(row.getCell(4)));
			map.put("MOBILE_SMS", ExcelUtil.getCellStringValue(row.getCell(5)));
			map.put("EMAIL", ExcelUtil.getCellStringValue(row.getCell(6)));
			map.put("UPDATE_FLAG", ExcelUtil.getCellStringValue(row.getCell(7)));
			//根据CODE查找表
			List<String> list = syncHrDao.getKData(String.valueOf(map.get("PRID")));
			if(list.size()>=1){
				//更新表
				syncHrDao.updateKData(map);
			} else  {
				if(String.valueOf(map.get("PRID"))!="" && String.valueOf(map.get("CHINESE_NAME"))!=""
						&& String.valueOf(map.get("GENDER"))!="" && String.valueOf(map.get("ESM_USER_TYPE"))!=""
						&& String.valueOf(map.get("MOBILE_SMS"))!="" && String.valueOf(map.get("EMAIL"))!=""
						&& String.valueOf(map.get("UPDATE_FLAG"))!=""){
					//插入表
					syncHrDao.addKData(map);
				}
			}
		}
	}
	
}
