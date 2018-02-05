package teamspeak.commands;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import teamspeak.VoteListener;

public class PauseCommand extends BotCommand {

    public PauseCommand(VoteListener voteListener) {
        super(voteListener);
        commandNames = new String[]{"!pause"};
        description = "Vote to pause the music";
    }

    @Override
    public void execute(String parameters, Client client) {
        api.sendChannelMessage(description);
    }

}
