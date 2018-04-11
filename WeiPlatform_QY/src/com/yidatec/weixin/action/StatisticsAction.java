package com.yidatec.weixin.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.yidatec.weixin.service.StatisticsService;

public class StatisticsAction extends BaseAction {

	private static final long serialVersionUID = 3481284878792563599L;
	private static final Logger log = LogManager
			.getLogger(StatisticsAction.class);
	private String param;
	private StatisticsService statisticsService;
	public InputStream excelStream = null;
	public String excelName = "";
	
	private String server;
	private String date;
	private String fromDate;
	private String toDate;

	/**
	 * 日数据
	 */
	public void searchDailyData() {
		try {
			sendJson(JSONObject.fromObject(
					statisticsService.searchDailyData(param)).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 月数据
	 */
	public void searchMonthlyData(){
		try {
			sendJson(JSONObject.fromObject(
					statisticsService.searchMonthlyData(param)).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 年数据
	 */
	public void searchYearlyData(){
		try {
			sendJson(JSONObject.fromObject(
					statisticsService.searchYearlyData(param)).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取坐席id和姓名
	 */
	public void getServers(){
		try {
			sendJson(JSONArray.fromObject(
					statisticsService.getServers()).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 根据座席ID以及日期导出
	 */
	public void exportTaskForDay() throws Exception{
		String fromDt = date + " 00:00:00";
		String toDt = date + " 23:59:59";
		// 数据
				List<Map<String, String>> exportDataList = statisticsService.exportTask(server,fromDt,toDt);
				String exportData = "";
				if (exportDataList != null) {
					exportData = JSONArray.fromObject(exportDataList).toString();
					int str1 = exportData.indexOf("[");
					int str2 = exportData.lastIndexOf("]");
					exportData = exportData.substring(str1, str2 + 1);
				}
				// 列名
				LinkedHashMap<String, String> columeNameMap = new LinkedHashMap<String, String>();
				columeNameMap.put("id", "请求单ID");
				columeNameMap.put("openid", "OPENID");
				columeNameMap.put("key_account","客户K账号");
				columeNameMap.put("custom_name","客户名称");
				columeNameMap.put("custom_cellphone","客户电话");
				columeNameMap.put("custom_email","客户邮箱");
				columeNameMap.put("service_id","工程师ID");
				columeNameMap.put("account","工程师登陆账号");
				columeNameMap.put("name","工程师名称");
				columeNameMap.put("task_status","状态");
				columeNameMap.put("service_score","对工程师的评分");
				columeNameMap.put("custom_asses_desc","对工程师的评价留言");
				columeNameMap.put("custom_score","对用户评分");
				columeNameMap.put("rid","RID");
				columeNameMap.put("type","咨询类型");
				columeNameMap.put("cause","非正常关闭原因");
				columeNameMap.put("create_user","创建者ID");
				columeNameMap.put("create_date","创建时间");
				columeNameMap.put("modify_user","修改者ID");
				columeNameMap.put("modify_date","修改时间");
				columeNameMap.put("first_date","首次回复时间");

				excelStream = this.exportAllData(exportData, columeNameMap);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHMMSS");
				excelName = sdf.format(new Date()) + ".xls";
				
				// 开始下载文件
				ServletOutputStream out = null;
				try {
					out = response.getOutputStream();
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition", "attachment; filename="
							+ excelName);
					byte[] b = new byte[10 * 1024];
					while (true) {
						int read = excelStream.read(b);
						if (read == -1) {
							break;
						}
						out.write(b, 0, read);
						out.flush();
					}
				} catch (Exception e) {
					throw e;
				} finally {
					if (out != null) {
						out.close();
					}
				}
	}
	
	/**
	 * 根据座席ID以及日期区间导出
	 */
	public void exportTaskForMonth() throws Exception{
		String fromDt = fromDate + " 00:00:00";
		String toDt = toDate + " 23:59:59";
		// 数据
				List<Map<String, String>> exportDataList = statisticsService.exportTask(server,fromDt,toDt);
				String exportData = "";
				if (exportDataList != null) {
					exportData = JSONArray.fromObject(exportDataList).toString();
					int str1 = exportData.indexOf("[");
					int str2 = exportData.lastIndexOf("]");
					exportData = exportData.substring(str1, str2 + 1);
				}
				// 列名
				LinkedHashMap<String, String> columeNameMap = new LinkedHashMap<String, String>();
				columeNameMap.put("id", "请求单ID");
				columeNameMap.put("openid", "OPENID");
				columeNameMap.put("key_account","客户K账号");
				columeNameMap.put("custom_name","客户名称");
				columeNameMap.put("custom_cellphone","客户电话");
				columeNameMap.put("custom_email","客户邮箱");
				columeNameMap.put("service_id","工程师ID");
				columeNameMap.put("account","工程师登陆账号");
				columeNameMap.put("name","工程师名称");
				columeNameMap.put("task_status","状态");
				columeNameMap.put("service_score","对工程师的评分");
				columeNameMap.put("custom_asses_desc","对工程师的评价留言");
				columeNameMap.put("custom_score","对用户评分");
				columeNameMap.put("rid","RID");
				columeNameMap.put("type","咨询类型");
				columeNameMap.put("cause","非正常关闭原因");
				columeNameMap.put("create_user","创建者ID");
				columeNameMap.put("create_date","创建时间");
				columeNameMap.put("modify_user","修改者ID");
				columeNameMap.put("modify_date","修改时间");
				columeNameMap.put("first_date","首次回复时间");

				excelStream = this.exportAllData(exportData, columeNameMap);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHMMSS");
				excelName = sdf.format(new Date()) + ".xls";
				
				// 开始下载文件
				ServletOutputStream out = null;
				try {
					out = response.getOutputStream();
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition", "attachment; filename="
							+ excelName);
					byte[] b = new byte[10 * 1024];
					while (true) {
						int read = excelStream.read(b);
						if (read == -1) {
							break;
						}
						out.write(b, 0, read);
						out.flush();
					}
				} catch (Exception e) {
					throw e;
				} finally {
					if (out != null) {
						out.close();
					}
				}
	}
	
	public InputStream exportAllData(String exportData,
			LinkedHashMap<String, String> map) throws Exception {
		HSSFWorkbook wb = this.exportData(exportData, map);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] content = os.toByteArray();
		return new ByteArrayInputStream(content);
	}
	
	
	/**
	 * 导出无自定义列功能的表格
	 * 
	 * @param allJsonStr
	 *            带全部字段的Json串数据
	 * @param columnsMap
	 *            获得所有字段名与显示名map
	 * @return
	 */
	public HSSFWorkbook exportData(String exportData,
			LinkedHashMap<String, String> columnsMap) {
		HSSFWorkbook wb = null;
		try {
			// 生成HSSFWorkbook
			wb = this.build(exportData, columnsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}
	
	
	/**
	 * 生成导出文件
	 * 
	 * @param columnNameMap
	 *            <表字段名,显示名>
	 * @throws Exception
	 */
	public HSSFWorkbook build(String exportData,
			LinkedHashMap<String, String> columnNameMap) throws Exception {

		// 表字段名Set集合
		Set<String> tableColumnSet = columnNameMap.keySet();

		// 表字段名List
		List<String> tableColumnList = new ArrayList<String>(tableColumnSet);

		// 显示内容
		List<String> displayColumnList = new ArrayList<String>(columnNameMap
				.values());

		// 创建新的Excel 工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		// 在Excel工作簿中建一工作表，其名为缺省值
		// HSSFSheet sheet = workbook.createSheet("sheet1");
		HSSFSheet sheet = wb.createSheet("data");

		// 生成表头
		HSSFRow headRow = sheet.createRow(0);

		// 添加表头
		fillHead(headRow, displayColumnList);

		JSONArray ja = JSONArray.fromObject(exportData);
		ja.toArray();
		int rowCount = ja.size();

		int columnCount = 0;
		if(ja.size()>0){
			columnCount =ja.optJSONObject(0).size();
		}
		
		
		// 填充内容
		for (int i = 0; i < rowCount; i++) {

			HSSFRow row = sheet.createRow(i + 1);

			JSONObject joContent = ja.optJSONObject(i);

			fillRowContent(row, tableColumnList, joContent);

		}

		// 自适应列宽
		for (int i = 0; i < columnCount; i++) {
			sheet.autoSizeColumn(i);
		}

		return wb;

	}
	
	
	/**
	 * 填充表头列名
	 * 
	 * @param row
	 * @param valueList
	 */
	private void fillHead(HSSFRow row, List<String> valueList) {

		int count = 0;

		for (Iterator<String> iterator = valueList.iterator(); iterator
				.hasNext(); count++) {

			String key = (String) iterator.next();

			// System.out.println(key+':'+jo.get(key));

			HSSFCell cell = row.createCell(count);

			cell.setCellValue(key);

		}
	}
	/**
	 * 填充xls表中一行内容
	 * 
	 * @param row
	 *            HSSFRow
	 * @param columnNameList
	 *            xls行字段名
	 * @param jsonObj
	 *            json内容
	 */
	private void fillRowContent(HSSFRow row, List<String> columnNameList,
			JSONObject jsonObj) throws Exception {

		int dataColumnNum = 0;

		for (Iterator<String> iterator = columnNameList.iterator(); iterator
				.hasNext(); dataColumnNum++) {

			String key = (String) iterator.next();

			String value = String.valueOf(jsonObj.opt(key));

			HSSFCell cell = row.createCell(dataColumnNum);

			// null的设置为空串
			if ("null".equals(value)) {
				value = "";
			}
			cell.setCellValue(value);

		}
	}
	
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getExcelName() {
		return excelName;
	}

	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
}
