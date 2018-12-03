package com.dnb.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.dnb.constants.Constant;

public class DbConnection {

	static Connection con = null;

	public static Connection getDatabaseConnection() throws ClassNotFoundException, SQLException {
		Class.forName(Constant.POSTGRESQL_DRIVER);
		String connectionURL = Constant.DB_URL + Constant.SCHEMA;
		con = DriverManager.getConnection(connectionURL, Constant.DBUSERNAME, Constant.PASSWORD);

		System.out.println("Connection Established");
		return con;
	}

	public static void closeDatabseConnection() throws SQLException {
		if (con != null) {
			con.close();

		}
		System.out.println("Connection Closed");
	}
}
