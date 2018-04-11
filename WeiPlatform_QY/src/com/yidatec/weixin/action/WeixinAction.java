package com.yidatec.weixin.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.session.SessionRegistry;

import com.thoughtworks.xstream.XStream;
import com.yidatec.weixin.common.ParamsConfig;
import com.yidatec.weixin.entity.AdviserEntity;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.ResponseEntity;
import com.yidatec.weixin.entity.message.ArticleCatagoryEntity;
import com.yidatec.weixin.entity.message.ArticleCatagoryListEntity;
import com.yidatec.weixin.entity.message.ArticleEntity;
import com.yidatec.weixin.entity.message.Articles;
import com.yidatec.weixin.entity.message.InMessage;
import com.yidatec.weixin.entity.message.MessageType;
import com.yidatec.weixin.entity.message.OutMessage;
import com.yidatec.weixin.entity.message.ServiceMessage;
import com.yidatec.weixin.entity.message.TaskDisplayEntity;
import com.yidatec.weixin.entity.message.TaskQueueEntity;
import com.yidatec.weixin.entity.message.UserDetail;
import com.yidatec.weixin.entity.message.UserType;
import com.yidatec.weixin.exception.CustomException;
import com.yidatec.weixin.helper.MessageHelper;
import com.yidatec.weixin.message.MessageDispatch;
import com.yidatec.weixin.message.SendMessage2Takeda;
import com.yidatec.weixin.message.WeixinHelper;
import com.yidatec.weixin.service.WeixinService;
import com.yidatec.weixin.service.seatmgr.AdviserManageService;
import com.yidatec.weixin.util.PropertiesUtil;
import com.yidatec.weixin.util.Tools;
import com.yidatec.weixin.util.XStreamFactory;

public class WeixinAction extends BaseAction {

	private static final long serialVersionUID = 317587766286576626L;

	private static final Logger log = LogManager.getLogger(WeixinAction.class);

	public final static String UPLOAD_IMAGES_PATH = "upload_images/";
	private String respcontent;

	// 验签用的字段
	private String signature;
	private String timestamp;
	private String nonce;
	private String echostr;

	private String msg_signature;

	//
	private String token = "1234567890";

	private AdviserEntity adviserEntity = null;
	private String fromUserName;
	private String freeTimeRadio;
	private String qqNo;
	private String phone;
	private String facePhone;
	private String account;
	private Integer adviseType;
	private String result;
	private String advisoryName;
	private String advisoryPhone;
	private String orderStatus;
	private String task_queue_id;
	private Integer service_score;
	private Integer custom_score;
	private String mediaId;
	private String open_id;
	private String asses;

	private String id;
	private String leaderIds;
	private String transferToId;
	private String taskId;
	private String kid;
	private String title_code;
	private String CatagoryID;
	private String menuname;
	private String CatagoryName;
	private int readNumber;
	// 中英文号标识
	private String cid;
	private String title;

	public String getRespcontent() {
		return respcontent;
	}

	public void setRespcontent(String respcontent) {
		this.respcontent = respcontent;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTransferToId() {
		return transferToId;
	}

	public void setTransferToId(String transferToId) {
		this.transferToId = transferToId;
	}

	public String getLeaderIds() {
		return leaderIds;
	}

	public void setLeaderIds(String leaderIds) {
		this.leaderIds = leaderIds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private List<ArticleCatagoryEntity> articleCatagoryList;
	private ArticleEntity articleEntity;
	private List<ArticleEntity> articleEntityList;
	private List<ArticleCatagoryListEntity> articleCatagoryListEntity;

	public ArticleEntity getArticleEntity() {
		return articleEntity;
	}

	public void setArticleEntity(ArticleEntity articleEntity) {
		this.articleEntity = articleEntity;
	}

	public List<ArticleCatagoryListEntity> getArticleCatagoryListEntity() {
		return articleCatagoryListEntity;
	}

	public void setArticleCatagoryListEntity(List<ArticleCatagoryListEntity> articleCatagoryListEntity) {
		this.articleCatagoryListEntity = articleCatagoryListEntity;
	}

	public List<ArticleEntity> getArticleEntityList() {
		return articleEntityList;
	}

	public void setArticleEntityList(List<ArticleEntity> articleEntityList) {
		this.articleEntityList = articleEntityList;
	}

	private String ArticleID;
	// 任务ID
	private String taskID = null;
	// 聊天信息
	private List<Map<String, Object>> list_message = new ArrayList<Map<String, Object>>();
	// 当前登录人的类型
	private int userType;

	// 判断是否需要显示history_window.jsp中的“结束咨询并发送评价邀请”按钮
	private int flag;

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public void setAdviseType(Integer adviseType) {
		this.adviseType = adviseType;
	}

	// 各种请求参数
	private String req_params = null;
	// 判断查询请求单的类型
	private String request_type = null;
	// 处理微信消息
	WeixinService weixinService = null;
	AdviserManageService adviserManageService = null;

	public void setWeixinService(WeixinService weixinService) {
		this.weixinService = weixinService;
	}

	public void setAdviserManageService(AdviserManageService adviserManageService) {
		this.adviserManageService = adviserManageService;
	}

	public List<ArticleCatagoryEntity> getArticleCatagoryList() {
		return articleCatagoryList;
	}

	public void setArticleCatagoryList(List<ArticleCatagoryEntity> articleCatagoryList) {
		this.articleCatagoryList = articleCatagoryList;
	}

	public String getArticleID() {
		return ArticleID;
	}

	public void setArticleID(String articleID) {
		ArticleID = articleID;
	}

	public void setAdviserEntity(AdviserEntity adviserEntity) {
		this.adviserEntity = adviserEntity;
	}

	public void test() {
		try {
			// getNumberOfUsers();
			// String url = weixinService.getMoodChartUrl("abc", request, 0);
			// System.out.println(url);

			/*
			 * InMessage inMsg = new InMessage();
			 * inMsg.setFromUserName("oNkX-ji99zP_SGJsNCdJln70elgE");
			 * inMsg.setEvent("unsubscribe"); OutMessage outMsg = new OutMessage();
			 * weixinService.processEventMessage(inMsg, request);
			 * System.out.println(outMsg.getContent());
			 */
			// System.out.println(weixinService.getAssessChartUrl("", request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试发送消息
	 */
	public void sendMessage() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			OutMessage outMsg = new OutMessage();
			outMsg.setFromUserName(jsonObj.optString("from_user"));
			outMsg.setToUserName(jsonObj.optString("to_user"));
			outMsg.setContent(jsonObj.optString("content"));
			outMsg.setCreateTime(new Date().getTime());
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			XStream xs = XStreamFactory.init(false);
			xs.alias("xml", OutMessage.class);
			xs.alias("item", Articles.class);
			xs.toXML(outMsg, response.getWriter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String execute() throws IOException {
		/**
		 * 验签，第一次微信平台验证服务器是否有效使用 验签走的是GET方法，以后所有请求都是POST
		 */
		if (request.getMethod().equalsIgnoreCase("GET")) {
			checkSignature();
			return null;
		}
		
		ServiceMessage servicemessage = new ServiceMessage();
		// 解析微信平台发来的消息
		InMessage inMsg = getInMessage();
		OutMessage outMsg = new OutMessage();
		try {
			outMsg.setAgentID(WeixinHelper.AgentID);
			if (inMsg.getMsgType().equals(MessageType.TEXT)) {
				// 我要咨询
				String checkResult = checkCurrentTimeIfValid();
				log.info("checkResult:"+checkResult);
				if (checkResult.equals("true")) {
					outMsg = weixinService.addMessage(inMsg, outMsg);
					weixinService.processTextMessage(inMsg, request);
				} else {
					outMsg.setContent(MessageHelper.getInstance().getMessage("NO_SERVICE_TIME_MESSAGE",
							new String[] { checkResult }));
				}
			} else if (inMsg.getMsgType().equals(MessageType.IMAGE)) {
				// 我要咨询
				String checkResult = checkCurrentTimeIfValid();
				if (checkResult.equals("true")) {
					outMsg = weixinService.addMessage(inMsg, outMsg);
					weixinService.processImageMessage(inMsg, request);
				} else {
					outMsg.setContent(MessageHelper.getInstance().getMessage("NO_SERVICE_TIME_MESSAGE",
							new String[] { checkResult }));
				}
			} else if (inMsg.getMsgType().equals(MessageType.VOICE)) {
				// 我要咨询
				String checkResult = checkCurrentTimeIfValid();
				if (checkResult.equals("true")) {
					outMsg = weixinService.addMessage(inMsg, outMsg);
					weixinService.processVoiceMessage(inMsg, request);
				} else {
					outMsg.setContent(MessageHelper.getInstance().getMessage("NO_SERVICE_TIME_MESSAGE",
							new String[] { checkResult }));
				}
			} else if (inMsg.getMsgType().equals(MessageType.LOCATION)) {
			} else if (inMsg.getMsgType().equals(MessageType.LINK)) {
			} else if (inMsg.getMsgType().equals(MessageType.EVENT)) {
				outMsg = weixinService.processEventMessage(inMsg, request);
			} else {
				log.error("错误的用户消息!");
			}
			
		} catch (Exception e) {
			servicemessage.setText("连接超时，请重试");
			log.error(e.getMessage(), e);
		}
		
		log.info("微信平台发来的消息:"+inMsg.getContent());
		log.info("回复的消息:"+outMsg.getContent());
		if (outMsg.getContent()!=null&&!outMsg.getContent().equals("")) {
			servicemessage.setText(outMsg.getContent());
			servicemessage.setAgentid(PropertiesUtil.ppsConfig.getProperty("TAKEDA_AGENTID"));
			servicemessage.setTouser(inMsg.getFromUserName());
			servicemessage.setMsgtype("text");
			log.info("回复："+servicemessage.getTouser()+"内容："+servicemessage.getText());
			SendMessage2Takeda.testPost(PropertiesUtil.ppsConfig.getProperty("TAKEDA_URL"), servicemessage);
		}
		return null;
	}

	private String checkCurrentTimeIfValid() {
		try {
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String tempWorkingTime = ParamsConfig.getParams().get("temp_working_time").getParam_value();
			if (StringUtils.isNotEmpty(tempWorkingTime)) {
				String[] dateArray = tempWorkingTime.split(" ");
				String dateBegin = dateArray[0].substring(0, dateArray[0].indexOf("/")) + " 00:00:00";
				String dateEnd = dateArray[0].substring(dateArray[0].indexOf("/") + 1) + " 23:59:59";

				if (now.after(format.parse(dateBegin)) && now.before(format.parse(dateEnd))) {
					String nowDateStr = format.format(now).split(" ")[0];
					String dateBegin1 = nowDateStr + " " + dateArray[1].substring(0, dateArray[1].indexOf("/")) + ":00";
					String dateEnd1 = nowDateStr + " " + dateArray[1].substring(dateArray[1].indexOf("/") + 1) + ":00";
					if (now.after(format.parse(dateBegin1)) && now.before(format.parse(dateEnd1))) {
						return "true";
					} else
						return dateArray[1].replace("/", "-");
				} else
					return dateArray[1].replace("/", "-");
			} else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(now);
				String nowDate = format.format(now).substring(0, 10);
				int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
				if (dayOfWeek == 1 || dayOfWeek == 7) {
					return "周一至周五9:00-18:00";
//					String restDayWorkingTime = ParamsConfig.getParams().get("restday_time").getParam_value();
//					String dateBegin = nowDate + " " + restDayWorkingTime.substring(0, restDayWorkingTime.indexOf("/"))
//							+ ":00";
//					String dateEnd = nowDate + " " + restDayWorkingTime.substring(restDayWorkingTime.indexOf("/") + 1)
//							+ ":00";
//					if (now.after(format.parse(dateBegin)) && now.before(format.parse(dateEnd)))
//						return "true";
//					else
//						return restDayWorkingTime.replace("/", "-");
				} else {
					String workDayWorkingTime = ParamsConfig.getParams().get("workday_time").getParam_value();
					String dateBegin = nowDate + " " + workDayWorkingTime.substring(0, workDayWorkingTime.indexOf("/"))
							+ ":00";
					String dateEnd = nowDate + " " + workDayWorkingTime.substring(workDayWorkingTime.indexOf("/") + 1)
							+ ":00";
					if (now.after(format.parse(dateBegin)) && now.before(format.parse(dateEnd)))
						return "true";
					else
						return workDayWorkingTime.replace("/", "-");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "";

	}

	/**
	 * 取得当前登录人的类型
	 */
	public void getLoginType() {
		try {
			// 当前登录人的类型
			userType = getCurrentUser().getType();
			int busyStatus = weixinService.getPlatfromBusyStatus(getCurrentUser().getId());
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("type", userType);
			jsonMap.put("busyStatus", busyStatus);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 取得某客服的未完成任务列表（包含激活状态的）
	 */
	public void getUnCompleteAdvisory() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			List<TaskDisplayEntity> taskDisplayList = weixinService.getUnCompleteAdvisory();
			jsonMap.put("total", taskDisplayList.size());
			jsonMap.put("rows", taskDisplayList);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 取得某客服的完成任务列表（包含无法处理的）
	 */
	public void getCompleteAdvisory() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			int count = weixinService.getCompleteAdvisoryCount();
			List<TaskDisplayEntity> taskDisplayList = weixinService.getCompleteAdvisory(getOffset(), getRows());
			jsonMap.put("total", count);
			jsonMap.put("rows", taskDisplayList);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 根据openid获得以往请求列表
	 */
	public void getRequestByOpenId() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			List<TaskDisplayEntity> taskDisplayList = weixinService.getRequestByOpenId(req_params);
			jsonMap.put("total", taskDisplayList.size());
			jsonMap.put("rows", taskDisplayList);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 管理员取得系统所有请求单
	 */
	public void getAllRequest() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			List<TaskDisplayEntity> taskDisplayList = weixinService.getAllRequest(req_params, request_type, getOffset(),
					getRows());
			jsonMap.put("total", weixinService.getAllRequestCount(req_params, request_type));
			jsonMap.put("rows", taskDisplayList);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 取得某客服的挂起任务列表
	 */
	public void getWaitAdvisory() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			List<TaskDisplayEntity> taskDisplayList = weixinService.getWaitAdvisory(getCurrentUser().getType());
			jsonMap.put("total", taskDisplayList.size());
			jsonMap.put("rows", taskDisplayList);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 加载聊天数据并打开聊天窗口<br/>
	 * 
	 * @return
	 */
	public String openChatWindow() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("taskID", taskID);
			// 如果是第一次打开聊天窗口要更新消息已读状态
			jsonObj.put("first", "1");
			list_message = weixinService.loadMessage(jsonObj);
			// 如果客户没有说话
			Map<String, Object> message = new HashMap<String, Object>();
			TaskQueueEntity taskQueue = new TaskQueueEntity();
			if (list_message.size() == 0) {
				message.put("task_queue_id", taskID);
				// 根据ID取得任务表数据
				taskQueue = weixinService.getTaskQueueByID(taskID);
				message.put("open_id", taskQueue.getOpen_id());
				list_message.add(message);
			}
			// 将数据翻转一下，这样从聊天框看最下面就是最近发的消息
			Collections.reverse(list_message);

			/*
			 * HttpServletRequest request = ServletActionContext.getRequest(); Locale locale
			 * = (Locale)request.getSession().getAttribute("lang"); if(locale != null){
			 * ActionContext.getContext().setLocale(locale); }
			 */

			return "chat_window";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return ERROR;
	}

	/**
	 * 加载聊天数据并打开聊天历史记录窗口
	 * 
	 * @return
	 */
	public String openHistoryWindow() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("taskID", taskID);
			jsonObj.put("openID", open_id);
			list_message = weixinService.getHistoryMessage(jsonObj);
			// 如果这个微信用户被分配后没有发言，咨询师也没有发言直接被挂起，对于挂起后关闭按钮的处理
			Map<String, Object> map = new HashMap<String, Object>();
			if (list_message.size() == 0) {
				map.put("task_queue_id", taskID);
				map.put("open_id", open_id);
				map.put("content", "没有聊天记录");
				list_message.add(map);
			}
			if (flag == 1) {
				// 完成咨询页发起的请求
				flag = 1;
			} else if (flag == 2) {
				// 关注人详细信息页中发起的请求
				flag = 2;
			} else {
				// 请求单管理与挂起发起的请求
				flag = 0;
			}
			// 将数据翻转一下，这样从聊天框看最下面就是最近发的消息
			Collections.reverse(list_message);

			// HttpServletRequest request = ServletActionContext.getRequest();
			// Locale locale = (Locale)request.getSession().getAttribute("lang");
			// if(locale != null){
			// ActionContext.getContext().setLocale(locale);
			// }

			return "history_window";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return ERROR;
	}

	/**
	 * 加载聊天数据并打开聊天历史记录窗口 QQ追加
	 *
	 * @return
	 */
	public String openHistoryWindowAll() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("taskID", taskID);
			jsonObj.put("openID", open_id);
			list_message = weixinService.getHistoryMessageall(jsonObj);
			// 如果这个微信用户被分配后没有发言，咨询师也没有发言直接被挂起，对于挂起后关闭按钮的处理
			Map<String, Object> map = new HashMap<String, Object>();
			if (list_message.size() == 0) {
				map.put("task_queue_id", taskID);
				map.put("open_id", open_id);
				map.put("content", "没有聊天记录");
				list_message.add(map);
			}
			if (flag == 1) {
				// 完成咨询页发起的请求
				flag = 1;
			} else if (flag == 2) {
				// 关注人详细信息页中发起的请求
				flag = 2;
			} else {
				// 请求单管理与挂起发起的请求
				flag = 0;
			}
			// 将数据翻转一下，这样从聊天框看最下面就是最近发的消息
			Collections.reverse(list_message);

			// HttpServletRequest request = ServletActionContext.getRequest();
			// Locale locale = (Locale)request.getSession().getAttribute("lang");
			// if(locale != null){
			// ActionContext.getContext().setLocale(locale);
			// }

			return "history_window";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return ERROR;
	}

	/**
	 * 向微信发一条聊天信息 发送XML给AI系统
	 */
	public void sendServiceMessage() {
		try {
			int resultCode = 0;
			JSONObject jsonObj = JSONObject.fromObject(URLDecoder.decode(req_params, "UTF-8"));
			resultCode = weixinService.sendServiceMessage(jsonObj, "text");
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("resultCode", resultCode);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}

	/**
	 * 取得未读消息
	 */
	public void getUnReadMessage() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			list_message = weixinService.getUnReadMessage(jsonObj);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			Collections.reverse(list_message);
			jsonMap.put("resultWeixinList", list_message);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}

	/**
	 * 取得微信用户咨询次数
	 */
	public void getUserAdvisoryCount() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			List<UserDetail> userDetailList = new ArrayList<UserDetail>();
			userDetailList = weixinService.getWeixinChatCount(jsonObj);
			int size = 0;
			if (userDetailList.size() > 0) {
				size = userDetailList.size();
			}
			jsonMap.put("count", size);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 取得微信用户咨询的信息
	 */
	public void getUserAdvisoryInfo() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			List<UserDetail> userDetailList = new ArrayList<UserDetail>();
			userDetailList = weixinService.getWeixinChatInfoByOpenID(jsonObj);
			// ridPrefix
			jsonMap.put("ridPrefix", ParamsConfig.getParams().get("rid_prefix").getParam_value());
			if (userDetailList.size() > 0) {
				jsonMap.put("size", 1);
				jsonMap.put("key_account", userDetailList.get(0).getKey_account());
				/*
				 * //根据kid查找KDATA Map<String,Object> map =
				 * weixinService.sreachKData(userDetailList.get(0).getKey_account());
				 * if(map!=null){ jsonMap.put("name", map.get("name")); jsonMap.put("sex",
				 * map.get("sex")); jsonMap.put("cellphone", map.get("cellphone"));
				 * jsonMap.put("email", map.get("email")); jsonMap.put("usertype",
				 * map.get("usertype")); jsonMap.put("activestatus", map.get("activestatus")); }
				 */
				jsonMap.put("name", userDetailList.get(0).getName());
				jsonMap.put("sex", userDetailList.get(0).getSex());
				jsonMap.put("cellphone", userDetailList.get(0).getCellphone());
				jsonMap.put("email", userDetailList.get(0).getEmail());
				jsonMap.put("usertype", userDetailList.get(0).getGrade());
				// 查找RID
				TaskQueueEntity taskQueueEntity = new TaskQueueEntity();
				taskQueueEntity = weixinService.getTaskQueueByID(jsonObj.optString("task_queue_id"));
				int len = ParamsConfig.getParams().get("rid_prefix").getParam_value().length();
				if (StringUtils.isNotEmpty(taskQueueEntity.getRid())) {
					jsonMap.put("rid", taskQueueEntity.getRid().substring(len, taskQueueEntity.getRid().length()));
				} else {
					jsonMap.put("rid", "");
				}
				jsonMap.put("type", taskQueueEntity.getType());
				jsonMap.put("customScore", taskQueueEntity.getCustom_score());
			} else {
				// 如果该次咨询没有保存，取得该微信用户上次咨询的年龄和性别
				userDetailList = weixinService.getWeixinChatInfoByOpenID(jsonObj);
				if (userDetailList.size() > 0) {
					jsonMap.put("size", 2);
					/*
					 * jsonMap.put("sex", userDetailList.get(0).getSex()); jsonMap.put("age",
					 * userDetailList.get(0).getAge());
					 */
				} else {
					jsonMap.put("size", 0);//
				}
			}
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void getUserInfoForUserID() {
		// try{
		// JSONObject jsonObj = JSONObject.fromObject(req_params);
		// WeixinHelper helper = new WeixinHelper();
		// String str = helper.getUserInfoForAZ(jsonObj.getString("user_id"));
		// JSONObject json_Obj_1 = JSONObject.fromObject(str);
		// Map<String, Object> jsonMap = new HashMap<String, Object>();
		// JSONObject json_Obj = json_Obj_1.getJSONObject("data");
		// jsonMap.put("kid", json_Obj.getString("userid"));
		// jsonMap.put("name", json_Obj.getString("name"));
		// jsonMap.put("mobile", json_Obj.getString("mobile"));
		// jsonMap.put("gender", json_Obj.getString("gender"));
		// jsonMap.put("email", json_Obj.getString("email"));
		// sendJson(JSONObject.fromObject(jsonMap).toString());
		// } catch (Exception e) {
		// log.error(e.getMessage(), e);
		// }
	}

	/**
	 * 保存用户信息
	 */
	public void saveUserInfo() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("result", weixinService.saveUserInfo(jsonObj));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 设置某一任务状态
	 */
	public void setStatus() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("result", weixinService.setStatus(jsonObj));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 给兼职客服分配任务
	 */
	public void getTaskForAmateur() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			// 取得未分配数
			int unprocessCount = weixinService.getUnProcessCount();
			if (unprocessCount > 0) {
				// 取得登陆坐席被分配的未完成非挂起任务数
				int count = weixinService.getTaskQueueCountByServiceID();
				// 判断是否超过兼职最大队列数
				if (count < Integer
						.valueOf(ParamsConfig.getParams().get("part-time_queue_max_count").getParam_value())) {
					// 分配任务并获取列表
					List<TaskDisplayEntity> taskDisplayList = weixinService.getTaskForAmateur();
					jsonMap.put("total", taskDisplayList.size());
					jsonMap.put("rows", taskDisplayList);
					jsonMap.put("maxcount", "1");
				} else {
					jsonMap.put("maxcount", "0");
				}
			} else {
				// 没有等待的请求单
				jsonMap.put("maxcount", "2");
			}
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 取得评价的URL
	 */
	public void getAppraisalUrl() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			jsonObj.put("service_id", getCurrentUser().getId());
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			int tmp = 0;
			boolean timeoutflag = false;
			// 取得最大失效小时数
			tmp = Integer.valueOf(ParamsConfig.getParams().get("max_hour").getParam_value());
			// 如果设置错误了，
			if (tmp >= 24) {
				tmp = 24;
			}
			// 取得最后聊天或者创建咨询时间跟系统时间差距小时数
			int syshour = 0;// 初始为0
			// 查询最后聊天记录或者创建时间
			syshour = weixinService.getInterval(jsonObj);
			if (syshour < tmp) {
				// 未超时，向微信用户发送评价消息
				jsonMap.put("result", weixinService.getAppraisalUrl(jsonObj));
			} else {
				// 超时了，不向微信用户发送评价消息
				timeoutflag = true;
				jsonMap.put("result", timeoutflag);
			}
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 设置非正常关闭原因
	 */
	public void SetCause() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("result", weixinService.SetCause(jsonObj));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 请求单升级后给用户发送通知消息
	 */
	public void sendMsgForStop() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			jsonObj.put("service_id", getCurrentUser().getId());
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			int tmp = 0;
			boolean timeoutflag = false;
			// 取得最大失效小时数
			tmp = Integer.valueOf(ParamsConfig.getParams().get("max_hour").getParam_value());
			// 如果设置错误了，
			if (tmp >= 24) {
				tmp = 24;
			}
			// 取得最后聊天或者创建咨询时间跟系统时间差距小时数
			int syshour = 0;// 初始为0
			// 查询最后聊天记录或者创建时间
			syshour = weixinService.getInterval(jsonObj);
			if (syshour < tmp) {
				// 未超时，向微信用户发送评价消息
				jsonMap.put("result", weixinService.sendMsgForStop(jsonObj));
			} else {
				// 超时了，不向微信用户发送评价消息
				timeoutflag = true;
				jsonMap.put("result", timeoutflag);
			}
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 监控图表
	 */
	public void getTodayProcessingCount() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			// 取得当日用户请求数量
			jsonMap.put("requestCount", weixinService.getRequestCount());
			// 取得当日活跃用户数量
			jsonMap.put("userActiveCount", weixinService.getUserActiveCount());
			// 取得当日的未处理任务数量
			jsonMap.put("unProcessCount", weixinService.getTodayCountByStatus());

			jsonMap.put("mainQueenTooLongTime", ParamsConfig.getParams().get("main_queen_too_long").getParam_value());

			jsonMap.put("requestTypeList", weixinService.searchRequestTypes());
			// 取得当日的每个时点的任务数量
			jsonMap.put("taskCount1", weixinService.getTaskCount1());
			jsonMap.put("taskCount2", weixinService.getTaskCount2());
			jsonMap.put("taskCount3", weixinService.getTaskCount3());
			jsonMap.put("taskCount4", weixinService.getTaskCount4());
			jsonMap.put("taskCount5", weixinService.getTaskCount5());
			jsonMap.put("taskCount6", weixinService.getTaskCount6());
			jsonMap.put("taskCount7", weixinService.getTaskCount7());
			jsonMap.put("taskCount8", weixinService.getTaskCount8());
			jsonMap.put("taskCount9", weixinService.getTaskCount9());
			jsonMap.put("taskCount10", weixinService.getTaskCount10());
			Map<String, Object> map = new HashMap<String, Object>();
			map = weixinService.getServiceStatus();
			jsonMap.put("nameList", map.get("nameList"));
			jsonMap.put("waitList", map.get("waitList"));
			jsonMap.put("processList", map.get("processList"));

			jsonMap.put("requestInfoList", weixinService.getRequestInfoList());

			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void getCurrentStatus() {
		weixinService.getCurrentStatus();
	}

	/**
	 * 个人月数据统计
	 */
	public void getPersonalMonthDataStatistics() {
		try {
			String service_id = getCurrentUser().getId();
			String date = req_params;
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			// 个人月数据
			jsonMap.put("data", weixinService.getPersonalMonthDataStatistics(service_id, date));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 根据ID或者Kcode查找任务列表
	 */
	public void searchTaskByIDAndKcode() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			int count = weixinService.getCompleteAdvisoryByTaskIDAndKcodeCount(jsonObj);
			List<TaskDisplayEntity> taskDisplayList = weixinService.getCompleteAdvisoryByTaskIDAndKcode(jsonObj,
					getOffset(), getRows());
			jsonMap.put("total", count);
			jsonMap.put("rows", taskDisplayList);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 根据日期查找任务列表
	 */
	public void searchTaskByDate() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			int count = weixinService.getCompleteAdvisoryByTaskDateCount(jsonObj);
			List<TaskDisplayEntity> taskDisplayList = weixinService.getCompleteAdvisoryByTaskDate(jsonObj, getOffset(),
					getRows());
			jsonMap.put("total", count);
			jsonMap.put("rows", taskDisplayList);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 设置平台账户繁忙状态
	 */
	public void setBusyStatus() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			/*
			 * if(jsonObj.optString("status").equals("1")){ getCurrentUser().setBusy(1); }
			 * else { getCurrentUser().setBusy(0); }
			 */
			// 更改该客服是否繁忙的状态位
			weixinService.updatePlatfromBusyStatus(jsonObj.optString("status"));
			jsonMap.put("result", "");
			sendJson(jsonObj.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取关注人数量
	 */
	public void loadSubscribeCount() {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("count", weixinService.loadSubscribeCount());
			sendJson(jsonObj.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 同步平台关注信息
	 */
	public void syncSubscribe() {
		try {
			ResponseEntity res = new ResponseEntity();
			res.setRes(weixinService.syncSubscribe());
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取咨询师详细信息
	 * 
	 * @return
	 */
	public String getAdviserDetail() {
		String guid = request.getParameter("guid");
		fromUserName = request.getParameter("fromUserName");
		try {
			adviserEntity = adviserManageService.getAdviserInfo(guid);
			weixinService.setDetailInfo(adviserEntity, request);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return SUCCESS;
	}

	/**
	 * 获取客服详细信息
	 * 
	 * @return
	 */
	public String getServiceDetail() {
		String account = request.getParameter("account");
		try {
			adviserEntity = adviserManageService.getAdviserInfo(account);
			// weixinService.setDetailInfo(adviserEntity,request);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "service_success";
	}

	/**
	 * 微信用户进行评价
	 */
	public String appraise() {
		title = "评价结果";
		try {
			int res = weixinService.appraise(task_queue_id, open_id, service_score, asses);
			// 查找类型
			// TaskQueueEntity taskQueueEntity =
			// weixinService.getTaskQueueByID(task_queue_id);
			if (res > 0) {
				// if("az_en".equals(taskQueueEntity.getWeichat_type())){
				// result = MessageHelper.getInstance().getMessage("APPRAISAL_SUCCESS_EN");
				// title = "Appraisal Result";
				// } else {
				result = MessageHelper.getInstance().getMessage("APPRAISAL_SUCCESS");
				// }
			} else {
				// if("az_en".equals(taskQueueEntity.getWeichat_type())){
				// result = MessageHelper.getInstance().getMessage("APPRAISAL_FAIL_EN");
				// title = "Appraisal Result";
				// } else {
				result = MessageHelper.getInstance().getMessage("APPRAISAL_FAIL");
				// }
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "appraise_success";
	}

	/**
	 * 获取请求类型
	 */
	public void getRequestType() {
		try {
			List<HashMap<String, String>> typeList = weixinService.getRequestType();
			// List<Map<String ,String>> typeList = new ArrayList<Map<String ,String>>();
			// String typeValue =
			// ParamsConfig.getParams().get("request_type").getParam_value();
			// StringTokenizer strTokenizer = new StringTokenizer(typeValue, ";；");
			// String type = "";
			// while (strTokenizer.hasMoreTokens()) {
			// type = strTokenizer.nextToken();
			// Map<String, String> map = new HashMap<String, String>();
			// map.put("value", type);
			// map.put("text", type);
			// typeList.add(map);
			// }
			sendJson(JSONArray.fromObject(typeList).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 取得帮助列表
	 * 
	 * @return
	 */
	public String getHelpList() {
		try {
			articleCatagoryList = weixinService.getHelpList();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "help_success";
	}

	/**
	 * 点击”帮助“取得相关文章
	 */
	public String getContent() {
		String str = "help_info_success";
		try {
			articleEntity = weixinService.getArticleByID(ArticleID);
			if (articleEntity == null) {
				str = "help_info_error";
			} else {
				str = "help_info_success";
				// 记录阅读数
				readNumber = weixinService.getReadNumber(ArticleID);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return str;
	}

	/**
	 * 用户发起预约
	 * 
	 * @return
	 */
	public String orderAdvisory() {
		try {
			String status = weixinService.checkIfExistsOrder(fromUserName);
			if (status.equals("1") || status.equals("2")) {
				result = "预约失败";
				orderStatus = "未完成";
				return "order_success";
			} else if (status.equals("3")) {
				result = "预约失败";
				orderStatus = "未反馈";
				return "order_success";
			}
			int resultFlag = weixinService.createAdvisory(freeTimeRadio, phone, qqNo, fromUserName, account, facePhone,
					adviseType);
			if (resultFlag == 0) {
				result = "预约失败";
				orderStatus = "失败";
			} else {
				result = "预约成功";
				orderStatus = "成功";
			}
			return "order_success";

		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 保存反馈信息
	 */
	public void saveFeedback() {
		try {
			ResponseEntity res = new ResponseEntity();
			res.setResDesc(weixinService.syncSaveFeedback(req_params));
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			if (e instanceof CustomException) {
				ResponseEntity res = new ResponseEntity();
				res.setResDesc(e.getMessage());
				sendJson(JSONObject.fromObject(res).toString());
			} else {
				sendErrorToFront();
			}
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 验签，第一次微信平台验证服务器是否有效使用
	 */
	private void checkSignature() {
		/*
		 * token = this.getCid().toLowerCase(); if (Tools.checkSignature(token,
		 * signature, timestamp, nonce)) { log.info("服务器验签成功!"); sendJson(echostr); }
		 */

		String sEchoStr; // 需要返回的明文
		try {
			WXBizMsgCrypt wxcpt = getWXBizMsgCrypt();
			sEchoStr = wxcpt.VerifyURL(msg_signature, timestamp, nonce, echostr);
			// 验证URL成功，将sEchoStr返回
			sendJson(sEchoStr);
		} catch (Exception e) {
			// 验证URL失败，错误原因请查看异常
			e.printStackTrace();
		}
	}

	/**
	 * 获取微信平台发来的消息
	 * 
	 * @return
	 */
	private InMessage getInMessage() {
		InMessage msg = null;
		try {

			ServletInputStream in = request.getInputStream();
			XStream xs = XStreamFactory.init(false);
			xs.alias("xml", InMessage.class);
			String xmlMsg = Tools.inputStream2String(in);
			WXBizMsgCrypt wxcpt = getWXBizMsgCrypt();
			// String sMsg = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, xmlMsg);
			msg = (InMessage) xs.fromXML(xmlMsg);

			// String xmlMsg = "";
			// for(Iterator iter =
			// request.getParameterMap().entrySet().iterator();iter.hasNext();){
			// Map.Entry element = (Map.Entry)iter.next();
			// Object strKey = element.getKey();
			// if (strKey instanceof String) {
			// String strKeyString = strKey.toString();
			// if (strKeyString.startsWith("<xml>")) {
			// xmlMsg = strKeyString;
			// }
			// }
			// }
			/*
			 * WXBizMsgCrypt wxcpt = getWXBizMsgCrypt(); String sMsg =
			 * wxcpt.DecryptMsg(msg_signature, timestamp, nonce, xmlMsg);
			 * 
			 * msg = (InMessage) xs.fromXML(sMsg);
			 */
			// msg = (InMessage) xs.fromXML(xmlMsg);
			// msg.setCid(cid);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			msg = new InMessage();
		}
		return msg;
	}

	/**
	 * 给微信平台用户发消息
	 * 
	 * @param inMsg
	 * @param outMsg
	 */
	@SuppressWarnings("static-access")
	private void sendMessage(InMessage inMsg, OutMessage outMsg) {
		try {
			/*
			 * outMsg.setCreateTime(new Date().getTime());
			 * outMsg.setToUserName(inMsg.getFromUserName());
			 * outMsg.setFromUserName(inMsg.getToUserName());
			 * response.setCharacterEncoding("UTF-8"); response.setContentType("text/xml");
			 * XStream xs = XStreamFactory.init(false); xs.alias("xml", OutMessage.class);
			 * xs.alias("item", Articles.class); //xs.toXML(outMsg, response.getWriter());
			 * WXBizMsgCrypt wxcpt = getWXBizMsgCrypt(); String sEncryptMsg =
			 * wxcpt.EncryptMsg(xs.toXML(outMsg), timestamp, nonce); sendJson(sEncryptMsg);
			 */

			WeixinHelper weixinHelper = new WeixinHelper();
			weixinHelper.sendMessageForAZ(inMsg.getFromUserName(), outMsg.getContent());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 */
	public WXBizMsgCrypt getWXBizMsgCrypt() {
		String sToken = "74CzcfsSMNyCEEBptfdSFIpWHu";
		// String sCorpID = "wxe174e8c779e5162a";//默沙东
		String sCorpID = "wx5ce7d77750e6abc3";// 微龙
		String sEncodingAESKey = "m3XS7fSyrgUIW9gVNEqlwjOBOGjaoPnJVCtlsMxFp2P";
		WXBizMsgCrypt wxcpt = null;
		try {
			wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
		} catch (AesException e) {
			e.printStackTrace();
		}
		return wxcpt;
	}

	/**
	 * 获取心情能量详情
	 * 
	 * @param weixin_code
	 * @param month
	 * @return
	 */
	public String getMoodDetail() {
		fromUserName = request.getParameter("fromUserName");
		int month = Integer.valueOf(request.getParameter("month"));
		try {
			adviserEntity = weixinService.getMoodDetail(adviserEntity, fromUserName, month, request);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "success_mood";
	}

	/**
	 * 获取所有客服
	 */
	SessionRegistry sessionRegistry;

	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	MessageDispatch messageDispatch;

	public void setMessageDispatch(MessageDispatch messageDispatch) {
		this.messageDispatch = messageDispatch;
	}

	public void getAllService() {
		try {
			List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

			List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

			for (int i = 0; i < allPrincipals.size(); i++) {
				Object obj = allPrincipals.get(i);
				if (obj instanceof PlatformUserEntity) {
					int busyStatus = messageDispatch.getPlatfromBusyStatus(((PlatformUserEntity) obj).getId());

					// if(((PlatformUserEntity)obj).getType() == UserType.SERVICE_STANDARD &&
					// busyStatus == 0){
					if (((PlatformUserEntity) obj).getType() == UserType.SERVICE_AMATEUR && busyStatus == 0) {
						// standard.add(obj);
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("id", ((PlatformUserEntity) obj).getId());
						map.put("account", ((PlatformUserEntity) obj).getAccount());
						map.put("name", ((PlatformUserEntity) obj).getName());
						map.put("sex", ((PlatformUserEntity) obj).getSex());
						map.put("mobile_phone", ((PlatformUserEntity) obj).getMobile_phone());

						list.add(map);
					}
				}

			}
			// list = weixinService.queryService(0, 50);

			List<HashMap<String, Object>> list1 = new ArrayList<HashMap<String, Object>>();

			if (list.size() > (getOffset() + getRows())) {

				for (int i = getOffset(); i < (getOffset() + getRows()); i++) {

					list1.add(list.get(i));

				}

			} else {
				for (int i = getOffset(); i < list.size(); i++) {

					list1.add(list.get(i));

				}
			}
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", list.size());
			jsonMap.put("rows", list1);

			// Map<String, Object> jsonMap = new HashMap<String, Object>();
			// jsonMap.put("total", weixinService.queryServiceCount());
			// jsonMap.put("rows", weixinService.queryService(getOffset(), getRows()));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}

	/**
	 * 获取某一位客服的leader
	 */
	public void getCurrentLeader() {
		try {
			// Map<String, Object> jsonMap = new HashMap<String, Object>();
			// jsonMap.put("total", weixinService.queryServiceCount());
			// jsonMap.put("rows", weixinService.queryService(getOffset(), getRows()));
			sendJson(JSONArray.fromObject(weixinService.getCurrentLeader(id)).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}

	/**
	 * 转发任务给其他客服
	 */
	public void transferTask() {
		try {
			// 直接转发

			weixinService.syncAcceptTransferTask("[1]", taskID, transferToId);

			weixinService.transferTask(taskID, "[1]", transferToId);
			sendSuccessToFront();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}

	/**
	 * 获取转发任务列表
	 */
	public void getTransferingTaskList() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", weixinService.getTransferingTaskCount());
			jsonMap.put("rows", weixinService.getTransferingTaskList(getOffset(), getRows()));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}

	/**
	 * leader同意转发
	 */
	public void acceptTransferTask() {
		try {
			weixinService.syncAcceptTransferTask(id, taskId, transferToId);
			sendSuccessToFront();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}

	/**
	 * 查找KDATA
	 */
	public void sreachKData() {
		try {
			JSONObject jsonObj = JSONObject.fromObject(req_params);
			sendJson(JSONObject.fromObject(weixinService.sreachKData(jsonObj.optString("kid"))).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}

	public void getAccessToken() {
		String accessToken = WeixinHelper.ACCESS_TOKEN;
		sendJson(accessToken);
	}

	/**
	 * 测试接口
	 */
	public void testService() {
		try {
			Map<String, Object> resMap = weixinService.sreachKData(kid);
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			/*
			 * jsonMap.put("kid", "k123456"); jsonMap.put("name", "张三");
			 */
			String str = "";
			if (resMap == null) {
				// jsonMap.put("content","您所查询的K账户不存在");
				str = "您所查询的K账户不存在";
			} else {
				// jsonMap.put("content", resMap.get("name")+ "，您好！欢迎使用阿斯利康IT服务台，您的K帐号是"+kid);
				str = resMap.get("name") + "，您好！欢迎使用阿斯利康IT服务台，您的电话是：" + resMap.get("cellphone") + ",您的K帐号是" + kid;
			}
			sendJson(str);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}

	/**
	 * 取得帮助列表
	 * 
	 * @return
	 */
	public String getTwoLevelMenuList() {
		try {
			String sectionID = "";
			if ("001".equals(title_code)) {
				sectionID = "54b705e4-bd1b-401f-a8f7-6af8693fafc9";
			} else if ("002".equals(title_code)) {
				sectionID = "260bcd80-d7e8-4c46-bbdb-af18bfb7f48a";
			} else if ("003".equals(title_code)) {
				sectionID = "d84b7907-90ec-46f4-a46c-9d3f483a0a9c";
			} else if ("004".equals(title_code)) {
				sectionID = "c2bfb2f4-fa55-4d8b-8c0b-748ea8f357eb";
			} else if ("005".equals(title_code)) {
				sectionID = "9fd7fad9-c661-4df4-b592-53e7b3b7ede8";
			}
			// 分类名
			menuname = weixinService.getSectionsNameByID(sectionID);
			// 子类列表
			articleCatagoryListEntity = weixinService.getTwoLevelMenuList(sectionID);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "train";
	}

	/**
	 * 查找标题列表
	 * 
	 * @return
	 */
	public String getTitleList() {
		try {
			articleEntityList = weixinService.getTitleList(CatagoryID);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "title_list";
	}

	public String getReq_params() {
		return req_params;
	}

	public void setReq_params(String req_params) {
		this.req_params = req_params;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getEchostr() {
		return echostr;
	}

	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public AdviserEntity getAdviserEntity() {
		return adviserEntity;
	}

	public String getFreeTimeRadio() {
		return freeTimeRadio;
	}

	public void setFreeTimeRadio(String freeTimeRadio) {
		this.freeTimeRadio = freeTimeRadio;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQqNo() {
		return qqNo;
	}

	public void setQqNo(String qqNo) {
		this.qqNo = qqNo;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getFacePhone() {
		return facePhone;
	}

	public void setFacePhone(String facePhone) {
		this.facePhone = facePhone;
	}

	public Integer getAdviseType() {
		return adviseType;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getAdvisoryName() {
		return advisoryName;
	}

	public void setAdvisoryName(String advisoryName) {
		this.advisoryName = advisoryName;
	}

	public String getAdvisoryPhone() {
		return advisoryPhone;
	}

	public void setAdvisoryPhone(String advisoryPhone) {
		this.advisoryPhone = advisoryPhone;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public List<Map<String, Object>> getList_message() {
		return list_message;
	}

	public void setList_message(List<Map<String, Object>> list_message) {
		this.list_message = list_message;
	}

	public String getTask_queue_id() {
		return task_queue_id;
	}

	public void setTask_queue_id(String task_queue_id) {
		this.task_queue_id = task_queue_id;
	}

	public Integer getService_score() {
		return service_score;
	}

	public void setService_score(Integer service_score) {
		this.service_score = service_score;
	}

	public Integer getCustom_score() {
		return custom_score;
	}

	public void setCustom_score(Integer custom_score) {
		this.custom_score = custom_score;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getAsses() {
		return asses;
	}

	public void setAsses(String asses) {
		this.asses = asses;
	}

	public String getRequest_type() {
		return request_type;
	}

	public void setRequest_type(String request_type) {
		this.request_type = request_type;
	}

	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getTitle_code() {
		return title_code;
	}

	public void setTitle_code(String title_code) {
		this.title_code = title_code;
	}

	public String getCatagoryID() {
		return CatagoryID;
	}

	public void setCatagoryID(String catagoryID) {
		CatagoryID = catagoryID;
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public String getCatagoryName() {
		return CatagoryName;
	}

	public void setCatagoryName(String catagoryName) {
		CatagoryName = catagoryName;
	}

	public int getReadNumber() {
		return readNumber;
	}

	public void setReadNumber(int readNumber) {
		this.readNumber = readNumber;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMsg_signature() {
		return msg_signature;
	}

	public void setMsg_signature(String msg_signature) {
		this.msg_signature = msg_signature;
	}

}
