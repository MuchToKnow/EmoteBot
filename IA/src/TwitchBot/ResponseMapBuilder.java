package TwitchBot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ResponseMapBuilder {
	
	public ResponseMapBuilder(){
		
	}
	
	public HashMap<String, MessageResponse> BuildMap(String filePath){
		String triggerWord;
		String response;
		HashMap<String, MessageResponse> responseMap = new HashMap<String, MessageResponse>();
		try {
			BufferedReader read = new BufferedReader(new FileReader(filePath));
			while((triggerWord = read.readLine()) != null && triggerWord != "" && triggerWord != " "){
				if((response = read.readLine()) != null && response != "" && response != " "){
				MessageResponse botSpeak = new MessageResponse(triggerWord, response);
				responseMap.put(triggerWord, botSpeak);
				}
				else{
					break;
				}
				
			}
			read.close();
			return responseMap;
		} catch (FileNotFoundException e) {
			System.out.println("Response map file was not found.");
			return null;
		} catch (IOException e) {
			System.out.println("Unexpected input in response map creator.");
			return null;
		}
	}
}