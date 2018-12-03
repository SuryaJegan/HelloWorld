package com.dnb.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.dnb.constants.Constant;
import com.dnb.exceptions.IP2DunsException;
import com.dnb.model.IspResultVO;
import com.dnb.model.ThresholdVO;
import com.dnb.util.DateUtil;
import com.dnb.util.MailUtil;
import com.dnb.util.MessageLogger;

public class IspResultStatsProcess {
	private Properties emailProperties = null;
	private Properties errorCodeProperties = null;
	private Properties processMappingproperties = null;

	public IspResultStatsProcess() throws IOException {

		this.emailProperties = new Properties();
		emailProperties.load(this.getClass().getResourceAsStream("/EmailProperties.properties"));

		this.errorCodeProperties = new Properties();
		errorCodeProperties.load(this.getClass().getResourceAsStream("/ErrorCodeProperties.properties"));
		this.processMappingproperties = new Properties();
		processMappingproperties.load(this.getClass().getResourceAsStream("/processMapping.properties"));
	}

	public boolean checkIspResultThreshold(IspResultVO ispResultVO, Map<String, String> finalResultMap) {
		System.out.println("Checking for all threshold ");
		ThresholdStatsProcess thresholdStatsProcess = new ThresholdStatsProcess();
		ThresholdVO thresholdVO = thresholdStatsProcess.getThresholdColor(finalResultMap, ispResultVO);
		boolean thresholdCheck = true;

		if (thresholdVO.getPercentRatioColorName().equals(Constant.RED_COLOR)) {
			System.out.println("failed for predicted ratio ");
			thresholdCheck = false;
			return thresholdCheck;
		}

		if (thresholdVO.getTrainingSetColorName().equals(Constant.RED_COLOR)) {
			thresholdCheck = false;
			System.out.println("failed for training count " + ispResultVO.getTrainingSetCount());
			return thresholdCheck;
		}
		if (thresholdVO.getIspRatioColorName().equals(Constant.RED_COLOR)) {
			System.out.println("failed for " + ispResultVO.getIspNonIspRatio());
			thresholdCheck = false;
			return thresholdCheck;
		}
		if (thresholdVO.getPredictedIspRatioColorName().equals(Constant.RED_COLOR)) {
			System.out.println("failed for " + ispResultVO.getPredictedNonIsp());
			thresholdCheck = false;
			return thresholdCheck;
		}
		
		if (thresholdVO.getPredictedNonIspColorName().equals(Constant.RED_COLOR)) {
			System.out.println("failed for " + ispResultVO.getPredictedNonIsp());
			thresholdCheck = false;
			return thresholdCheck;
		}
		if (thresholdVO.getPercentRatioColorName().equals(Constant.RED_COLOR)) {
			System.out.println("failed for percent ratio ");

			thresholdCheck = false;
			return thresholdCheck;
		}

		if (thresholdVO.getRecallColorName().equals(Constant.RED_COLOR)) {
			System.out.println("failed for recall ");
			thresholdCheck = false;
			return thresholdCheck;

		}

		if (thresholdVO.getForColorName().equals(Constant.RED_COLOR)) {
			System.out.println("failed for falseOmission ");
			thresholdCheck = false;
			return thresholdCheck;
		}

		return thresholdCheck;
	}

	public Map<String, String> getIspModelResultDetails() {
		System.out.println("getting data from file");
		String line = null;
		Map<String, String> getResultMap = new LinkedHashMap<>();
		File file = new File(Constant.TRAINING_MODEL_FILENAME);
		try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
			while ((line = reader.readLine()) != null) {
				String[] split = line.split(":");
				getResultMap.put(split[0], split[1]);
			}

		} catch (FileNotFoundException e) {
			MessageLogger.printMessage(e.getMessage());

		} catch (IOException e) {
			MessageLogger.printMessage(e.getMessage());

		}
		System.out.println(getResultMap);
		return getResultMap;
	}

	public void draftMail(IspResultVO ispResultVO, Map<String, String> finalResultMap, String[] args)
			throws IOException, AddressException, MessagingException, IP2DunsException {
		String sourceName = args[0];
		String emailContent = null;
		String result = args[1];
		String errorMsg = null;
		String recipientsEmails = null;

		String errorHeader = emailProperties.getProperty("BIZ_IP_MESSAGE");

		String subject = emailProperties.getProperty("ISP_RESULT_SUBJECT") + "-" + DateUtil.getDate();
		MailUtil mu = new MailUtil();
		recipientsEmails = emailProperties.getProperty("BIZ_IP_CREATION_EMAIL");

		if (result.equals(Constant.SUCCESS)) {
			emailContent = mu.getNotificationHtmlContent(ispResultVO, finalResultMap);

		} else {
			errorMsg = emailProperties.getProperty("ISP_NOTIFCATION_GATE_ERROR");
			recipientsEmails = emailProperties.getProperty("BIZ_IP_CREATION_ERROR_EMAIL");
			MessageLogger.printMessage("Drafting mail...");
			emailContent = mu.getErrorHtmlContent(errorHeader, errorMsg);

		}

		mu.sendMail(subject, emailContent, sourceName, recipientsEmails);

	}

	public boolean checkGlobalMatchTheshold(float globalMatchRate) {
		System.out.println("Checking for all threshold ");
		ThresholdStatsProcess thresholdStatsProcess = new ThresholdStatsProcess();
		String globalMatchColor = thresholdStatsProcess.getGlobalColor(globalMatchRate);
		boolean thresholdCheck = true;
		if (globalMatchColor.equals(Constant.RED_COLOR)) {
			thresholdCheck = false;
		}

		return thresholdCheck;
	}
}
