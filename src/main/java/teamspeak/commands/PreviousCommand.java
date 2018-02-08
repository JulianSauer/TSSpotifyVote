package teamspeak.commands;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import spotify.Spotify;
import teamspeak.VoteListener;

public class PreviousCommand extends BotCommand {

    private Spotify spotify;

    public PreviousCommand(VoteListener voteListener) {
        super(voteListener);
        commandNames = new String[]{"!previous"};
        description = "Vote to rewind to the last song";
        spotify = voteListener.getSpotify();
    }

    @Override
    public void execute(String parameters, Client client) {
        spotify.previousSong(voteListener.getMusicBot());
    }

}
