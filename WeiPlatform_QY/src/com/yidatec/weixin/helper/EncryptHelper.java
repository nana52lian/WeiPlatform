package com.yidatec.weixin.helper;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.entity.RequestEntity;
import com.yidatec.weixin.util.Base64;
import com.yidatec.weixin.util.MD5Util;
import com.yidatec.weixin.util.TripleDES;

/**
 * 加解密助手
 * 
 * 前置平台向客户端发送响应报文的加密方法
 * 报文数据域加密算法：3DES
 * 加密报文体格式：
 * 正常响应格式：1|BASE64(3DES(报文))|BASE64(MD5(报文))
 * 错误响应格式：0|错误码|BASE64(错误描述)
 * 报文加密：
 * 前置平台如果正确解析报文，则响应“1”，响应报文使用请求报文中的解密出来的3DES的对称密钥进行加密响应结果报文，
 * 并使用MD5对响应报文原文进行签名，分别对后两段采用BASE64编码，按正常响应格式组织报文，并将加密后的报文传输给客户端。
 * 前置平台如果无法正常解析报文，则响应“0”，带回错误码，并将错误描述采用BASE64编码，按错误响应格式编码，发回客户端。
 * 备注:
 * 3DES使用DESede/ECB/PKCS5Padding组合模式补位。	
 * 
 * ************************************************************************************************************
 * 
 * 客户端向前置平台发送请求报文的加密方法
 * 报文数据域加密算法：RSA，3DES
 * 加密报文体格式：1|BASE64(前置证书版本号)|BASE64(RSA(报文加密密钥))| BASE64(3DES(报文原文))| BASE64(MD5(报文原文))
 * 报文加密密钥生成：由客户端自行生成，不限定生成该对称密钥的具体方法。
 * 报文加密：客户端使用加密密钥和3DES算法对报文内容进行加密。前置平台解密出报文加密密钥后，使用3DES算法解密报文。
 * 报文加密密钥加密：使用前置平台公钥加密报文加密密钥，前置平台收到报文后，使用前置私钥解密出报文加密密钥。
 * MD5摘要生成：使用不可逆算法MD5，对报文原文进行全文MD5，取得32位摘要。
 * 报文体中三段分别用BASE64编码，并使用“|”连接。
 * 备注:
 * RSA使用 RSA/ECB/PKCS1Padding 组合模式补位。
 * 3DES使用DESede/ECB/PKCS5Padding组合模式补位。
 * 
 * @author Lance
 */	
public class EncryptHelper {
	
	private static final Logger log = LogManager.getLogger(EncryptHelper.class);
	
	// 3DES加解密密钥
	private static final byte[] keyBytes = {
		0x21, 0x22, 0x4F, 0x38, (byte)0x78, 0x10, 0x41, 0x28, 
		0x25, 0x15, 0x79, 0x51, (byte)0xCB, (byte)0xDD, 0x55, 
		0x56, 0x77, 0x29, 0x74, (byte)0x96, 0x30, 0x40, 0x37, (byte)0xE2};    //24字节的密钥
	
	/**
	 * 加密成功报文
	 * @param three_des_key
	 * @param source_str
	 * @return
	 */
	public static String encryptSuccessContent(String three_des_key, String source_str) throws Exception {		
		String seq1 = "1";		
		String seq2 = Base64.encodeString(TripleDES.encryptMode(three_des_key.getBytes(), source_str.getBytes()));
		//log.error("Before base64::" + source_str);
		//String seq2 = Base64.encodeString(source_str);
		//log.error("After base64::" + seq2);
		String seq3 = Base64.encodeString(MD5Util.MD5Encode(source_str, "UTF-8"));
		//String rtnVal = URLEncoder.encode(seq1 + "|" + seq2 + "|" + seq3, "UTF-8");
		String rtnVal = seq1 + "|" + seq2 + "|" + seq3;
		System.out.println("Send return value :: " + rtnVal);
		return rtnVal;
	}
	
	/**
	 * 加密失败报文
	 * @param err_code
	 * @param err_msg
	 * @return
	 */
	public static String encryptFailedContent(String err_code, String err_msg) {
		String seq1 = "0";		
		String seq2 = err_code;
		String seq3 = Base64.encodeString(err_msg);
		return seq1 + "|" + seq2 + "|" + seq3;
	}
	
	public static void main(String[] args) {
		System.out.println(Base64.decodeString("eyJkYXRhIjpudWxsLCJlcnJvckluZm8iOiIiLCJlcnJvck5vIjoiMCIsImV2ZW50RGF0YUxpc3QiOlt7ImNvcnBfYmVnaW5fZGF0ZSI6IjIwMTMwNDE1IiwicHJvZmVzc2lvbl9jb2RlIjoiICIsIm1vYmlsZXRlbGVwaG9uZSI6IiAiLCJjb3JwX3Jpc2tfbGV2ZWwiOiIxIiwib3Blbl9kYXRlIjoiMjAwNDA4MTMiLCJjb3JwX2VuZF9kYXRlIjoiMjAxNTA0MTQiLCJvcmdhbl9wcm9wX25hbWUiOiLmnKror4TmtYsiLCJvcmdhbl9wcm9wIjoiMCIsImNsaWVudF9yaWdodHMiOiJXY2VwdCIsImFjY291bnRfZGF0YV9uYW1lIjoi5ZCI5qC86LSm5oi3LOinhOiMgyIsIm1haWxfbmFtZSI6IiAiLCJwaG9uZWNvZGUiOiIwMTExMTExMTExMSIsImVfbWFpbCI6IiAiLCJmdW5kX2FjY291bnQiOiI1MDAwMDAwOCIsIm9yZ2FuX25hbWUiOiLkuKrkuroiLCJjbGllbnRfaWQiOiIwNTAwMDAwMDgiLCJjbGllbnRfbmFtZSI6IjA1MDAwMDAwOCIsImJyYW5jaF9uYW1lIjoiODUwIiwiZnVuZF9jYXJkIjoiNTAwMDAwMDgiLCJncm91cF9uYW1lIjoi5q2j5bi4IiwiYnJhbmNoX25vIjoiODUwIiwiY2xpZW50X2dyb3VwIjoiMTAyMyIsImNsaWVudF9zdGF0dXMiOiIwIiwiZmF4IjoiICIsInppcGNvZGUiOiIgIiwiYWNjb3VudF9kYXRhIjoiQTAiLCJpZF9hZGRyZXNzIjoiICIsIm5hdGlvbmFsaXR5IjoiQ0hOIiwiYWRkcmVzcyI6IiAiLCJwYXBlcl9zY29yZSI6IjAiLCJyaXNrX2luZm8iOiIiLCJyaXNrX25hbWUiOiIiLCJjbGllbnRfZ2VuZGVyIjoiMCIsImlkX2tpbmQiOiIwIiwiaWRfbm8iOiI4NTAwNTAwMDAwMDgiLCJmdWxsX25hbWUiOiIwNTAwMDAwMDgifV0sInJldHVybkNvZGUiOiIwIn0="));
//		String des_key = "123456789012345678901234";
//		String req_data = "{\"client_pub_modulus\":\"123456\"}";
//		String enc = Base64.encodeString(MD5Util.MD5Encode(req_data, "UTF-8"));
//		System.out.println("enc::" + Base64.encodeString(MD5Util.MD5Encode(req_data, "UTF-8")));
	}
	
	/**
	 * 1|BASE64(前置证书版本号)|BASE64(RSA(报文加密密钥))| BASE64(3DES(报文原文))| BASE64(MD5(报文原文))<br/>
	 * 暂时用下面方式解密
	 * 1|BASE64(前置证书版本号)|BASE64(报文加密密钥)| BASE64(3DES(报文原文))| BASE64(MD5(报文原文))<br/>
	 * @param source_str
	 * @return
	 */
	public static RequestEntity decrypt(String source_str) throws Exception {		
		RequestEntity req = new RequestEntity();		
		String[] tmp_dec = source_str.split("\\|");
		// 1. 客户端类型
		String seq1 = tmp_dec[0];
		req.setClient_type(seq1);
		// TODO:后期根据不同的客户端解密方式有变化
		// 2. 公钥证书版本号
		String seq2 = Base64.decodeString(tmp_dec[1]);
		req.setPub_key_ver(seq2);
		// 3. BASE64(报文加密密钥)
		String seq3 = new String(Base64.decode(tmp_dec[2]));
		seq3 = dec3DesKey(seq3);
		req.setThree_des_key(seq3);		
		// 4. BASE64(3DES(报文原文))
		String seq4 = new String(TripleDES.decryptMode(seq3.getBytes(), Base64.decode(tmp_dec[3])));
		req.setJson_string(seq4);
		// 5. BASE64(MD5(报文原文)
		String seq5 = Base64.decodeString(tmp_dec[4]);		
		// 做防串改验证
		if (seq5.equals(MD5Util.MD5Encode(seq4, "UTF-8"))) {
			return req;
		} else {
			return null;
		}		
	}
	
	/**
	 * 解密3DES的Key
	 * @param encKey
	 * @return
	 */
	private static String dec3DesKey(String encKey) {
		// 第一段
		String decKey = encKey.substring(1, 9);
		// 第二段
		decKey = decKey + encKey.substring(20, 28);
		// 第三段
		decKey = decKey + encKey.substring(10, 18);
		return decKey;
	}
}
