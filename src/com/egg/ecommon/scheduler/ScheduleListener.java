package com.egg.ecommon.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ScheduleListener implements ServletContextListener {

	private static final Log LOG = LogFactory.getLog(ScheduleListener.class);
	private static final long NO_DELAY = 0; // 无延时
	private static final long MILLI_DAY = 1000 * 60 * 60 * 24; // 一天的毫秒数

	private Timer timer = null;

	public ScheduleListener() {
		super();
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		LOG.info("timer bigen ...");
		timer = new Timer("定时任务", true);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if (timer != null) {
			timer.cancel();
		}

		LOG.info("timer destroyed ...");
	}

	// private method ===============================================
	/**
	 * 指定每天的某一时刻执行
	 * 
	 * @param task
	 *            定时任务
	 * @param time
	 *            HH:mm:ss
	 */
	private void scheduleByTime(TimerTask task, String time) {
		if (timer == null) {
			timer = new Timer("定时任务", true);
		}

		try {
			timer.scheduleAtFixedRate(task, getFirstTime(time), MILLI_DAY);
			LOG.info("添加定时任务：每天[ " + time + " ] 执行 [ " + task.getClass() + " ]");
		} catch (Exception e) {
			LOG.error(ScheduleListener.class.getSimpleName() + ".scheduleByTime() : 方法异常 : " + e.getMessage(), e);
		}
	}

	private Date getFirstTime(String time) throws ParseException {
		SimpleDateFormat sdDate = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String ymd = sdDate.format(now);

		SimpleDateFormat sdDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date firstTime = sdDateTime.parse(ymd + " " + time);

		if (firstTime.before(now)) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(firstTime);
			cal.add(Calendar.DATE, 1);
			firstTime = cal.getTime();
		}
		return firstTime;
	}
}
