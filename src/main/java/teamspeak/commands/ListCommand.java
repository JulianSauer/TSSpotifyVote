package teamspeak.commands;

import teamspeak.VoteListener;

import java.util.Arrays;

public class ListCommand extends BotCommand {

    public ListCommand(VoteListener voteListener) {
        super(voteListener);
        commandNames = new String[]{"!list"};
        description = "shows a list of all the commands available";
    }

    @Override
    public void execute(String parameters, int client) {
        for (BotCommand botCommand : BotCommand.getCommands()) {
            api.sendPrivateMessage(client, "Command: " + Arrays.toString(botCommand.getCommandNames()));
            api.sendPrivateMessage(client, "Description: " + botCommand.getDescription());
        }
    }

}
