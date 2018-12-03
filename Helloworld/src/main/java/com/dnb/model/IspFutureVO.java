package com.dnb.model;

public class IspFutureVO {
	private boolean ispFlag;
	private String ispFlagReason;
	private int ipRanges;
	private int ipRangesFuture;
	private int ipRangesChange;
	private int dunsCount;
	private int dunsFutureCount;
	private int dunsChangeCount;

	public boolean isIspFlag() {
		return ispFlag;
	}

	public void setIspFlag(boolean ispFlag) {
		this.ispFlag = ispFlag;
	}

	public String getIspFlagReason() {
		return ispFlagReason;
	}

	public void setIspFlagReason(String ispFlagReason) {
		this.ispFlagReason = ispFlagReason;
	}

	public int getIpRanges() {
		return ipRanges;
	}

	public void setIpRanges(int ipRanges) {
		this.ipRanges = ipRanges;
	}

	public int getIpRangesFuture() {
		return ipRangesFuture;
	}

	public void setIpRangesFuture(int ipRangesFuture) {
		this.ipRangesFuture = ipRangesFuture;
	}

	public int getIpRangesChange() {
		return ipRangesChange;
	}

	public void setIpRangesChange(int ipRangesChange) {
		this.ipRangesChange = ipRangesChange;
	}

	public int getDunsCount() {
		return dunsCount;
	}

	public void setDunsCount(int dunsCount) {
		this.dunsCount = dunsCount;
	}

	public int getDunsFutureCount() {
		return dunsFutureCount;
	}

	public void setDunsFutureCount(int dunsFutureCount) {
		this.dunsFutureCount = dunsFutureCount;
	}

	public int getDunsChangeCount() {
		return dunsChangeCount;
	}

	public void setDunsChangeCount(int dunsChangeCount) {
		this.dunsChangeCount = dunsChangeCount;
	}
}
