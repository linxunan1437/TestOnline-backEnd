package datatype;

public class Question {
	private String question,answer,type;//type锟斤拷选锟斤拷锟斤拷,锟酵癸拷锟斤拷锟斤拷锟�,锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	private String id;
	private int score;//为锟斤拷时锟斤拷锟斤拷,锟斤拷锟皆撅拷同锟斤拷锟斤拷同
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getId() {
		return id;
	}

	public String getQuestion() {
		return question;
	}
	public Question(){}
	public Question(String answer,int score){this.answer=answer;this.score=score;}
	public Question(String id,String question, String answer, String type,int score) {
		super();
		this.question = question;
		this.answer = answer;
		this.type = type;
		this.id = id;
		this.score=score;
	}
	public void setId(String id) {
		this.id = id;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	
	
}
