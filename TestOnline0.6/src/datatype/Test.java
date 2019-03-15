package datatype;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Test {
	private String name;
	private String id;
	public int getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	private List<Question> questions;
	private String starttime,endtime;
	private int totalScore,timeLimit;
	private String classid;	
	public String getClassid() {
		return classid;
	}
	public void setClassid(String classid) {
		this.classid = classid;
	}
	public String getStarttime() {
		return starttime;
	}
	public Test(String name, String id, List<Question> questions, String starttime, String endtime, int totalScore,
			int timeLimit) {
		super();
		this.name = name;
		this.id = id;
		this.questions = questions;
		this.starttime = starttime;
		this.endtime = endtime;
		this.totalScore = totalScore;
		this.timeLimit = timeLimit;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public void setStarttime(Timestamp timestamp) {
		this.starttime = timestamp.toString();
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(Timestamp timestamp) {
		this.endtime = timestamp.toString();
	}
	public Test(){questions=new ArrayList<Question>();}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}
	public int getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Test(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}
	public Test(String name, String id,int score) {
		super();
		this.name = name;
		this.id = id;
		this.totalScore=score;
	}
}
