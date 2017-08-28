package TwitchBot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class EmoteImporter {	
	public EmoteImporter(){
	
	}
	public HashMap<String, Integer> getEmoteMap(String filePath){
		HashMap<String, Integer> emotes= new HashMap<String, Integer>();
		try {
			BufferedReader emoteReader = new BufferedReader(new FileReader("src\\Emotes.txt"));
			String emote;
			while((emote = emoteReader.readLine()) != null && emote != "" && emote != " "){
				emotes.put(emote, 0);
			}
			emoteReader.close();
			return emotes;
		} 
		catch (IOException e) {
			System.out.println("Emote importing error");
			e.printStackTrace();
		}
		return null;
	}
}
