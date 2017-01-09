package com.egg.common.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.egg.common.log.LogKit;

public class EncryptUtil {

	public static final String PASSWORD_KEY = "zxc!23";

	/**
	 * 加密密码
	 * @param passwd 密码明文
	 * @return md5(md5(passwd) + PASSWORD_KEY);
	 */
	public static String passwd(String passwd) {
		if (null == passwd) {
			return "";
		}
		return md5(new StringBuffer(md5(passwd)).append(PASSWORD_KEY).toString());
	}

	/**
	 * 验证密码
	 * @param passwd 原密码
	 * @param encryptPwd 加密密码
	 */
	public static boolean verifyPasswd(String passwd, String encryptPwd) {
		String password = passwd(passwd);
		if (password == null || password.isEmpty()) {
			return false;
		}
		return password.equalsIgnoreCase(encryptPwd);
	}

	/**
	 * MD5加密
	 * @param str 要加密的字符串
	 */
	public static String md5(String str) {
		try {
			if (null == str) {
				return "";
			}
			byte[] bs = str.getBytes("UTF-8");
			return md5(bs);
		} catch (Exception e) {
			LogKit.error("md5方法出现异常", e);
			return "";
		}
	}

	/**
	 * MD5加密
	 * @param bs 要加密的字节数组
	 */
	public static String md5(byte[] bs) {
		try {
			if (null == bs) {
				return "";
			}
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bs);
			byte[] digest = md.digest();
			return Hex.encodeHexString(digest).toLowerCase();
		} catch (Exception e) {
			LogKit.error("md5方法出现异常", e);
			return "";
		}
	}

	/**
	 * DES加密，密文转成Base64
	 * @param str 要加密的字符串
	 * @param keyStr 密钥值，使用 keyStr 中的前 8 个字节作为 DES 密钥的密钥内容
	 */
	public static String encodeDES(String str, String keyStr) {
		try {
			if (null == str || null == keyStr) {
				return "";
			}

			// 生成KEY
			byte[] key_bs = keyStr.getBytes();
			DESKeySpec dks = new DESKeySpec(key_bs);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
			SecretKey key = factory.generateSecret(dks);

			// 加密
			// 生成Cipher对象，使用DES算法，使用ECB加密方式，使用PKCS5算法进行填充。
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, new SecureRandom());
			byte[] bs = cipher.doFinal(str.getBytes("UTF-8"));
			return Base64.encodeBase64String(bs).trim();
		} catch (Exception e) {
			LogKit.error(EncryptUtil.class + ".encodeDES():方法出现异常:" + e.getMessage(), e);
			return "";
		}
	}

	/**
	 * DES解密
	 * @param str 密文
	 * @param keyStr 密钥值
	 */
	public static String decodeDES(String str, String keyStr) {
		try {
			if (null == str || null == keyStr) {
				return "";
			}

			// 生成KEY
			byte[] key_bs = keyStr.getBytes();
			DESKeySpec dks = new DESKeySpec(key_bs);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
			SecretKey key = factory.generateSecret(dks);

			// 解密
			// 生成Cipher对象，使用DES算法，使用ECB加密方式，使用PKCS5算法进行填充。
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new SecureRandom());
			byte[] bs = cipher.doFinal(Base64.decodeBase64(str));
			return new String(bs, "UTF-8");
		} catch (Exception e) {
			LogKit.error(EncryptUtil.class + ".decodeDES():方法出现异常:" + e.getMessage(), e);
			return "";
		}
	}

	public static String sha1(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes("UTF-8"));
			byte[] digest = md.digest();
			return Hex.encodeHexString(digest).toLowerCase();
		} catch (Exception e) {
			LogKit.error(EncryptUtil.class + ".sha1():方法出现异常:" + e.getMessage(), e);
			return "";
		}
	}

}
