package TwitchBot;

public class MessageResponse {
	private String in;
	private String out;
	
	public MessageResponse(String input, String botResponse){
		this.in = input;
		this.out = botResponse;
	}
	
	public String getTriggerMessage(){
		return in;
	}
	
	public String getBotResponse(){
		return out;
	}
}