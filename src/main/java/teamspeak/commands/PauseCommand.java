package teamspeak.commands;

import teamspeak.VoteListener;

public class PauseCommand extends BotCommand {

    public PauseCommand(VoteListener voteListener) {
        super(voteListener);
        commandNames = new String[]{"!pause"};
        description = "Vote to pause the music";
    }

    @Override
    public void execute(String parameters, int client) {
        api.sendChannelMessage(description);
    }

}
