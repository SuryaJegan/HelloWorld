package com.dnb.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.dnb.constants.Constant;
import com.dnb.exceptions.IP2DunsException;
import com.dnb.model.MatchRateVO;
import com.dnb.util.DateUtil;
import com.dnb.util.MailUtil;
import com.dnb.util.MessageLogger;

public class GlobalMatchRateProcess {
	private Properties emailProperties = null;
	private Properties errorCodeProperties = null;
	private Properties processMappingproperties = null;

	public GlobalMatchRateProcess() throws IOException {

		this.emailProperties = new Properties();
		emailProperties.load(this.getClass().getResourceAsStream("/EmailProperties.properties"));

		this.errorCodeProperties = new Properties();
		errorCodeProperties.load(this.getClass().getResourceAsStream("/ErrorCodeProperties.properties"));
		this.processMappingproperties = new Properties();
		processMappingproperties.load(this.getClass().getResourceAsStream("/processMapping.properties"));
	}

	public void draftMail(ArrayList<MatchRateVO> matchVO, float globalMatchRate, String[] args)
			throws IOException, AddressException, MessagingException, IP2DunsException {
		String sourceName = args[0];
		String emailContent = null;
		String result = args[1];
		String errorMsg = null;
		String recipientsEmails = null;

		String errorHeader = emailProperties.getProperty("BIZ_IP_MESSAGE");

		String subject = emailProperties.getProperty("GLOBAL_MATCH_RATE_SUBJECT") + "-" + DateUtil.getDate();
		
		MailUtil mu = new MailUtil();
		recipientsEmails = emailProperties.getProperty("BIZ_IP_CREATION_EMAIL");

		if (result.equals(Constant.SUCCESS)) {
			emailContent = mu.getGlobalMatchHtmlContent(matchVO, globalMatchRate);

		} else {
			errorMsg = emailProperties.getProperty("ISP_NOTIFCATION_GATE_ERROR");
			recipientsEmails = emailProperties.getProperty("BIZ_IP_CREATION_ERROR_EMAIL");
			MessageLogger.printMessage("Drafting mail...");
			emailContent = mu.getErrorHtmlContent(errorHeader, errorMsg);

		}

		mu.sendMail(subject, emailContent, sourceName, recipientsEmails);

	}

}
