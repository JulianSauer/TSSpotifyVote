package teamspeak.commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import teamspeak.VoteListener;

import java.util.HashSet;
import java.util.Set;

public class BotCommand {

    /**
     * the list that stores the commands and definitions
     */
    private static Set<BotCommand> commands = new HashSet<>();

    protected String commandNames[];
    protected String description;

    protected VoteListener voteListener;
    protected TS3Api api;

    public BotCommand(VoteListener voteListener) {
        this.voteListener = voteListener;
        api = voteListener.getTS3Api();
    }

    public void addCommand(BotCommand command) {
        commands.add(command);
    }

    /**
     * Creates a list of all available commands
     *
     * @return the list with the commands
     */
    public static Set<BotCommand> getCommands() {
        return commands;
    }

    /**
     * Finds the description of a command
     *
     * @param commandName the command to find the definition to
     * @return the definition of the command
     */
    public String getDescription(String commandName) {
        for (BotCommand botCommand : commands)
            for (String alias : botCommand.getCommandNames())
                if (commandName.startsWith(alias))
                    return botCommand.getDescription();
        return "";
    }

    public boolean contains(String message) {
        for (BotCommand botCommand : commands)
            for (String alias : botCommand.getCommandNames())
                if (message.startsWith(alias))
                    return true;
        return false;
    }

    public void execute(String message, int client) {
        for (BotCommand botCommand : commands) {
            for (String alias : botCommand.getCommandNames()) {
                if (message.startsWith(alias)) {
                    String parameters = message.replace(alias + " ", "");
                    botCommand.execute(parameters, client);
                    return;
                }
            }
        }

    }

    public int getUserCount(int musicBot) {
        int botCount = 0;
        for (Client client : api.getClients()) {
            if (!client.isServerQueryClient() && client.getId() != musicBot) //query clients don't count as men
                botCount++;
        }
        return botCount;
    }

    public String[] getCommandNames() {
        return commandNames;
    }

    public String getDescription() {
        return description;
    }

}
