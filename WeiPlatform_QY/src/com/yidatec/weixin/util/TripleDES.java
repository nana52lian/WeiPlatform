package com.yidatec.weixin.util;

import java.net.URLEncoder;
import java.security.Key;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class TripleDES {

	//定义 加密算法,可用 DES,DESede,Blowfish
	private static final String Algorithm = "DESede"; 
	
	public static final byte[] keyBytes = {
		0x21, 0x22, 0x4F, 0x38, (byte)0x78, 0x10, 0x41, 0x28, 
		0x25, 0x15, 0x69, 0x51, (byte)0xCB, (byte)0xDD, 0x55, 
		0x56, 0x78, 0x29, 0x74, (byte)0x96, 0x31, 0x40, 0x37, (byte)0xE2};    //24字节的密钥
	
	/**
	 * @param keybyte 为加密密钥，长度为24字节
	 * @param src 为被加密的数据缓冲区（源）
	 * @return
	 */
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
       try {
    	   Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    	   KeySpec keySpec = new DESedeKeySpec(keybyte);    	   
    	   Key secretKey = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);
    	   cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    	   return cipher.doFinal(src);
//            //生成密钥
//            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
//            //加密
//            Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
//            c1.init(Cipher.ENCRYPT_MODE, deskey);
//            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * @param keybyte 为加密密钥，长度为24字节
     * @param src 为加密后的缓冲区
     * @return
     */
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {      
    	try {
    		Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
     	   	KeySpec keySpec = new DESedeKeySpec(keybyte);    	   
     	   	Key secretKey = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);
     	   	cipher.init(Cipher.DECRYPT_MODE, secretKey);
     	   	return cipher.doFinal(src);
            //生成密钥
//            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
//            //解密
//            Cipher c1 = Cipher.getInstance(Algorithm);
//            c1.init(Cipher.DECRYPT_MODE, deskey);
//            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    
  	/**
  	 * 转换成十六进制字符串
  	 * @param b
  	 * @return
  	 */
    public static String byte2hex(byte[] b) {
        String hs="";
        String stmp="";

        for (int n=0;n<b.length;n++) {
            stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1) hs=hs+"0"+stmp;
            else hs=hs+stmp;
            if (n<b.length-1)  hs=hs+":";
        }
        return hs.toUpperCase();
    }
    
    public static void main(String[] args) {  
    	try {    		
    		String st = "oEVwAD7Dj6ux/la7P94Frnp/Z7+B0F1r4tFrJQsl97JWB8UkQ572oSoisKEdvEQHWWVuJWrh1PtMENzl/ZM71J5UZR/6hRsAWKDA8GDzmMQhwVa3vW9FUgZe2noZ/qU+Pq4Ber3Vu7hCfWFU2b7jPiFVosbNzqTFnpFV7oRA+QLO/kc2MT1gZRYGo/Fu2sl0uDQDCyLPPlqHNzrJu90FZjw6xAkWsKrW4Qjqx0IPx5E3mZ9J+nU/4rLLG/Subay6jaaQgv5ouK1YoMDwYPOYxHNndSqJC4ZVHN+vGM85E5LeVbY+QcKRp9hCwuGU9Vn7GXcPWxnmERjrvsg8F1yXIIVfo7fe4bLCHOHn8+Mx9ELpY5vKxknojdJKE097zKXfghX/SXoRiDFOWdnEYtAFO/EeBmJKAkkbbQoBobs/PTnYscPlB1ho/+0npGbfZsjbjJFcErcbfd2QPyFImc0vXzSEJA9Rjw9E/1fx659HTulpipkj1/7ayERv+4R4AHGtMoCGU78UmgGonEG84nGp+hqqfjZ+LE+zgo2u6BAWg8gWSsU1ihK0jr+mY+qEZIJn0lf7QekM9rt2AImZxdxHw2PLxKt4qjS6SclRYm9BZ7h6M83GCoWuDD9poc4QluTBXl/hhv0Jmn1vj9ffEDnhfejU0O6vLpJysUNWad3H4xFQk+wqIfJeFnImXIWcuGcQImRJcOuEu4/81ZhWSICFPnl+o0lThwmCfJjwNzb0CkL+YU5bPKABAIVfo7fe4bLCiMDX56fKk6ccfZON0ikjlnOwuaYjyQWL9TdYCG0b9g416imKT/qbqaQLVM4EODNSh7bYHRpCZ2xpAMyZmuzpmhOu7czYlGcFnTHx9q8yFq29cMYVIl/leb9l6IlCyNsMV9k/0A6xZQ1zN2+T3MnQW5Bc7do86UKAvOM3gkGgvkjUbCCXUdWaZ9fp7GQ5X4XsDluYnh5M7XClcWEAI0i/AdhCwuGU9Vn7Li2R0i8+cS948gtZQzI+6o6hKQmQmFNx2XGvyLtT1rZLIdYCqvHS4p/4BsPYIR+Xvoj6LRibzbZCM1Rse8qImb+mY+qEZIJnC8eyVj9uNYjC0VQ7VkD5PMeGU6wwRIYRCRF1h9G99fc=";
    		System.out.println(URLEncoder.encode(st, "UTF-8"));
    		String str = "hello world!中国";
    		String key = "123456789012345678901234";
    		byte[] raw = encryptMode(key.getBytes(), str.getBytes("UTF-8"));    		
    		System.out.println(Base64.encodeString(raw));    	
    		byte[] dec = decryptMode(key.getBytes(), Base64.decode("SdHQCpbVRzlzE65M4lMYPAkdwf/q9mTq"));
			System.out.println(new String(dec, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	
        //添加新安全算法,如果用JCE就要把它添加进去
        
//    	Security.addProvider(new com.sun.crypto.provider.SunJCE());
//        String szSrc = "This is a 3DES test. 测试";
//        
//        System.out.println("加密前的字符串:" + szSrc);
//        
//        byte[] encoded = encryptMode(TripleDES.keyBytes, szSrc.getBytes());        
//        System.out.println("加密后的字符串:" + new String(encoded));
//        
//        byte[] srcBytes = decryptMode(TripleDES.keyBytes, encoded);
//        System.out.println("解密后的字符串:" + (new String(srcBytes)));
    }
}
