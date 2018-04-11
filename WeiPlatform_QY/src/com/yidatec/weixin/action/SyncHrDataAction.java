package com.yidatec.weixin.action;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.yidatec.weixin.service.SyncHrService;
import com.yidatec.weixin.util.CommonMethod;
import com.yidatec.weixin.util.ZipThread;

public class SyncHrDataAction extends BaseAction {
	
	private static final long serialVersionUID = 7583977936775610018L;
	public final static String UPLOAD_HR_PATH = "/upload_hr/";
	private File hrzip;
	private String hrzipFileName;
	private String hrzipContentType;
	private Logger m_logger = LogManager.getLogger(SyncHrDataAction.class);
	private SyncHrService syncHrService;
	
	public void uploadHrFile() {
		try {
			if (StringUtils.isNotEmpty(hrzipFileName)) {
                //新文件名
				hrzipFileName = new Date().getTime() + CommonMethod.getExtention(hrzipFileName);
				//文件路径
				final String pathName = ServletActionContext.getServletContext().getRealPath(UPLOAD_HR_PATH) + "/" + hrzipFileName;
				File tmpFile = new File(pathName);
				// 上传到服务器
				CommonMethod.copyFile(hrzip, tmpFile);
				// 上传成功
				sendJson("Success");
				//ZIP文件解压后的目标地址
				final String targetPath = ServletActionContext.getServletContext().getRealPath(UPLOAD_HR_PATH) + "/" ;
				ZipThread zipThread = new ZipThread();
				//启动线程解压ZIP文件
				zipThread.unziprun(pathName,targetPath,syncHrService);
			} else {
				sendJson("Fail");
			}
		} catch (Exception ex) {
			m_logger.error(ex.getMessage(),ex);
			sendJson("Fail");
		}
	}

	public File getHrzip() {
		return hrzip;
	}

	public void setHrzip(File hrzip) {
		this.hrzip = hrzip;
	}

	public String getHrzipFileName() {
		return hrzipFileName;
	}

	public void setHrzipFileName(String hrzipFileName) {
		this.hrzipFileName = hrzipFileName;
	}


	public String getHrzipContentType() {
		return hrzipContentType;
	}


	public void setHrzipContentType(String hrzipContentType) {
		this.hrzipContentType = hrzipContentType;
	}

	public static String getUploadHrPath() {
		return UPLOAD_HR_PATH;
	}

	public void setSyncHrService(SyncHrService syncHrService) {
		this.syncHrService = syncHrService;
	}
	
}
