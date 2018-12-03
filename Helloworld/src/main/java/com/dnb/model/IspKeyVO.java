package com.dnb.model;

public class IspKeyVO {

	private String ipKeyStatus;

	private int ipKeyCount;
	private int priorDunsCount;
	private int newDunsount;

	public String getIpKeyStatus() {
		return ipKeyStatus;
	}

	public void setIpKeyStatus(String ipKeyStatus) {
		this.ipKeyStatus = ipKeyStatus;
	}

	public int getIpKeyCount() {
		return ipKeyCount;
	}

	public void setIpKeyCount(int ipKeyCount) {
		this.ipKeyCount = ipKeyCount;
	}

	public int getPriorDunsCount() {
		return priorDunsCount;
	}

	public void setPriorDunsCount(int priorDunsCount) {
		this.priorDunsCount = priorDunsCount;
	}

	public int getNewDunsount() {
		return newDunsount;
	}

	public void setNewDunsount(int newDunsount) {
		this.newDunsount = newDunsount;
	}

}
