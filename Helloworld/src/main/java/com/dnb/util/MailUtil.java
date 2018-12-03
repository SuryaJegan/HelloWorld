package com.dnb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.dnb.constants.Constant;
import com.dnb.exceptions.IP2DunsException;
import com.dnb.model.BizCompVO;
import com.dnb.model.IspFutureVO;
import com.dnb.model.IspKeyVO;
import com.dnb.model.IspResultVO;
import com.dnb.model.MatchRateVO;
import com.dnb.model.StatisticsVO;
import com.dnb.model.ThresholdVO;
import com.dnb.process.ThresholdStatsProcess;

public class MailUtil {

	private Properties emailProperties = null;
	static int noOfTimesFailed = 0;

	BodyPart messageBodyPart = new MimeBodyPart();

	public MailUtil() throws IOException {

		this.emailProperties = new Properties();
		emailProperties.load(this.getClass().getResourceAsStream("/EmailProperties.properties"));

	}

	public void sendMail(String subject, String content, String sourceName, String recipientsEmails)
			throws AddressException, MessagingException, IOException, IP2DunsException {

		// Create a Properties object to contain connection configuration
		// information.
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		if (noOfTimesFailed > 0) {
			props.put("mail.smtp.port", emailProperties.getProperty("ALTERNATE_PORT"));
		} else {
			props.put("mail.smtp.port", emailProperties.getProperty("PORT"));
		}
		// Set properties indicating that we want to use STARTTLS to encrypt the
		// connection.
		// The SMTP session will begin on an unencrypted connection, and then
		// the client will issue a STARTTLS command to upgrade to an encrypted
		// connection.

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		// Create a Session object to represent a mail session with the
		// specified properties.

		Session session = Session.getDefaultInstance(props);

		// Create a message with the specified information
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(emailProperties.getProperty("From")));
		// String recipientsEmails =
		// emailProperties.getProperty("RecipientsEmail");
		StringTokenizer st = new StringTokenizer(recipientsEmails, ",");
		while (st.hasMoreElements()) {
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(st.nextElement().toString()));
		}

		msg.setSubject(subject);
		MimeMultipart multipart = new MimeMultipart("related");

		// first part (the html)
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(content, "text/html");
		multipart.addBodyPart(messageBodyPart);

		if (sourceName.equals(Constant.DUNS_COMPARISON) && !(content.contains(Constant.FAILURE))) {
			String excelFileName = Constant.FILE_NAME.replace("<MMM_YYYY>", DateUtil.getFileDate());
			String path = Constant.PATH + excelFileName;
			addAttachment(multipart, path, excelFileName);
		}

		// second part (the image)
		messageBodyPart = new MimeBodyPart();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		DataSource ds = new ByteArrayDataSource(classLoader.getResourceAsStream("logo.jpg"), "image/jpeg");
		messageBodyPart.setDataHandler(new DataHandler(ds));
		messageBodyPart.setHeader("Content-Type", "image/jpeg; name=image.jpg");
		messageBodyPart.setHeader("Content-ID", "<image>");
		messageBodyPart.setHeader("Content-Disposition", "inline");
		multipart.addBodyPart(messageBodyPart);
		msg.setContent(multipart);
		// Create a transport.
		Transport transport = session.getTransport();
		try {
			System.out.println("Sending Email....!!");

			// Connect to Amazon SES using the SMTP username and password you
			// specified above.
			transport.connect(emailProperties.getProperty("HOST"), emailProperties.getProperty("SMTP_USERNAME"),
					emailProperties.getProperty("SMTP_PASSWORD"));

			// Send the email
			transport.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Email sent!");
			noOfTimesFailed = 0;
		} catch (MessagingException ex) {
			noOfTimesFailed++;
			if (noOfTimesFailed <= Constant.EMAIL_ATTEMPTS) {
				MessageLogger.printMessage("Resending messsage with different port again");
				sendMail(subject, content, sourceName, recipientsEmails);
			} else {
				MessageLogger.printMessage("The email was not sent.");
				MessageLogger.printMessage("Error message: " + ex.getMessage());
			}
		} finally {
			transport.close();
		}

	}

	public void addAttachment(MimeMultipart multipart, String filename, String fileName) throws MessagingException {
		DataSource source = new FileDataSource(filename);
		messageBodyPart = new MimeBodyPart();
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(fileName);
		multipart.addBodyPart(messageBodyPart);

	}

	public String getHtmlContent(List<Object> statsVOList, String sourceName) throws IOException {
		String htmlContent = "";
		if (sourceName.equals(Constant.ISP_FUTURE_STATS)) {
			InputStream in = this.getClass().getResourceAsStream("/header.html");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuffer sb = new StringBuffer();
			String curentLine;
			while ((curentLine = reader.readLine()) != null) {
				sb.append(curentLine);
			}
			Iterator<Object> itr = statsVOList.iterator();
			while (itr.hasNext()) {
				IspFutureVO ispFutureVO = (IspFutureVO) itr.next();
				String isp_flag = String.valueOf(ispFutureVO.isIspFlag());
				String isp_flag_reason = ispFutureVO.getIspFlagReason();
				String duns_change = String.valueOf(ispFutureVO.getDunsChangeCount());
				String duns = String.valueOf(ispFutureVO.getDunsCount());
				String duns_future = String.valueOf(ispFutureVO.getDunsFutureCount());
				String ip_ranges = String.valueOf(ispFutureVO.getIpRanges());
				String ip_ranges_change = String.valueOf(ispFutureVO.getIpRangesChange());
				String ip_ranges_future = String.valueOf(ispFutureVO.getIpRangesFuture());

				StringBuffer msgBuffer = new StringBuffer();
				in = this.getClass().getResourceAsStream("/message.html");
				reader = new BufferedReader(new InputStreamReader(in));
				while ((curentLine = reader.readLine()) != null) {
					msgBuffer.append(curentLine);
				}
				sb.append(msgBuffer.toString().replace("@(isp_flag)", isp_flag == null ? "" : isp_flag)
						.replace("@(isp_flag_reason)", isp_flag_reason == null ? "" : isp_flag_reason)
						.replace("@(duns)", duns == null ? "" : duns)
						.replace("@(duns_change)", duns_change == null ? "" : duns_change)
						.replace("@(duns_future)", duns_future == null ? "" : duns_future)
						.replace("@(ip_ranges)", ip_ranges == null ? "" : ip_ranges)
						.replace("@(ip_ranges_change)", ip_ranges_change == null ? "" : ip_ranges_change)
						.replace("@(ip_ranges_future)", ip_ranges_future == null ? "" : ip_ranges_future));
			}
			in = this.getClass().getResourceAsStream("/footer.html");
			reader = new BufferedReader(new InputStreamReader(in));
			while ((curentLine = reader.readLine()) != null) {
				sb.append(curentLine);
			}
			htmlContent = new String(sb);
			reader.close();

		} else if (sourceName.equals(Constant.BIZ_IP_FUTURE_MID)) {
			InputStream in = this.getClass().getResourceAsStream("/bizHeader.html");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuffer sb = new StringBuffer();
			String curentLine;
			while ((curentLine = reader.readLine()) != null) {
				sb.append(curentLine);
			}
			StringBuffer msgBuffer = new StringBuffer();
			in = this.getClass().getResourceAsStream("/bizMessage.html");
			reader = new BufferedReader(new InputStreamReader(in));
			while ((curentLine = reader.readLine()) != null) {
				msgBuffer.append(curentLine);
			}
			sb.append(msgBuffer.toString());

			in = this.getClass().getResourceAsStream("/bizFooter.html");
			reader = new BufferedReader(new InputStreamReader(in));
			while ((curentLine = reader.readLine()) != null) {
				sb.append(curentLine);
			}
			htmlContent = new String(sb);
			reader.close();

		}

		return htmlContent.replace("@(MONTH_YEAR)", DateUtil.getDate()).replace("@(sourcName)", sourceName);
	}

	public String getHtmlContentSummary(StatisticsVO statsVo, String sourceName) throws IOException {
		String htmlContent = "";
		if (sourceName.equals(Constant.BIZ_IP_COMP)) {
			InputStream in = this.getClass().getResourceAsStream("/ispHeader.html");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuffer sb = new StringBuffer();
			String curentLine;

			while ((curentLine = reader.readLine()) != null) {
				sb.append(curentLine);
			}
			ArrayList<IspKeyVO> statsVOList = statsVo.getIspKeyList();
			Iterator<IspKeyVO> itr = statsVOList.iterator();
			while (itr.hasNext()) {
				IspKeyVO isKeyVO = itr.next();
				String ip_key_status = isKeyVO.getIpKeyStatus();
				int ip_key_count = isKeyVO.getIpKeyCount();
				int prior_duns_count = isKeyVO.getPriorDunsCount();
				int new_duns_count = isKeyVO.getNewDunsount();

				StringBuffer msgBuffer = new StringBuffer();
				in = this.getClass().getResourceAsStream("/ispMessage.html");
				reader = new BufferedReader(new InputStreamReader(in));
				while ((curentLine = reader.readLine()) != null) {
					msgBuffer.append(curentLine);
				}
				sb.append(msgBuffer.toString().replace("@(ip_key_status)", ip_key_status)
						.replace("@(ip_key_count)", Integer.toString(ip_key_count))
						.replace("@(prior_duns_count)", Integer.toString(prior_duns_count))
						.replace("@(new_duns_count)", Integer.toString(new_duns_count)));
			}

			in = this.getClass().getResourceAsStream("/bizIpCompareHeader.html");
			reader = new BufferedReader(new InputStreamReader(in));
			while ((curentLine = reader.readLine()) != null) {
				sb.append(curentLine);
			}

			ArrayList<BizCompVO> statsVOList2 = statsVo.getBizCompList();
			Iterator<BizCompVO> itr2 = statsVOList2.iterator();
			while (itr2.hasNext()) {
				BizCompVO bizCompVO = itr2.next();
				String source = bizCompVO.getSource();
				int ranges = bizCompVO.getRanges();
				String ip_addresses = bizCompVO.getIpaddresses().toString();
				int ranges_w_duns = bizCompVO.getRangeswduns();
				int ranges_nonisp = bizCompVO.getRangesnonisp();
				int ranges_isp = bizCompVO.getRangesisp();
				StringBuffer msgBuffer = new StringBuffer();
				in = this.getClass().getResourceAsStream("/bizIpCompareMessage.html");
				reader = new BufferedReader(new InputStreamReader(in));
				while ((curentLine = reader.readLine()) != null) {
					msgBuffer.append(curentLine);
				}

				sb.append(msgBuffer.toString().replace("@(source)", source)
						.replace("@(ranges)", Integer.toString(ranges)).replace("@(ip_addresses)", ip_addresses)
						.replace("@(ranges_w_duns)", Integer.toString(ranges_w_duns))
						.replace("@(ranges_nonisp)", Integer.toString(ranges_nonisp))
						.replace("@(ranges_isp)", Integer.toString(ranges_isp)));
			}

			in = this.getClass().getResourceAsStream("/ispFooter.html");
			reader = new BufferedReader(new InputStreamReader(in));
			while ((curentLine = reader.readLine()) != null) {
				sb.append(curentLine);
			}
			htmlContent = new String(sb);
			reader.close();

		}
		return htmlContent.replace("@(MONTH_YEAR)", DateUtil.getDate()).replace("@(sourcName)", sourceName);

	}

	public String getErrorHtmlContent(String errorHeader, String content) throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/errorMessage.html");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer sb = new StringBuffer();
		String curentLine;
		while ((curentLine = reader.readLine()) != null) {
			sb.append(curentLine);
		}
		String htmlContent = new String(sb);
		reader.close();
		return htmlContent.replace("@(ERROR_HEADER)", errorHeader).replace("@(content)", content);
	}

	public String getBizIPHtmlContent(String alertMsg) throws IOException {
		InputStream in = this.getClass().getResourceAsStream("/nextgenMatchedMessage.html");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer sb = new StringBuffer();
		String curentLine;
		while ((curentLine = reader.readLine()) != null) {
			sb.append(curentLine);
		}
		String htmlContent = new String(sb);
		reader.close();
		return htmlContent.replace("@(MATCH_DETAIL)", alertMsg);
	}

	public String getNotificationHtmlContent(IspResultVO ispResultVO, Map<String, String> finalResultMap)
			throws IOException {

		String htmlContent = "";
		ThresholdStatsProcess thresholdStatsProcess = new ThresholdStatsProcess();
		InputStream in = this.getClass().getResourceAsStream("/ispNotificationHeader.html");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer sb = new StringBuffer();
		String curentLine;

		while ((curentLine = reader.readLine()) != null) {
			sb.append(curentLine);
		}
		ThresholdVO thresholdVO = thresholdStatsProcess.getThresholdColor(finalResultMap, ispResultVO);
		String training_set_count = String.valueOf(ispResultVO.getTrainingSetCount());
		String test_set_count = String.valueOf(ispResultVO.getTestSetCount());
		String training_isp = String.valueOf(ispResultVO.getIspCount());
		String training_non_isp = String.valueOf(ispResultVO.getNonIspCount());
		String isp_ratio = String.valueOf(ispResultVO.getIspNonIspRatio());
		String tp = finalResultMap.get(Constant.TP);
		String fp = finalResultMap.get(Constant.FP);
		String fn = finalResultMap.get(Constant.FN);
		String tn = finalResultMap.get(Constant.TN);
		String correct = finalResultMap.get(Constant.CORRECT);
		String wrong = finalResultMap.get(Constant.WRONG);
		String percent = finalResultMap.get(Constant.PERCENT_RATIO);
		String recall = finalResultMap.get(Constant.RECALL);
		String foRate = finalResultMap.get(Constant.FOR);
		String isp = finalResultMap.get(Constant.ISP);
		String nonIsp = finalResultMap.get(Constant.NON_ISP);
		String trainingSetColor = thresholdVO.getTrainingSetColor();
		String ispRatioColor = thresholdVO.getIspRatioColor();
		String percentRatioColor = thresholdVO.getPercentRatioColor();
		String recallColor = thresholdVO.getRecallColor();
		String forColor = thresholdVO.getForColor();
		String predictedIspRatioColor = thresholdVO.getPredictedIspRatioColor();
		String predictedNonIspColor = thresholdVO.getPredictedNonIspColor();
		String predictedIsp = String.valueOf(ispResultVO.getPredictedIsp());
		String predictedNonIsp = String.valueOf(ispResultVO.getPredictedNonIsp());
		String predictedIspRatio = String.valueOf(thresholdVO.getPredictedIspRatio());
		StringBuffer msgBuffer = new StringBuffer();
		String status = thresholdVO.getStatus();

		in = this.getClass().getResourceAsStream("/ispNotificationMessage.html");
		reader = new BufferedReader(new InputStreamReader(in));
		while ((curentLine = reader.readLine()) != null) {
			msgBuffer.append(curentLine);
		}

		sb.append(msgBuffer.toString().replace("@(training_set_count)", training_set_count)
				.replace("@(test_set_count)", test_set_count)
				.replace("@(isp_ratio_color)", thresholdVO.getIspRatioColorName())
				.replace("@(training_set_color)", thresholdVO.getTrainingSetColorName())
				.replace("@(predicted_isp_ratio_color)", thresholdVO.getPredictedIspRatioColorName())
				.replace("@(training_isp)", training_isp).replace("@(training_non_isp)", training_non_isp)
				.replace("@(isp_ratio)", isp_ratio).replace("@(tp)", tp).replace("@(fp)", fp).replace("@(fn)", fn)
				.replace("@(tn)", tn).replace("@(correct)", correct).replace("@(wrong)", wrong)
				.replace("@(percent)", percent).replace("@(recall)", recall).replace("@(for)", foRate)
				.replace("@(isp)", isp).replace("@(nonisp)", nonIsp).replace("@(predictedisp)", predictedIsp)
				.replace("@(predictednonisp)", predictedNonIsp).replace("@(training_color)", trainingSetColor)
				.replace("@(isp_color)", ispRatioColor).replace("@(predicted_isp_ratio)", predictedIspRatioColor)
				.replace("@(percent_color)", percentRatioColor).replace("@(recall_color)", recallColor)
				.replace("@(for_color)", forColor).replace("@(predictedRatio)", predictedIspRatio)
				.replace("@(overall_status)", status).replace("@(status_color)", thresholdVO.getStatusColor())
				.replace("@(predicted_non_isp_color)", thresholdVO.getPredictedNonIspColorName())
				.replace("@(predicted_nonisp_color)", predictedNonIspColor));

		in = this.getClass().getResourceAsStream("/ispNotificationFooter.html");
		reader = new BufferedReader(new InputStreamReader(in));
		while ((curentLine = reader.readLine()) != null) {
			sb.append(curentLine);
		}
		htmlContent = new String(sb);
		reader.close();

		return htmlContent;
	}

	public String getGlobalMatchHtmlContent(ArrayList<MatchRateVO> matchVO, float globalMatchRate) throws IOException {
		String htmlContent = "";
		ThresholdStatsProcess thresholdStatsProcess = new ThresholdStatsProcess();
		String globalMatchRateColorName = null;
		InputStream in = this.getClass().getResourceAsStream("/globalMatchNotificationHeader.html");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer sb = new StringBuffer();
		StringBuffer headMsg = new StringBuffer();
		String curentLine;

		while ((curentLine = reader.readLine()) != null) {
			headMsg.append(curentLine);
		}

		String globalMatchRateColor = thresholdStatsProcess.getGlobalColor(globalMatchRate);

		if (globalMatchRateColor.equals(Constant.RED_COLOR)) {
			globalMatchRateColorName = Constant.RED;
		} else if (globalMatchRateColor.equals(Constant.ORANGE_COLOR)) {
			globalMatchRateColorName = Constant.ORANGE;
		} else {
			globalMatchRateColorName = Constant.GREEN;
		}
		sb.append(headMsg.toString().replace("@(status_color)", globalMatchRateColorName)
				.replace("@(global_match_rate)", String.valueOf(globalMatchRate))
				.replace("@(overall_status)", globalMatchRateColor));
		Iterator<MatchRateVO> iterator = matchVO.iterator();
		while (iterator.hasNext()) {
			MatchRateVO matchRateVO = iterator.next();
			String customerID = String.valueOf(matchRateVO.getCustomerId());
			String totalVists = String.valueOf(matchRateVO.getTotalVisits());
			String matchedVisits = String.valueOf(matchRateVO.getMatchedVisits());
			String expectedMatch = String.valueOf(matchRateVO.getExpectedMatch());
			String oldPercentMatchRate = String.valueOf(matchRateVO.getOldPercentMatchRate());
			String expectedPercentMatchRate = String.valueOf(matchRateVO.getExpectedPercentMatchRate());
			String netPercentDifference = String.valueOf(matchRateVO.getNetPercentDifference());
			StringBuffer msgBuffer = new StringBuffer();
			in = this.getClass().getResourceAsStream("/globalMatchNotificationMessage.html");
			reader = new BufferedReader(new InputStreamReader(in));
			while ((curentLine = reader.readLine()) != null) {
				msgBuffer.append(curentLine);
			}
			sb.append(msgBuffer.toString().replace("@(customer_id)", customerID).replace("@(total_visits)", totalVists)
					.replace("@(match_visits)", matchedVisits).replace("@(expected_match)", expectedMatch)
					.replace("@(old_match_rate)", oldPercentMatchRate)
					.replace("@(expected_match_rate)", expectedPercentMatchRate)
					.replace("@(net_match_difference)", netPercentDifference));
		}
		in = this.getClass().getResourceAsStream("/footer.html");
		reader = new BufferedReader(new InputStreamReader(in));
		while ((curentLine = reader.readLine()) != null) {
			sb.append(curentLine);
		}
		htmlContent = new String(sb);
		reader.close();

		return htmlContent;
	}

}
