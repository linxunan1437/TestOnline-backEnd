package datatype;

public class User {
	private int uid;
	private String username,password,tutor;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getTutor() {
		return tutor;
	}
	public void setTutor(String tutor) {
		this.tutor = tutor;
	}
	public User(int uid,String username){
		super();
		this.uid = uid;
		this.username = username;
	}
	
	public User(int uid, String username, String password,String tutor) {
		super();
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.tutor=tutor;
	}
	public User(int uid){
		this.uid=uid;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
