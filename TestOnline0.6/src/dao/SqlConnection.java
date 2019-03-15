package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {
	public static Connection open(String url){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(url,"root", "linxunan1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void sqlClose(Connection conn) throws SQLException{
		conn.close();
	}
}
