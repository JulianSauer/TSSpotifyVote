package teamspeak.commands;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import teamspeak.VoteListener;

import java.util.Arrays;

public class ListCommand extends BotCommand {

    public ListCommand(VoteListener voteListener) {
        super(voteListener);
        commandNames = new String[]{"!list"};
        description = "shows a list of all the commands available";
    }

    @Override
    public void execute(String parameters, Client client) {
        for (BotCommand botCommand : BotCommand.getCommands()) {
            api.sendPrivateMessage(client.getId(), "Command: " + Arrays.toString(botCommand.getCommandNames()));
            api.sendPrivateMessage(client.getId(), "Description: " + botCommand.getDescription());
        }
    }

}
