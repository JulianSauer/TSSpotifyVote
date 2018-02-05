package teamspeak.commands;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import teamspeak.VoteListener;

public class ResumeCommand extends BotCommand {

    public ResumeCommand(VoteListener voteListener) {
        super(voteListener);
        commandNames = new String[]{"!resume"};
        description = "Vote to resume paused music";
    }

    @Override
    public void execute(String parameters, Client client) {
        api.sendChannelMessage(description);
    }

}
