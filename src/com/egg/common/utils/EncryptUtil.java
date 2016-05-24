package com.egg.common.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum EncryptUtil {
	INSTANCE;

	public static final String PASSWORD_KEY = "zxc!23";
	private static final Logger LOG = LoggerFactory.getLogger(EncryptUtil.class);

	/**
	 * 加密登录密码
	 * @param id 用户ID
	 * @param passwd 密码明文
	 * @return md5(md5(passwd) + Constants.PASSWORD_KEY);
	 */
	public String encryptPasswd(String passwd) {
		if (null == passwd) {
			return "";
		}
		return md5(new StringBuffer(md5(passwd)).append(PASSWORD_KEY).toString());
	}

	public boolean checkPasswd(String passwd, String encryptPwd) {
		String password = encryptPasswd(passwd);
		if (StringUtils.isBlank(password)) {
			return false;
		}
		return password.equalsIgnoreCase(encryptPwd);
	}

	/**
	 * MD5加密
	 * @param str 要加密的字符串
	 * @return
	 */
	public String md5(String str) {
		try {
			if (null == str) {
				return "";
			}
			byte[] bs = str.getBytes("UTF-8");
			return md5(bs);
		} catch (Exception e) {
			LOG.error(EncryptUtil.class + ":.md5():方法出现异常:" + e.getMessage(), e);
			return "";
		}
	}

	/**
	 * MD5加密
	 * @param bs 要加密的字节数组
	 * @return
	 */
	public String md5(byte[] bs) {
		try {
			if (null == bs) {
				return "";
			}
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bs);
			byte[] digest = md.digest();
			return Hex.encodeHexString(digest).toLowerCase();
		} catch (Exception e) {
			LOG.error(EncryptUtil.class + ":.md5():方法出现异常:" + e.getMessage(), e);
			return "";
		}
	}

	/**
	 * DES加密，密文转成Base64
	 * @param str 要加密的字符串
	 * @param keyStr 密钥值，使用 keyStr 中的前 8 个字节作为 DES 密钥的密钥内容
	 * @return
	 */
	public String encodeDES(String str, String keyStr) {
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
			LOG.error(EncryptUtil.class + ".encodeDES():方法出现异常:" + e.getMessage(), e);
			return "";
		}
	}

	/**
	 * DES解密
	 * @param str 密文
	 * @param keyStr 密钥值
	 * @return
	 */
	public String decodeDES(String str, String keyStr) {
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
			LOG.error(EncryptUtil.class + ".decodeDES():方法出现异常:" + e.getMessage(), e);
			return "";
		}
	}

	public String sha1(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes("UTF-8"));
			byte[] digest = md.digest();
			return Hex.encodeHexString(digest).toLowerCase();
		} catch (Exception e) {
			LOG.error(EncryptUtil.class + ".sha1():方法出现异常:" + e.getMessage(), e);
			return "";
		}
	}

}
