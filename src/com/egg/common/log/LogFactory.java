package com.egg.common.log;

import com.egg.common.log.support.SLF4JLog;

public class LogFactory {

	private static volatile Log LOG = null;

	public static Log getLog() {
		if (LOG == null) {
			synchronized (LogFactory.class) {
				if (LOG == null) {
					LOG = new SLF4JLog();
				}
			}
		}
		return LOG;
	}

	public static void setLog(Log log) {
		synchronized (LogFactory.class) {
			LOG = log;
		}
	}

}
