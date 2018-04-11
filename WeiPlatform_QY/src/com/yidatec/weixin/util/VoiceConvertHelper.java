package com.yidatec.weixin.util;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;

import java.io.File;

public class VoiceConvertHelper {

	/**
	 * 将amr格式文件转换成mp3文件
	 * 
	 * @param amr_path
	 *            amr文件在服务器的绝对路径
	 * @return mp3文件在服务器的绝对路径
	 */
	public static String amr2mp3(String amr_path) {
		String mp3_path = amr_path.substring(0, amr_path.lastIndexOf("."))
				+ ".mp3";
		System.out.println("mp3_path::" + mp3_path);
		System.out.println("amr_path::" + amr_path);
		File source = new File(amr_path);
		File target = new File(mp3_path);
		AudioAttributes audio = new AudioAttributes();
		// pcm_s16le libmp3lame libvorbis libfaac
		audio.setCodec("libmp3lame");
		Encoder encoder = new Encoder();
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		try {
			encoder.encode(source, target, attrs);
		} catch (Exception e) {}
		return mp3_path;
	}
	
	public static void main(String[] args){
		amr2mp3("c:\\1.amr");
	}
}
