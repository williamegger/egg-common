package com.egg.common.scheduler;

import java.util.Calendar;
import java.util.TimerTask;

public abstract class DefaultDateTask extends TimerTask {

	private int date;

	public DefaultDateTask(int date) {
		super();
		this.date = date;
	}

	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
		int date = cal.get(Calendar.DATE);
		if (this.date == date) {
			task();
		}
	}

	public abstract void task();

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

}
