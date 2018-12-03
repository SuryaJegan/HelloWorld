package com.dnb.model;

import java.math.BigDecimal;

public class BizCompVO {

	private String source;
	private int ranges;
	private BigDecimal ipaddresses;
	private int rangeswduns;
	private int rangesnonisp;
	private int rangesisp;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getRanges() {
		return ranges;
	}

	public void setRanges(int ranges) {
		this.ranges = ranges;
	}

	public BigDecimal getIpaddresses() {
		return ipaddresses;
	}

	public void setIpaddresses(BigDecimal bigDecimal) {
		this.ipaddresses = bigDecimal;
	}

	public int getRangeswduns() {
		return rangeswduns;
	}

	public void setRangeswduns(int rangeswduns) {
		this.rangeswduns = rangeswduns;
	}

	public int getRangesnonisp() {
		return rangesnonisp;
	}

	public void setRangesnonisp(int rangesnonisp) {
		this.rangesnonisp = rangesnonisp;
	}

	public int getRangesisp() {
		return rangesisp;
	}

	public void setRangesisp(int rangesisp) {
		this.rangesisp = rangesisp;
	}

}
