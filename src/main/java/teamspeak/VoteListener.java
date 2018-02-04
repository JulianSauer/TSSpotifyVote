package teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import plugin.Config;
import spotify.Spotify;

public class VoteListener extends TS3EventAdapter {
    private final TS3Api api;
    private final int clientId;
    private int musicBot;

    /**
     * Stores votes of the users
     */
    private BallotBox ballotBox;

    private Spotify spotify;

    public VoteListener(TS3Api api) {
        this.api = api;
        clientId = api.whoAmI().getId();

        ballotBox = new BallotBox();
        spotify = new Spotify();

        api.registerAllEvents();

        System.out.println("Searching for " + Config.getInstance().getProperty("BOTNAME"));
        for (Client c : api.getClients())
            if (c.getNickname().equals(Config.getInstance().getProperty("BOTNAME"))) {
                musicBot = c.getId();
                System.out.println("Found you, sneaky little bot");
                api.moveQuery(api.getClientInfo(musicBot).getChannelId());
                break;
            }

        if (musicBot == 0) {
            System.out.println("Could not find your music bot, maybe it is not online yet?");
            System.out.println("Waiting for your music bot to get online...");
        } else System.out.println("Client is now initialized and ready to use");
    }


    @Override
    public void onClientJoin(ClientJoinEvent e) {
        if (api.getClientInfo(clientId).getChannelId() != api.getClientInfo(e.getClientId()).getChannelId())
            if (api.getClientInfo(e.getClientId()).getNickname().equals(Config.getInstance().getProperty("BOTNAME"))) {
                musicBot = e.getClientId();
                api.moveQuery(api.getClientInfo(musicBot).getChannelId());
                System.out.println("Client is now initialized and ready to use");
            }
    }

    @Override
    public void onClientMoved(ClientMovedEvent e) {
        if (api.getClientInfo(e.getClientId()).getNickname().equals(Config.getInstance().getProperty("BOTNAME"))) {
            api.moveQuery(api.getChannelInfo(e.getTargetChannelId()));
            System.out.println("Moved after music bot");        //TODO DEBUG
        }
    }

    @Override
    public void onTextMessage(TextMessageEvent e) {
        // if client permissions are sufficient, the bot can receive private messages and process them as public server messages
        // the bot is treated as a query client, so it is invisible in the channel list, unless you check the box "show ServerQuery Clients" in the favorites windows
        if (e.getTargetMode() == TextMessageTargetMode.CHANNEL) {
            String message = e.getMessage();
            String user = e.getInvokerName();


            BotCommands commands = new BotCommands();

            if (commands.contain(message)) {
                System.out.println("found command " + message + " in list");
                if (message.equals("!list")) {
                    for (Object com : commands.listCommands()) {
                        String output = com + ":\t\t" + commands.getDefinition((String) com);
                        api.sendPrivateMessage(api.getClientsByName(user).get(0).getId(), output);
                    }
                } else {
                    if (!ballotBox.contains(user, message)) ballotBox.castVoteFor(message, user);

                    int mansNotBot = 0;
                    for (Client c : api.getClients()) {
                        mansNotBot += !c.isServerQueryClient() && c.getId() != musicBot ? 1 : 0;      //query clients don't count as men
                    }

                    api.sendChannelMessage(ballotBox.countVotesFor(message) + "/" + (int) Math.ceil(((float) mansNotBot) / 2) + " Users have voted for \"" + message + "\"");
                    if (ballotBox.countVotesFor(message) == (int) Math.ceil(((float) mansNotBot) / 2)) {         //vote successful (clear voteList?)
                        botStuff(message);
                        ballotBox.clear(message);
                    }
                }
            }
        }
    }

    private void botStuff(String command) {
        //TODO implement spotify interface
        System.out.println("Processing: " + command + ". The spotify interface can work from here on");

        if ("!next".equals(command)) {
            api.sendChannelMessage("Playing that same song");
        } else if ("!uri".equals(command) || "!auth".equals(command)) {
            api.sendChannelMessage("Authorization link: " + spotify.getAuthorizationCodeUri());
            api.sendChannelMessage("Please enter the authorization code using !code");
        } else if (command.startsWith("!code")) {
            String code = command.replace("!code ", "");
            spotify.storeSpotifyUser(code);
            api.sendChannelMessage("Adding user");
        }
    }

}
