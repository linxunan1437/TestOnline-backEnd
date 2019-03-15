package datatype;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassAnswer {
	Test test;
	Classes classes;
	List<Answer> answer;
	public Test getTest() {
		return test;
	}
	public void setTest(Test test) {
		this.test = test;
	}
	public Classes getClasses() {
		return classes;
	}
	public void setClasses(Classes classes) {
		this.classes = classes;
	}
	public List<Answer> getAnswer() {
		return answer;
	}
	public void setAnswer(List<Answer> answer) {
		this.answer = answer;
	}
	public String getStatus(int qid){
		Question q=test.getQuestions().get(qid);
		int num=classes.getStudents().size();
		List<Answer> temp=new ArrayList<Answer>();
		for(int i=0;i<answer.size();i++) {
			if (answer.get(i).getQuestion().getId().equals(q.getId())) {
				temp.add(answer.get(i));
			}
		}
		int counter[]=new int[classes.getStudents().size()];//鏈�澶氱殑浜旂绛旀璁℃暟鍣�
		String mostAnswer[]=new String[classes.getStudents().size()];//浜旂绛旀璁板綍鍣�
		for(int i=0;i<classes.getStudents().size();i++){counter[i]=0;mostAnswer[i]="";}
		double average=0.0;
		for(int i=0;i<temp.size();i++){
			//閬嶅巻绛旀,鎵惧嚭鍑虹幇鏈�澶氱殑绛旀骞惰绠楀钩鍧囧垎
			for(int j=0;j<num;j++){
				if(mostAnswer[j].equals("")){
					mostAnswer[j]=temp.get(i).getQuestion().getAnswer();//鑻ユ浣嶇疆涓虹┖,鎻掑叆姝ょ瓟妗�
					counter[j]++;
					break;
				}
				if(mostAnswer[j].equals(temp.get(i).getQuestion().getAnswer())){
					counter[j]++;//宸叉湁鍒欒嚜澧�
					break;
				}
			}
			average+=temp.get(i).getQuestion().getScore();
		}
		average/=num;

		String result="";
		for(int i=0;i<mostAnswer.length;i++)
			result+=mostAnswer[i]+" "+counter[i]+" "+counter[i]/num*100+"%\n";
		result+=average;
		return result;
	}


}
