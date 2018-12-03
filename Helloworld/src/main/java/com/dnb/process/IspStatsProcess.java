package com.dnb.process;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.dnb.constants.Constant;
import com.dnb.exceptions.IP2DunsException;
import com.dnb.util.DateUtil;
import com.dnb.util.MailUtil;

public class IspStatsProcess {

	private Properties emailProperties = null;

	public IspStatsProcess() throws IOException {

		this.emailProperties = new Properties();
		emailProperties.load(this.getClass().getResourceAsStream("/EmailProperties.properties"));
	}

	public void draftMail(List<Object> statsVO, String[] emailArg)
			throws IOException, IP2DunsException, AddressException, MessagingException {
		String sourceName = emailArg[0];
		String emailContent = null;
		String result = emailArg[1];
		String errorMsg = null;
		String errorHeader = emailProperties.getProperty("BIZ_IP_MESSAGE");
		String recipientsEmails = null;
		String subject = emailProperties.getProperty("BIZ_IP_ISP_SUBJECT") + "-" + DateUtil.getDate();
		MailUtil mu = new MailUtil();
		recipientsEmails = emailProperties.getProperty("BIZ_IP_CREATION_EMAIL");
		if (result.equalsIgnoreCase(Constant.SUCCESS)) {
			emailContent = mu.getHtmlContent(statsVO, sourceName);
		} else {
			errorMsg = emailProperties.getProperty("BIZ_IP_ISP_ERROR");
			recipientsEmails = emailProperties.getProperty("BIZ_IP_CREATION_ERROR_EMAIL");

			emailContent = mu.getErrorHtmlContent(errorHeader, errorMsg);

		}

		mu.sendMail(subject, emailContent, sourceName, recipientsEmails);
	}

}
