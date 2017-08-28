package TwitchBot;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import org.jibble.pircbot.PircBot;

public class Bot extends PircBot {
	public static final int	DISPOSE_ON_CLOSE = 2;
	public static final int	DO_NOTHING_ON_CLOSE = 0;
	public static final int	EXIT_ON_CLOSE =	3;
	public static final int	HIDE_ON_CLOSE = 1;
	private HashMap <String, UserStats> userStatsMap;
	//Username, returns userStats object
	private HashMap <String, MessageResponse> responseMap;
	private HashMap <String, Integer> emotesMap;
	//input string returns response
	private int addingResponses = 0;
	private int editingResponses= 0;
	private String holderTrigger;
	private MessageResponse holderResponse;
	private String responseMapPath;
	private String userStatsMapPath;
	private String emoteMapPath;
	public EmoteChart chart;
	
	public Bot(){
		this.setName("HiImEmoteBot");
	} 
	
	//method to add messages directly from main 
	public void addMessage(String message, MessageResponse botResponse){
		responseMap.put(message, botResponse);
		addToResponseMap(responseMap, responseMapPath, message, botResponse);
	}
	
	//method to add a user stats map
	public void addUserStats(HashMap<String, UserStats> userStatsMap){
		this.userStatsMap = userStatsMap;
	}
	
	//method to add a response map
	public void addResponseMap(HashMap<String, MessageResponse> responseMap){
		this.responseMap = responseMap;
	}
	
	//methods to add file paths
	public void setResponseMapPath(String responseMapPath){
		this.responseMapPath = responseMapPath;
	}
	
	public void setUserStatsMapPath(String userStatsMapPath){
		this.userStatsMapPath = userStatsMapPath;
	}
	public void setEmoteMapPath(String emotePath){
		this.emoteMapPath = emotePath;
	}
	
	public void addEmoteMap(HashMap<String, Integer> emotesMap, String channel){
		this.emotesMap = emotesMap;
		chart = new EmoteChart("Emote Usage in " + channel, "Emote Use", this.emotesMap);
		chart.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	public EmoteChart getEmoteChart(){
		return chart;
	}
	
	//method to update responsemap
	public void addToResponseMap(HashMap<String, MessageResponse> toUpdate, String filePath, String key, MessageResponse value){
		toUpdate.put(key, value);
		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(filePath, true));
			write.write(key);
			write.newLine();
			write.write(value.getBotResponse());
			write.newLine();
			write.flush();
			write.close();
		} 
		catch (FileNotFoundException e){
			System.out.println("File not found");
		}
		catch (IOException e) {
			System.out.println("Bad write input");
		}
	}
	
	public void deleteFromResponseMap(HashMap<String, MessageResponse> toDelete, String filePath, String key){
		StringBuilder build = new StringBuilder();
		String line;
		//Rebuild file without the response
		try {
			BufferedReader read = new BufferedReader(new FileReader(filePath));
			while((line = read.readLine()) != null){
				if(line.equalsIgnoreCase(key)){
					//skip the response
					read.readLine();
				}
				//else add the line to the string builder
				else{
					build.append(line);
					build.append(System.getProperty("line.separator"));
				}
			}
			read.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found");
		} 
		catch (IOException e) {
			System.out.println("Bad input");
		}
		
		try {
			BufferedWriter write = new BufferedWriter(new FileWriter(new File(filePath)));
			write.write(build.toString());
			write.flush();
			write.close();
		}
		catch (FileNotFoundException e){
			System.out.println("File not found");
		}
		catch (IOException e) {
			System.out.println("Bad input");
		}
		
		//Delete from hashmap
		toDelete.remove(key);
	}
	
	
	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message){
		String hashtagSender = "#" + sender;
		
		if(responseMap.containsKey(message)){
			System.out.println("!!!");
			sendMessage(channel, responseMap.get(message).getBotResponse());
		}
		
		Set<String> emotes = emotesMap.keySet();
		String[] emotesArray = emotes.toArray(new String[emotes.size()]);
		for(int i = 0; i < emotesArray.length; i++){
			if(message.contains(emotesArray[i])){
				int count = emotesMap.get(emotesArray[i]);
				count++;
				emotesMap.remove(emotesArray[i]);
				emotesMap.put(emotesArray[i], count);
				chart.dispose();
				chart = new EmoteChart("Emote Usage in " + channel, "Emote Use", emotesMap);
				chart.setDefaultCloseOperation(HIDE_ON_CLOSE);
				chart.pack();
				chart.setVisible(true);
				chart.toFront();
			}
		}
		
		//Adding messages to user stats
		//if(userStatsMap.containsKey(sender)){
		//	userStatsMap.get(sender).addChat();
		//}
		//else{
			//userStatsMap.put(sender, new UserStats(sender));
		//}
	
		//Bot response adding
		if(addingResponses == 2 && hashtagSender.equals(channel)){
			if(message.equalsIgnoreCase("!Cancel")){
				addingResponses = 0;
				sendMessage(channel, ", Response editing canceled");
			}
			else{
			MessageResponse botResponse = new MessageResponse(holderTrigger, message);
			addToResponseMap(responseMap, responseMapPath, holderTrigger, botResponse);
			sendMessage(channel, "@" + sender + ", Response added!");
			addingResponses = 0;
			}
		}	
		
		if(addingResponses == 1 && hashtagSender.equals(channel)){
			if(message.equalsIgnoreCase("!Cancel")){
				addingResponses = 0;
				sendMessage(channel, "@" + sender + " , Response editing canceled");
			}
			else{
				holderTrigger = "!" + message;
				addingResponses = 2;
				sendMessage(channel, "@" + sender + ", Trigger word entered, please type the response");
			}
		}
		
		if(message.equalsIgnoreCase("!AddResponse") && hashtagSender.equals(channel)){
			sendMessage(channel, "@" + sender + ", Input trigger word without '!' or type '!Cancel'");
			addingResponses = 1;
		}
		
		

		//Bot response editing
		//editing response
		if(editingResponses == 4 && hashtagSender.equals(channel)){
			if(message.equalsIgnoreCase("!Cancel")){
				sendMessage(channel, "@" + sender + "Request canceled.");
				editingResponses = 0;
			}
			else{
				deleteFromResponseMap(responseMap, responseMapPath, holderTrigger);
				MessageResponse newResponse = new MessageResponse(holderTrigger, message);
				addToResponseMap(responseMap, responseMapPath, holderTrigger, newResponse);
				editingResponses = 0;
				sendMessage(channel, "@" + sender + ", response changed to: " + message);
			}
		}
		
		//editing trigger word
		if(editingResponses == 3 && hashtagSender.equals(channel)){
			if(message.equalsIgnoreCase("!Cancel")){
				sendMessage(channel, "@" + sender + "Request canceled.");
				editingResponses = 0;
			}
			else{
				deleteFromResponseMap(responseMap, responseMapPath, holderTrigger);
				addToResponseMap(responseMap, responseMapPath, "!" + message, holderResponse);
				editingResponses = 0;
				sendMessage(channel, "@" + sender + ", trigger word changed to: " + "!" + message);
			}
		}
		
		if(editingResponses == 2 && hashtagSender.equals(channel)){
			if(message.equalsIgnoreCase("!Delete")){
				editingResponses = 0;
				deleteFromResponseMap(responseMap, responseMapPath, holderTrigger);
			}
			else if(message.equalsIgnoreCase("!EditTrigger")){
				editingResponses = 3;
				sendMessage(channel, "@" + sender + ", Enter the new trigger word without an '!' for the response: " + holderResponse.getBotResponse());
			}
			else if(message.equalsIgnoreCase("!EditResponse")){
				editingResponses = 4;
				sendMessage(channel, "@" + sender + ", Enter the new response word for the trigger: " + holderTrigger);
			}
			else if(message.equalsIgnoreCase("!Cancel")){
				sendMessage(channel, "@" + sender + "Request canceled.");
				editingResponses = 0;
			}
			else{
				sendMessage(channel, "@" + sender + "That was not a valid input, Type !Delete, !EditTrigger, !EditResponse, or !Cancel");
			}
		}
		//prompt for action
		if(editingResponses == 1 && hashtagSender.equals(channel)){
			if(responseMap.containsKey("!" + message)){
				holderResponse = responseMap.get("!" + message);
				holderTrigger = "!" + message;
				sendMessage(channel, "@" + sender + ", trigger word entered.  Type !Delete to delete the entire entry,"
						+ " !EditTrigger to edit the trigger word, or !EditResponse to edit my response");
				editingResponses = 2;
			}
			else if (message.equalsIgnoreCase("!Cancel")){
				sendMessage(channel, "@" + sender + "Request canceled.");
				editingResponses = 0;
			}
			else{
				sendMessage(channel, "@" + sender + ", the trigger word was not found in my logs! The request was canceled!");
				editingResponses = 0;
			}
		}
		
		if(message.equalsIgnoreCase("!EditBotSpeak") && hashtagSender.equals(channel)){
			sendMessage(channel, "@" + sender + ", Input the trigger word without '!' or type '!Cancel'");
			editingResponses = 1;
		}
	
	}
}