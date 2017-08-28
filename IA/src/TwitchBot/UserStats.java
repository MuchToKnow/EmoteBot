package TwitchBot;

public class UserStats {
	private int chatCount;
	private int offenses;
	private String name;
	
	public UserStats(String name){
		this.name = name;
	}
	
	public void addOffense(int severity){
		offenses = offenses + severity;
	}
	
	public void addChat(){
		chatCount++;
	}
}
