package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datatype.Answer;
import datatype.ClassAnswer;
import datatype.Classes;
import datatype.Question;
import datatype.Test;
import datatype.User;
import staticThings.Setting;


public class TestDao {

	//���ҵ�ǰ�༶���ŵĿ���
	public static  ArrayList SeekOpeningTest(int uid){
		
		Connection conn=SqlConnection.open(Setting.databaseSite);
		String sql="select * from testlist where class in (select classid from classbelong where uid=?)";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
			pstmt.setInt(1, uid);
			ResultSet rs=pstmt.executeQuery();
			ArrayList<Test> temp=new ArrayList<Test>();
			Timestamp time = new Timestamp(new Date().getTime());
			
			while(rs.next()){
				Timestamp t1=rs.getTimestamp(6),t2=rs.getTimestamp(7);
				if(new Date().after(t2))
					continue;
				PreparedStatement pstmt2=conn.prepareStatement("select uid from testtake where testid=? and uid=?",ResultSet.TYPE_SCROLL_INSENSITIVE);
				System.out.println(rs.getString(1)+"\n"+uid);
				pstmt2.setString(1, rs.getString(1));
				pstmt2.setInt(2, uid);
				ResultSet rs2=pstmt2.executeQuery();
				boolean e=rs2.next();
				if(e)
					continue;//�Ѿ��μӹ�����
				Test test =new Test(rs.getString(2),rs.getString(1));
				test.setStarttime(rs.getTimestamp(6));
				test.setEndtime(rs.getTimestamp(7));
				temp.add(test);
				
			}
			return temp;
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
	//��id�Ų����ض����Բ����ؿ�����Ϣ�������б�
	public static Test findTest(String testID){
		
		Connection conn=SqlConnection.open(Setting.databaseSite);
		Test temp=new Test();
		String sql="select * from testlist where testid=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, testID);
			ResultSet rs=pstmt.executeQuery();
			
			
			while(rs.next()){
				temp=new Test(rs.getString(2),rs.getString(1));
				temp.setTotalScore(rs.getInt(4));
				temp.setTimeLimit(rs.getInt(5));
				temp.setStarttime(rs.getTimestamp(6));
				temp.setEndtime(rs.getTimestamp(7));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{

		}
		
		//�������ڴ˿��Ե���Ŀ
		List<Question> qlist=new ArrayList<Question>();
		sql="select * from questionlist natural join testhave where testid=? order by testqid";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, testID);
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()){
				qlist.add(new Question(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(6)));
			}
			
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
			}
		}
		//��������뵽test�ļ���ȥ
		temp.setQuestions(qlist);
		return temp;
	}
	//�ϴ��𰸲��Զ����Ŀ͹���,�������Ŀ͹������Ծ�(�޸Ĵ���÷�)
	public static Test updateAnswer(int uid,Test answer){
		
		String testID=answer.getId();
		//�ȸ���id��ѯ��׼��
		List<Question> correctAnswer=new ArrayList<Question>();
		Connection conn=SqlConnection.open(Setting.databaseSite);
		String sql="select answer,point from testhave natural join questionlist where testid=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, testID);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				correctAnswer.add(new Question(rs.getString(1),rs.getInt(2)));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		}
		

		int numOfQuestionanswer=answer.getQuestions().size();
		List<Question> qlist=answer.getQuestions();
		boolean all=true;
		int totalScore=0;
		//��ֱ����ԭ�Ծ��Ͻ�������
		for(int i=0;i<numOfQuestionanswer;i++)
		{
//			if(qlist.get(i).getType().equals("subjective")){all=false;continue;}//����������
			if(qlist.get(i).getAnswer().equals(correctAnswer.get(i).getAnswer()))
			{
				answer.getQuestions().get(i).setScore(correctAnswer.get(i).getScore());
			}
			else{answer.getQuestions().get(i).setScore(0);}
			totalScore+=answer.getQuestions().get(i).getScore();
		}
		answer.setTotalScore(totalScore);
		
		//�ϴ���
		
		sql="insert into studentanswer values(?,?,?,?,?); ";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			for(int i=0;i<answer.getQuestions().size();i++){
				pstmt.setInt(1, uid);
				pstmt.setString(2, testID);
				pstmt.setString(3, qlist.get(i).getId());
				pstmt.setString(4, qlist.get(i).getAnswer());
				pstmt.setInt(5, qlist.get(i).getScore());
				pstmt.execute();
			}
			sql="insert into testtake values(?,?,?);";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, uid);
			pstmt.setString(2, testID);
			if(all==false)pstmt.setInt(3,-1);//û������ķ�����ʱΪ-1��
			if(all==true)pstmt.setInt(3,answer.getTotalScore());
			pstmt.execute();
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
		return answer;
		
	}
	
	//���Ҵ����Ѿ�������ɵĴ�� 
	public static  ArrayList SeekTestResult(int uid){
		
		Connection conn=SqlConnection.open(Setting.databaseSite);
		String sql="select testid,testname,score from testlist natural join testtake where uid = ? and score<>-1";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
			pstmt.setInt(1, uid);
			ResultSet rs=pstmt.executeQuery();
			ArrayList<Test> temp=new ArrayList<Test>();
			while(rs.next()){
				temp.add(new Test(rs.getString(2),rs.getString(1),rs.getInt(3)));
			}
			return temp;
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

	
	//���ĳ�ο��ԵĿ��Խ��
	public static Test findTestResult(int uid,String testid){
		Test temp=new Test();
		temp.setId(testid);
		List<Question> qlist=new ArrayList<Question>();
		Connection conn=SqlConnection.open(Setting.databaseSite);
		String sql="select studentanswer.answer,studentanswer.point from studentanswer join testhave "+
				"on studentanswer.testid=testhave.testid "
				+"and studentanswer.qid=testhave.qid "
				+"where uid=? and testhave.testid=? ORDER BY testhave.testqid";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
			pstmt.setInt(1, uid);
			pstmt.setString(2, testid);
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()){
				qlist.add(new Question(rs.getString(1),rs.getInt(2)));
			}
			temp.setQuestions(qlist);
			return temp;
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
		
		
		
		
		return temp;
	}
	
	//�鿴���˵��ľ�����
	public static  ArrayList SeekTestMarking(int uid){
		Connection conn=SqlConnection.open(Setting.databaseSite);
		String sql="select testid,testname from questionmarking natural join testlist where marker= ?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
			pstmt.setInt(1, uid);
			ResultSet rs=pstmt.executeQuery();
			ArrayList<Test> temp=new ArrayList<Test>();
			while(rs.next()){
				temp.add(new Test(rs.getString(2),rs.getString(1)));
			}
			return temp;
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
	//����id��Ѱ�Ҵ˴��ľ��������Ŀ������
	public static  Test findTestMarking(int uid,String testID){
		Connection conn=SqlConnection.open(Setting.databaseSite);
		Test test = new Test();
		List<Question> qlist = new ArrayList<Question>();
		//���ص��ľ�������testid
		test.setId(testID);
		String sql="select answer,testhave.qid,answeruid "
			+"from questionmarking inner join "
			+"(studentanswer inner join  testhave on studentanswer.qid=testhave.qid and studentanswer.testid=testhave.testid) "
			+"on (questionmarking.answeruid=studentanswer.uid and questionmarking.testqid=testhave.testqid) "
			+"where questionmarking.marker = ?  and testhave.testid = ? "
			+"ORDER BY questionmarking.testqid";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
			pstmt.setInt(1, uid);
			pstmt.setString(2, testID);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				Question q=new Question(rs.getString(1),-1);q.setId(rs.getString(2));
				qlist.add(q);
			}
			test.setQuestions(qlist);
			
			return test;
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
	//�ϴ������Ĺ����������
	public static void updateMarking(int uid,Test answer){
		
		String testID=answer.getId();
		
		List<Question> correctAnswer=new ArrayList<Question>();
		Connection conn=SqlConnection.open(Setting.databaseSite);
		String sql="update ";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, testID);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				correctAnswer.add(new Question(rs.getString(1),rs.getInt(2)));
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
		
	}
	
	
	
	//���Ҵ˽�ʦִ�̰༶���Ծ�
	public static  ArrayList SeekTeacherTestResult(int uid){
		
		Connection conn=SqlConnection.open(Setting.databaseSite);
		String sql="select * from classlist join testlist on classlist.id=testlist.class where teacherid=?;";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
			pstmt.setInt(1, uid);
			ResultSet rs=pstmt.executeQuery();
			ArrayList<Test> temp=new ArrayList<Test>();
			while(rs.next()){
				Timestamp ed=rs.getTimestamp(10);
				if(ed.after(new Date()))continue;//������ɺ����ʾ�˽��
				Test t=new Test();t.setId(rs.getString(4));t.setName(rs.getString(5));
				temp.add(t);
			}
			return temp;
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
	//��ʦ��ȡ������ѧ��ĳ�ο��ԵĵĴ������
	public static ClassAnswer findClassAnswer(int uid,String testid){
		ClassAnswer result=new ClassAnswer();
		//Ѱ�ұ��εĴ�
		String sql="select uid,testhave.testqid,answer,studentanswer.point from studentanswer join testhave on (studentanswer.qid=testhave.qid and studentanswer.testid=testhave.testid) where studentanswer.uid in (select uid from classbelong join classlist on classbelong.classid=classlist.id WHERE teacherid=?) and studentanswer.testid=?";
		Connection conn=SqlConnection.open(Setting.databaseSite);
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, uid);
			pstmt.setString(2, testid);
			ResultSet rs=pstmt.executeQuery();
			List<Answer> temp=new ArrayList<Answer>();
			Question q;
			while(rs.next()){
				q=new Question(rs.getString(3),rs.getInt(4));q.setId(rs.getString(2));
				temp.add(new Answer(new User(rs.getInt(1),""),q));
			}
			result.setAnswer(temp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Ѱ�ұ��ε�����
		sql="select testqid,question,questionlist.answer,testhave.point from testhave natural join questionlist where testid = ? order by testqid";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, testid);
			ResultSet rs=pstmt.executeQuery();
			List<Question> temp=new ArrayList<Question>();
			Test t=new Test();
			while(rs.next()){
				Question q=new Question(rs.getString(1),rs.getString(2),rs.getString(3),"",rs.getInt(4));
				temp.add(q);
			}
			t.setQuestions(temp);
			result.setTest(t);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//��ѧ��
		sql="select uid from testtake where testid=?;";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, testid);
			ResultSet rs=pstmt.executeQuery();
			List<User> temp=new ArrayList<User>();
			Classes classes = new Classes();
			while(rs.next()){
				temp.add(new User(rs.getInt(1)));
			}
			classes.setStudents(temp);
			result.setClasses(classes);
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
		
		return result;
	}
	
	//�ѳ�������Ŀ�е������
	public static String CreateNewQuestion(String question,String answer,String questiontype,boolean edit){
		String sql="select qid from questionlist";
		Connection conn=SqlConnection.open(Setting.databaseSite);
		int max=0;
		if(edit==false){
			try {
				PreparedStatement pstmt=conn.prepareStatement(sql);
	
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()){
					int temp=Integer.parseInt(rs.getString(1));
					if(temp>max)max=temp;
				}
				max++;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sql="insert into questionlist values(?,?,?,?)";
			
			try {
				PreparedStatement pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, String.valueOf(max));
				pstmt.setString(2, question);
				pstmt.setString(3, answer);
				pstmt.setString(4, questiontype);
				pstmt.execute();
	
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
			return String.valueOf(max);
		}
		else{
			sql="update into questionlist values(?,?,?,?)";
			
			try {
				PreparedStatement pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, String.valueOf(max));
				pstmt.setString(2, question);
				pstmt.setString(3, answer);
				pstmt.setString(4, questiontype);
				pstmt.execute();
	
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
			return "";
		}
	}
	//���µĿ��Էŵ����ݿ���
	public static String CreateNewTest(Test test){
		String sql="select testid from testlist";
		Connection conn=SqlConnection.open(Setting.databaseSite);
		//�ҵ����ʵ�id
		int max=0;
			try {
				PreparedStatement pstmt=conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()){
					int temp=Integer.parseInt(rs.getString(1));
					if(temp>max)max=temp;
				}
				max++;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sql="insert into testlist values(?,?,?,?,?,?,?)";
			
			try {
				PreparedStatement pstmt=conn.prepareStatement(sql);
				pstmt.setString(1, String.valueOf(max));
				pstmt.setString(2, test.getName());
				pstmt.setString(3, test.getClassid());
				pstmt.setInt(4, test.getTotalScore());
				pstmt.setInt(5, test.getTimeLimit());
				Timestamp st = null,ed = null;
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				try {
					st=new Timestamp(format.parse(test.getStarttime()).getTime());
					ed=new Timestamp(format.parse(test.getEndtime()).getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				pstmt.setTimestamp(6, st);
				pstmt.setTimestamp(7, ed);
				pstmt.execute();
				for(int i=0;i<test.getQuestions().size();i++){
					pstmt=conn.prepareStatement("insert into testhave values(?,?,?,?)");
					pstmt.setString(1, String.valueOf(max));
					pstmt.setString(2, test.getQuestions().get(i).getId());
					pstmt.setInt(3, test.getQuestions().get(i).getScore());
					pstmt.setInt(4,i);
					pstmt.execute();
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
			
			return "success";
		

	}
	public static String deleteQuestion(Question q){
		String sql="delete from questionlist where qid=?";
		Connection conn=SqlConnection.open(Setting.databaseSite);
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, q.getId());
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}finally{
			try {
				SqlConnection.sqlClose(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "delete success";
	}
}
