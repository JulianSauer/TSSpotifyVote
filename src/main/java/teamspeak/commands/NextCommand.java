package teamspeak.commands;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import spotify.Spotify;
import teamspeak.BallotBox;
import teamspeak.VoteListener;

public class NextCommand extends BotCommand {

    private BallotBox ballotBox;
    private Spotify spotify;

    public NextCommand(VoteListener voteListener) {
        super(voteListener);
        ballotBox = voteListener.getBallotBox();
        this.spotify = voteListener.getSpotify();
        commandNames = new String[]{"!next"};
        description = "Vote to skip the current song";
    }

    @Override
    public void execute(String parameters, Client client) {

        int userCount = getUserCount(voteListener.getMusicBot());
        if (!ballotBox.contains(client.getUniqueIdentifier(), commandNames[0])) {
            if (ballotBox.castVoteFor(commandNames[0], client.getUniqueIdentifier(), userCount))
                spotify.nextSong(voteListener.getMusicBot());
            api.sendChannelMessage(ballotBox.countVotesFor(commandNames[0]) + 1 + "/" + (int) Math.ceil(((float) userCount) / 2) + " Users have voted for \"" + commandNames[0] + "\"");

        } else
            api.sendChannelMessage("Can't vote twice!");

    }

}
