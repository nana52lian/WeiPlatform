package com.yidatec.weixin.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.yidatec.weixin.dao.StatisticsDao;
import com.yidatec.weixin.util.CommonMethod;

public class StatisticsService {

	private StatisticsDao statisticsDao;

	/**
	 * 日查询
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> searchDailyData(String param)
			throws Exception {
		JSONObject jsonObj = JSONObject.fromObject(param);
		String serviceId = jsonObj.optString("id");
		String date = jsonObj.optString("date");
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fromDt = sdf.parse(date + " 00:00:00");
		Date toDt = sdf.parse(date + " 23:59:59");
		List<Object> pieData = statisticsDao.searchEmployeeTask(fromDt, toDt,serviceId);
		
		List<HashMap<String, Object>> tempData = statisticsDao
				.searchDailyTask(date,serviceId);
		List<Object> barData = fillList(tempData, 8, 21);
		int dailyTotalCount = statisticsDao.dailyTotalCount(fromDt, toDt,serviceId);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pieData", pieData);
		map.put("barData", barData);
		map.put("dailyTotalCount", dailyTotalCount);
		return map;
	}

	private List<Object> fillList(List<HashMap<String, Object>> tempData,
			int begin, int end) {
		List<Object> returnList = new ArrayList<Object>();
		for (int i = begin; i <= end; i++) {
			for (HashMap<String, Object> data : tempData) {
				int hh = (Integer) data.get("categories");
				if (i != hh) {
					returnList.add(0);
					break;
				} else {
					returnList.add((Integer) data.get("taskNum"));
					tempData.remove(0);
					break;
				}

			}
		}

		return returnList;

	}

	/**
	 * 月查询
	 * 
	 * @param param
	 * @return
	 */
	public HashMap<String, Object> searchMonthlyData(String date)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date selectDate = sdf.parse(date + " 00:00:00");
		int month = CommonMethod.getMonth(selectDate);
		List<Object> pieData = statisticsDao.searchMonthlyEmployeeTask(month);
		List<HashMap<String, Object>> tempData = statisticsDao
				.searchMonthlyData(month);
		int end = CommonMethod.getDaysOfMonth(selectDate);
		List<Object> barData = fillList(tempData, 1, end);
		int monthlyTotalCount = statisticsDao.monthlyTotalCount(month);
		List<String> daysList = new ArrayList<String>();
		for (int i = 0; i < end; i++) {
			daysList.add(String.valueOf(i + 1));
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pieData", pieData);
		map.put("barData", barData);
		map.put("categories", daysList);
		map.put("monthlyTotalCount", monthlyTotalCount);
		return map;
	}

	/**
	 * 年查询
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> searchYearlyData(String param)
			throws Exception {
		JSONObject jsonObj = JSONObject.fromObject(param);
		String serviceId = jsonObj.optString("id");
		String fromDt = jsonObj.optString("fromDt") + " 00:00:00";
		String toDt = jsonObj.optString("toDt") + " 23:59:59";
		List<Object> pieData = statisticsDao.searchEmployeeYearlyTask(fromDt,
				toDt,serviceId);
		List<HashMap<String, Object>> tempData = statisticsDao
				.searchYearlyTask(fromDt, toDt,serviceId);
		
		int yearlyTotalCount = statisticsDao.yearlyTotalCount(fromDt, toDt,serviceId);
		List<String> monthsList = new ArrayList<String>();
		List<Integer> barData = new ArrayList<Integer>();
		for (HashMap<String, Object> map:tempData) {
			String dateStr = (String) map.get("categories");
			monthsList.add(dateStr);
			barData.add((Integer)map.get("taskNum"));
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pieData", pieData);
		map.put("barData", barData);
		map.put("yearlyTotalCount", yearlyTotalCount);
		map.put("categories", monthsList);
		return map;
	}

	public void setStatisticsDao(StatisticsDao statisticsDao) {
		this.statisticsDao = statisticsDao;
	}

	/**
	 * 获取坐席的id和姓名
	 * @return
	 */
	public List<HashMap<String, String>> getServers() throws Exception{
		List<HashMap<String, String>> list =  statisticsDao.getServers();
		
		HttpServletRequest request = ServletActionContext.getRequest();  
        Locale locale = (Locale)request.getSession().getAttribute("lang");  
        if(locale != null){  
            ActionContext.getContext().setLocale(locale);  
        } 
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "-1");
		if(locale.toString().equals("en_US")){
			map.put("name", "-please select-");
		} else {
			map.put("name", "-请选择-");
		}
		
		list.add(0,map);
		return list;
	}

	
	/**
	 * 根据座席ID以及日期区间导出
	 * @param server
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> exportTask(String server,String fromDate,String toDate) throws Exception{
		return statisticsDao.exportTask(server,fromDate,toDate);
	}
}
