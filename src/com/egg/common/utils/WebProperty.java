package com.egg.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 本类实现对配置文件的读写
 */
public class WebProperty {

	// 属性文件的路径
	static String profilepath = WebProperty.class.getClassLoader().getResource("").getPath() + "web_config.properties";

	/**
	 * 采用静态方法
	 */
	private static Properties props = new Properties();
	static {
		try {
			FileInputStream fin = new FileInputStream(profilepath);
			// 转换成utf-8编码
			InputStreamReader in = new InputStreamReader(fin, "UTF-8");
			props.load(in);

			in.close();
			fin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.exit(-1);
		}
	}

	/**
	 * 读取属性文件中相应键的值
	 * @param key 主键
	 * @return String
	 */
	public static synchronized String getKeyValue(String key) {
		return props.getProperty(key);
	}

	/**
	 * 读取属性文件中相应键的值
	 * @param key 主键
	 * @return int
	 */
	public static synchronized int getKeyValue(String key, int def) {
		String val = props.getProperty(key);
		if (val == null || val.isEmpty()) {
			return def;
		}
		try {
			return Integer.parseInt(val);
		} catch (Exception e) {
			return def;
		}
	}

	/**
	 * 写属性文件中相应键的值
	 * @param key 主键
	 * @return String
	 */
	public static synchronized void setKeyValue(String key, String value) {
		props.setProperty(key, value);
		try {
			FileOutputStream fos = new FileOutputStream(profilepath);
			props.store(fos, null);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.exit(-1);
		}
	}
}
