import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class VoteListener extends TS3EventAdapter
{
    private final TS3Api api;
    private final int clientId;

    /**
     * Stores votes of the users
     */
    private UserVoteList table;

    VoteListener(TS3Api api)
    {
        this.api=api;
        clientId=api.whoAmI().getId();

        api.registerAllEvents();

        table=new UserVoteList();
    }

    @Override
    public void onTextMessage(TextMessageEvent e)
    {
        //if client permissions are sufficient, the bot can receive private messages and process them as public server messages
        //the bot is treated as a query client, so it is invisible in the channel list, unless you check the box "show ServerQuery Clients" in the favorites windows
        if(e.getTargetMode() == TextMessageTargetMode.CLIENT)
        {
            String message=e.getMessage().toLowerCase();
            String user=e.getInvokerName();

            BotCommands commands=new BotCommands();

            if(commands.listCommands().contains(message))
            {
                if(message.equals("!list"))
                {
                    for(Object com : commands.listCommands())
                    {
                        String output=com + ":\t\t" + commands.getDefinition((String) com);
                        api.sendPrivateMessage(api.getClientsByName(user).get(0).getId(), output);
                    }
                }
                else
                {
                    if(!table.contains(user, message)) table.add(user, message);

                    int mansNotBot=0;
                    for(Client c : api.getClients())
                    {
                        mansNotBot+=!c.isServerQueryClient() ? 1 : 0;      //query clients don't count as men
                    }

                    api.sendServerMessage(table.size(message) + "/" + mansNotBot + " Users have voted for \"" + message + "\"");
                    if(table.size(message) > mansNotBot / 2)
                    {         //vote successful (clear voteList?)
                        botStuff(message);
                    }
                }
            }
        }
    }

    private void botStuff(String command)
    {
        //TODO implement spotify interface
        System.out.println("Processing: " + command);

        switch(command)
        {
            case "!next":
                api.sendServerMessage("Playing that same song");
                break;
        }
    }

}
