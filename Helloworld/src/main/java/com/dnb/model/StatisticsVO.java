package com.dnb.model;

import java.util.ArrayList;

public class StatisticsVO {
	private ArrayList<BizCompVO> bizCompList;
	private ArrayList<IspKeyVO> ispKeyList;

	public ArrayList<BizCompVO> getBizCompList() {
		return bizCompList;
	}

	public void setBizCompList(ArrayList<BizCompVO> bizCompList) {
		this.bizCompList = bizCompList;
	}

	public ArrayList<IspKeyVO> getIspKeyList() {
		return ispKeyList;
	}

	public void setIspKeyList(ArrayList<IspKeyVO> ispKeyList) {
		this.ispKeyList = ispKeyList;
	}

}
