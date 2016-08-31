package com.egg.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogKit {

	private static final Logger LOG = LoggerFactory.getLogger(LogKit.class);

	private static String buildMessage(StackTraceElement ele, String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append(ele.getClassName());
		sb.append(".");
		sb.append(ele.getMethodName());
		sb.append("(");
		sb.append(ele.getFileName());
		sb.append(":");
		sb.append(ele.getLineNumber());
		sb.append(") - ");
		if (msg != null) {
			sb.append(msg);
		}
		return sb.toString();
	}

	public static void debug(String msg) {
		LOG.debug(buildMessage(Thread.currentThread().getStackTrace()[1], msg));
	}

	public static void info(String msg) {
		LOG.info(buildMessage(Thread.currentThread().getStackTrace()[1], msg));
	}

	public static void error(String msg) {
		LOG.error(buildMessage(Thread.currentThread().getStackTrace()[1], msg));
	}

	public static void error(String msg, Throwable e) {
		LOG.error(buildMessage(Thread.currentThread().getStackTrace()[1], msg), e);
	}

}
