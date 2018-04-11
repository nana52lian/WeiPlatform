package com.yidatec.weixin.entity.message;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送给客户的消息
 * @author Lance
 *
 */
public class OutMessage {

	/** 接收方帐号（收到的OpenID） */
	private String ToUserName;
	
	/** 开发者微信号 */
	private String FromUserName;
	
	/** 消息创建时间 （整型） */
	private Long CreateTime;
	
	/** 消息类型<br/>
	 * text		: 回复文本消息<br/>
	 * image	: 回复图片消息<br/>
	 * voice	: 回复语音消息<br/>
	 * video	: 回复视频消息<br/>
	 * music	: 回复音乐消息<br/>
	 * news		: 回复图文消息<br/>
	 */
	private String MsgType = "text";
	
	/** */
	private Long MsgId;
	
	/** 回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示） */
	private String Content;
	
	/** 通过上传多媒体文件，得到的id */
	private String MediaId;

	/** 缩略图的媒体id，通过上传多媒体文件，得到的id */
	private String ThumbMediaId;

	/** */
	private int FuncFlag = 0;

	/** 音乐链接 */
	private String MusicUrl;
	
	/** 高质量音乐链接，WIFI环境优先使用该链接播放音乐 */
	private String HQMusicUrl;

	/** 图文消息个数，限制为10条以内 */
	private Integer ArticleCount;
	
	/** 标题 */
	private String Title;
	
	/** 描述 */
	private String Description;
	
	/** 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200 */
	private String PicUrl;
	
	/** 点击图文消息跳转链接 */
	private String Url;

	/** 多条图文消息信息，默认第一个item为大图 */
	private List<Articles> Articles;

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

	public int getFuncFlag() {
		return FuncFlag;
	}

	public void setFuncFlag(int funcFlag) {
		FuncFlag = funcFlag;
	}

	public String getMusicUrl() {
		return MusicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		MusicUrl = musicUrl;
	}

	public String getHQMusicUrl() {
		return HQMusicUrl;
	}

	public void setHQMusicUrl(String hQMusicUrl) {
		HQMusicUrl = hQMusicUrl;
	}

	public int getArticleCount() {
		if (this.Articles != null && Articles.size() > 0)
			return this.Articles.size();
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
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

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public List<Articles> getArticles() {
		return Articles;
	}

	public void setArticles(List<Articles> articles) {
		if(articles.size()>10)
			articles  = new ArrayList<Articles>(articles.subList(0, 10));
		Articles = articles;
	}
	
	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getThumbMediaId() {
		return ThumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		ThumbMediaId = thumbMediaId;
	}

	public void setArticleCount(Integer articleCount) {
		ArticleCount = articleCount;
	}
}