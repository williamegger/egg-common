package com.egg.common.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.egg.common.log.LogKit;

public abstract class Schedule implements ServletContextListener {

	protected static final long NO_DELAY = 0; // 无延时
	protected static final long ONE_DAY_MILLIS = 1000 * 60 * 60 * 24; // 一天的毫秒数

	private Timer timer = null;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		LogKit.info("timer bigen ...");
		timer = new Timer("定时任务", true);
		init(event);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if (timer != null) {
			timer.cancel();
		}
		destroy(event);
		LogKit.info("timer destroyed ...");
	}

	protected abstract void init(ServletContextEvent event);

	protected abstract void destroy(ServletContextEvent event);

	/**
	 * 指定每天的某一时刻执行任务
	 * 
	 * @param task 定时任务
	 * @param time HH:mm:ss
	 */
	protected void addTask(TimerTask task, String time) {
		if (timer == null) {
			timer = new Timer("定时任务", true);
		}

		try {
			timer.scheduleAtFixedRate(task, getFirstTime(time), ONE_DAY_MILLIS);
			LogKit.info("添加定时任务：每天[ " + time + " ] 执行 [ " + task.getClass() + " ]");
		} catch (Exception e) {
			LogKit.error("定时任务添加错误  [ " + task.getClass() + " ]", e);
		}
	}

	/**
	 * 得到开始时间 HH:mm:ss
	 * 
	 * <pre>
	 * 如果当前时间小于time，开始时间则为当日的时间；
	 * 如果当前时间大于time，开始时间则为明日的时间；
	 * </pre>
	 */
	protected Date getFirstTime(String time) throws ParseException {
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

	public Timer getTimer() {
		return timer;
	}

}
