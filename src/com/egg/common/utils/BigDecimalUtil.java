package com.egg.common.utils;

import java.math.BigDecimal;

import com.egg.common.log.LogKit;

public class BigDecimalUtil {

	private static final int DEF_SCALE = 2;
	private BigDecimal val;

	private BigDecimalUtil(double d) {
		val = new BigDecimal(String.valueOf(d));
	}

	public static BigDecimalUtil val(double d) {
		LogKit.debug("");
		LogKit.debug("大数计算 :");
		LogKit.debug(String.valueOf(d));
		return new BigDecimalUtil(d);
	}

	public double val() {
		return val.doubleValue();
	}

	public int valInt() {
		return val.intValue();
	}

	/**
	 * 加法
	 */
	public BigDecimalUtil add(double n) {
		LogKit.debug("+ " + n);
		val = val.add(new BigDecimal(String.valueOf(n)));
		LogKit.debug("= " + val.doubleValue());
		return this;
	}

	/**
	 * 减法
	 */
	public BigDecimalUtil subtract(double n) {
		LogKit.debug("- " + n);
		val = val.subtract(new BigDecimal(String.valueOf(n)));
		LogKit.debug("= " + val.doubleValue());
		return this;
	}

	/**
	 * 乘法
	 */
	public BigDecimalUtil multiply(double n) {
		LogKit.debug("* " + n);
		val = val.multiply(new BigDecimal(String.valueOf(n)));
		LogKit.debug("= " + val.doubleValue());
		return this;
	}

	/**
	 * 除法（默认四舍五入2位小数）
	 */
	public BigDecimalUtil divide(double n) {
		return divide(n, DEF_SCALE);
	}

	/**
	 * 除法
	 */
	public BigDecimalUtil divide(double n, int scale) {
		LogKit.debug("/ " + n);
		val = val.divide(new BigDecimal(String.valueOf(n)), scale, BigDecimal.ROUND_HALF_UP);
		LogKit.debug("= " + val.doubleValue());
		return this;
	}

}
