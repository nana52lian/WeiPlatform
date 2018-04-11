package com.yidatec.weixin.entity.message;

/**
 * 微信平台传来的消息
 * @author Lance
 *
 */
public class InMessage {

	/** 应用TOKEN ID */
	private String cid;
	
	/** 开发者微信号 */
	private String ToUserName;
	
	/** 发送方帐号（一个OpenID） */
	private String FromUserName;
	
	/** 消息创建时间 （整型） */
	private Long CreateTime;
	
	/**
	 * image 	: 图片消息
	 * voice	: 语音消息
	 * video	: 视频消息
	 * location	: 地理位置消息
	 * link		: 链接消息
	 */
	private String MsgType = "";
	
	/** 消息id，64位整型 */
	private Long MsgId;
	
	/** 文本消息内容 */
	private String Content;
	
	/** 图片消息链接 */
	private String PicUrl;
	
	/** 【图片、语音、视频】消息媒体id，可以调用多媒体文件下载接口拉取数据 */
	private String MediaId;
	
	/** 语音格式，如amr，speex等 */
	private String Format;
	
	/** 语音识别结果，UTF8编码 */
	private String Recognition;

	/** 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据 */
	private String ThumbMediaId;
	
	/** 地理位置维度 */
	private String Location_X;
	
	/** 地理位置精度 */
	private String Location_Y;
	
	/** 地图缩放大小 */
	private Long Scale;
	
	/** 地理位置信息 */
	private String Label;
	
	/** 消息标题 */
	private String Title;
	
	/** 消息描述 */
	private String Description;
	
	/** 消息链接 */
	private String Url;
	
	/** 事件类型，subscribe(订阅)、unsubscribe(取消订阅) */
	private String Event;
	
	/** 事件KEY值
	 * 扫描带参数二维码事件:qrscene_为前缀，后面为二维码的参数值 
	 */
	private String EventKey;
	
	/** 二维码的ticket，可用来换取二维码图片 */
	private String Ticket;
	
	/** 地理位置纬度 */
	private String Latitude;
	
	/** 地理位置经度 */
	private String Longitude;
	
	/** 地理位置精度 */
	private String Precision;
	
	private String msgsource;
	
	/** 企业号里应用ID */
	private String AgentID;

	public String getAgentID() {
		return AgentID;
	}

	public void setAgentID(String agentID) {
		AgentID = agentID;
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public Long getMsgId() {
		return MsgId;
	}

	public void setMsgId(Long msgId) {
		MsgId = msgId;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}
	
	public String getRecognition() {
		return Recognition;
	}

	public void setRecognition(String recognition) {
		Recognition = recognition;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}

	public String getLocation_X() {
		return Location_X;
	}

	public void setLocation_X(String location_X) {
		Location_X = location_X;
	}

	public String getLocation_Y() {
		return Location_Y;
	}

	public void setLocation_Y(String location_Y) {
		Location_Y = location_Y;
	}

	public Long getScale() {
		return Scale;
	}

	public void setScale(Long scale) {
		Scale = scale;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getEvent() {
		return Event;
	}

	public void setEvent(String event) {
		Event = event;
	}

	public String getEventKey() {
		return EventKey;
	}

	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}

	public String getTicket() {
		return Ticket;
	}

	public void setTicket(String ticket) {
		Ticket = ticket;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getPrecision() {
		return Precision;
	}

	public void setPrecision(String precision) {
		Precision = precision;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getMsgsource() {
		return msgsource;
	}

	public void setMsgsource(String msgsource) {
		this.msgsource = msgsource;
	}
	
}
