package com.egg.ecommon.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class URLTool {

	private static final Log LOG = LogFactory.getLog(URLTool.class);
	private static final String UTF8 = "UTF-8";
	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final int TIMEOUT = 1000 * 30;
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 5.1; rv:24.0) Gecko/20100101 Firefox/24.0";

	public static String doGet(String urlStr, List<URLParam> params, Map<String, String> headers) {
		boolean isOpened = false;
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		try {
			// 请求参数信息
			String paramsStr = _buildParamsStr(params, UTF8);
			if (paramsStr != null && paramsStr.length() > 0) {
				int ind = urlStr.lastIndexOf('?');
				if (ind == -1) {
					urlStr += "?";
				} else if (ind < (urlStr.length() - 1)) {
					urlStr += "&";
				}
				urlStr += paramsStr;
			}

			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(GET);
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(TIMEOUT);

			// 请求头信息
			conn.addRequestProperty("User-Agent", USER_AGENT);
			if (headers != null && !headers.isEmpty()) {
				Set<String> keys = headers.keySet();
				for (String key : keys) {
					conn.addRequestProperty(key, headers.get(key));
				}
			}

			conn.connect();
			isOpened = true;
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF8));
			String str = null;
			StringBuffer sb = new StringBuffer();
			while ((str = reader.readLine()) != null) {
				sb.append(str).append("\r\n");
			}
			return sb.toString();
		} catch (Exception e) {
			LOG.error(".doGet():方法异常:", e);
			return "";
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.error(".doGet():关闭BufferedReader方法异常:", e);
				}
			}
			if (isOpened) {
				conn.disconnect();
			}
		}
	}

	public static String doPost(String urlStr, List<URLParam> params, Map<String, String> headers) {
		boolean isOpened = false;
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(POST);
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(TIMEOUT);
			
			// 请求头信息
			conn.addRequestProperty("User-Agent", USER_AGENT);
			if (headers != null && !headers.isEmpty()) {
				Set<String> keys = headers.keySet();
				for (String key : keys) {
					conn.addRequestProperty(key, headers.get(key));
				}
			}
			
			// 请求参数信息
			String paramsStr = _buildParamsStr(params, UTF8);
			if (paramsStr != null && paramsStr.length() > 0) {
				conn.setDoOutput(true);
				conn.getOutputStream().write(paramsStr.getBytes(UTF8));
				conn.getOutputStream().flush();
				conn.getOutputStream().close();
			}
			
			conn.connect();
			isOpened = true;
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF8));
			String str = null;
			StringBuffer sb = new StringBuffer();
			while ((str = reader.readLine()) != null) {
				sb.append(str);//.append("\r\n");
			}
			return sb.toString();
		} catch (Exception e) {
			LOG.error(".doPost():方法异常:", e);
			return "";
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.error(".doPost():关闭BufferedReader方法异常:", e);
				}
			}
			if (isOpened) {
				conn.disconnect();
			}
		}
	}
	
	public static String doPost(String urlStr, String dataStr, Map<String, String> headers) {
		boolean isOpened = false;
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(POST);
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(TIMEOUT);

			// 请求头信息
			conn.addRequestProperty("User-Agent", USER_AGENT);
			if (headers != null && !headers.isEmpty()) {
				Set<String> keys = headers.keySet();
				for (String key : keys) {
					conn.addRequestProperty(key, headers.get(key));
				}
			}

			// 请求参数信息
			if (dataStr != null && dataStr.length() > 0) {
				conn.setDoOutput(true);
				conn.getOutputStream().write(dataStr.getBytes(UTF8));
				conn.getOutputStream().flush();
				conn.getOutputStream().close();
			}

			conn.connect();
			isOpened = true;
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF8));
			String str = null;
			StringBuffer sb = new StringBuffer();
			while ((str = reader.readLine()) != null) {
				sb.append(str);//.append("\r\n");
			}
			return sb.toString();
		} catch (Exception e) {
			LOG.error(".doPost():方法异常:", e);
			return "";
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.error(".doPost():关闭BufferedReader方法异常:", e);
				}
			}
			if (isOpened) {
				conn.disconnect();
			}
		}
	}

	private static String _buildParamsStr(List<URLParam> params, String encoding) throws UnsupportedEncodingException {
		if (params == null || params.isEmpty()) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (URLParam param : params) {
			if (param.getKey() == null) {
				continue;
			}
			if (!first) {
				sb.append("&");
			}
			first = false;

			sb.append(URLEncoder.encode(param.getKey(), encoding));
			sb.append("=");
			if (param.getValue() != null) {
				sb.append(URLEncoder.encode(param.getValue(), encoding));
			}
		}

		return sb.toString();
	}

}
