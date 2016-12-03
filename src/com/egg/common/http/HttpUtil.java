package com.egg.common.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

	public static void main(String[] args) {
		String urlStr = "https://192.168.0.163/zhibo/admin.jsf";
		List<HttpParam> params = null;
		Map<String, String> headers = null;
		String res = HttpUtil.get(urlStr, params, headers);
		System.out.println(res);
	}

	private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);
	static final String UTF8 = "UTF-8";
	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final int TIMEOUT = 1000 * 30;
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 5.1; rv:24.0) Gecko/20100101 Firefox/24.0";
	private static final String BOUNDARY = "---------------------------6686390573788478321";

	public static String get(String urlStr, List<HttpParam> params, Map<String, String> headers) {
		try {
			// 请求参数信息
			String paramsStr = buildParamsBody(params, UTF8);
			if (paramsStr != null && paramsStr.length() > 0) {
				int ind = urlStr.lastIndexOf('?');
				if (ind == -1) {
					urlStr += "?";
				} else if (ind < (urlStr.length() - 1)) {
					urlStr += "&";
				}
				urlStr += paramsStr;
			}

			RespRes res = doReq(urlStr, GET, headers, null);
			return res.toStr(UTF8);
		} catch (Exception e) {
			LOG.error(".get():方法异常:", e);
			return "";
		}
	}

	public static String post(String urlStr, List<HttpParam> params, Map<String, String> headers) {
		try {
			String body = buildParamsBody(params, UTF8);
			RespRes res = doReq(urlStr, POST, headers, body, UTF8);
			return res.toStr(UTF8);
		} catch (Exception e) {
			LOG.error(".post():方法异常:", e);
			return "";
		}
	}

	public static String post(String urlStr, String dataStr, Map<String, String> headers) {
		try {
			RespRes res = doReq(urlStr, POST, headers, dataStr, UTF8);
			return res.toStr(UTF8);
		} catch (Exception e) {
			LOG.error(".post():方法异常:", e);
			return "";
		}
	}

	public static String upload(String urlStr, List<HttpParam> params, List<HttpFile> files, Map<String, String> headers) {
		try {
			if (headers == null) {
				headers = new HashMap<String, String>();
				headers.put("Connection", "Keep-Alive");
				headers.put("Content-Type", "multipart/form-data; charset=" + UTF8 + "; boundary=" + BOUNDARY);
			}

			byte[] body = buildUploadBody(params, files, UTF8, BOUNDARY);
			RespRes res = doReq(urlStr, POST, headers, body);
			return res.toStr(UTF8);
		} catch (Exception e) {
			LOG.error(".upload():方法异常:", e);
			return "";
		}
	}

	// ------------
	// private
	// ------------
	private static RespRes doReq(String url, String method, Map<String, String> headers, String body, String encoding)
			throws Exception {
		byte[] b = null;
		if (body != null && !body.isEmpty()) {
			b = body.getBytes(encoding);
		}
		return doReq(url, method, headers, b);
	}

	private static RespRes doReq(String url, String method, Map<String, String> headers, byte[] body) throws Exception {
		// 得到Http连接对象
		HttpURLConnection conn = getConn(new URL(url), method);
		// 添加请求头
		addRequestHeader(conn, headers);
		// 写入内容
		write(conn, body, true);
		// 连接并返回结果
		return connect(conn);
	}

	private static HttpURLConnection getConn(URL url, String method) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(TIMEOUT);
		conn.setReadTimeout(TIMEOUT);
		conn.setRequestMethod(method);
		conn.setDoInput(true);

		// https
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			HttpsURLConnection httpsConn = (HttpsURLConnection) conn;

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new MyX509TrustManager() }, new SecureRandom());
			httpsConn.setSSLSocketFactory(sc.getSocketFactory());
			httpsConn.setHostnameVerifier(new MyHostnameVerifier());
		}

		return conn;
	}

	private static HttpURLConnection addRequestHeader(HttpURLConnection conn, Map<String, String> headers) {
		conn.addRequestProperty("User-Agent", USER_AGENT);
		if (headers != null && !headers.isEmpty()) {
			Set<String> keys = headers.keySet();
			for (String key : keys) {
				conn.addRequestProperty(key, headers.get(key));
			}
		}
		return conn;
	}

	private static HttpURLConnection write(HttpURLConnection conn, String body, String encoding, boolean isCloseOut)
			throws Exception {
		if (body != null && !body.isEmpty()) {
			write(conn, body.getBytes(encoding), isCloseOut);
		}
		return conn;
	}

	private static HttpURLConnection write(HttpURLConnection conn, byte[] b, boolean isCloseOut) throws Exception {
		if (b != null && b.length > 0) {
			conn.setDoOutput(true);
			conn.getOutputStream().write(b);
			conn.getOutputStream().flush();
			if (isCloseOut) {
				conn.getOutputStream().close();
			}
		}

		return conn;
	}

	private static RespRes connect(HttpURLConnection conn) {
		RespRes res = new RespRes();
		boolean isOpened = false;
		ByteArrayOutputStream out = null;
		InputStream in = null;
		try {
			conn.connect();
			isOpened = true;

			res.setHeader(conn.getHeaderFields());

			out = new ByteArrayOutputStream();
			in = conn.getInputStream();
			byte[] b = new byte[4096];
			int len = 0;
			while ((len = in.read(b)) > 0) {
				out.write(b, 0, len);
				out.flush();
			}
			res.setRes(out.toByteArray());
		} catch (Exception e) {
			LOG.error(".conn():方法异常:", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (isOpened) {
				conn.disconnect();
			}
		}
		return res;
	}

	private static boolean isNotBlank(List<?> list) {
		return (list != null && !list.isEmpty());
	}

	private static String contentType(String filename) {
		if (filename.toLowerCase().endsWith(".png")) {
			return "image/png";
		}
		String contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(filename);
		if (contentType == null || "".equals(contentType)) {
			contentType = "application/octet-stream";
		}
		return contentType;
	}

	private static String buildParamsBody(List<HttpParam> params, String encoding) throws IOException {
		if (params == null || params.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (HttpParam param : params) {
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

	private static byte[] buildUploadBody(List<HttpParam> params, List<HttpFile> files, String encoding, String boundary)
			throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		StringBuilder sb;
		// 请求参数信息
		if (isNotBlank(params)) {
			sb = new StringBuilder();
			for (HttpParam param : params) {
				sb.append("\r\n--" + boundary + "\r\n");
				sb.append("Content-Disposition: form-data; name=\"" + param.getKey() + "\"").append("\r\n");
				sb.append("Content-Type: text/plant; charset=" + encoding + "\r\n\r\n");
				sb.append(param.getValue());
			}
			out.write(sb.toString().getBytes(encoding));
		}
		// 文件
		if (isNotBlank(files)) {
			InputStream in;
			byte[] bs;
			for (HttpFile file : files) {
				sb = new StringBuilder();
				sb.append("\r\n--" + boundary + "\r\n");
				sb.append("Content-Disposition: form-data; name=\"" + file.getName() + "\"; filename=\""
						+ file.getFilename() + "\"\r\n");
				sb.append("Content-Type:" + contentType(file.getFilename()) + "\r\n\r\n");
				out.write(sb.toString().getBytes(encoding));

				bs = new byte[4096];
				int len = 0;
				in = file.getIn();
				while ((len = file.getIn().read(bs)) > 0) {
					out.write(bs, 0, len);
				}
				in.close();
			}
		}
		// 结束
		if (isNotBlank(params) || isNotBlank(files)) {
			sb = new StringBuilder();
			sb.append("\r\n--" + boundary + "--\r\n");
			out.write(sb.toString().getBytes(encoding));
		}

		out.flush();
		return out.toByteArray();
	}

}
