package com.egg.common.utils;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BigDecimalUtil {

	private static final Logger LOG = LoggerFactory.getLogger(BigDecimalUtil.class);
	private static final int DEF_SCALE = 2;
	private BigDecimal val;

	private BigDecimalUtil(double d) {
		val = new BigDecimal(String.valueOf(d));
	}

	public static BigDecimalUtil val(double d) {
		LOG.debug("");
		LOG.debug("大数计算 :");
		LOG.debug(String.valueOf(d));
		return new BigDecimalUtil(d);
	}

	public double val() {
		return val.doubleValue();
	}
	
	public int valInt() {
		return val.intValue();
	}

	public BigDecimalUtil add(double n) {
		LOG.debug("+ " + n);
		val = val.add(new BigDecimal(String.valueOf(n)));
		LOG.debug("= " + val.doubleValue());
		return this;
	}

	public BigDecimalUtil subtract(double n) {
		LOG.debug("- " + n);
		val = val.subtract(new BigDecimal(String.valueOf(n)));
		LOG.debug("= " + val.doubleValue());
		return this;
	}

	public BigDecimalUtil multiply(double n) {
		LOG.debug("* " + n);
		val = val.multiply(new BigDecimal(String.valueOf(n)));
		LOG.debug("= " + val.doubleValue());
		return this;
	}

	public BigDecimalUtil divide(double n) {
		return divide(n, DEF_SCALE);
	}

	public BigDecimalUtil divide(double n, int scale) {
		LOG.debug("/ " + n);
		val = val.divide(new BigDecimal(String.valueOf(n)), scale, BigDecimal.ROUND_HALF_UP);
		LOG.debug("= " + val.doubleValue());
		return this;
	}

}
