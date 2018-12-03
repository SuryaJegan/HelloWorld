package com.dnb.process;

import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.dnb.client.SqlClient;
import com.dnb.constants.Constant;
import com.dnb.exceptions.IP2DunsException;
import com.dnb.util.DateUtil;
import com.dnb.util.MailUtil;
import com.dnb.util.MessageLogger;

public class BizIpCreationProcess {
	private Properties emailProperties = null;
	private Properties errorCodeProperties = null;
	private Properties processMappingproperties = null;

	public BizIpCreationProcess() throws IOException {

		this.emailProperties = new Properties();
		emailProperties.load(this.getClass().getResourceAsStream("/EmailProperties.properties"));

		this.errorCodeProperties = new Properties();
		errorCodeProperties.load(this.getClass().getResourceAsStream("/ErrorCodeProperties.properties"));
		this.processMappingproperties = new Properties();
		processMappingproperties.load(this.getClass().getResourceAsStream("/processMapping.properties"));
	}

	public void draftMail(String[] emailArg)
			throws IOException, IP2DunsException, AddressException, MessagingException {
		int processID = Integer.parseInt(emailArg[0]);
		String processName = processMappingproperties.getProperty(Integer.toString(processID));
		String convertedProcessName = processName.trim().replaceAll(" ", "_").toUpperCase();
		MessageLogger.printMessage(processName + " Email notification process Started...");
		String processStatus = emailArg[1];
		StringBuffer messageBuffer = new StringBuffer();
		String emailContent = null;
		String recipientsEmails = null;
		String subject = emailProperties.getProperty(convertedProcessName + "_SUBJECT") + "-" + DateUtil.getDate();
		MailUtil mu = new MailUtil();
		SqlClient sqlClient = new SqlClient();
		boolean updateStatus1 = true;
		if (processStatus.equalsIgnoreCase(Constant.SUCCESS)) {
			messageBuffer.append(emailProperties.getProperty(convertedProcessName));
			recipientsEmails = emailProperties.getProperty("BIZ_IP_CREATION_EMAIL");
			updateStatus1 = sqlClient.updateProcessStatus(processID, true, processStatus,
					"Completed by automated process.");
			MessageLogger.printMessage("Drafting mail...");
			emailContent = mu.getBizIPHtmlContent(messageBuffer.toString());
		} else {
			StringBuffer errorMsg = new StringBuffer();
			errorMsg.append(errorCodeProperties.getProperty(processStatus));
			messageBuffer.append(emailProperties.getProperty(convertedProcessName + "_ERROR"));
			recipientsEmails = emailProperties.getProperty("BIZ_IP_CREATION_ERROR_EMAIL");
			StringBuffer sqlMessage = new StringBuffer();
			sqlMessage.append("Automated Process ").append(errorMsg);
			boolean updateStatus2 = sqlClient.updateProcessStatus(processID, false, processStatus,
					sqlMessage.toString());
			if (!updateStatus2) {
				messageBuffer.append(" ").append(emailProperties.getProperty("TABLE_UPDATE_FAILURE"));
			}
			MessageLogger.printMessage("Drafting mail...");

			emailContent = mu.getErrorHtmlContent(messageBuffer.toString(), errorMsg.toString());
		}

		try {
			mu.sendMail(subject, emailContent, emailArg[0], recipientsEmails);
			if (!updateStatus1) {
				MessageLogger.printMessage("Drafting Alert for database update failure...");
				recipientsEmails = emailProperties.getProperty("EMAIL_ERROR_RECIPIENTS");
				mu.sendMail(subject, mu.getBizIPHtmlContent(emailProperties.getProperty("TABLE_UPDATE_FAILURE")),
						emailArg[0], recipientsEmails);
			}
		} catch (MessagingException e) {
			throw new IP2DunsException(e);

		}
	}
}
