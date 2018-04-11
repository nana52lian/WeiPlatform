package com.yidatec.weixin.entity.message;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 发送客服消息
 * @author Lance
 *
 */
public class ServiceMessage {

	/** 普通用户openid */
	private String touser = null;
	/** 消息类型 */
	private String msgtype = MessageType.TEXT;
	/** 文本消息 */
	private Map<String, String> text = new HashMap<String, String>();
	/** 图片消息 */
	private Map<String, String> image = new HashMap<String, String>();
	/** 语音消息 */
	private Map<String, String> voice = new HashMap<String, String>();
	/** 视频消息 */
	private Map<String, String> video = new HashMap<String, String>();
	/** 音乐消息 */
	private Map<String, String> music = new HashMap<String, String>();
	/** 图文消息 */
	private Map<String, String> news = new HashMap<String, String>();
	
    private String agentid;
    
	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public Map<String, String> getText() {
		return text;
	}

	public void setText(String text) {		
		this.text.put("content", text);
	}

	public Map<String, String> getImage() {
		return image;
	}

	public void setImage(Map<String, String> image) {
		this.image = image;
	}

	public Map<String, String> getVoice() {
		return voice;
	}

	public void setVoice(Map<String, String> voice) {
		this.voice = voice;
	}

	public Map<String, String> getVideo() {
		return video;
	}

	public void setVideo(Map<String, String> video) {
		this.video = video;
	}

	public Map<String, String> getMusic() {
		return music;
	}

	public void setMusic(Map<String, String> music) {
		this.music = music;
	}

	public Map<String, String> getNews() {
		return news;
	}

	public void setNews(Map<String, String> news) {
		this.news = news;
	}
	
	/**
	 * 转Json字符串
	 * @return
	 */
	public String toJson() {
		return JSONObject.fromObject(this).toString();
	}


	public static void main(String args[]) {
		ServiceMessage s = new ServiceMessage();
		s.setTouser("lance");
		s.setMsgtype(MessageType.TEXT);
		s.getText().put("content", "hello world");
		System.out.println(JSONObject.fromObject(s).toString());
	}
}
