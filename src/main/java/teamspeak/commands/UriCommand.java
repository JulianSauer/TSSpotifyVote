package teamspeak.commands;

import spotify.Spotify;
import teamspeak.VoteListener;

public class UriCommand extends BotCommand {

    private Spotify spotify;

    public UriCommand(VoteListener voteListener) {
        super(voteListener);
        this.spotify = voteListener.getSpotify();
        commandNames = new String[]{"!auth", "!uri"};
        description = "Prints authorization uri to grant access";
    }

    @Override
    public void execute(String parameters, int client) {
        api.sendChannelMessage("Authorization link: " + spotify.getAuthorizationCodeUri());
        api.sendChannelMessage("Please enter the authorization code using !code");
    }

}
