package com.dnb.model;

public class IspResultVO {

	private int trainingSetCount;
	private int ispCount;
	private int nonIspCount;
	private float ispNonIspRatio;
	private int testSetCount;
	private int predictedNonIsp;
	private int predictedIsp;

	public int getTrainingSetCount() {
		return trainingSetCount;
	}

	public void setTrainingSetCount(int trainingSetCount) {
		this.trainingSetCount = trainingSetCount;
	}

	public int getIspCount() {
		return ispCount;
	}

	public void setIspCount(int ispCount) {
		this.ispCount = ispCount;
	}

	public int getNonIspCount() {
		return nonIspCount;
	}

	public void setNonIspCount(int nonIspCount) {
		this.nonIspCount = nonIspCount;
	}

	public float getIspNonIspRatio() {
		return ispNonIspRatio;
	}

	public void setIspNonIspRatio(float ispNonIspRatio) {
		this.ispNonIspRatio = ispNonIspRatio;
	}

	public int getTestSetCount() {
		return testSetCount;
	}

	public void setTestSetCount(int testSetCount) {
		this.testSetCount = testSetCount;
	}

	public int getPredictedNonIsp() {
		return predictedNonIsp;
	}

	public void setPredictedNonIsp(int predictedNonIsp) {
		this.predictedNonIsp = predictedNonIsp;
	}

	public int getPredictedIsp() {
		return predictedIsp;
	}

	public void setPredictedIsp(int predictedIsp) {
		this.predictedIsp = predictedIsp;
	}

}
