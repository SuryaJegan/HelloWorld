package com.dnb.model;

public class MatchRateVO {

	int customerId;
	int totalVisits;
	int matchedVisits;
	int expectedMatch;
	float oldPercentMatchRate;
	float expectedPercentMatchRate;
	float netPercentDifference;

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getTotalVisits() {
		return totalVisits;
	}

	public void setTotalVisits(int totalVisits) {
		this.totalVisits = totalVisits;
	}

	public int getMatchedVisits() {
		return matchedVisits;
	}

	public void setMatchedVisits(int matchedVisits) {
		this.matchedVisits = matchedVisits;
	}

	public int getExpectedMatch() {
		return expectedMatch;
	}

	public void setExpectedMatch(int expectedMatch) {
		this.expectedMatch = expectedMatch;
	}

	public float getOldPercentMatchRate() {
		return oldPercentMatchRate;
	}

	public void setOldPercentMatchRate(float oldPercentMatchRate) {
		this.oldPercentMatchRate = oldPercentMatchRate;
	}

	public float getExpectedPercentMatchRate() {
		return expectedPercentMatchRate;
	}

	public void setExpectedPercentMatchRate(float expectedPercentMatchRate) {
		this.expectedPercentMatchRate = expectedPercentMatchRate;
	}

	public float getNetPercentDifference() {
		return netPercentDifference;
	}

	public void setNetPercentDifference(float netPercentDifference) {
		this.netPercentDifference = netPercentDifference;
	}

}
