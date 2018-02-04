package teamspeak.commands;

import teamspeak.VoteListener;

public class PreviousCommand extends BotCommand {

    public PreviousCommand(VoteListener voteListener) {
        super(voteListener);
        commandNames = new String[]{"!previous"};
        description = "Vote to rewind to the last song";
    }

    @Override
    public void execute(String parameters, int client) {
        api.sendChannelMessage(description);
    }

}
