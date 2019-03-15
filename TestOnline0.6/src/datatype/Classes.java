package datatype;

import java.util.List;

public class Classes {
	String id,className;
	List<User> students;
	User teacher;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public Classes(String id,String className,User teacher){
		super();
		this.id = id;
		this.className = className;
		this.teacher = teacher;
	}
	public Classes(String id, String className, List<User> students, User teacher) {
		super();
		this.id = id;
		this.className = className;
		this.students = students;
		this.teacher = teacher;
	}
	public Classes() {
		// TODO Auto-generated constructor stub
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<User> getStudents() {
		return students;
	}
	public void setStudents(List<User> students) {
		this.students = students;
	}
	public User getTeacher() {
		return teacher;
	}
	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}
	@Override
	public String toString(){return '['+this.id+']'+' '+this.className+' '+this.teacher;}
}
