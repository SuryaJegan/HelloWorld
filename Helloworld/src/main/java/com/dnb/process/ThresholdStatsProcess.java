package com.dnb.process;

import java.util.Map;

import com.dnb.constants.Constant;
import com.dnb.model.IspResultVO;
import com.dnb.model.ThresholdVO;

public class ThresholdStatsProcess {

	public ThresholdVO getThresholdColor(Map<String, String> finalResultMap, IspResultVO ispResultVO) {

		ThresholdVO thresholdVO = new ThresholdVO();
		float percentRatio = Float.valueOf(finalResultMap.get(Constant.PERCENT_RATIO));
		float recall = Float.valueOf(finalResultMap.get(Constant.RECALL));
		float falseOmission = Float.valueOf(finalResultMap.get(Constant.FOR));
		int isp = Integer.valueOf(finalResultMap.get(Constant.ISP));
		int nonIsp = Integer.valueOf(finalResultMap.get(Constant.NON_ISP));
		float predictedIspRatio;
		boolean orangeFlag = false;
		boolean redFlag = false;
		predictedIspRatio = (isp * 100) / nonIsp;
		thresholdVO.setPredictedIspRatio(predictedIspRatio);
		if (ispResultVO.getTrainingSetCount() > Constant.TRAINING_SET_COUNT_LOWER_LIMIT
				&& ispResultVO.getTrainingSetCount() <= Constant.TRAINING_SET_COUNT_UPPER_LIMIT) {

			thresholdVO.setTrainingSetColor(Constant.ORANGE);
			thresholdVO.setTrainingSetColorName(Constant.ORANGE_COLOR);
			orangeFlag = true;
		} else if (ispResultVO.getTrainingSetCount() < Constant.TRAINING_SET_COUNT_LOWER_LIMIT) {
			thresholdVO.setTrainingSetColor(Constant.RED);
			thresholdVO.setTrainingSetColorName(Constant.RED_COLOR);
			redFlag = true;
		} else {
			thresholdVO.setTrainingSetColor(Constant.GREEN);
			thresholdVO.setTrainingSetColorName(Constant.GREEN_COLOR);
		}

		if (ispResultVO.getIspNonIspRatio() >= Constant.ISP_NON_ISP_RATIO_LOWER_LIMIT
				&& ispResultVO.getIspNonIspRatio() < Constant.ISP_NON_ISP_RATIO_UPPER_LIMIT_1) {
			thresholdVO.setIspRatioColor(Constant.ORANGE);
			thresholdVO.setIspRatioColorName(Constant.ORANGE_COLOR);
			orangeFlag = true;
		} else if (ispResultVO.getIspNonIspRatio() < Constant.ISP_NON_ISP_RATIO_LOWER_LIMIT
				|| ispResultVO.getIspNonIspRatio() > Constant.ISP_NON_ISP_RATIO_UPPER_LIMIT) {
			thresholdVO.setIspRatioColor(Constant.RED);
			thresholdVO.setIspRatioColorName(Constant.RED_COLOR);
			redFlag = true;
		} else if (ispResultVO.getIspNonIspRatio() >= Constant.ISP_NON_ISP_RATIO_UPPER_LIMIT_1
				&& ispResultVO.getIspNonIspRatio() <= Constant.ISP_NON_ISP_RATIO_UPPER_LIMIT) {
			thresholdVO.setIspRatioColor(Constant.GREEN);
			thresholdVO.setIspRatioColorName(Constant.GREEN_COLOR);
		}

		if (ispResultVO.getPredictedNonIsp() >= Constant.PREDICTED_NON_ISP_LOWER_LIMIT && ispResultVO.getPredictedNonIsp() <= Constant.PREDICTED_NON_ISP_UPPER_LIMIT) {
			thresholdVO.setPredictedNonIspColor(Constant.ORANGE);
			thresholdVO.setPredictedNonIspColorName(Constant.ORANGE_COLOR);
			orangeFlag = true;
		} else if (ispResultVO.getPredictedNonIsp() > Constant.PREDICTED_NON_ISP_UPPER_LIMIT) {
			thresholdVO.setPredictedNonIspColor(Constant.RED);
			thresholdVO.setPredictedNonIspColorName(Constant.RED_COLOR);
			redFlag = true;
		} else {
			thresholdVO.setPredictedNonIspColor(Constant.GREEN);
			thresholdVO.setPredictedNonIspColorName(Constant.GREEN_COLOR);
		}
		if (predictedIspRatio >= Constant.PREDICTED_ISP_RATIO_LOWER_LIMIT && predictedIspRatio <= Constant.PREDICTED_ISP_RATIO_UPPER_LIMIT) {
			thresholdVO.setPredictedIspRatioColor(Constant.ORANGE);
			thresholdVO.setPredictedIspRatioColorName(Constant.ORANGE_COLOR);
			orangeFlag = true;
		} else if (predictedIspRatio > Constant.PREDICTED_ISP_RATIO_UPPER_LIMIT) {
			thresholdVO.setPredictedIspRatioColor(Constant.RED);
			thresholdVO.setPredictedIspRatioColorName(Constant.RED_COLOR);
			redFlag = true;
		} else {
			thresholdVO.setPredictedIspRatioColor(Constant.GREEN);
			thresholdVO.setPredictedIspRatioColorName(Constant.GREEN_COLOR);
		}
		if (percentRatio >= Constant.PERCENT_RATIO_LOWER_LIMIT && percentRatio <= Constant.PERCENT_RATIO_UPPER_LIMIT) {
			thresholdVO.setPercentRatioColor(Constant.ORANGE);
			thresholdVO.setPercentRatioColorName(Constant.ORANGE_COLOR);
			orangeFlag = true;
		} else if (percentRatio > Constant.PERCENT_RATIO_UPPER_LIMIT) {
			thresholdVO.setPercentRatioColor(Constant.RED);
			thresholdVO.setPercentRatioColorName(Constant.RED_COLOR);
			redFlag = true;
		} else {
			thresholdVO.setPercentRatioColor(Constant.GREEN);
			thresholdVO.setPercentRatioColorName(Constant.GREEN_COLOR);
		}
		if (recall >= Constant.RECALL_LOWER_LIMIT && recall <= Constant.RECALL_UPPER_LIMIT) {
			thresholdVO.setRecallColor(Constant.ORANGE);
			thresholdVO.setRecallColorName(Constant.ORANGE_COLOR);
			orangeFlag = true;
		} else if (recall < Constant.RECALL_LOWER_LIMIT) {
			thresholdVO.setRecallColor(Constant.RED);
			thresholdVO.setRecallColorName(Constant.RED_COLOR);
			redFlag = true;
		} else {
			thresholdVO.setRecallColor(Constant.GREEN);
			thresholdVO.setRecallColorName(Constant.GREEN_COLOR);
		}
		if (falseOmission >= Constant.FALSE_OMISSION_LOWER_LIMIT && falseOmission <= Constant.FALSE_OMISSION_UPPER_LIMIT) {
			thresholdVO.setForColor(Constant.ORANGE);
			thresholdVO.setForColorName(Constant.ORANGE_COLOR);
			orangeFlag = true;
		} else if (falseOmission > Constant.FALSE_OMISSION_UPPER_LIMIT) {
			thresholdVO.setForColor(Constant.RED);
			thresholdVO.setForColorName(Constant.RED_COLOR);
			redFlag = true;
		} else if (falseOmission < Constant.FALSE_OMISSION_LOWER_LIMIT) {
			thresholdVO.setForColor(Constant.GREEN);
			thresholdVO.setForColorName(Constant.GREEN_COLOR);
		}
		if (redFlag) {
			thresholdVO.setStatus(Constant.RED_COLOR);
			thresholdVO.setStatusColor(Constant.RED);
		} else if (orangeFlag) {
			thresholdVO.setStatus(Constant.ORANGE_COLOR);
			thresholdVO.setStatusColor(Constant.ORANGE);
		} else {
			thresholdVO.setStatus(Constant.GREEN_COLOR);
			thresholdVO.setStatusColor(Constant.GREEN);
		}
		return thresholdVO;
	}

	public String getGlobalColor(float globalMatchRate) {

		String globalMatchColor = null;
		if (((globalMatchRate >= Constant.GLOBAL_MATCH_LOWER_LIMIT_1) && (globalMatchRate <= Constant.GLOBAL_MATCH_UPPER_LIMIT_1))
				|| ((globalMatchRate >= Constant.GLOBAL_MATCH_LOWER_LIMIT_2) && (globalMatchRate <= Constant.GLOBAL_MATCH_UPPER_LIMIT_2))) {
			globalMatchColor = Constant.ORANGE_COLOR;
		} else if ((globalMatchRate < Constant.GLOBAL_MATCH_LOWER_LIMIT_1) || (globalMatchRate > Constant.GLOBAL_MATCH_UPPER_LIMIT_2)) {
			globalMatchColor = Constant.RED_COLOR;
		} else {
			globalMatchColor = Constant.GREEN_COLOR;
		}
		return globalMatchColor;
	}

}
