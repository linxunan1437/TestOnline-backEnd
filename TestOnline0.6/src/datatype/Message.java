package datatype;

public class Message {
	String message,to;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Message(String message, String to) {
		super();
		this.message = message;
		this.to = to;
	}
	
}
