import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import java.util.ArrayList;

public class VoteListener extends TS3EventAdapter {

    private final TS3Api api;
    private final int clientId;

    private int voteCounter;
    private int counterNext;

    //TODO create class with arraylist to store users and votes to quickly check for votes and users
    /**
     * Stores votes of the users
     */
    private ArrayList<String> VotedUsers;

    private UserVoteList table;

    VoteListener(TS3Api api) {
        this.api = api;
        clientId = api.whoAmI().getId();
        voteCounter = 0;

        api.registerAllEvents();

        VotedUsers = new ArrayList<String>();  //deprecated
        table = new UserVoteList();
    }

    @Override
    public void onTextMessage(TextMessageEvent e) {
        if (e.getTargetMode() == TextMessageTargetMode.SERVER && e.getInvokerId() != clientId) {        //can process public messages
            String message = e.getMessage().toLowerCase();

            if (message.equals("!next")) {

                voteCounter++;
                api.sendServerMessage("Votes: " + voteCounter);
            }
        }
        //if client permissions are sufficient, the bot can receive private messages and process them as public server messages
        //the bot is treated as a query client, so it is invisible in the channel list, unless you check the box "show ServerQuery Clients" in the favorites windows
        if(e.getTargetMode() == TextMessageTargetMode.CLIENT) {
            String message = e.getMessage().toLowerCase();
            String user = e.getInvokerName();

            System.out.println("bot received: "+message);			//DEBUG

            BotCommands commands = new BotCommands();

            if(commands.listCommands().contains(message)) {

                if(message.equals("!list"))
                {
                    System.out.println("Got list");
                    for(Object com:commands.listCommands())
                        api.sendServerMessage(com + ": "+commands.getDefinition((String)com));      //TODO change to PM
                }
                else {

                    if (!table.contains(user, message))
                        table.add(user, message);


                    int indexBotGroup = api.getClientsByName("Vote bot").get(0).getServerGroups()[0];       //Ansatz 1 um Bots nicht mitzuzählen.#
                    int mansNotBot = api.getClients().size() - api.getServerGroupClients(indexBotGroup).size();
                    api.getClientInfo(api.getClientsByName(user).get(0).getId()).isServerQueryClient();     //Ansatz 2 um Bots nicht mitzuzählen.

                    if (table.size(message) > mansNotBot / 2) {         //vote successful (clear voteList?)
                        botStuff(message);
                    }
                }
            }
        }
    }

    private void botStuff(String command) {
        System.out.println("Processing: " +command);
    }

}
