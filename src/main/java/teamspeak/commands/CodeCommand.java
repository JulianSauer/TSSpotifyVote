package teamspeak.commands;

import spotify.Spotify;
import teamspeak.VoteListener;

public class CodeCommand extends BotCommand {

    private Spotify spotify;

    public CodeCommand(VoteListener voteListener) {
        super(voteListener);
        this.spotify = voteListener.getSpotify();
        commandNames = new String[]{"!code"};
        description = "Uses the given code to retrieve access to spotify";
    }

    @Override
    public void execute(String parameters, int client) {
        spotify.storeSpotifyUser(parameters, client);
        api.sendChannelMessage("Adding user");
    }

}
