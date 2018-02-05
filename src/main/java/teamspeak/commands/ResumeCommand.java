package teamspeak.commands;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import spotify.Spotify;
import teamspeak.VoteListener;

public class ResumeCommand extends BotCommand {

    private Spotify spotify;

    public ResumeCommand(VoteListener voteListener) {
        super(voteListener);
        commandNames = new String[]{"!resume"};
        description = "Vote to resume paused music";
        spotify = voteListener.getSpotify();
    }

    @Override
    public void execute(String parameters, Client client) {
        spotify.resumeSong(voteListener.getMusicBot().getUniqueIdentifier());
    }

}
