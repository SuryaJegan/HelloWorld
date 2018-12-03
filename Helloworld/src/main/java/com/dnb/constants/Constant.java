package com.dnb.constants;

public final class Constant {

	private Constant() {

	}

	public static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
	public static final String DB_URL = "jdbc:postgresql://10.241.4.56:5439/dev?currentSchema=";
	public static final String DBUSERNAME = "etl_user";
	public static final String PASSWORD = "Adsoln@123";
	public static final String PATH = "src/files/";
	public static final String SCHEMA = "public";
	public static final String FILE_NAME = "biz_ip_mid_<MMM_YYYY>.xlsx";
	public static final String SUCCESS = "0";
	public static final String FAILURE = "failed";
	public static final String DUNS_COMPARISON = "201";
	public static final String BIZ_IP_FUTURE_MID = "16";
	public static final String ISP_FUTURE_STATS = "35";
	public static final String BIZ_IP_COMP = "17";
	public static final String TRAINING_MODEL = "37";
	public static final int EMAIL_ATTEMPTS = 3;
	public static final String HOLD_PROCESS = "108";
	public static final String VERIFY_ISP_RESULT = "39";
	public static final String ISP_FAILURE_CODE = "127";
	public static final int TRAINING_SET_COUNT_UPPER_LIMIT = 10000;
	public static final int TRAINING_SET_COUNT_LOWER_LIMIT = 8000;
	public static final float ISP_NON_ISP_RATIO_LOWER_LIMIT = 25;
	public static final float ISP_NON_ISP_RATIO_UPPER_LIMIT = 60;
	public static final float ISP_NON_ISP_RATIO_UPPER_LIMIT_1 = 30;
	public static final float PREDICTED_NON_ISP_LOWER_LIMIT = 30;
	public static final float PREDICTED_NON_ISP_UPPER_LIMIT = 40;
	public static final float PREDICTED_ISP_RATIO_UPPER_LIMIT = 55;
	public static final float PREDICTED_ISP_RATIO_LOWER_LIMIT = 45;
	public static final float PERCENT_RATIO_LOWER_LIMIT = 7;
	public static final float PERCENT_RATIO_UPPER_LIMIT = 10;
	public static final float RECALL_UPPER_LIMIT = 85;
	public static final float RECALL_LOWER_LIMIT = 70;
	public static final float FALSE_OMISSION_LOWER_LIMIT = 7;
	public static final float FALSE_OMISSION_UPPER_LIMIT = 10;
	public static final float GLOBAL_MATCH_LOWER_LIMIT_1 = 16;
	public static final float GLOBAL_MATCH_UPPER_LIMIT_1 = 17;
	public static final float GLOBAL_MATCH_LOWER_LIMIT_2 = 24;
	public static final float GLOBAL_MATCH_UPPER_LIMIT_2 = 26;
	public static final String TP = "TP_1";
	public static final String FP = "FP";
	public static final String TN = "TN_0";
	public static final String FN = "FN";
	public static final String CORRECT = "CORRECT";
	public static final String WRONG = "WRONG";
	public static final String PERCENT_RATIO = "PercentRatio";
	public static final String RECALL = "Recall_Sensitivitiy";
	public static final String FOR = "False_Omission_Rate_FOR";
	public static final String ISP = "1";
	public static final String NON_ISP = "0";	
	public static final String ORANGE = "#ffa500";
	public static final String RED = "#ff0000";
	public static final String GREEN = "#008000";
	public static final String ORANGE_COLOR = "Orange";
	public static final String RED_COLOR = "Red";
	public static final String GREEN_COLOR = "Green";
	public static final String TRAINING_MODEL_FILENAME ="/data/automation/ip2duns_creation/output/model_verification_result/model_verification_result.txt";
	public static final String ISP_FLAG_FAILURE_CODE = "128";
}
