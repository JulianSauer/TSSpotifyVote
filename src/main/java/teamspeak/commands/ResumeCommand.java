package teamspeak.commands;

import teamspeak.VoteListener;

public class ResumeCommand extends BotCommand {

    public ResumeCommand(VoteListener voteListener) {
        super(voteListener);
        commandNames = new String[]{"!resume"};
        description = "Vote to resume paused music";
    }

    @Override
    public void execute(String parameters, int client) {
        api.sendChannelMessage(description);
    }

}
