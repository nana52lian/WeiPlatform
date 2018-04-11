package com.yidatec.weixin.service;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import org.springframework.security.core.session.SessionRegistry;

import com.opensymphony.xwork2.ActionContext;
import com.yidatec.weixin.action.AesException;
import com.yidatec.weixin.action.WXBizMsgCrypt;
import com.yidatec.weixin.common.EnumRes;
import com.yidatec.weixin.common.ParamsConfig;
import com.yidatec.weixin.dao.DataBase.SerialNoType;
import com.yidatec.weixin.dao.WeixinDao;
import com.yidatec.weixin.entity.AdviserEntity;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.UserFeedbackEntity;
import com.yidatec.weixin.entity.UserScheduleEntity;
import com.yidatec.weixin.entity.message.ArticleCatagoryEntity;
import com.yidatec.weixin.entity.message.ArticleCatagoryListEntity;
import com.yidatec.weixin.entity.message.ArticleEntity;
import com.yidatec.weixin.entity.message.Articles;
import com.yidatec.weixin.entity.message.DataSetValue;
import com.yidatec.weixin.entity.message.EventKey;
import com.yidatec.weixin.entity.message.EventType;
import com.yidatec.weixin.entity.message.InMessage;
import com.yidatec.weixin.entity.message.MessageType;
import com.yidatec.weixin.entity.message.OutMessage;
import com.yidatec.weixin.entity.message.ServiceMessage;
import com.yidatec.weixin.entity.message.TaskDisplayEntity;
import com.yidatec.weixin.entity.message.TaskQueueEntity;
import com.yidatec.weixin.entity.message.TaskStatus;
import com.yidatec.weixin.entity.message.UserDetail;
import com.yidatec.weixin.entity.message.UserType;
import com.yidatec.weixin.exception.CustomException;
import com.yidatec.weixin.helper.MessageHelper;
import com.yidatec.weixin.message.MessageHandle;
import com.yidatec.weixin.message.SendMessage2Takeda;
import com.yidatec.weixin.message.WeixinHelper;
import com.yidatec.weixin.util.CommonMethod;
import com.yidatec.weixin.util.PropertiesUtil;
import com.yidatec.weixin.util.VoiceConvertHelper;

/**
 * 处理Text消息
 * 
 * @author Lance
 * 
 */
public class WeixinService {

	private static final Logger log = LogManager.getLogger(WeixinService.class);
	public final static String DOWNLOAD_VOICE_PATH = "/voice_files/";

	private WeixinDao weixinDao = null;

	private MessageHandle messageHandle = null;
	SessionRegistry sessionRegistry;

	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	public void setWeixinDao(WeixinDao weixinDao) {
		this.weixinDao = weixinDao;
	}

	public void setMessageHandle(MessageHandle messageHandle) {
		this.messageHandle = messageHandle;
	}

	/**
	 * 我要咨询
	 * 
	 * @param inMsg
	 * @return
	 * @throws Exception
	 */
	public OutMessage addMessage(InMessage inMsg, OutMessage outMsg) throws Exception {
		return messageHandle.addMessage(inMsg, outMsg, syncSerialNoType());
	}

	/**
	 * 处理文本消息
	 * 
	 * @param inMsg
	 * @return
	 * @throws Exception
	 */
	public OutMessage processTextMessage(InMessage inMsg, HttpServletRequest request) throws Exception {
		OutMessage outMsg = new OutMessage();
		String content = inMsg.getContent();
		/*
		 * // 输入0，返回主菜单 if(content.equals(FuncID.MAIN_MENU)){
		 * outMsg.setContent(MessageHelper.getInstance().getMessage("MAIN_MENU")); } //
		 * 输入菜单编号S，返回阳光操场主菜单 else if (content.equalsIgnoreCase(FuncID.S_MENU)) {
		 * outMsg.setContent(MessageHelper.getInstance().getMessage( "S_MENU")); } //
		 * 输入心情记录分数 else if (content.startsWith("e") && content.length()>1) {
		 * saveMoodScore(inMsg, outMsg); } // 输入e，查看当月心情能量棒 else if
		 * (content.equalsIgnoreCase(FuncID.MOOD_CHART)){ getMoodChart(inMsg, outMsg,
		 * request); } //输入咨询菜单1，返回可预约的心理顾问 else
		 * if(content.equalsIgnoreCase(FuncID.ADVISORY_MENU_1)){
		 * getOptionalAdvisers(inMsg, outMsg, request.getContextPath(),0); }
		 * //输入咨询菜单2，返回预约过的心理顾问 else
		 * if(content.equalsIgnoreCase(FuncID.ADVISORY_MENU_2)){
		 * getReservedAdvisers(inMsg, outMsg, request); } // 输入菜单编号3，返回我的预约记录 else if
		 * (content.equalsIgnoreCase(FuncID.MY_RESERVE)) { getUserSchedule(inMsg,
		 * outMsg); } //输入4返回我的反馈 else if(content.equalsIgnoreCase(FuncID.MY_FEEDBACK))
		 * { loadMyFeedback(inMsg, outMsg); } //输入5返回顾问使用和说明 else
		 * if(content.equalsIgnoreCase(FuncID.USER_GUIDE)) { loadUSERGUIDE(inMsg,
		 * outMsg); } else if(content.equalsIgnoreCase(FuncID.PAGINATION)){
		 * getOptionalAdvisers(inMsg, outMsg, request.getContextPath(),1); } // TEST
		 * else if(content.equalsIgnoreCase(FuncID.TEST)){ log.error("From user::" +
		 * inMsg.getFromUserName() + ", To user::" + inMsg.getToUserName()); } // 客服消息处理
		 * else { //查找是否有该客户的任务,有就更新状态,没有不处理 messageHandle.modifyTaskStatus(inMsg,
		 * outMsg); }
		 */
		messageHandle.modifyTaskStatus(inMsg, outMsg);
		return outMsg;
	}

	/**
	 * 处理图片消息
	 * 
	 * @param inMsg
	 * @return
	 * @throws Exception
	 */
	public OutMessage processImageMessage(InMessage inMsg, HttpServletRequest request) throws Exception {
		OutMessage outMsg = new OutMessage();
		// inMsg.setContent("<img src=\"" + inMsg.getPicUrl() + "\" width=\"330px\"
		// />");
		inMsg.setContent("<a target=\"_blank\" href=\"" + inMsg.getPicUrl() + "\" ><img src=\"" + inMsg.getPicUrl()
				+ "\" width=\"330px\" /></a>");
		messageHandle.modifyTaskStatus(inMsg, outMsg);
		return outMsg;
	}

	/**
	 * 处理语音消息
	 * 
	 * @param inMsg
	 * @return
	 * @throws Exception
	 */
	public OutMessage processVoiceMessage(InMessage inMsg, HttpServletRequest request) throws Exception {
		OutMessage outMsg = new OutMessage();
		String pathName = ServletActionContext.getServletContext().getRealPath(DOWNLOAD_VOICE_PATH);
		String mp3name = inMsg.getMediaId();
		log.info(pathName+"\\"+mp3name+".amr");
		String mp3Path = VoiceConvertHelper.amr2mp3(pathName+"\\"+mp3name+".amr");
		inMsg.setContent(DOWNLOAD_VOICE_PATH+mp3name+".mp3");
		messageHandle.modifyTaskStatus(inMsg, outMsg);
		return outMsg;
	}

	/**
	 * 获取关注数量
	 * 
	 * @return
	 * @throws Exception
	 */
	public int loadSubscribeCount() throws Exception {
		WeixinHelper helper = new WeixinHelper();
		JSONObject jsonObject = helper.getSubscribeList();
		if (jsonObject.optInt("errcode") != 0)
			return 0;
		return jsonObject.optInt("total");
	}

	/**
	 * 同步平台关注信息
	 */
	public EnumRes syncSubscribe() throws Exception {
		// 1. 获取公众平台关注列表
		WeixinHelper helper = new WeixinHelper();
		JSONObject jsonObject = helper.getSubscribeList();
		if (jsonObject.optInt("errcode") != 0)
			return EnumRes.FAILED;

		JSONObject data = JSONObject.fromObject(jsonObject.optString("data"));
		JSONArray list_openids = data.optJSONArray("openid");
		List<String> server_openids = new ArrayList<String>();
		for (int i = 0; i < list_openids.size(); i++) {
			server_openids.add(list_openids.getString(i));
		}
		/**
		 * 关注者数量超过10000时 while (!"".equals(jsonObject.optString("next_openid"))) {
		 * jsonObject = helper.getSubscribeList(jsonObject.optString("next_openid"));
		 * data = JSONObject.fromObject(jsonObject.optString("data")); list_openids =
		 * data.optJSONArray("openid"); for (int i=0; i<list_openids.size(); i++) {
		 * server_openids.add(list_openids.getString(i)); } }
		 */
		// 2. 获取本地已存的关注人列表
		List<String> local_openids = weixinDao.loadSubscribeOpenids();
		// 合并两个数组，并且去掉重复的记录
		server_openids.removeAll(local_openids);
		server_openids.addAll(local_openids);
		// 3. 获取每个人的详细信息
		List<JSONObject> userInfo_update = new ArrayList<JSONObject>();
		List<JSONObject> userInfo_create = new ArrayList<JSONObject>();
		JSONObject user = null;
		for (int i = 0; i < server_openids.size(); i++) {
			user = helper.getUserInfo(server_openids.get(i));
			// 如果本地曾经存在，要进行更新
			if (local_openids.contains(user.optString("openid"))) {
				userInfo_update.add(user);
			} else if (!"".equals(user.optString("openid"))) {
				userInfo_create.add(user);
			}
		}
		// 4.更新数据库
		int[] list_res = weixinDao.addBatchWeixinUser(userInfo_create);
		list_res = weixinDao.updateBatchWeixinUser(userInfo_update);
		return EnumRes.SUCCESS;
	}

	/**
	 * 加载需要反馈的记录
	 * 
	 * @param inMsg
	 * @param outMsg
	 * @throws Exception
	 */
	public void loadMyFeedback(InMessage inMsg, OutMessage outMsg) throws Exception {
		List<UserFeedbackEntity> userFeedbackEntities = weixinDao.loadMyFeedback(inMsg.getFromUserName());
		if (userFeedbackEntities.size() == 0) {
			outMsg.setContent(MessageHelper.getInstance().getMessage("FEEDBACK_NO_MSG"));
		} else {
			UserFeedbackEntity entity = userFeedbackEntities.get(0);
			// 咨询日期:{0}\r\n咨询师:{1}\r\n电话:{2}\r\n请您点击<a href="{3}">这里</a>对此次咨询服务进行反馈，谢谢。
			outMsg.setContent(MessageHelper.getInstance().getMessage("FEEDBACK_MSG", new String[] {
					// entity.getBegin_time().split(" ")[0],
					entity.getName(), entity.getPhone(), PropertiesUtil.ppsConfig.getProperty("FEEDBACK_URL")
							+ entity.getId() + "&account=" + entity.getAdvisory_account() }));
		}
	}

	/**
	 * 保存反馈信息
	 * 
	 * @param req_params
	 * @return
	 * @throws Exception
	 */
	public String syncSaveFeedback(String req_params) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		// 1. 保存反馈信息
		int res = weixinDao.saveFeedback(jsonObject);
		if (res == 0) {
			throw new CustomException("10002", jsonObject.optString("user_schedule_id"));
		}
		// 2. 保存对咨询师的评价
		res = weixinDao.saveAdviserAssess(jsonObject);
		if (res == 0) {
			throw new CustomException("10001", jsonObject.optString("account"));
		}
		// 3. 改变咨询记录的状态为
		res = weixinDao.updateUserScheduleStatus(jsonObject.optString("user_schedule_id"), 4);
		if (res == 0) {
			throw new CustomException("10003", jsonObject.optString("account"));
		}
		return EnumRes.SUCCESS.getDescription();
	}

	/**
	 * 生成心情图片
	 * 
	 * @param inMsg
	 * @param outMsg
	 * @param request
	 * @throws Exception
	 */
	private void getMoodChart(InMessage inMsg, OutMessage outMsg, HttpServletRequest request) throws Exception {
		AdviserEntity adviserEntity = this.getMoodDetail(null, inMsg.getFromUserName(), 0, request);
		String result = adviserEntity.getPresentation();
		Articles articles = new Articles();
		articles.setTitle("我的心情");
		articles.setDescription("疾病源于习惯和情绪，您过去两周状态：" + result);
		articles.setPicUrl(getMoodChartUrl(inMsg.getFromUserName(), request, 0));
		articles.setUrl(PropertiesUtil.ppsConfig.getProperty("SERVER_ADDR") + request.getContextPath()
				+ "/weixin!getMoodDetail?fromUserName=" + inMsg.getFromUserName() + "&month=0");
		List<Articles> list = new ArrayList<Articles>();
		list.add(articles);
		outMsg.setMsgType(MessageType.NEWS);
		outMsg.setArticleCount(1);
		outMsg.setArticles(list);
		// log.error(articles.getPicUrl());
	}

	/**
	 * 处理事件消息<br/>
	 * subscribe(订阅) <br/>
	 * unsubscribe(取消订阅) <br/>
	 * CLICK(自定义菜单点击事件) <br/>
	 * 
	 * @param inMsg
	 * @return
	 * @throws Exception
	 */
	public OutMessage processEventMessage(InMessage inMsg, HttpServletRequest request) throws Exception {
		OutMessage outMsg = new OutMessage();
		// 订阅
		if (inMsg.getEvent().equals(EventType.SUBSCRIBE)) {
			if (inMsg.getCid().equals("az_en")) {
				outMsg.setContent(MessageHelper.getInstance().getMessage("WELCOME_TXT_EN"));
			} else {
				outMsg.setContent(MessageHelper.getInstance().getMessage("WELCOME_TXT"));
			}
			// 获取当前订阅的用户的open_id
			String open_id = inMsg.getFromUserName();
			// 根据open_id判断这次订阅的用户在不在users表里，如果存在，就根据open_id更新信息，不存在就重新插入一条记录
			WeixinHelper msgHelper = new WeixinHelper();
			JSONObject jsonObj = msgHelper.getUserInfoByToken(open_id, inMsg.getCid());
			// 判断下返回内容，有时候会请求失败
			if (!"".equals(jsonObj.optString("openid"))) {
				if (weixinDao.getUsersCountByOpenId(open_id) > 0) {
					// 存在，更新信息
					weixinDao.updateForSubscribe(open_id, jsonObj);
				} else {
					// 不存在，插入一条记录
					weixinDao.subscribe(jsonObj);
				}
			}
		} else if (inMsg.getEvent().equals(EventType.UNSUBSCRIBE)) {
			// 取消订阅后，更新users表里的subscribe=0
			String open_id = inMsg.getFromUserName();
			weixinDao.unsubscribe(open_id);
		} else if (inMsg.getEvent().equals(EventType.CLICK)) {
			outMsg = processMenuClick(inMsg, request);
		}
		return outMsg;
	}

	/**
	 * 处理按钮点击消息
	 * 
	 * @param inMsg
	 * @return
	 * @throws Exception
	 */
	private OutMessage processMenuClick(InMessage inMsg, HttpServletRequest request) throws Exception {
		OutMessage outMsg = new OutMessage();
		if (EventKey.MOOD_RECORD.equals(inMsg.getEventKey())) {
			// 返回主菜单，菜单中去掉了提示输入e和s的部分
			outMsg.setContent(MessageHelper.getInstance().getMessage("MAIN_MENU"));
		} else if (EventKey.MOOD_SEARCH.equals(inMsg.getEventKey())) {
			// 查看当月心情能量棒
			getMoodChart(inMsg, outMsg, request);
		} else if (EventKey.ADVISER_AVAILABLE.equals(inMsg.getEventKey())) {
			// 可预约的咨询顾问
			getOptionalAdvisers(inMsg, outMsg, request.getContextPath(), 0);
		} else if (EventKey.ADVISER_USED.equals(inMsg.getEventKey())) {
			// 预约过的咨询顾问
			getReservedAdvisers(inMsg, outMsg, request);
		} else if (EventKey.CURRENT_RESERVE.equals(inMsg.getEventKey())) {
			// 我的预约记录
			getUserSchedule(inMsg, outMsg);
		} else if (EventKey.ASSESS.equals(inMsg.getEventKey())) {
			// 评价
			loadMyFeedback(inMsg, outMsg);
		} else if (EventKey.USER_GUIDE.equals(inMsg.getEventKey())) {
			// 咨询使用说明
			loadUSERGUIDE(inMsg, outMsg);
		} else if (EventKey.CONSULT.equals(inMsg.getEventKey())) {

		} else {

		}
		return outMsg;
	}

	/**
	 * 单据号
	 * 
	 * @return
	 */
	public SerialNoType syncSerialNoType() {
		return SerialNoType.TASK_ID;
	}

	/**
	 * 获取用户当前咨询信息
	 * 
	 * @param inMsg
	 * @param outMsg
	 * @return
	 */
	private OutMessage getUserSchedule(InMessage inMsg, OutMessage outMsg) {
		UserScheduleEntity userSchedule = weixinDao.getCurrentUserSchedule(inMsg.getFromUserName());
		if (userSchedule != null) {
			String fromDt = userSchedule.getBeginTime();
			String toDt = userSchedule.getEndTime();
			outMsg.setContent(MessageHelper.getInstance().getMessage("USER_SCHEDULE_TXT",
					new String[] { userSchedule.getAdvisoryName(), userSchedule.getMobile_phone(),
							CommonMethod.strToDate(fromDt), CommonMethod.strToTime(fromDt),
							CommonMethod.strToTime(toDt), userSchedule.getChargeAccount() }));
		} else {
			outMsg.setContent(MessageHelper.getInstance().getMessage("NO_USER_SCHEDULE_TXT"));
		}
		return outMsg;
	}

	/**
	 * 保存心情指数
	 * 
	 * @param inMsg
	 * @param outMsg
	 * @return
	 */
	private OutMessage saveMoodScore(InMessage inMsg, OutMessage outMsg) {
		String content = inMsg.getContent();
		int score = 0;
		Date date = null;
		// 回复格式为：e5
		if (content.indexOf("#") == -1) {
			try {
				score = Integer.parseInt(content.substring(1));
				if (score < -5 || score > 5) {
					// 数字超出范围
					outMsg.setContent(MessageHelper.getInstance().getMessage("MOOD_SCORE_RANGE_ERROR"));
					return outMsg;
				}
			} catch (NumberFormatException e) {
				// 不是数字
				outMsg.setContent(MessageHelper.getInstance().getMessage("MOOD_SCORE_NOT_NUMBER_ERROR"));
				return outMsg;
			}
			date = CommonMethod.getNowDateShort();
		}
		// 回复格式为：score#xxxx-xx-xx#5
		else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				date = format.parse(content.substring(content.lastIndexOf("#") + 1));
			} catch (ParseException e) {
				// 日期格式输入错误
				outMsg.setContent(MessageHelper.getInstance().getMessage("MOOD_DATE_FORMAT_ERROR"));
				return outMsg;
			}
			try {
				score = Integer.parseInt(content.substring(1, content.lastIndexOf("#")));
				if (score < -5 || score > 5) {
					// 数字超出范围
					outMsg.setContent(MessageHelper.getInstance().getMessage("MOOD_SCORE_RANGE_ERROR"));
					return outMsg;
				}
			} catch (NumberFormatException e) {
				// 不是数字
				outMsg.setContent(MessageHelper.getInstance().getMessage("MOOD_SCORE_NOT_NUMBER_ERROR"));
				return outMsg;
			}
		}
		// 回复格式错误
		/*
		 * else { outMsg.setContent(MessageHelper.getInstance().getMessage(
		 * "MOOD_SCORE_REPLY_ERROR")); return outMsg; }
		 */
		int i = weixinDao.saveMoodScore(inMsg.getFromUserName(), date, score);
		if (i > 0)
			outMsg.setContent(MessageHelper.getInstance().getMessage("RECORD_SUCCESS"));
		else
			outMsg.setContent(MessageHelper.getInstance().getMessage("RECORD_FAILED"));
		return outMsg;
	}

	/**
	 * 获取空闲的咨询师
	 * 
	 * @param inMsg
	 * @param outMsg
	 * @return
	 */
	private OutMessage getOptionalAdvisers(InMessage inMsg, OutMessage outMsg, String contextPath, int flag)
			throws Exception {
		int lineNmu = 0;
		if (flag == 0) {
			int lastLine = weixinDao.getLastLine("1", inMsg.getFromUserName());
			if (lastLine == 0) {
				weixinDao.initPagination(1, 0, inMsg.getFromUserName());
			}
		} else {
			lineNmu = weixinDao.getLastLine("1", inMsg.getFromUserName());
		}

		List<AdviserEntity> adviserList = weixinDao.getOptionalAdvisers(lineNmu);

		if (adviserList == null) {
			outMsg.setContent(MessageHelper.getInstance().getMessage("ADVISORY_NO_OPTIONAL_ADVISERS"));
			return outMsg;
		}
		int currentLineNum = lineNmu + adviserList.size();
		weixinDao.updatePagination(1, currentLineNum, inMsg.getFromUserName());
		List<Articles> list = new ArrayList<Articles>();
		Articles articles = new Articles();
		articles.setTitle("咨询师列表");
		articles.setPicUrl("");
		articles.setUrl("");
		list.add(articles);
		for (AdviserEntity ae : adviserList) {
			articles = new Articles();
			if (ae.getProfessional() == 0) {
				BigDecimal totalTime = new BigDecimal(ae.getDuration());
				BigDecimal fee = new BigDecimal(0);
				BigDecimal freeTime = new BigDecimal(20);
				if (totalTime.compareTo(freeTime) >= 0) {
					fee = totalTime.subtract(freeTime).divideToIntegralValue(freeTime).multiply(new BigDecimal(30))
							.add(new BigDecimal(50));
				}
				fee = fee.add(new BigDecimal(80));
				// articles.setTitle(ae.getName()+" ["+ae.getLevel()+"]");
				articles.setTitle(ae.getName() + " " + ae.getProvinceName() + ae.getCityName() + ae.getRegionName()
						+ "  " + fee.toString() + " 元/45分钟");
			} else {
				articles.setTitle(ae.getName() + " " + ae.getProvinceName() + ae.getCityName() + ae.getRegionName());
			}
			articles.setDescription(ae.getLevel());
			articles.setPicUrl("");
			articles.setUrl(PropertiesUtil.ppsConfig.getProperty("SERVER_ADDR") + contextPath
					+ "/advisory!getAdviserDetail?guid=" + ae.getId() + "&fromUserName=" + inMsg.getFromUserName());
			// articles.setUrl("http://192.168.149.219" + contextPath +
			// "/advisory!getAdviserDetail?guid="+ae.getId()+"&fromUserName="+inMsg.getFromUserName());
			list.add(articles);
		}
		int advisersCount = weixinDao.getAdvisersCount();
		String paginationTxt = "输入 N 翻到下一页";
		if (advisersCount == currentLineNum)
			paginationTxt = "已达尾页，请输入 1 返回首页";
		articles = new Articles();
		articles.setTitle(paginationTxt);
		articles.setPicUrl("");
		articles.setUrl("");
		list.add(articles);
		outMsg.setMsgType(MessageType.NEWS);
		outMsg.setArticleCount(list.size());
		outMsg.setArticles(list);
		return outMsg;
	}

	/**
	 * 获取预约过的咨询师
	 * 
	 * @param inMsg
	 * @param outMsg
	 * @return
	 */
	private OutMessage getReservedAdvisers(InMessage inMsg, OutMessage outMsg, HttpServletRequest request) {
		List<AdviserEntity> adviserList = weixinDao.getReservedAdvisers(inMsg.getFromUserName());
		if (adviserList == null) {
			outMsg.setContent(MessageHelper.getInstance().getMessage("ADVISORY_NO_Reserved_ADVISERS"));
			return outMsg;
		}
		List<Articles> list = new ArrayList<Articles>();
		Articles articles = new Articles();
		articles.setTitle("咨询师列表");
		articles.setDescription("");
		articles.setPicUrl("");
		articles.setUrl("");
		list.add(articles);
		for (AdviserEntity ae : adviserList) {
			articles = new Articles();
			articles.setTitle(ae.getName() + " " + ae.getProvinceName() + ae.getCityName() + ae.getRegionName());
			articles.setDescription(ae.getLevel());
			articles.setPicUrl("");
			articles.setUrl(PropertiesUtil.ppsConfig.getProperty("SERVER_ADDR") + request.getContextPath()
					+ "/advisory!getAdviserDetail?guid=" + ae.getId());
			list.add(articles);
		}
		outMsg.setMsgType(MessageType.NEWS);
		outMsg.setArticleCount(list.size());
		outMsg.setArticles(list);
		return outMsg;
	}

	/**
	 * 获取用户评价图
	 * 
	 * @param account
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String getAssessChartUrl(AdviserEntity adviserEntityt, HttpServletRequest request) throws Exception {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		String s = "Assess";
		dataset.addValue(Double.valueOf(adviserEntityt.getAssess1()), s, "有收获的");
		dataset.addValue(Double.valueOf(adviserEntityt.getAssess2()), s, "被理解和尊重");
		dataset.addValue(Double.valueOf(adviserEntityt.getAssess3()), s, "感觉舒适的");
		dataset.addValue(Double.valueOf(adviserEntityt.getAssess4()), s, "可信赖的");
		dataset.addValue(Double.valueOf(adviserEntityt.getAssess5()), s, "倾向于指导");
		dataset.addValue(Double.valueOf(adviserEntityt.getAssess6()), s, "倾向于启发");
		dataset.addValue(Double.valueOf(adviserEntityt.getAssess7()), s, "倾向于倾听");
		JFreeChart chart = ChartFactory.createBarChart("用户评价", "", "", dataset, PlotOrientation.HORIZONTAL, false, true,
				false);
		setBarTheme(chart, null);
		String filename = java.util.UUID.randomUUID().toString() + ".png";
		FileOutputStream os = new FileOutputStream(ServletActionContext.getServletContext().getRealPath("/")
				+ PropertiesUtil.ppsConfig.getProperty("MOOD_CHART_PATH") + "\\" + filename);
		ChartUtilities.writeChartAsPNG(os, chart, 640, 480);
		os.close();
		// PropertiesUtil.ppsConfig.getProperty("SERVER_ADDR")
		return PropertiesUtil.ppsConfig.getProperty("SERVER_ADDR") + request.getContextPath() + "/"
				+ PropertiesUtil.ppsConfig.getProperty("MOOD_CHART_PATH") + "/" + filename;

	}

	/**
	 * 获取心情图
	 * 
	 * @param weixin_code
	 * @param request
	 * @param month
	 *            取哪个月 传0表示取当前月的
	 * @return
	 * @throws Exception
	 */
	public String getMoodChartUrl(String weixin_code, HttpServletRequest request, int month) throws Exception {
		List<DataSetValue> list_value = weixinDao.loadMoodData(weixin_code, month);
		DefaultCategoryDataset dataset = getDefaultCategoryDataset(list_value);
		JFreeChart chart = ChartFactory.createBarChart("心情能量棒", // 图表标题
				"日期", // 目录轴的显示标签
				"指数", // 数值轴的显示标签
				dataset, // 数据集
				PlotOrientation.VERTICAL, // 图表方向：水平、垂直
				false, // 是否显示图例(对于简单的柱状图必须是false)
				true, // 是否生成工具
				true // 是否生成URL链接
		);
		setBarTheme(chart, list_value);
		CategoryPlot plot = chart.getCategoryPlot();
		ValueAxis valueAxis = plot.getRangeAxis();
		valueAxis.setLowerBound(-5);
		valueAxis.setUpperBound(5);
		StandardEntityCollection sec = new StandardEntityCollection();
		ChartRenderingInfo info = new ChartRenderingInfo(sec);
		String filename = java.util.UUID.randomUUID().toString() + ".png";
		FileOutputStream os = new FileOutputStream(ServletActionContext.getServletContext().getRealPath("/")
				+ PropertiesUtil.ppsConfig.getProperty("MOOD_CHART_PATH") + "\\" + filename);
		ChartUtilities.writeChartAsPNG(os, chart, 640, 510);
		os.close();
		return PropertiesUtil.ppsConfig.getProperty("SERVER_ADDR") + request.getContextPath() + "/"
				+ PropertiesUtil.ppsConfig.getProperty("MOOD_CHART_PATH") + "/" + filename;
		// HttpSession session = request.getSession();
		// String filename = ServletUtilities.saveChartAsPNG(chart, 960, 480, info,
		// session);
		// return PropertiesUtil.ppsConfig.getProperty("SERVER_ADDR") +
		// request.getContextPath() + "/DisplayChart.servlet?filename=" + filename;
	}

	/**
	 * 封装JFREECHART数据集合
	 * 
	 * @param list_value
	 * @return
	 */
	private DefaultCategoryDataset getDefaultCategoryDataset(List<DataSetValue> list_value) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < list_value.size(); i++) {
			DataSetValue value = list_value.get(i);
			dataset.addValue(value.getValue(), value.getRowkey(), value.getColumnKey());
		}
		return dataset;
	}

	/**
	 * 设置柱状图的样式
	 * 
	 * @param chart
	 */
	private void setBarTheme(JFreeChart chart, List<DataSetValue> list_value) {
		if (chart == null) {
			return;
		}
		Font font = new Font("黑体", Font.BOLD, 25); // 设置字体，否则中文显示乱码
		chart.setBackgroundPaint(new Color(229, 241, 241)); // 设置背景色
		chart.getTitle().setFont(font); // 设置标题字体
		CategoryPlot plot = chart.getCategoryPlot();

		plot.setBackgroundPaint(new Color(214, 240, 250)); // 设置绘图区背景色
		plot.setRangeGridlinePaint(Color.gray); // 设置水平方向背景线颜色
		plot.setRangeGridlinesVisible(true); // 设置是否显示水平方向背景线,默认值为True
		plot.setDomainGridlinePaint(Color.black); // 设置垂直方向背景线颜色
		// plot.setForegroundAlpha(0.80f);// 设置柱的透明度
		// plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);//设置X轴显示位置
		// plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);// 设置Y轴显示位置

		// 设置Y轴显示整数
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 25));
		numberAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 20));
		// 设置X轴
		CategoryAxis categoryAxis = plot.getDomainAxis();
		// 设置距离图片左端距离
		categoryAxis.setLowerMargin(0.05);
		categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 25));// 设置字体
		// 分类标签以45度角倾斜
		// categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		categoryAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 20));

		BarRenderer3D renderer = new BarRenderer3D();
		/*
		 * { // 重写BarRenderer，使每个柱子呈现不同颜色 private static final long serialVersionUID =
		 * -1255550568726683896L;
		 * 
		 * private Paint colors[] = new Paint[] { new GradientPaint(0.0F, 0.0F, new
		 * Color(0x00ee6f6f), 0.0F, 0.0F, new Color(0x00ee6f6f)), new
		 * GradientPaint(0.0F, 0.0F, new Color(0x006fee6f), 0.0F, 0.0F, new
		 * Color(0x006fee6f)), new GradientPaint(0.0F, 0.0F, new Color(0x006f6fee),
		 * 0.0F, 0.0F, new Color(0x006f6fee)) };
		 * 
		 * public Paint getItemPaint(int i, int j) { return colors[j % 3]; } };
		 */
		if (list_value == null) {
			// 设置柱的颜色
			renderer.setSeriesPaint(0, new Color(0x006f6fee));
		} else {
			for (int i = 0; i < list_value.size(); i++) {
				DataSetValue value = list_value.get(i);
				if (list_value.get(i).getValue() < 0) {
					// 设置柱的颜色,负值蓝色
					renderer.setSeriesPaint(i, new Color(0x000070c0));
				} else {
					// 设置柱的颜色,正值绿色
					renderer.setSeriesPaint(i, new Color(0x0000b050));
				}
			}
		}
		// 最大宽度
		renderer.setMaximumBarWidth(0.08);
		renderer.setItemMargin(0.001);
		renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());// 显示鼠标提示
		// renderer.setWallPaint(Color.gray);// 设置 Wall 的颜色
		renderer.setItemLabelAnchorOffset(10D);// 设置柱形图上的文字偏离值
		renderer.setBaseItemLabelFont(new Font("宋体", Font.PLAIN, 20), true);// 设置柱形图上的文字
		// 显示每个柱的数值，并修改该数值的字体属性
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBasePositiveItemLabelPosition(
				new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		renderer.setBaseItemLabelsVisible(true);
		plot.setRenderer(renderer);
	}

	/**
	 * 获取某位咨询师的空闲时间
	 * 
	 * @param account
	 * @return
	 */
	public List<String> getFreeTimes(String account) {
		return weixinDao.getFreeTimes(account);
	}

	/**
	 * 用户发起预约
	 * 
	 * @param freeTimeRadio
	 * @param phone
	 * @param qqNo
	 * @param fromUserName
	 */
	public int createAdvisory(String freeTimeRadio, String phone, String qqNo, String fromUserName, String account,
			String facePhone, int adviseType) throws Exception {
		int dataInt = Integer.parseInt(freeTimeRadio.substring(1));
		String flag = freeTimeRadio.substring(0, 1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		Date date = new Date();
		String currentDate = sdf.format(date);
		if (flag.equals("b")) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.DATE, 1);
			date = calendar.getTime();
			currentDate = sdf.format(date);
		} else if (flag.equals("c")) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.DATE, 2);
			date = calendar.getTime();
			currentDate = sdf.format(date);
		}

		String fromDt = currentDate + String.format("%02d", dataInt);
		String toDt = currentDate + String.format("%02d", dataInt + 1);
		sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		if (adviseType == 1) {
			phone = "";
			facePhone = "";
		} else if (adviseType == 3) {
			qqNo = "";
			phone = facePhone;
		}
		return weixinDao.createAdvisory(fromUserName, account, sdf.parse(fromDt), sdf.parse(toDt), phone, qqNo,
				adviseType);
	}

	/**
	 * 获取咨询师空闲时间以及收费等信息
	 * 
	 * @param adviserEntity
	 */
	public void setDetailInfo(AdviserEntity adviserEntity, HttpServletRequest request) throws Exception {
		List<String> freeTimes1 = weixinDao.getFreeTimes(adviserEntity.getAccount());
		List<String> freeTimes2 = new ArrayList<String>();
		freeTimes2.addAll(freeTimes1);
		List<String> todayFreeTimes = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("H");
		int hour = Integer.parseInt(sdf.format(new Date()));
		for (String time : freeTimes1) {
			if (Integer.parseInt(time) > hour) {
				todayFreeTimes.add(time);
			}
		}

		List<String> usedTimes = weixinDao.getAllUsedTimes(adviserEntity.getAccount());
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		int today = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(calendar.DAY_OF_MONTH, 1);
		int tomorrow = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(calendar.DAY_OF_MONTH, 1);
		int acquired = calendar.get(Calendar.DAY_OF_MONTH);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (String time : usedTimes) {
			Calendar calendar2 = new GregorianCalendar();
			calendar2.setTime(format.parse(time));
			if (calendar2.get(Calendar.DAY_OF_MONTH) == today) {
				todayFreeTimes.remove(String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY)));
			} else if (calendar2.get(Calendar.DAY_OF_MONTH) == tomorrow) {
				freeTimes1.remove(String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY)));
			} else if (calendar2.get(Calendar.DAY_OF_MONTH) == acquired) {
				freeTimes2.remove(String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY)));
			}
		}

		adviserEntity.setTodayFreeTimes(todayFreeTimes);
		adviserEntity.setFreeTimes1(freeTimes1);
		adviserEntity.setFreeTimes2(freeTimes2);

		if (adviserEntity.getProfessional() == 0) {
			BigDecimal totalTime = new BigDecimal(adviserEntity.getDuration());
			BigDecimal fee = new BigDecimal(0);
			BigDecimal freeTime = new BigDecimal(20);
			if (totalTime.compareTo(freeTime) >= 0) {
				fee = totalTime.subtract(freeTime).divideToIntegralValue(freeTime).multiply(new BigDecimal(30))
						.add(new BigDecimal(50));
			}
			adviserEntity.setPrice_plan(fee.toString() + " 元/45分钟");
		}
		adviserEntity.setAssessImgUrl(this.getAssessChartUrl(adviserEntity, request));
	}

	/**
	 * 查询是否有没有完成的咨询
	 * 
	 * @param fromUserName
	 * @return
	 */
	public String checkIfExistsOrder(String fromUserName) {
		return weixinDao.checkIfExistsOrder(fromUserName);
	}

	/**
	 * 加载顾问使用和说明
	 * 
	 * @param inMsg
	 * @param outMsg
	 * @throws Exception
	 */
	public void loadUSERGUIDE(InMessage inMsg, OutMessage outMsg) throws Exception {
		outMsg.setContent(MessageHelper.getInstance().getMessage("ADVISORY_DIRECTION",
				new String[] { PropertiesUtil.ppsConfig.getProperty("ADVISORY_DIRECTION_URL") }));

	}

	/**
	 * 获取当月心情详情
	 * 
	 * @param adviserEntity
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public AdviserEntity getMoodDetail(AdviserEntity adviserEntity, String fromUserName, int month,
			HttpServletRequest request) throws Exception {
		adviserEntity = new AdviserEntity();
		int currentMonth;
		if (month == 0) {
			// 页面上显示的月份
			Calendar cal = Calendar.getInstance();
			currentMonth = cal.get(Calendar.MONTH) + 1;
			adviserEntity.setCurrentMonth(currentMonth);
			// 页面上隐藏的winxin_code,用于查看上个月的心情能量详情的url中的fromUserName
			adviserEntity.setFromUserName(fromUserName);
			// 获取当前月图
			String url = getMoodChartUrl(fromUserName, request, month);
			String newUrl = url.substring(url.indexOf("advisory"));
			adviserEntity.setMoodImg(newUrl);
			// 上个月心情能量详情Url
			String lastMonthMoodUrl = url.substring(url.indexOf("h"), url.lastIndexOf("mood_chart"));
			adviserEntity.setLastMonthMoodUrl(lastMonthMoodUrl);
			// 获取当前月提示语
			List<DataSetValue> list_value = weixinDao.loadMoodData(fromUserName, month);
			if (list_value.size() > 0) {
				float sum = 0;
				for (int i = 0; i < list_value.size(); i++) {
					double s = list_value.get(i).getValue();
					System.out.println(s);
					sum += list_value.get(i).getValue();
					System.out.println(sum);
				}
				float score = sum / list_value.size();
				int average_score = new BigDecimal(score).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
				switch (average_score) {
				case -5:
					adviserEntity.setPresentation("能量为零，您需要立即找一个心理顾问，以防恶化影响神经系统稳定");
					break;
				case -4:
					adviserEntity.setPresentation("能量极低，建议找一个心理顾问。负能量不仅影响自己，也会影响他人");
					break;
				case -3:
					adviserEntity.setPresentation("能量偏低，建议找一个心理顾问，负能量不仅影响自己，也会影响他人");
					break;
				case -2:
					adviserEntity.setPresentation("能量不足，寻找适合自己的活动放松心情");
					break;
				case -1:
					adviserEntity.setPresentation("能量不足，寻找适合自己的活动放松心情");
					break;
				case 0:
					adviserEntity.setPresentation("心情还好，马马虎虎");
					break;
				case 1:
					adviserEntity.setPresentation("心情轻快，一身轻松");
					break;
				case 2:
					adviserEntity.setPresentation("心情轻快，一身轻松");
					break;
				case 3:
					adviserEntity.setPresentation("心情不错，能量充足");
					break;
				case 4:
					adviserEntity.setPresentation("情绪高涨，能量充足");
					break;
				case 5:
					adviserEntity.setPresentation("心情能量超负荷，注意休息");
					break;
				}
			}

		}
		if (month != 0) {
			// 页面上显示的月份
			int lastMonth = month - 1;
			adviserEntity.setCurrentMonth(lastMonth);
			// 页面上显示的月份
			adviserEntity.setCurrentMonth(lastMonth);
			// 页面上隐藏的winxin_code,用于查看上个月的心情能量详情的url中的fromUserName
			adviserEntity.setFromUserName(fromUserName);
			// 获取上个月
			String url = getMoodChartUrl(fromUserName, request, lastMonth);
			String newUrl = url.substring(url.indexOf("advisory"));
			adviserEntity.setMoodImg(newUrl);
			// 上个月心情能量详情Url
			String lastMonthMoodUrl = url.substring(url.indexOf("h"), url.lastIndexOf("mood_chart"));
			adviserEntity.setLastMonthMoodUrl(lastMonthMoodUrl);
			// 获取上个月提示语
			List<DataSetValue> list_value = weixinDao.loadMoodData(fromUserName, lastMonth);
			if (list_value.size() > 0) {
				float sum = 0;
				for (int i = 0; i < list_value.size(); i++) {
					double s = list_value.get(i).getValue();
					System.out.println(s);
					sum += list_value.get(i).getValue();
					System.out.println(sum);
				}
				float score = sum / list_value.size();
				int average_score = new BigDecimal(score).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
				switch (average_score) {
				case -5:
					adviserEntity.setPresentation("能量为零，您需要立即找一个心理顾问，以防恶化影响神经系统稳定");
					break;
				case -4:
					adviserEntity.setPresentation("能量极低，建议找一个心理顾问。负能量不仅影响自己，也会影响他人");
					break;
				case -3:
					adviserEntity.setPresentation("能量偏低，建议找一个心理顾问，负能量不仅影响自己，也会影响他人");
					break;
				case -2:
					adviserEntity.setPresentation("能量不足，寻找适合自己的活动放松心情");
					break;
				case -1:
					adviserEntity.setPresentation("能量不足，寻找适合自己的活动放松心情");
					break;
				case 0:
					adviserEntity.setPresentation("心情还好，马马虎虎");
					break;
				case 1:
					adviserEntity.setPresentation("心情轻快，一身轻松");
					break;
				case 2:
					adviserEntity.setPresentation("心情轻快，一身轻松");
					break;
				case 3:
					adviserEntity.setPresentation("心情不错，能量充足");
					break;
				case 4:
					adviserEntity.setPresentation("情绪高涨，能量充足");
					break;
				case 5:
					adviserEntity.setPresentation("心情能量超负荷，注意休息");
					break;
				}
			}
		}

		return adviserEntity;

	}

	/**
	 * 取得某客服的未完成任务列表（包含激活状态的）
	 */
	public List<TaskDisplayEntity> getUnCompleteAdvisory() throws Exception {
		return weixinDao.getUnCompleteAdvisory();
	}

	/**
	 * 取得某客服的完成任务列表（包含无法处理的）次数
	 */
	public int getCompleteAdvisoryCount() throws Exception {
		return weixinDao.getCompleteAdvisoryCount().size();
	}

	/**
	 * 取得某客服的完成任务列表（包含无法处理的）
	 */
	public List<TaskDisplayEntity> getCompleteAdvisory(int offset, int rows) throws Exception {
		return weixinDao.getCompleteAdvisory(offset, rows);
	}

	/**
	 * 根据openid获得以往请求列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getRequestByOpenId(String req_params) throws Exception {
		return weixinDao.getRequestByOpenId(req_params);
	}

	/**
	 * 管理员取得系统所有请求单个数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getAllRequestCount(String req_params, String request_type) throws Exception {
		return weixinDao.getAllRequestCount(req_params, request_type);
	}

	/**
	 * 管理员取得系统所有请求单
	 * 
	 * @param request_type
	 * @param rows
	 * @param offset
	 */
	public List<TaskDisplayEntity> getAllRequest(String req_params, String request_type, int offset, int rows)
			throws Exception {
		return weixinDao.getAllRequest(req_params, request_type, offset, rows);
	}

	/**
	 * 根据任务ID取得某客服的任务次数
	 */
	public int getCompleteAdvisoryByTaskIDAndKcodeCount(JSONObject jsonObj) throws Exception {
		return weixinDao.getCompleteAdvisoryByTaskIDAndKcodeCount(jsonObj).size();
	}

	/**
	 * 根据任务ID取得某客服的任务
	 */
	public List<TaskDisplayEntity> getCompleteAdvisoryByTaskIDAndKcode(JSONObject jsonObj, int offset, int rows)
			throws Exception {
		return weixinDao.getCompleteAdvisoryByTaskIDAndKcode(jsonObj, offset, rows);
	}

	/**
	 * 根据任务日期取得某客服的任务次数
	 */
	public int getCompleteAdvisoryByTaskDateCount(JSONObject jsonObj) throws Exception {
		return weixinDao.getCompleteAdvisoryByTaskDateCount(jsonObj).size();
	}

	/**
	 * 根据任务日期取得某客服的任务
	 */
	public List<TaskDisplayEntity> getCompleteAdvisoryByTaskDate(JSONObject jsonObj, int offset, int rows)
			throws Exception {
		return weixinDao.getCompleteAdvisoryByTaskDate(jsonObj, offset, rows);
	}

	/**
	 * 取得某客服的挂起等待任务列表
	 */
	public List<TaskDisplayEntity> getWaitAdvisory(int type) throws Exception {
		// 检查现在挂起用户的数量
		int size = weixinDao.getWaitAdvisory().size();
		int sysMaxTimeout = 0;
		if (type == UserType.SERVICE_STANDARD) {
			// 全职
			sysMaxTimeout = Integer.valueOf(ParamsConfig.getParams().get("waitqueue_max_count").getParam_value());
		} else if (type == UserType.SERVICE_AMATEUR) {
			// 兼职
			sysMaxTimeout = Integer
					.valueOf(ParamsConfig.getParams().get("part-time_waitqueue_max_count").getParam_value());
		}
		// 如果挂起用户的数量已经大于等于系统配置里的二级队列最大挂起数量(兼职或者全职)，新的请求不被挂起
		boolean flag = false;
		if (size >= sysMaxTimeout) {
			flag = true;
		} else {// 如果小于那么挂起
			// 查找需要挂起的微信用户
			List<Map<String, Object>> mapList = weixinDao
					.getOpenIDForWait(ParamsConfig.getParams().get("suspend_timeout").getParam_value());

			// 查询当前咨询
			List<TaskDisplayEntity> taskDisplayList = weixinDao.getUnCompleteAdvisory();
			if (taskDisplayList.size() > 0) {
				for (int j = 0; j < taskDisplayList.size(); j++) {
					// 更新挂起状态
					weixinDao.ModifyTaskStatusForWait(taskDisplayList.get(j).getId(),
							ParamsConfig.getParams().get("suspend_timeout").getParam_value());
				}
			}
			// 发送微信消息
			WeixinHelper helper = new WeixinHelper();
			ServiceMessage message = new ServiceMessage();
			for (int i = 0; i < mapList.size(); i++) {
				message.setTouser(mapList.get(i).get("openID").toString());
				String str = MessageHelper.getInstance().getMessage("WAIT_MESSAGE");
				// 根据OPENID查找是哪个微信公众平台
				List<Map<String, Object>> appTypeList = weixinDao
						.getTaskQueueByOpenID(mapList.get(i).get("openID").toString());
				// if("az_en".equals(appTypeList.get(i).get("weichat_type").toString())){
				// str = MessageHelper.getInstance().getMessage("WAIT_MESSAGE_EN");
				// }
				message.setText(str);
				// 在消息表中插入一条数据
				weixinDao.addSystemMessage(mapList.get(i), str);
				// 给微信客户端发消息
				// 迁移了helper.sendMessageByLand(message,appTypeList.get(i).get("weichat_type").toString());
//				helper.sendMessageForAZ(message.getTouser(), str);
				SendMessage2Takeda.testPost(PropertiesUtil.ppsConfig.getProperty("TAKEDA_URL"), message);
			}
		}

		// 挂起的列表
		List<TaskDisplayEntity> taskDisplayList = weixinDao.getWaitAdvisory();
		// 定义个新列表做超时判断
		List<TaskDisplayEntity> newTaskDisplayList = new ArrayList<TaskDisplayEntity>();
		for (int i = 0; i < taskDisplayList.size(); i++) {
			// 和系统时间进行比较
			int count = CommonMethod.dateSubtract(taskDisplayList.get(i).getMessage_create_date());
			// 最大挂起时间转成INT类型
			int max_wait = Integer.valueOf(ParamsConfig.getParams().get("max_timeout").getParam_value());
			// 如果比较的小时数大于等于系统配置的请求挂起最长时间，就设置超时状态
			if (count >= max_wait) {
				// 设置超时状态
				taskDisplayList.get(i).setTask_status("6");
			}
			/*
			 * if(count < max_wait){ //最大挂起时间之前的关闭提醒小时数（设置时要小于最大挂起时间） int max_before =
			 * Integer.valueOf(ParamsConfig.getParams().get("max_timeout_before").
			 * getParam_value()); //未到达最大挂起时间,但是等于提醒时间,发送消息给微信端 if((max_wait - count) ==
			 * max_before){ WeixinHelper helper = new WeixinHelper(); ServiceMessage message
			 * = new ServiceMessage();
			 * message.setTouser(taskDisplayList.get(i).getOpen_id());
			 * message.setText(MessageHelper.getInstance().getMessage("CLOSE_REMIND_MESSAGE"
			 * )); //给客户端发消息 helper.sendMessage(message); } }
			 */
			// 设置弹出框
			if (flag) {
				taskDisplayList.get(i).setMaxAlterFlag("1");
				flag = false;
			}
			newTaskDisplayList.add(taskDisplayList.get(i));
		}
		return newTaskDisplayList;
	}

	/**
	 * 取得聊天信息
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> loadMessage(JSONObject jsonObj) throws Exception {
		List<Map<String, Object>> list_message = weixinDao.loadMessage(jsonObj);
		if (jsonObj.optString("first").length() > 0) {
			weixinDao.updateMessageStatus(jsonObj.optString("taskID"));
		}
		// 分配变为处理中
		weixinDao.updateTaskStatus(jsonObj.optString("taskID"));
		return list_message;
	}

	/**
	 * 取得历史聊天信息
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getHistoryMessage(JSONObject jsonObj) throws Exception {
		return weixinDao.loadMessage(jsonObj);
	}

	/**
	 * 取得历史聊天信息 QQ追加
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getHistoryMessageall(JSONObject jsonObj) throws Exception {
		return weixinDao.loadMessage_Historyall(jsonObj);
	}

	/**
	 * 向武田AI发送图片XML
	 * 
	 * @param touser
	 *            用户openid
	 * @param uploadUrl
	 * @param uploadFile
	 * @return
	 */
	public String postImageFile(String touser, String uploadUrl) {
		try {
			URL url = new URL(PropertiesUtil.ppsConfig.getProperty("TAKEDA_URL"));
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			// con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");

			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());

			StringBuilder sb = new StringBuilder();
			sb.append("<xml>");
			sb.append("    <ToUserName>" + touser + "</ToUserName>");
			sb.append("    <FromUserName>" + PropertiesUtil.ppsConfig.getProperty("TAKEDA_CORPID") + "</FromUserName>");
			sb.append("    <CreateTime>" + System.currentTimeMillis() + "</CreateTime>");
			sb.append("    <MsgType>image</MsgType>");
			sb.append("    <PicUrl>" + uploadUrl + "</PicUrl>");
			sb.append("    <AgentID>" + PropertiesUtil.ppsConfig.getProperty("TAKEDA_AGENTID") + "</AgentID>");
			sb.append("</xml>");
			String xmlInfo = sb.toString();
			out.write(new String(xmlInfo.getBytes("UTF-8")));// ISO-8859-1
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String line = "";
			for (line = br.readLine(); line != null; line = br.readLine()) {
				System.out.println(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return uploadUrl;

	}


	/**
	 * 给客户端发送消息并保存聊天信息(发送文本消息)
	 * 
	 * @param reqEvent
	 * @return
	 * @throws Exception
	 */
	public int sendServiceMessage(JSONObject jsonObj, String type) throws Exception {
		ServiceMessage message = new ServiceMessage();
		int result = -1;
		List<TaskQueueEntity> taskQueue = new ArrayList<TaskQueueEntity>();
		// 如果关闭后就不能发送消息了
		taskQueue = weixinDao.getTaskQueueByID(jsonObj.getString("task_queue_id"));
		if (taskQueue.size() > 0) {
			int status = taskQueue.get(0).getTask_status();
			// 如果状态不是关闭或者升级才发送信息
			if (status != TaskStatus.COMPLETED && status != TaskStatus.STOP) {
				message.setTouser(jsonObj.optString("open_id"));
				message.setText(jsonObj.optString("contentReplace"));
				// 保存聊天记录
				result = weixinDao.saveMessage(jsonObj);
			} else {
				result = 2;
			}
		}
		SendMessage2Takeda.testPost(PropertiesUtil.ppsConfig.getProperty("TAKEDA_URL"), message);
		return result;
	}

	/**
	 * 给客户端发送图片消息后保存记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public int sendServiceImageMessage(JSONObject jsonObj) throws Exception {
		int result = -1;
		List<TaskQueueEntity> taskQueue = new ArrayList<TaskQueueEntity>();
		// 如果关闭后就不能发送消息了
		taskQueue = weixinDao.getTaskQueueByID(jsonObj.getString("task_queue_id"));
		if (taskQueue.size() > 0) {
			int status = taskQueue.get(0).getTask_status();
			// 如果状态不是关闭或者升级才发送信息
			if (status != TaskStatus.COMPLETED && status != TaskStatus.STOP) {
				// 保存聊天记录
				result = weixinDao.saveMessage(jsonObj);
			} else {
				result = 2;
			}
		}
		return result;
	}

	/**
	 * 取得最后聊天或者创建咨询时间跟系统时间差距小时数
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public int getInterval(JSONObject jsonObj) throws Exception {
		return weixinDao.getInterval(jsonObj);
	}

	/**
	 * 给微信发送评价URL
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public boolean getAppraisalUrl(JSONObject jsonObj) throws Exception {
		WeixinHelper helper = new WeixinHelper();
		ServiceMessage message = new ServiceMessage();
		message.setTouser(jsonObj.optString("open_id"));
		String ipAndPort = PropertiesUtil.ppsConfig.getProperty("SERVER_IP") + ":"
				+ PropertiesUtil.ppsConfig.getProperty("SERVER_PORT");
		String url = "<a href=\"" 
//				+ ipAndPort + "/WeiPlatform_QY/admin/weixin/appraise.jsp?task_queue_id="
				+ jsonObj.optString("task_queue_id") + jsonObj.optString("open_id") 
				+ "\">点击这里</a>";
		// 根据OPENID查找是哪个微信公众平台
		List<Map<String, Object>> appTypeList = weixinDao.getTaskQueueByOpenID(jsonObj.optString("open_id"));
		String str = MessageHelper.getInstance().getMessage("APPRAISAL",
				new String[] { url, jsonObj.optString("rid") });
		message.setText(str);
		// 在消息表中插入一条数据
		Map<String, Object> taskQueue = new HashMap<String, Object>();
		taskQueue.put("task_queue_id", jsonObj.optString("task_queue_id"));
		taskQueue.put("openID", jsonObj.optString("open_id"));
		taskQueue.put("service_id", jsonObj.optString("service_id"));
		weixinDao.addSystemMessage(taskQueue, str);
		return 	SendMessage2Takeda.testPost(PropertiesUtil.ppsConfig.getProperty("TAKEDA_URL"), message);
	}

	/**
	 * 设置
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public boolean SetCause(JSONObject jsonObj) throws Exception {
		ServiceMessage message = new ServiceMessage();
		message.setTouser(jsonObj.optString("open_id"));
		String str = MessageHelper.getInstance().getMessage("ABNORMAL_CLOSE");
		log.info(str);
		message.setText(str);
		SendMessage2Takeda.testPost(PropertiesUtil.ppsConfig.getProperty("TAKEDA_URL"), message);
		return weixinDao.SetCause(jsonObj) > 0;
	}

	/**
	 * 请求单升级后给用户发送消息
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public boolean sendMsgForStop(JSONObject jsonObj) throws Exception {
		// 给客户端发消息
		WeixinHelper helper = new WeixinHelper();
		ServiceMessage message = new ServiceMessage();
		message.setTouser(jsonObj.optString("open_id"));
		message.setText(
				MessageHelper.getInstance().getMessage("STOP_MESSAGE", new String[] { jsonObj.optString("rid") }));
		// 在消息表中插入一条数据
		Map<String, Object> taskQueue = new HashMap<String, Object>();
		taskQueue.put("task_queue_id", jsonObj.optString("task_queue_id"));
		taskQueue.put("openID", jsonObj.optString("open_id"));
		taskQueue.put("service_id", jsonObj.optString("service_id"));
		weixinDao.addSystemMessage(taskQueue,
				MessageHelper.getInstance().getMessage("STOP_MESSAGE", new String[] { jsonObj.optString("rid") }));
		// 根据OPENID查找是哪个微信公众平台
		List<Map<String, Object>> appTypeList = weixinDao.getTaskQueueByOpenID(jsonObj.optString("open_id"));
		// 迁移了return
		// helper.sendMessageByLand(message,appTypeList.get(0).get("weichat_type").toString());
		return helper.sendMessageForAZ(message.getTouser(),
				MessageHelper.getInstance().getMessage("STOP_MESSAGE", new String[] { jsonObj.optString("rid") }));
	}

	/**
	 * 取得未读消息
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUnReadMessage(JSONObject jsonObj) throws Exception {
		// 取得
		List<Map<String, Object>> list = weixinDao.unReadMessage(jsonObj);
		// 更新为已读
		weixinDao.ModifyMessageStatus(list);
		return list;
	}

	/**
	 * 根据OPENID取得咨询次数
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public List<UserDetail> getWeixinChatCount(JSONObject jsonObj) throws Exception {
		// 根据OPENID取得所有咨询内容
		return weixinDao.getWeixinChatCount(jsonObj);
	}

	/**
	 * 根据TASKID取得所有咨询内容
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public List<UserDetail> getWeixinChatInfo(JSONObject jsonObj) throws Exception {
		// 根据taskID取得咨询内容
		return weixinDao.getWeixinChatInfo(jsonObj);
	}

	/**
	 * 根据OPENID取得所有咨询内容
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public List<UserDetail> getWeixinChatInfoByOpenID(JSONObject jsonObj) throws Exception {
		// 根据openID取得咨询内容
		return weixinDao.getWeixinChatInfoByOpenID(jsonObj);
	}

	/**
	 * 保存用户信息
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public int saveUserInfo(JSONObject jsonObj) throws Exception {
		int res = 0;
		weixinDao.updateTaskByID(jsonObj);
		// 查找KDATA
		List<UserDetail> list = weixinDao.getWeixinChatInfoByOpenID(jsonObj);
		// 有就更新
		if (list.size() > 0) {
			res = weixinDao.updateUserInfo(jsonObj);
		} else {
			// 没有就插入KDATA
			res = weixinDao.saveUserInfo(jsonObj);
		}
		return res;
	}

	/**
	 * 手动设置挂起
	 * 
	 * @return
	 */
	public int setStatus(JSONObject jsonObj) throws Exception {
		int tempStatus = 0;
		if (jsonObj.optString("task_type").equals("wait")) {
			tempStatus = TaskStatus.WAIT;
		} else if (jsonObj.optString("task_type").equals("stop")) {
			tempStatus = TaskStatus.STOP;
		} else if (jsonObj.optString("task_type").equals("completed")) {
			tempStatus = TaskStatus.COMPLETED;
		}
		return weixinDao.setStatus(tempStatus, jsonObj.optString("task_queue_id"));
	}

	/**
	 * 给兼职客服分配任务
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getTaskForAmateur() throws Exception {
		// 找到一个未分配的数据分配给兼职
		int res = weixinDao.modifyOneOfUnProcess();
		List<TaskDisplayEntity> taskDisplay = weixinDao.getUnCompleteAdvisoryForJZ();
		if (taskDisplay.size() > 0) {
			if (res > 0) {
				// 给微信发送信息
				WeixinHelper helper = new WeixinHelper();
				ServiceMessage message = new ServiceMessage();
				message.setTouser(taskDisplay.get(0).getOpen_id());
				String ipAndPort = PropertiesUtil.ppsConfig.getProperty("SERVER_IP") + ":"
						+ PropertiesUtil.ppsConfig.getProperty("SERVER_PORT");
				List<PlatformUserEntity> platformUserList = weixinDao
						.getPlatformUsersByID(taskDisplay.get(0).getService_id());
				if (platformUserList.size() > 0) {
					String url = platformUserList.get(0).getName();
					// 根据OPENID查找是哪个微信公众平台
					List<Map<String, Object>> appTypeList = weixinDao
							.getTaskQueueByOpenID(taskDisplay.get(0).getOpen_id());
					String str = MessageHelper.getInstance().getMessage("SERVICE_MESSAGE", new String[] { url });
					message.setText(str);
					// 在消息表中插入一条数据
					Map<String, Object> taskQueue = new HashMap<String, Object>();
					taskQueue.put("task_queue_id", taskDisplay.get(0).getId());
					taskQueue.put("openID", taskDisplay.get(0).getOpen_id());
					taskQueue.put("service_id", taskDisplay.get(0).getService_id());
					weixinDao.addSystemMessage(taskQueue, str);
					SendMessage2Takeda.testPost(PropertiesUtil.ppsConfig.getProperty("TAKEDA_URL"), message);
				}
				// 更新消息列表
				weixinDao.modifyTaskMessage(taskDisplay);
			}
		}
		// 返回一个该客服的处理中和被分配的数据
		return taskDisplay;
	}

	/**
	 * 取得未分配数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getUnProcessCount() throws Exception {
		return weixinDao.getUnProcessTask().size();
	}

	/**
	 * 微信用户进行评价
	 * 
	 * @param task_queue_id
	 * @param service_score
	 * @throws Exception
	 */
	public int appraise(String task_queue_id, String open_id, Integer service_score, String asses) throws Exception {
		return weixinDao.appraise(task_queue_id, open_id, service_score, asses);
	}

	/**
	 * 根据ID查找任务列表
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TaskQueueEntity getTaskQueueByID(String id) throws Exception {
		return weixinDao.getTaskQueueByID(id).get(0);
	}

	/**
	 * 取得文章类型表ID取得文章
	 * 
	 * @return
	 */
	public List<ArticleCatagoryEntity> getHelpList() {
		List<ArticleCatagoryEntity> articleCatagoryList = new ArrayList<ArticleCatagoryEntity>();
		// 取得文章类型表数据
		List<Map<String, Object>> list = weixinDao.getAllArticleCatagories();
		for (int i = 0; i < list.size(); i++) {
			// 取得文章类型表ID取得文章
			List<ArticleEntity> articList = weixinDao.getArticleList(list.get(i).get("id").toString());
			ArticleCatagoryEntity articleCatagory = new ArticleCatagoryEntity();
			articleCatagory.setCatagory_id(list.get(i).get("id").toString());
			articleCatagory.setName(list.get(i).get("name").toString());
			articleCatagory.setArticleList(articList);
			articleCatagoryList.add(articleCatagory);
		}
		return articleCatagoryList;
	}

	/**
	 * 查找子类列表
	 * 
	 * @param sectionID
	 * @return
	 */
	public List<ArticleCatagoryListEntity> getTwoLevelMenuList(String sectionID) {
		List<ArticleCatagoryListEntity> articleCatagoryListEntityList = new ArrayList<ArticleCatagoryListEntity>();
		List<Map<String, Object>> list = weixinDao.getAllArticleCatagoriesBySectionID(sectionID);
		for (int i = 0; i < list.size(); i++) {
			ArticleCatagoryListEntity articleCatagoryListEntity = new ArticleCatagoryListEntity();
			articleCatagoryListEntity.setCatagory_id(list.get(i).get("id").toString());
			articleCatagoryListEntity.setName(list.get(i).get("name").toString());
			// 查找子类节点下还有没有标题
			List<ArticleEntity> articList = weixinDao.getArticleList(list.get(i).get("id").toString());
			// 有，显示标题列表
			if (articList.size() > 0) {
				if (!articList.get(0).getTitle().equals(list.get(i).get("name").toString())) {// 有标题
					articleCatagoryListEntity.setHerf_path(PropertiesUtil.ppsConfig.getProperty("SERVER_IP") + "/"
							+ PropertiesUtil.ppsConfig.getProperty("PROJECT_NAME") + "/"
							+ "weixin!getTitleList?CatagoryID=" + list.get(i).get("id").toString() + "&CatagoryName="
							+ list.get(i).get("name").toString());

				} else {// 标题相同
					articleCatagoryListEntity.setHerf_path(PropertiesUtil.ppsConfig.getProperty("SERVER_IP") + "/"
							+ PropertiesUtil.ppsConfig.getProperty("PROJECT_NAME") + "/"
							+ "weixin!getContent?ArticleID=" + articList.get(0).getId());
				}
			} else {// 没有，显示内容，并记录阅读数
				articleCatagoryListEntity.setHerf_path(PropertiesUtil.ppsConfig.getProperty("SERVER_IP") + "/"
						+ PropertiesUtil.ppsConfig.getProperty("PROJECT_NAME") + "/"
						+ "weixin!getContent?ArticleID=aaaaaa");
			}
			// 返回子类列表
			articleCatagoryListEntityList.add(articleCatagoryListEntity);
		}
		return articleCatagoryListEntityList;
	}

	/**
	 * 根据ID查找菜单名
	 * 
	 * @param sectionID
	 * @return
	 */
	public String getSectionsNameByID(String sectionID) {
		List<Map<String, Object>> list = weixinDao.getSectionsNameByID(sectionID);
		return list.get(0).get("name").toString();
	}

	/**
	 * 根据ID查找题目列表
	 * 
	 * @param catagoryID
	 * @return
	 */
	public List<ArticleEntity> getTitleList(String catagoryID) {
		return weixinDao.getArticleList(catagoryID);
	}

	/**
	 * 根据ID取得文章
	 * 
	 * @param id
	 * @return
	 */
	public ArticleEntity getArticleByID(String id) {
		if (weixinDao.getArticleByID(id).size() > 0) {
			return weixinDao.getArticleByID(id).get(0);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @return
	 */
	public int getReadNumber(String ArticleID) throws Exception {
		// 更新阅读数
		weixinDao.updateReadNumber(ArticleID);
		// 查找阅读数
		return weixinDao.getReadNumber(ArticleID);
	}

	/**
	 * 取得当日用户请求数量
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getRequestCount() throws Exception {
		return weixinDao.getRequestCount().size();
	}

	/**
	 * 取得当日活跃用户数量
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getUserActiveCount() throws Exception {
		return weixinDao.getUserActiveCount().size();
	}

	/**
	 * 根据状态取得当日任务的数量
	 */
	public int getTodayCountByStatus() throws Exception {
		return weixinDao.getTodayCountByStatus(TaskStatus.UN_PROCESS).size();
	}

	/**
	 * 9点当日任务的数量
	 */
	public int getTaskCount1() throws Exception {
		return weixinDao.getTaskCountByTime(9).size();
	}

	/**
	 * 10点当日任务的数量
	 */
	public int getTaskCount2() throws Exception {
		return weixinDao.getTaskCountByTime(10).size();
	}

	/**
	 * 11点当日任务的数量
	 */
	public int getTaskCount3() throws Exception {
		return weixinDao.getTaskCountByTime(11).size();
	}

	/**
	 * 12点当日任务的数量
	 */
	public int getTaskCount4() throws Exception {
		return weixinDao.getTaskCountByTime(12).size();
	}

	/**
	 * 13点当日任务的数量
	 */
	public int getTaskCount5() throws Exception {
		return weixinDao.getTaskCountByTime(13).size();
	}

	/**
	 * 14点当日任务的数量
	 */
	public int getTaskCount6() throws Exception {
		return weixinDao.getTaskCountByTime(14).size();
	}

	/**
	 * 15点当日任务的数量
	 */
	public int getTaskCount7() throws Exception {
		return weixinDao.getTaskCountByTime(15).size();
	}

	/**
	 * 16点当日任务的数量
	 */
	public int getTaskCount8() throws Exception {
		return weixinDao.getTaskCountByTime(16).size();
	}

	/**
	 * 17点当日任务的数量
	 */
	public int getTaskCount9() throws Exception {
		return weixinDao.getTaskCountByTime(17).size();
	}

	/**
	 * 18点当日任务的数量
	 */
	public int getTaskCount10() throws Exception {
		return weixinDao.getTaskCountByTime(18).size();
	}

	/**
	 * 取得当日客服状态
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getServiceStatus() throws Exception {
		// Map<String, Object> map = weixinDao.getServiceStatus();

		Map<String, Object> map = weixinDao.getServiceStatusOnline(sessionRegistry.getAllPrincipals());

		List<String> idList = (List<String>) map.get("idList");
		List<String> nameList = (List<String>) map.get("nameList");
		List<String> onlineNameList = new ArrayList<String>();
		List<String> onlineIdList = new ArrayList<String>();

		List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

		for (int j = 0; j < allPrincipals.size(); j++) {
			Object obj = allPrincipals.get(j);
			onlineIdList.add(((PlatformUserEntity) obj).getId());
			onlineNameList.add(((PlatformUserEntity) obj).getName());
		}

		String offline = "(off line)";
		String busy = "(busy)";
		String noaction = "(no action)";
		HttpServletRequest request = ServletActionContext.getRequest();
		Locale locale = (Locale) request.getSession().getAttribute("lang");
		if (locale != null) {
			ActionContext.getContext().setLocale(locale);
			if (locale.toString().equals("en_US")) {
				offline = "(off line)";
				busy = "(busy)";
				noaction = "(no action)";
			} else {
				offline = "(离线)";
				busy = "(忙碌)";
				noaction = "(无动作)";
			}
		}

		for (int i = 0; i < idList.size(); i++) {
			if (!onlineIdList.contains(idList.get(i))) {
				nameList.set(i, nameList.get(i) + offline);
			}
		}

		for (int i = 0; i < idList.size(); i++) {
			for (int j = 0; j < allPrincipals.size(); j++) {
				Object obj = allPrincipals.get(j);
				if (obj instanceof PlatformUserEntity) {
					if (((PlatformUserEntity) obj).getType() == UserType.SERVICE_STANDARD
							&& ((PlatformUserEntity) obj).getId().equals(idList.get(i))) {
						if (((PlatformUserEntity) obj).getBusy() == 1) {
							nameList.set(i, nameList.get(i) + busy);
							break;
						}
					}
				}
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int noActionWarning = Integer.parseInt(ParamsConfig.getParams().get("no_action_warning").getParam_value());
		Date now = new Date();
		for (int i = 0; i < idList.size(); i++) {
			if (onlineIdList.contains(idList.get(i))) {
				int dateDiff1 = 60;
				int dateDiff2 = 60;
				int dateDiff3 = 60;
				String serviceId = idList.get(i);
				String lastMessageTime = weixinDao.getLastMessage(serviceId);
				if (lastMessageTime != null) {
					Date date1 = sdf.parse(weixinDao.getLastMessage(serviceId));
					dateDiff1 = (int) ((now.getTime() - date1.getTime()) / (1000 * 60));
				}

				String lastOperateQueenTime = weixinDao.getLastOperateTime(serviceId);
				if (lastOperateQueenTime != null) {
					Date date2 = sdf.parse(weixinDao.getLastOperateTime(serviceId));
					dateDiff2 = (int) ((now.getTime() - date2.getTime()) / (1000 * 60));
				}

				String saveTime = weixinDao.getLastSaveTime(serviceId);
				if (saveTime != null) {
					Date date3 = sdf.parse(weixinDao.getLastSaveTime(serviceId));
					dateDiff3 = (int) ((now.getTime() - date3.getTime()) / (1000 * 60));
				}

				if (dateDiff1 > noActionWarning && dateDiff2 > noActionWarning && dateDiff3 > noActionWarning)
					nameList.set(i, nameList.get(i) + noaction);
			}
		}

		return map;
	}

	/**
	 * 个人月数据统计
	 */
	public Map<String, Object> getPersonalMonthDataStatistics(String service_id, String date) throws Exception {
		return weixinDao.getPersonalMonthDataStatistics(service_id, date);
	}

	/**
	 * 日请求类型
	 * 
	 * @return
	 */
	public List<Object> searchRequestTypes() throws Exception {
		return weixinDao.searchRequestTypes();
	}

	/**
	 * 获取接单关单状态
	 */
	public void getCurrentStatus() {

	}

	/**
	 * 流水
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getRequestInfoList() throws Exception {
		return weixinDao.getRequestInfoList();
	}

	/**
	 * 正在咨询的数量
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getTaskQueueCountByServiceID() throws Exception {
		return weixinDao.getTaskQueueCountByServiceID().size();
	}

	/**
	 * 获取请求类型tree数据
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getRequestType() {
		List<HashMap<String, String>> list = weixinDao.getRequestType();
		Map<String, HashMap<String, Object>> tempMap = new HashMap<String, HashMap<String, Object>>();
		for (HashMap<String, String> requestType : list) {
			HashMap<String, Object> treeRequestType = new HashMap<String, Object>();
			treeRequestType.put("id", requestType.get("id"));
			treeRequestType.put("text", requestType.get("description"));
			treeRequestType.put("stat", "closed");
			treeRequestType.put("parentId", requestType.get("parentId"));
			treeRequestType.put("children", new ArrayList());
			tempMap.put(requestType.get("id"), treeRequestType);
		}

		List returnList = new ArrayList();
		Iterator iter = tempMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, HashMap<String, String>> entry = (Map.Entry<String, HashMap<String, String>>) iter.next();
			HashMap<String, Object> tmpTreeType = tempMap.get(entry.getValue().get("parentId"));
			if (tmpTreeType != null) {
				((List) tmpTreeType.get("children")).add(entry.getValue());
			} else {
				returnList.add(entry.getValue());
			}

		}

		return returnList;
	}

	public int queryServiceCount() {
		return weixinDao.queryServiceCount();
	}

	public List<HashMap<String, Object>> queryService(int offset, int rows) {
		return weixinDao.queryService(offset, rows);
	}

	public List<HashMap<String, Object>> getCurrentLeader(String serviceId) {
		return weixinDao.getCurrentLeader(serviceId);
	}

	public void transferTask(String taskID, String leaderIds, String transferToId) {
		JSONArray jsonArrayleaderIds = JSONArray.fromObject(leaderIds);
		JSONArray jsonArraytransferToId = JSONArray.fromObject(transferToId);
		JSONArray jsonArraytaskID = JSONArray.fromObject(taskID);

		weixinDao.transferTask(jsonArraytaskID, jsonArrayleaderIds, jsonArraytransferToId);
	}

	public Object getTransferingTaskCount() {
		return weixinDao.getTransferingTaskCount();
	}

	public List<HashMap<String, Object>> getTransferingTaskList(int offset, int rows) {
		return weixinDao.getTransferingTaskList(offset, rows);
	}

	public void syncAcceptTransferTask(String id, String taskId, String transferToId) {
		JSONArray ids = JSONArray.fromObject(id);
		JSONArray taskIds = JSONArray.fromObject(taskId);
		JSONArray toUserIds = JSONArray.fromObject(transferToId);
		weixinDao.cancleTransfer(ids);
		weixinDao.acceptTransferTask(taskIds, toUserIds);
	}

	/**
	 * 查找KID
	 * 
	 * @param kid
	 * @return
	 */
	public Map<String, Object> sreachKData(String kid) {
		return weixinDao.sreachKData(kid);
	}

	/**
	 * 更改客服繁忙状态位
	 * 
	 * @param busyStatus
	 * @return
	 * @throws Exception
	 */
	public int updatePlatfromBusyStatus(String busyStatus) throws Exception {
		return weixinDao.updatePlatfromBusyStatus(busyStatus);
	}

	/**
	 * 根据ID查找客服繁忙状态
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int getPlatfromBusyStatus(String id) throws Exception {
		return weixinDao.getPlatfromBusyStatus(id);
	}

	public void addFileBodyPart(String paramName, ArrayList<FileBody> fileBodys, MultipartEntity reqEntity) {
		if (fileBodys == null || fileBodys.size() < 1 || reqEntity == null || paramName == null) {
			return;
		}
		for (FileBody iteam : fileBodys) {
			reqEntity.addPart(paramName, iteam);
		}
	}

	/**
	 * 给武田AI发送图片和openid
	 * 
	 * @param media
	 * @param openId
	 */
	public void sendImageTextFile(File media, String mediaFileName, String openId) {

		// 1:创建一个httpclient对象
		HttpClient httpclient = new DefaultHttpClient();
		Charset charset = Charset.forName("UTF-8");// 设置编码
		try {
			// 2：创建http的发送方式对象，是GET还是post
			HttpPost httppost = new HttpPost(PropertiesUtil.ppsConfig.getProperty("TAKEDA_IMG_URL"));

			// 3：创建要发送的实体，就是key-value的这种结构，借助于这个类，可以实现文件和参数同时上传，很简单的。
			MultipartEntity reqEntity = new MultipartEntity();

			FileBody bin = new FileBody(media);
			// FileBody bin1 = new FileBody(new File(""));
			StringBody touser = new StringBody(openId, charset);
			StringBody mediafilename = new StringBody(mediaFileName, charset);
			ArrayList<FileBody> fileBodys = new ArrayList<>();
			fileBodys.add(bin);
			// fileBodys.add(bin1);

			addFileBodyPart("upLoadImage", fileBodys, reqEntity);
			reqEntity.addPart("openId", touser);
			reqEntity.addPart("mediaFileName", mediafilename);
			httppost.setEntity(reqEntity);

			// 4：执行httppost对象，从而获得信息
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();

			// 获得返回来的信息，转化为字符串string
			String resString = EntityUtils.toString(resEntity);
			System.out.println(resString);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception ignore) {
			}
		}

	}
}
