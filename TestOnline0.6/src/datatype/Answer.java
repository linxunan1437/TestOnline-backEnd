package datatype;

public class Answer {
    User user;
    Question question;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Question getQuestion() {
        return question;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }
    public Answer(User user, Question question) {
        super();
        this.user = user;
        this.question = question;
    }


}
