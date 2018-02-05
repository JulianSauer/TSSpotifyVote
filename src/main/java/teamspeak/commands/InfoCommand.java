package teamspeak.commands;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import spotify.Spotify;
import teamspeak.VoteListener;

public class InfoCommand extends BotCommand {

    private Spotify spotify;

    public InfoCommand(VoteListener voteListener) {
        super(voteListener);
        commandNames = new String[]{"!info"};
        description = "Info about currently played song";
        spotify = voteListener.getSpotify();
    }

    @Override
    public void execute(String parameters, Client client) {
        api.sendChannelMessage(spotify.getCurrentSong(voteListener.getMusicBot().getUniqueIdentifier()));
    }

}
