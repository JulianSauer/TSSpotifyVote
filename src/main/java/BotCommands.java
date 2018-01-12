import java.util.HashMap;
import java.util.Set;

public final class BotCommands {

    /**
     * the list that stores the commands and definitions
     */
    private static HashMap<String, String> list;

    /**
     * creates a static instance for the bot commands
     */
    BotCommands() {
        list = new HashMap<String, String>();

        list.put("!next", "Vote to skip the current song");
        list.put("!previous", "Vote to rewind to the last song");
        list.put("!pause", "Vote to pause the music");
        list.put("!resume", "Vote to resume paused music");
        list.put("!list", "shows a list of all the commands available");

        //TODO castVoteFor missing commands or implement different solution for initialization of commands
    }


    /**
     * creates a list of all available commands
     *
     * @return the list with the commands
     */
    public Set listCommands() {
        return list.keySet();
    }

    /**
     * checks the list for the given command and returns the definition
     *
     * @param command the command to find the definition to
     * @return the definition of the command
     */
    public String getDefinition(String command) {
        return list.get(command);
    }

}
