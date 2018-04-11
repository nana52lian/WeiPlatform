package com.yidatec.weixin.util;

import java.io.IOException;
import org.apache.poi.ss.usermodel.Sheet;
import com.yidatec.weixin.service.SyncHrService;

public class ZipThread extends Thread {
	
	private SyncHrService syncHrService;
	
	private String zipFilePath;
	
	private String targetPath;

	private ZipUtil zipUtil = new  ZipUtil();
	
	@SuppressWarnings("static-access")
	public void run(){
		try {
			//解压
			zipUtil.unzip(zipFilePath, targetPath);
			//POI读取excel文件（2007或者2003）并插入数据库
			PoiReadExcel poiReadExcel = new PoiReadExcel();
			/*Sheet sheet = poiReadExcel.readExcel(targetPath + "work"+".xlsx");
			//插入KData表
			try {
				syncHrService.addKData(sheet);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 解压
	 * @param zipFilePath
	 * @param targetPath
	 */
	public void unziprun(String zipFilePath,String targetPath,SyncHrService syncHrService){
		ZipThread myThread = new ZipThread();
		myThread.setZipFilePath(zipFilePath);
		myThread.setTargetPath(targetPath);
		myThread.setSyncHrService(syncHrService);
		Thread thread = new Thread(myThread);
		thread.start();
	}
	
	public String getZipFilePath() {
		return zipFilePath;
	}

	public void setZipFilePath(String zipFilePath) {
		this.zipFilePath = zipFilePath;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public void setSyncHrService(SyncHrService syncHrService) {
		this.syncHrService = syncHrService;
	}

}
