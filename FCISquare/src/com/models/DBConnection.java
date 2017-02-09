package com.models;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
	private static Connection connection = null;
	/**
	 * establish the connection to the database or the openshift 
	 * @return
	 */

	public static Connection getActiveConnection() {
	
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			
			//connection = DriverManager
				//.getConnection("jdbc:mysql://127.3.133.2:3306/ongoingcommunication_new?"
					//		+ "user=adminECueurb&password=fG8X_CPy75VM&characterEncoding=utf8");
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ongoingcommunication_new?"
							+ "user=root&password=root&characterEncoding=utf8");
			return connection;
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
