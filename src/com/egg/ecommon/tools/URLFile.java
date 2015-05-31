package com.egg.ecommon.tools;

import java.io.InputStream;

public class URLFile {

	private String name;
	private String filename;
	private InputStream in;

	public URLFile() {
	}

	public URLFile(String name, String filename, InputStream in) {
		this.name = name;
		this.filename = filename;
		this.in = in;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}

}
