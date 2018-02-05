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
import teamspeak.commands.*;

public class VoteListener extends TS3EventAdapter {
    private final TS3Api api;
    private final int clientId;
    private Client musicBot;

    /**
     * Stores votes of the users
     */
    private BallotBox ballotBox;

    private Spotify spotify;

    private BotCommand commands;

    public VoteListener(TS3Api api) {
        this.api = api;
        clientId = api.whoAmI().getId();

        ballotBox = new BallotBox();
        spotify = new Spotify();

        api.registerAllEvents();

        commands = new BotCommand(this);
        commands.addCommand(new CodeCommand(this));
        commands.addCommand(new ListCommand(this));
        commands.addCommand(new NextCommand(this));
        commands.addCommand(new PauseCommand(this));
        commands.addCommand(new PreviousCommand(this));
        commands.addCommand(new ResumeCommand(this));
        commands.addCommand(new UriCommand(this));

        System.out.println("Searching for " + Config.getInstance().getProperty("BOTNAME"));
        for (Client client : api.getClients())
            if (client.getNickname().equals(Config.getInstance().getProperty("BOTNAME"))) {
                musicBot = client;
                System.out.println("Found you, sneaky little bot");
                if (spotify.loadUser(client.getUniqueIdentifier()))
                    api.sendChannelMessage("Voting enabled for " + client.getNickname());
                api.moveQuery(musicBot.getChannelId());
                break;
            }

        if (musicBot == null) {
            System.out.println("Could not find your music bot, maybe it is not online yet?");
            System.out.println("Waiting for your music bot to get online...");
        } else System.out.println("Client is now initialized and ready to use");
    }


    @Override
    public void onClientJoin(ClientJoinEvent e) {
        if (api.getClientInfo(clientId).getChannelId() != api.getClientInfo(e.getClientId()).getChannelId())
            if (api.getClientInfo(e.getClientId()).getNickname().equals(Config.getInstance().getProperty("BOTNAME"))) {
                musicBot = api.getClientByUId(e.getUniqueClientIdentifier());
                api.moveQuery(musicBot.getChannelId());
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
            Client client = api.getClientsByName(user).get(0);
            commands.execute(message, client);
        }
    }

    public TS3Api getTS3Api() {
        return api;
    }

    public BallotBox getBallotBox() {
        return ballotBox;
    }

    public Spotify getSpotify() {
        return spotify;
    }

    public Client getMusicBot() {
        return musicBot;
    }

}
