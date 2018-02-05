package teamspeak.commands;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import spotify.Spotify;
import teamspeak.VoteListener;

public class PauseCommand extends BotCommand {

    private Spotify spotify;

    public PauseCommand(VoteListener voteListener) {
        super(voteListener);
        commandNames = new String[]{"!pause"};
        description = "Vote to pause the music";
        spotify = voteListener.getSpotify();
    }

    @Override
    public void execute(String parameters, Client client) {
        spotify.pauseSong(voteListener.getMusicBot().getUniqueIdentifier());
    }

}
