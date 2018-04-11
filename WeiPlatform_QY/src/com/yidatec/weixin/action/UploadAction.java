package com.yidatec.weixin.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.yidatec.weixin.entity.message.TaskQueueEntity;
import com.yidatec.weixin.message.WeixinHelper;
import com.yidatec.weixin.service.WeixinService;
import com.yidatec.weixin.util.CommonMethod;
import com.yidatec.weixin.util.PropertiesUtil;

public class UploadAction extends BaseAction {

	private static final long serialVersionUID = 3901880620665306039L;

	public final static String UPLOAD_MEDIA_PATH = "/upload_images/";
	//public final static String WEINXIN_UPLOAD_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload?";
	public final static String WEINXIN_UPLOAD_URL = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?";
	private Logger m_logger = LogManager.getLogger(UploadAction.class);

	// 上传的文件
	private File media;
	// 上传的文件名
	private String mediaFileName;
	// 上传的文件类型
	private String mediaContentType;

	private String type;

	private String taskId;

	private String openId;

	WeixinService weixinService = null;

	public void setWeixinService(WeixinService weixinService) {
		this.weixinService = weixinService;
	}

	/**
	 * 错误类型定义
	 */
	// 成功
	public final static String FILE_SUCCESS = "200";
	// 服务器错误
	public final static String FILE_SERVER_ERROR = "500";
	// 文件类型错误
	public final static String FILE_TYPE_ERROR = "601";
	// 文件名空
	public final static String FILE_NAME_EMPTY = "602";
	// 删除文件错误
	public final static String FILE_DELETE_ERROR = "603";
	// 文件不存在
	public final static String FILE_NOT_EXIST = "604";
	// 超出128K
	public final static String FILE_MAXSIZE = "128";

	public void uploadMediaFile() {
		String imgUrl = "";
		try {
			if (StringUtils.isNotEmpty(mediaFileName)) {

				mediaFileName = new Date().getTime()
						+ CommonMethod.getExtention(mediaFileName);
				
				String pathName = ServletActionContext.getServletContext()
						.getRealPath(UPLOAD_MEDIA_PATH) + "/" + mediaFileName;
				
				System.out.println(pathName);
				
				File tmpFile = new File(pathName);
				
				CommonMethod.copyFile(media, tmpFile);// 上传到服务器

				FileInputStream fis = new FileInputStream(tmpFile) ; 
				if(fis.available() <= 128*1024){
					m_logger.info("Upload file:" + mediaFileName);

					String contextPath = ServletActionContext.getServletContext()
							.getContextPath();
					imgUrl = PropertiesUtil.ppsConfig
							.getProperty("SERVER_IP")
							+ contextPath
							+ UPLOAD_MEDIA_PATH + mediaFileName;
					
					System.out.println(mediaFileName);
					//给武田发openid以及图片
					weixinService.sendImageTextFile(media,mediaFileName,openId);
					
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("open_id", openId);
					jsonObj.put("task_queue_id", taskId);
					jsonObj.put("content", "<img src=\"" + imgUrl
							+ "\" width=\"330px\" />");
					weixinService.sendServiceImageMessage(jsonObj);

					sendJson("{\"res\":\"" + FILE_SUCCESS + "\", \"imgUrl\":\""
							+ imgUrl + "\"}");
				} else  {
					tmpFile.deleteOnExit();
					sendJson("{\"res\":\"" + FILE_MAXSIZE + "\", \"imgUrl\":\""
							+ imgUrl + "\"}");
				}
			} else {
				sendJson("{\"res\":\"" + FILE_NAME_EMPTY
						+ "\", \"mediaFileName\":\"" + mediaFileName + "\"}");
			}

		} catch (Exception ex) {
			m_logger.error(ex.getMessage(),ex);
			sendJson("{\"res\":\"" + FILE_SERVER_ERROR + "\", \"mediaFileName\":\""
					+ mediaFileName + "\"}");

		}
	}

	public void setMedia(File media) {
		this.media = media;
	}

	public void setMediaFileName(String mediaFileName) {
		this.mediaFileName = mediaFileName;
	}

	public void setMediaContentType(String mediaContentType) {
		this.mediaContentType = mediaContentType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

}
