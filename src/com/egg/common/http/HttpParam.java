package com.egg.common.http;

public class HttpParam {

	private String key;
	private String value;

	public HttpParam() {
	}

	public HttpParam(String key, Object value) {
		this.key = key;
		this.value = (value == null) ? "" : String.valueOf(value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
