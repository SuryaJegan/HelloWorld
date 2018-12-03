package com.dnb.process;

import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.dnb.constants.Constant;
import com.dnb.exceptions.IP2DunsException;
import com.dnb.model.StatisticsVO;
import com.dnb.util.DateUtil;
import com.dnb.util.MailUtil;
import com.dnb.util.MessageLogger;

public class BizIpSummaryProcess {
	private Properties emailProperties = null;

	public BizIpSummaryProcess() throws IOException {

		this.emailProperties = new Properties();
		emailProperties.load(this.getClass().getResourceAsStream("/EmailProperties.properties"));
	}

	public void draftMail(StatisticsVO statsVO, String[] emailArg)
			throws IOException, IP2DunsException, AddressException, MessagingException {
		String sourceName = emailArg[0];
		String emailContent = null;
		String result = emailArg[1];
		String errorMsg = null;
		String recipientsEmails = null;
		// boolean updateStatus1 = true;
		String errorHeader = emailProperties.getProperty("BIZ_IP_MESSAGE");
		// SqlClient sqlClient= new SqlClient();
		// int processID=0;
		String subject = emailProperties.getProperty("BIZ_IP_SUMMARY_SUBJECT") + "-" + DateUtil.getDate();
		MailUtil mu = new MailUtil();
		recipientsEmails = emailProperties.getProperty("BIZ_IP_CREATION_EMAIL");

		if (result.equals(Constant.SUCCESS)) {
			emailContent = mu.getHtmlContentSummary(statsVO, sourceName);
			/*
			 * processID=Integer.parseInt(Constant.BIZ_IP_CREATION_PROCESS_ID);
			 * updateStatus1= sqlClient.updateProcessStatus(processID, true,
			 * "Completed by automated process.");
			 */

		} else {
			errorMsg = emailProperties.getProperty("BIZ_IP_SUMMARY_ERROR");
			// StringBuffer sqlMessage = new StringBuffer();
			recipientsEmails = emailProperties.getProperty("BIZ_IP_CREATION_ERROR_EMAIL");
			/*
			 * sqlMessage.append("Automated Process ").append(errorMsg);
			 * processID=Integer.parseInt(Constant.BIZ_IP_CREATION_PROCESS_ID);
			 * 
			 * boolean updateStatus2 = sqlClient.updateProcessStatus(processID,
			 * false, sqlMessage.toString()); if (!updateStatus2) {
			 * errorMsg=emailProperties.getProperty("TABLE_UPDATE_FAILURE"); }
			 */
			MessageLogger.printMessage("Drafting mail...");
			emailContent = mu.getErrorHtmlContent(errorHeader, errorMsg);

		}

		mu.sendMail(subject, emailContent, sourceName, recipientsEmails);
		/*
		 * if (!updateStatus1) { recipientsEmails =
		 * emailProperties.getProperty("BIZ_IP_CREATION_ERROR_EMAIL");
		 * MessageLogger.printMessage(
		 * "Drafting Alert for database update failure...");
		 * errorMsg=emailProperties.getProperty("TABLE_UPDATE_FAILURE");
		 * emailContent = mu.getErrorHtmlContent(errorHeader,errorMsg);
		 * mu.sendMail(subject, emailContent, sourceName,recipientsEmails); }
		 */
	}

}
