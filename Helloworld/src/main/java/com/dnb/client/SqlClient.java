package com.dnb.client;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.dnb.constants.Constant;
import com.dnb.model.BizCompVO;
import com.dnb.model.BizVO;
import com.dnb.model.IspFutureVO;
import com.dnb.model.IspKeyVO;
import com.dnb.model.IspResultVO;
import com.dnb.model.MatchRateVO;
import com.dnb.model.StatisticsVO;
import com.dnb.util.MessageLogger;

public class SqlClient {

	private Properties statsproperty = null;
	private Properties sourceproperty = null;
	private Properties processmappingproperty = null;

	public SqlClient() throws IOException {
		InputStream is = null;

		this.statsproperty = new Properties();
		is = this.getClass().getResourceAsStream("/StatisticsQuery.properties");
		statsproperty.load(is);

		this.sourceproperty = new Properties();
		is = this.getClass().getResourceAsStream("/SourceTablesDetails.properties");
		sourceproperty.load(is);

		this.processmappingproperty = new Properties();
		is = this.getClass().getResourceAsStream("/processMapping.properties");
		processmappingproperty.load(is);

	}

	public List<Object> StatisticsCount(Connection dbCon, String[] args) throws IOException, SQLException {
		List<Object> bizIpCount;
		bizIpCount = new ArrayList<Object>();
		String key = args[0];
		if (statsproperty.containsKey(key)) {
			if (key.equals(Constant.BIZ_IP_FUTURE_MID)) {

				MessageLogger.printMessage("Collecting data for " + processmappingproperty.getProperty(key));
				String value = statsproperty.getProperty(key);
				PreparedStatement query = dbCon.prepareStatement(value);
				ResultSet rs = query.executeQuery();

				while (rs.next()) {
					BizVO bizVo = new BizVO();
					bizVo.setCntryName(rs.getString("cntry"));
					bizVo.setBizIpFutureDunsCount(rs.getInt("biz_ip_future_duns"));
					bizVo.setBizIpDunsCount(rs.getInt("biz_ip_duns"));
					bizVo.setOverlappingDunsSum(rs.getInt("overlapping_duns"));
					bizVo.setPercentOfPrior(rs.getInt("percent_of_prior"));
					bizIpCount.add(bizVo);
				}

			}
			if (key.equals(Constant.ISP_FUTURE_STATS)) {

				MessageLogger.printMessage("Collecting data for " + processmappingproperty.getProperty(key));
				String value = statsproperty.getProperty(key);
				PreparedStatement query = dbCon.prepareStatement(value);
				ResultSet rs = query.executeQuery();

				while (rs.next()) {
					IspFutureVO ispFutureVo = new IspFutureVO();
					ispFutureVo.setIspFlag(rs.getBoolean("isp_flag"));
					ispFutureVo.setIspFlagReason(rs.getString("isp_flag_reason"));
					ispFutureVo.setIpRanges(rs.getInt("ip_ranges"));
					ispFutureVo.setIpRangesFuture(rs.getInt("ip_ranges_future"));
					ispFutureVo.setIpRangesChange(rs.getInt("ip_range_change"));
					ispFutureVo.setDunsCount(rs.getInt("duns"));
					ispFutureVo.setDunsFutureCount(rs.getInt("duns_future"));
					ispFutureVo.setDunsChangeCount(rs.getInt("duns_change"));
					bizIpCount.add(ispFutureVo);
				}
			}

		}

		return bizIpCount;
	}

	public StatisticsVO SummaryStatsCount(Connection dbCon, String[] args) throws IOException, SQLException {
		StatisticsVO statsVo = new StatisticsVO();
		String key = args[0];
		String compDate = args[3];
		StringBuilder compareDate = new StringBuilder();
		compareDate.append(compDate.substring(0, 4)).append("_").append(compDate.substring(4, 6));
		if (key.equals(Constant.BIZ_IP_COMP)) {
			ArrayList<BizCompVO> bizIpCount = new ArrayList<BizCompVO>();
			ArrayList<IspKeyVO> ispKeyVoCount = new ArrayList<IspKeyVO>();
			MessageLogger.printMessage("Collecting data for " + processmappingproperty.getProperty(key));
			String value = statsproperty.getProperty(key);
			String sqlvalue = value.replace("<YYYYMM>", compareDate.toString());
			PreparedStatement query = dbCon.prepareStatement(sqlvalue);
			ResultSet rs = query.executeQuery();

			while (rs.next()) {
				BizCompVO bizCompVo = new BizCompVO();
				bizCompVo.setSource(rs.getString("source"));
				bizCompVo.setRanges(rs.getInt("ranges"));
				bizCompVo.setIpaddresses(rs.getBigDecimal("ip_addresses"));
				bizCompVo.setRangeswduns(rs.getInt("ranges_w_duns"));
				bizCompVo.setRangesnonisp(rs.getInt("ranges_nonisp"));
				bizCompVo.setRangesisp(rs.getInt("ranges_isp"));
				bizIpCount.add(bizCompVo);
			}
			statsVo.setBizCompList(bizIpCount);

			String value2 = statsproperty.getProperty("isp_key_status");
			PreparedStatement query2 = dbCon.prepareStatement(value2);
			ResultSet rs2 = query2.executeQuery();

			while (rs2.next()) {
				IspKeyVO ispKeyVo = new IspKeyVO();
				ispKeyVo.setIpKeyStatus(rs2.getString("ip_key_status"));
				ispKeyVo.setIpKeyCount(rs2.getInt("ip_key_count"));
				ispKeyVo.setPriorDunsCount(rs2.getInt("prior_duns_count"));
				ispKeyVo.setNewDunsount(rs2.getInt("new_duns_count"));
				ispKeyVoCount.add(ispKeyVo);
			}
			statsVo.setIspKeyList(ispKeyVoCount);

		}
		return statsVo;
	}

	public IspResultVO IspResultStat(Connection con, String[] args) throws SQLException {

		IspResultVO ispResultVO = new IspResultVO();
		String key = args[0];
		MessageLogger.printMessage("Collecting data for " + processmappingproperty.getProperty(key));
		MessageLogger.printMessage("Fetching training set count");
		String queryString = statsproperty.getProperty("training_set_count");
		PreparedStatement query = con.prepareStatement(queryString);
		ResultSet rs = query.executeQuery();
		while (rs.next()) {
			ispResultVO.setTrainingSetCount(rs.getInt("trainingSetCount"));

		}
		MessageLogger.printMessage("Fetching isp ratio");
		String queryString2 = statsproperty.getProperty("isp_ratio");
		PreparedStatement query2 = con.prepareStatement(queryString2);
		ResultSet rs2 = query2.executeQuery();
		while (rs2.next()) {
			ispResultVO.setNonIspCount(rs2.getInt("NonIsp"));
			ispResultVO.setIspCount(rs2.getInt("Isp"));
			ispResultVO.setIspNonIspRatio(rs2.getFloat("Ratio_ISPoverNonISP"));
		}

		MessageLogger.printMessage("Fetching test set count");
		String queryString3 = statsproperty.getProperty("test_set_count");
		PreparedStatement query3 = con.prepareStatement(queryString3);
		ResultSet rs3 = query3.executeQuery();
		while (rs3.next()) {
			ispResultVO.setTestSetCount(rs3.getInt("testSetCount"));
		}
		MessageLogger.printMessage("Fetching top 200 isp|non isp count");
		String queryString4 = statsproperty.getProperty("top_isp_non_isp_count");
		PreparedStatement query4 = con.prepareStatement(queryString4);
		ResultSet rs4 = query4.executeQuery();
		while (rs4.next()) {
			ispResultVO.setPredictedIsp(rs4.getInt("predictedISP"));
			ispResultVO.setPredictedNonIsp(rs4.getInt("predictedNonISP"));
		}

		return ispResultVO;
	}

	public boolean updateProcessStatus(int processID, boolean flag, String processStatus, String message) {
		MessageLogger.printMessage("Process for updating Process status tracker table started...");
		boolean updateStatus = true;
		Connection con = null;
		String query = null;
		long resPstID = 0;
		int count = 0;
		String status = null;
		PreparedStatement statement = null;
		PreparedStatement statement2 = null;
		PreparedStatement statement3 = null;
		if (flag) {
			status = "Completed";
		} else {

			if (processStatus.equals(Constant.HOLD_PROCESS)) {
				status = "On-Hold";
			} else {
				status = "Failed";
			}
		}
		try {
			MessageLogger.printMessage("Looking for process with process ID " + processID);
			con = DbConnection.getDatabaseConnection();
			query = statsproperty.getProperty("check_process_status");
			statement = con.prepareStatement(query);
			statement.setInt(1, processID);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				resPstID = result.getLong("pst_id");
				count++;
			}
			if (count == 1) {
				MessageLogger.printMessage("Process with process id " + processID + " exist...");
				MessageLogger.printMessage("Updating process with process id " + processID);
				query = statsproperty.getProperty("update_process_status");
				statement2 = con.prepareStatement(query);
				statement2.setString(1, status);
				statement2.setString(2, message);
				statement2.setLong(3, resPstID);
				int update = statement2.executeUpdate();
				if (update == 1) {
					MessageLogger.printMessage("Updated successfully");
				}

			} else if (count == 0) {
				MessageLogger.printMessage("Process with process id " + processID + " doesn't exist...");
				MessageLogger.printMessage("Inserting process with process id " + processID);
				query = statsproperty.getProperty("insert_process_status");
				statement3 = con.prepareStatement(query);
				statement3.setInt(1, processID);
				statement3.setString(2, status);
				statement3.setString(3, message);
				int update = statement3.executeUpdate();
				if (update == 1) {
					MessageLogger.printMessage("Inserted successfully");
				}

			} else if (count > 1) {
				MessageLogger.printMessage("Process with process id " + processID + "has multiple entries...");
				updateStatus = false;
			}
		} catch (ClassNotFoundException e1) {
			MessageLogger.printMessage(e1.getMessage());
			updateStatus = false;
		} catch (SQLException e2) {
			updateStatus = false;
			MessageLogger.printMessage(e2.getMessage());
		} finally {
			try {
				if (null != statement) {
					statement.close();
				}
				if (null != statement2) {
					statement2.close();
				}
				if (null != statement3) {
					statement3.close();
				}
				DbConnection.closeDatabseConnection();
			} catch (SQLException e) {
				MessageLogger.printMessage("Error: " + e.getMessage());
			}
		}
		return updateStatus;

	}

	public ArrayList<MatchRateVO> getIspMatchRate(Connection con) throws ClassNotFoundException, SQLException {

		ArrayList<MatchRateVO> matchVO = new ArrayList<>();
		MessageLogger.printMessage("Fetching customer match rate");
		String queryString = statsproperty.getProperty("customer_match_rate");
		PreparedStatement query = con.prepareStatement(queryString);
		ResultSet rs = query.executeQuery();
		while (rs.next()) {
			MatchRateVO matchRateVO = new MatchRateVO();
			matchRateVO.setCustomerId(rs.getInt("customer_id"));
			matchRateVO.setTotalVisits(rs.getInt("total_visits"));
			matchRateVO.setMatchedVisits(rs.getInt("Matched_visits"));
			matchRateVO.setExpectedMatch(rs.getInt("Expected_match"));
			matchRateVO.setOldPercentMatchRate(rs.getFloat("OldPercent_Match_Rate"));
			matchRateVO.setExpectedPercentMatchRate(rs.getFloat("ExpectedPercent_Match_Rate"));
			matchRateVO.setNetPercentDifference(rs.getFloat("NetPercent_Difference"));
			matchVO.add(matchRateVO);
		}

		return matchVO;

	}

	public float getPercentGlobalMatchRate(Connection con) throws ClassNotFoundException, SQLException {

		MessageLogger.printMessage("Collecting data for global match rate");
		MessageLogger.printMessage("Pulling ISP flag into vi_logs_current from biz_ip");
		float globalMatchRate = 0;
		String queryString = statsproperty.getProperty("add_isp_flag");
		PreparedStatement query = con.prepareStatement(queryString);
		query.executeUpdate();
		MessageLogger.printMessage("Vi_logs_current table altered successfully");
		MessageLogger.printMessage("Updating isp flags in Vi_logs_current");
		String queryString2 = statsproperty.getProperty("update_isp_flag_vi_logs");
		PreparedStatement query2 = con.prepareStatement(queryString2);
		query2.executeUpdate();
		MessageLogger.printMessage("Isp flags updated in Vi_logs_current successfully");
		MessageLogger.printMessage("Calculating global match rate percent");
		String queryString3 = statsproperty.getProperty("percent_global_match_rate");
		PreparedStatement query3 = con.prepareStatement(queryString3);
		ResultSet rs = query3.executeQuery();
		while (rs.next()) {
			globalMatchRate = rs.getFloat("percent_global_matchrate");

		}

		return globalMatchRate;

	}

}
