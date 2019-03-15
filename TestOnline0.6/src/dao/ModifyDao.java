package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datatype.Classes;
import datatype.Question;
import datatype.Test;
import datatype.User;
import staticThings.Setting;
public class ModifyDao {
	//修改密码
	public static  String modifyPass(int uid,String oldpass,String newpass){
		Connection conn=SqlConnection.open(Setting.databaseSite);

		String sql="update userlist set userpassword=? where uid=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, newpass);
			pstmt.setInt(2, uid);
			pstmt.execute();
			System.out.println(uid+"\n"+newpass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "database fail";
		}finally{
			try {
				SqlConnection.sqlClose(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "database fail";
			}
		}
		
		return "success";
		
	}
	//修改班级	
	public static String modifyClass(int uid,String classid,String operation){
			Connection conn=SqlConnection.open(Setting.databaseSite);
			String sql=new String();
			if(operation.equals("delete")){
				sql="delete from classbelong where uid=? and classid=?";
			}
			if(operation.equals("add")){
				sql="insert into classbelong values(?,?)";
				System.out.println("add success");
			}
			
			try {
				PreparedStatement pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, classid);
				pstmt.setInt(2, uid);
				pstmt.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "database fail";
			}finally{
				try {
					SqlConnection.sqlClose(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "database fail";
				}
			}
			
			return "success";
			
		}
	//搜索班级
	public static List<Classes> searchClass(String key){
		Connection conn=SqlConnection.open(Setting.databaseSite);
		List<Classes> list= new ArrayList<Classes>();
		String sql="select * from classlist where "
				+ "id=? or classname like ?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
			pstmt.setString(1, key);
			pstmt.setString(2, "%"+key+"%");
			System.out.println(key+"\n");
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				//获得教师信息
				User teacher;
				pstmt=conn.prepareStatement("select * from userlist where uid=?");
				pstmt.setInt(1, rs.getInt(3));
				ResultSet rs2=pstmt.executeQuery();
				rs2.next();
				teacher = new User(rs2.getInt(1),rs2.getString(2));
				list.add(new Classes(rs.getString(1),rs.getString(2),teacher));//此处使用特殊的构造方案,不显示其他学生
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			try {
				SqlConnection.sqlClose(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
	}

	//创建班级
	public static String createClass(Classes key,String operation){
		String result="null";
		String sql;
		if(operation.equals("delete"))
			sql="delete from classlist where id=? and classname=? and teacherid=?";
		else
			sql="insert into classlist values(?,?,?)";
		Connection conn=SqlConnection.open(Setting.databaseSite);
		int max=0;
			try {
				PreparedStatement pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, key.getId());
				pstmt.setString(2, key.getClassName());
				pstmt.setInt(3, key.getTeacher().getUid());
				pstmt.execute();
				result="success";
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result="database fail";
			}
			if(operation.equals("delete"))
				sql="delete from classbelong where classid=? and uid=?";
			else
				sql="insert into classbelong values(?,?)";
			
			try {
				PreparedStatement pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, key.getId());
				pstmt.setInt(2, key.getTeacher().getUid());
				pstmt.execute();
				if(operation.equals("delete"))
					result="delete success";
				else
					result="success";
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result="database fail";
			}finally{
				try {
					SqlConnection.sqlClose(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
			
		return result;
	}
	//为单个用户寻找其所有参与的课程 
	public static List<Classes> searchForUserClass(String uid){

		Connection conn=SqlConnection.open(Setting.databaseSite);
		List<Classes> list= new ArrayList<Classes>();
		String sql="select classid,classname,teacherid from classbelong join classlist on classid=id where uid=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
			pstmt.setInt(1, Integer.parseInt(uid));
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				//获得教师信息
				User teacher;
				pstmt=conn.prepareStatement("select * from userlist where uid=?");
				pstmt.setInt(1, rs.getInt(3));
				ResultSet rs2=pstmt.executeQuery();
				rs2.next();
				teacher = new User(rs2.getInt(1),rs2.getString(2));
				list.add(new Classes(rs.getString(1),rs.getString(2),teacher));//此处使用特殊的构造方案,不显示其他学生
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			try {
				SqlConnection.sqlClose(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	//搜索问题
	public static List<Question> searchQuestion(String key){
		Connection conn=SqlConnection.open(Setting.databaseSite);
		List<Question> list= new ArrayList<Question>();
		String sql="select * from questionlist where "
				+ "qid=? or question like ? or answer like ? order by qid desc";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
			pstmt.setString(1, key);
			pstmt.setString(2, "%"+key+"%");
			pstmt.setString(3, key);
			System.out.println(key+"\n");
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				list.add(new Question(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),0));//此处使用特殊的构造方案,不显示其他学生
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			try {
				SqlConnection.sqlClose(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
	}

	//搜索最高的五个问题
	public static List<Question> searchInitQuestion(){
		Connection conn=SqlConnection.open(Setting.databaseSite);
		List<Question> list= new ArrayList<Question>();
		String sql="select * from questionlist where "
				+ "order by qid desc";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
			ResultSet rs=pstmt.executeQuery();
			for(int i=0;rs.next()&&i<5;i++){
				list.add(new Question(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),0));//此处使用特殊的构造方案,不显示其他学生
			}
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			try {
				SqlConnection.sqlClose(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
	}
}
