package com.yidatec.weixin.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class that returns a encrypted string
 *
 * @version
 * @author lance
 */

public class Encrypt {
	
    /**
     * encrypt password
     * @param str
     * @return
     */
	public static String encryptPwd(String str) {
		try {
			byte[] tdesKeyData = {
					(byte)0xA2, (byte)0x15, (byte)0x37, (byte)0x07, (byte)0xCB, (byte)0x62, 
					(byte)0xC1, (byte)0xD3, (byte)0xF8, (byte)0xF1, (byte)0x97, (byte)0xDF,
					(byte)0xD0, (byte)0x13, (byte)0x4F, (byte)0x79, (byte)0x01, (byte)0x67, 
					(byte)0x7A, (byte)0x85, (byte)0x94, (byte)0x16, (byte)0x31, (byte)0x92 };


			byte[] myIV = {(byte)50,(byte)51,(byte)52,(byte)53,(byte)54,(byte)55,(byte)56,(byte)57};

			Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
			IvParameterSpec ivspec = new IvParameterSpec(myIV);

			c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
			byte[] utf8 = str.getBytes("UTF8");
			byte[] enc = c3des.doFinal(utf8);
			return new sun.misc.BASE64Encoder().encode(enc);
		} catch (javax.crypto.BadPaddingException e) {}
		catch (javax.crypto.IllegalBlockSizeException e) {}
		catch (java.io.IOException e) {}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
    /**
     * decrypt password
     * @param str
     * @return
     */
	public static String decryptPwd(String str) {
		try {
			if (str == null)
				return "";

			byte[] tdesKeyData = {
					(byte)0xA2, (byte)0x15, (byte)0x37, (byte)0x07, (byte)0xCB, (byte)0x62, 
					(byte)0xC1, (byte)0xD3, (byte)0xF8, (byte)0xF1, (byte)0x97, (byte)0xDF,
					(byte)0xD0, (byte)0x13, (byte)0x4F, (byte)0x79, (byte)0x01, (byte)0x67, 
					(byte)0x7A, (byte)0x85, (byte)0x94, (byte)0x16, (byte)0x31, (byte)0x92 };


			byte[] myIV = {(byte)50,(byte)51,(byte)52,(byte)53,(byte)54,(byte)55,(byte)56,(byte)57};

			Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
			IvParameterSpec ivspec = new IvParameterSpec(myIV);

			c3des.init(Cipher.DECRYPT_MODE, myKey, ivspec);

			// Decode base64 to get bytes
			byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

			// Decrypt
			byte[] utf8 = c3des.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, "UTF8");
		} catch (javax.crypto.BadPaddingException e) {
		} catch (javax.crypto.IllegalBlockSizeException e) {
		}
		catch (java.io.IOException e) {
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加密的时候用到了用户名作为salt
	 * 所以解密的时候要用用户名解
	 * @param str
	 * @param salt
	 * @return
	 */
	public static String decryptPwdWithSalt(String str, String salt) {		
		return decryptPwd(str).replaceFirst(salt, "");
	}
	
    /**
     * encrypt password
     * @param str
     * @param password
     * @return
     */
	public static String encryptPwd(String str, String password) {
		try {
			/* The logic followed is: 
			 * The password can be 8-12 chars in length
			 * Suppose the length of password is 8 chars: numOfCharsToBeAppended = (24-8)-8  = 8
			 * 								  is 9 chars: numOfCharsToBeAppended = (24-9)-9 = 6
			 * 								  is 10 chars:numOfCharsToBeAppended = (24-10)-10 = 4
			 */
			int numOfCharsToBeAppended = 24 - password.length(); //16 for password length 8
			int numOfCharsRemaining = numOfCharsToBeAppended - password.length(); //16 - 8 = 8
		
			String strPadding = password + password + password.substring(0, numOfCharsRemaining);;
			
			byte[] tdesKeyData = strPadding.getBytes();
			String strMyIV = password.substring(0, 8);			
			byte[] myIV = strMyIV.getBytes();
									
			Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
			IvParameterSpec ivspec = new IvParameterSpec(myIV);

			c3des.init(Cipher.ENCRYPT_MODE, myKey, ivspec);
			byte[] utf8 = str.getBytes("UTF8");
			byte[] enc = c3des.doFinal(utf8);
			return new sun.misc.BASE64Encoder().encode(enc);

		} catch (javax.crypto.BadPaddingException e) {}
		catch (javax.crypto.IllegalBlockSizeException e) {}
		catch (java.io.IOException e) {}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
    /**
     * decrypt password
     * @param str
     * @param password
     * @return
     */
	public static String decryptPwd(String str, String password) {
		try {			
			/* The logic followed is: 
			 * The password can be 8-12 chars in length
			 * Suppose the length of password is 8 chars: numOfCharsToBeAppended = (24-8)-8  = 8
			 * 								  is 9 chars: numOfCharsToBeAppended = (24-9)-9 = 6
			 * 								  is 10 chars:numOfCharsToBeAppended = (24-10)-10 = 4
			 */
			int numOfCharsToBeAppended = 24 - password.length(); //16 for password length 8
			int numOfCharsRemaining = numOfCharsToBeAppended - password.length(); //16 - 8 = 8
		
			String strPadding = password + password + password.substring(0, numOfCharsRemaining);;
			
			byte[] tdesKeyData = strPadding.getBytes();
			String strMyIV = password.substring(0, 8);			
			byte[] myIV = strMyIV.getBytes();
									
			Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
			SecretKeySpec myKey = new SecretKeySpec(tdesKeyData, "DESede");
			IvParameterSpec ivspec = new IvParameterSpec(myIV);

			c3des.init(Cipher.DECRYPT_MODE, myKey, ivspec);
			
			// Decode base64 to get bytes
			byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

			// Decrypt
			byte[] utf8 = c3des.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, "UTF8");
		} catch (javax.crypto.BadPaddingException e) {
		} catch (javax.crypto.IllegalBlockSizeException e) {
		}
		catch (java.io.IOException e) {
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public static void main(String args[]) {
		// 00000a 是密码，guest作为salt
		// System.out.println(Encrypt.encryptPwd("m00100000a"));
		System.out.println(java.util.UUID.randomUUID().toString());
	}
}
