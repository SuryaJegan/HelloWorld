package com.dnb.client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.dnb.constants.Constant;
import com.dnb.exceptions.IP2DunsException;
import com.dnb.model.IspResultVO;
import com.dnb.model.MatchRateVO;
import com.dnb.model.StatisticsVO;
import com.dnb.process.BizIpCreationProcess;
import com.dnb.process.BizIpMidProcess;
import com.dnb.process.BizIpSummaryProcess;
import com.dnb.process.GlobalMatchRateProcess;
import com.dnb.process.IspResultStatsProcess;
import com.dnb.process.IspStatsProcess;
import com.dnb.util.DateUtil;
import com.dnb.util.ExcelUtil;
import com.dnb.util.MessageLogger;

public class IP2DunsStatsClient {
	static Connection con = null;

	/*
	 * arg[0]=process Id arg[1]=Shell Exit code arg[2]=current date
	 * arg[3]=message/comparison date for biz_ip
	 * 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, AddressException,
			MessagingException, IP2DunsException {
		SqlClient sqlClient = new SqlClient();
		BizIpCreationProcess bizIpCreation = new BizIpCreationProcess();
		Map<String, String> finalResultMap = new LinkedHashMap<>();
		con = DbConnection.getDatabaseConnection();

		String currentDate = "";
		String pattern = "^((20|21)\\d\\d)(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$";
		if (args.length == 3 || args.length == 4) {
			if (args[0] != null && args[1] != null && args[2] != null) {
				if (args[2].matches(pattern)) {
					currentDate = args[2];

					DateUtil.setDate(currentDate);
					if (args[0].equals(Constant.BIZ_IP_FUTURE_MID) && args[1].equals(Constant.SUCCESS)) {

						List<Object> statsVO = sqlClient.StatisticsCount(con, args);
						ExcelUtil excelUtil = new ExcelUtil();
						BizIpMidProcess bizProcess = new BizIpMidProcess();
						excelUtil.writeToExcel(statsVO);
						bizProcess.draftMail(statsVO, args);
						DbConnection.closeDatabseConnection();
						bizIpCreation.draftMail(args);

					} else if (args[0].equals(Constant.ISP_FUTURE_STATS) && args[1].equals(Constant.SUCCESS)) {

						IspStatsProcess ispStatProcess = new IspStatsProcess();
						GlobalMatchRateProcess globalMatchRateProcess = new GlobalMatchRateProcess();
						float globalMatchRate = sqlClient.getPercentGlobalMatchRate(con);
						IspResultStatsProcess ispResultStatsProcess = new IspResultStatsProcess();
						List<Object> statsVO = sqlClient.StatisticsCount(con, args);
						ispStatProcess.draftMail(statsVO, args);
						boolean checkThreshold = ispResultStatsProcess.checkGlobalMatchTheshold(globalMatchRate);
						ArrayList<MatchRateVO> matchVO = sqlClient.getIspMatchRate(con);
						globalMatchRateProcess.draftMail(matchVO, globalMatchRate, args);
						if (checkThreshold) {
							MessageLogger.printMessage("All values are within the threshold");
						} else {
							args[1] = Constant.ISP_FLAG_FAILURE_CODE;
							MessageLogger.printMessage("All values are not within the threshold");
						}
						DbConnection.closeDatabseConnection();
						bizIpCreation.draftMail(args);

					} else if (args[0].equals(Constant.BIZ_IP_COMP) && args[3] != null
							&& args[1].equals(Constant.SUCCESS)) {

						StatisticsVO statsVO = sqlClient.SummaryStatsCount(con, args);
						BizIpSummaryProcess bizSummary = new BizIpSummaryProcess();
						bizSummary.draftMail(statsVO, args);
						DbConnection.closeDatabseConnection();
						bizIpCreation.draftMail(args);
					} else if (args[0].equals(Constant.VERIFY_ISP_RESULT) && args[1].equals(Constant.SUCCESS)) {

						IspResultVO ispResultVO = sqlClient.IspResultStat(con, args);
						System.out.println(ispResultVO.getIspCount());
						IspResultStatsProcess ispResultStatsProcess = new IspResultStatsProcess();
						finalResultMap = ispResultStatsProcess.getIspModelResultDetails();
						boolean thresholdCheck = ispResultStatsProcess.checkIspResultThreshold(ispResultVO,
								finalResultMap);
						ispResultStatsProcess.draftMail(ispResultVO, finalResultMap, args);
						if (thresholdCheck) {
							MessageLogger.printMessage("All values are within the threshold");
						} else {
							args[1] = Constant.ISP_FAILURE_CODE;
							MessageLogger.printMessage("All values are not within the threshold");
						}
						bizIpCreation.draftMail(args);
						DbConnection.closeDatabseConnection();
					} else {
						bizIpCreation.draftMail(args);
					}

				} else {
					MessageLogger.printMessage("Arguments are not in YYYYMMDD format");
				}
			}

		}
	}

}
