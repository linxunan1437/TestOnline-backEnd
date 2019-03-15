package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import datatype.User;
import staticThings.Setting;
public class UserLoginDao {
	public static  User login(String username,String password,String tutor){
		Connection conn=SqlConnection.open(Setting.databaseSite);
		String sql="select uid,username,userpassword,tutor from userlist where username=? and userpassword=? and tutor=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setString(3, tutor);
			System.out.println(username+password+tutor);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				User u =new User(rs.getInt(1),username,password,tutor);
				System.out.println(tutor);
				return u;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				SqlConnection.sqlClose(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
		
	}
	public static User signup(String username,String password,String tutor){
		Connection conn=SqlConnection.open(Setting.databaseSite);
		//查重
		String sql="select uid from userlist where username=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, username);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				return new User(0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//赋予新的uid
		sql="select max(uid) from userlist";
		int newid=0;
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				newid=rs.getInt(1);
				newid++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sql="insert into userlist values(?,?,?,?)";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, newid);
			pstmt.setString(2, username);
			pstmt.setString(3, password);
			pstmt.setString(4, tutor);
			System.out.println(username+password+tutor);
			pstmt.execute();
			return new User(newid,username,password,tutor);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				SqlConnection.sqlClose(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
