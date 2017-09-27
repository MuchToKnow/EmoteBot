# EmoteBot
Bot for Twitch Utilizing PircBot and JFreeChart.

**I swear my code is not this messy now.  Rereading my code now I don't think I even knew the concept of data abstraction back when I wrote this**

Created during my grade 12 year for my IA during CompSci IB HL.


# How to Run
Run MainBot.java after downloading PircBot and JFreeChart.  EmoteBot will prompt you for a twitch channel name to connect to.  Simply type the desired channel with no extra syntax.  

# Adjusting settings
Change botMessages.txt to change triggers and response strings.

Do not line space response strings and triggers

Example botMessages:

!about

This is MuchToKnow's twitch channel!

!aboutbot

MuchToKnow made me for his IA project in grade 12


In the above example typing '!about' in an emotebot connected chat would result in emotebot sending 'This is MuchToKnow's twitch channel!' back to the channel.

The default botMessages.txt contains a single trigger '!test' that responds: 'Test successful!'.  The next trigger response pair should be added on line 3 and 4.
