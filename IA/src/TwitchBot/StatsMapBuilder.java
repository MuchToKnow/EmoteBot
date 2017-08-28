package TwitchBot;

import java.util.HashMap;

public class StatsMapBuilder {
	private String fp;
	private HashMap<String, UserStats> userStatsMap;
	
	public void StatsMapBuilder(String filePath){
		fp = filePath;
	}
	public HashMap<String, UserStats> BuildStatsMap(){
		//figure out .txt formating to save stats
		return userStatsMap; 
	}
}