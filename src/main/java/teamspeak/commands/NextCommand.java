package teamspeak.commands;

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
    public void execute(String parameters, int client) {

        int userCount = getUserCount(voteListener.getMusicBot());
        if (!ballotBox.contains(client, commandNames[0])) {
            if (ballotBox.castVoteFor(commandNames[0], client, userCount))
                spotify.nextSong(client);
            api.sendChannelMessage(ballotBox.countVotesFor(commandNames[0]) + 1 + "/" + (int) Math.ceil(((float) userCount) / 2) + " Users have voted for \"" + commandNames[0] + "\"");


        } else
            api.sendChannelMessage("Can't vote twice!");

    }

}
