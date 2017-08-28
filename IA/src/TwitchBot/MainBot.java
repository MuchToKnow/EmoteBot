package TwitchBot;

import java.util.Scanner;

import org.jibble.pircbot.IrcException;


public class MainBot{

	public static void main(String[] args) throws Exception{
		//Create objects required
		Bot emoteBot = new Bot();
		emoteBot.setVerbose(false);
		String responseMapPath = "src\\botMessages.txt";
		String emotePath = "src\\Emotes.txt";
		//emoteBot.setVerbose(true);
		Scanner input = new Scanner(System.in);
		//getting imports
		ResponseMapBuilder buildResponse = new ResponseMapBuilder();
		EmoteImporter buildEmotes = new EmoteImporter();
		emoteBot.setResponseMapPath(responseMapPath);
		emoteBot.setEmoteMapPath(emotePath);
		emoteBot.addResponseMap(buildResponse.BuildMap(responseMapPath));
		
		//Gathering primitives
		System.out.println("Enter your channel to connect to");
		String channel = "#" + input.nextLine();
		emoteBot.addEmoteMap(buildEmotes.getEmoteMap(emotePath), channel);
		String twitchServer = "irc.twitch.tv";
		input.close();
		
		
		//Setting emote bot settings
		emoteBot.changeNick("HiImEmoteBot");
		//emoteBot.setResponseMapPath("");
		//emoteBot.setUserStatsMapPath("");
		
		//Attempt to connect to the server
		try{
		emoteBot.connect(twitchServer, 6667, "oauth:2p8agazvct141qf1qtjuh84xarlaq8");
		System.out.println("Connected to " + channel);
		}
		catch(IrcException e){
			System.out.println("Not able to connect due to IRC issue");
		}
		emoteBot.joinChannel(channel);
		emoteBot.sendMessage(channel, "I am connected!");
		}
	}

