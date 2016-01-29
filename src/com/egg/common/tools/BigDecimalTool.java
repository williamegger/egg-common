package com.egg.common.tools;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BigDecimalTool {

	private static final Log LOG = LogFactory.getLog(BigDecimalTool.class);
	private static final int DEF_SCALE = 2;
	private BigDecimal val;

	private BigDecimalTool(double d) {
		val = new BigDecimal(String.valueOf(d));
	}

	public static BigDecimalTool val(double d) {
		LOG.debug("");
		LOG.debug("大数计算 :");
		LOG.debug(d);
		return new BigDecimalTool(d);
	}

	public double val() {
		return val.doubleValue();
	}
	
	public int valInt() {
		return val.intValue();
	}

	public BigDecimalTool add(double n) {
		LOG.debug("+ " + n);
		val = val.add(new BigDecimal(String.valueOf(n)));
		LOG.debug("= " + val.doubleValue());
		return this;
	}

	public BigDecimalTool subtract(double n) {
		LOG.debug("- " + n);
		val = val.subtract(new BigDecimal(String.valueOf(n)));
		LOG.debug("= " + val.doubleValue());
		return this;
	}

	public BigDecimalTool multiply(double n) {
		LOG.debug("* " + n);
		val = val.multiply(new BigDecimal(String.valueOf(n)));
		LOG.debug("= " + val.doubleValue());
		return this;
	}

	public BigDecimalTool divide(double n) {
		return divide(n, DEF_SCALE);
	}

	public BigDecimalTool divide(double n, int scale) {
		LOG.debug("/ " + n);
		val = val.divide(new BigDecimal(String.valueOf(n)), scale, BigDecimal.ROUND_HALF_UP);
		LOG.debug("= " + val.doubleValue());
		return this;
	}

}
