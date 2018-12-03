package com.dnb.model;

public class BizVO {
	private String cntryName;
	private int bizIpFutureDunsCount;
	private int bizIpDunsCount;
	private int overlappingDunsSum;
	private int percentOfPrior;

	public String getCntryName() {
		return cntryName;
	}

	public void setCntryName(String cntryName) {
		this.cntryName = cntryName;
	}

	public int getBizIpFutureDunsCount() {
		return bizIpFutureDunsCount;
	}

	public void setBizIpFutureDunsCount(int bizIpFutureDunsCount) {
		this.bizIpFutureDunsCount = bizIpFutureDunsCount;
	}

	public int getBizIpDunsCount() {
		return bizIpDunsCount;
	}

	public void setBizIpDunsCount(int bizIpDunsCount) {
		this.bizIpDunsCount = bizIpDunsCount;
	}

	public int getOverlappingDunsSum() {
		return overlappingDunsSum;
	}

	public void setOverlappingDunsSum(int overlappingDunsSum) {
		this.overlappingDunsSum = overlappingDunsSum;
	}

	public int getPercentOfPrior() {
		return percentOfPrior;
	}

	public void setPercentOfPrior(int percentOfPrior) {
		this.percentOfPrior = percentOfPrior;
	}

}
