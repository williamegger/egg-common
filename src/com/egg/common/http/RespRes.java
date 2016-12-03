package com.egg.common.http;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public class RespRes {

	private Map<String, List<String>> header;
	private byte[] res;

	public RespRes() {
	}

	public RespRes(Map<String, List<String>> header, byte[] res) {
		this.header = header;
		this.res = res;
	}

	public String toStr(String encoding) throws UnsupportedEncodingException {
		if (res == null || res.length == 0) {
			return "";
		}
		if (encoding == null) {
			encoding = HttpUtil.UTF8;
		}
		return new String(res, encoding);
	}

	public Map<String, List<String>> getHeader() {
		return header;
	}

	public void setHeader(Map<String, List<String>> header) {
		this.header = header;
	}

	public byte[] getRes() {
		return res;
	}

	public void setRes(byte[] res) {
		this.res = res;
	}

}
