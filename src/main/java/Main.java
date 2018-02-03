import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;

public class Main {

    public static void main(String[] args) {

        System.out.println("Starting Bot...");

        final TS3Config config = new TS3Config();
        config.setHost(Config.getInstance().getProperty("IP"));

        final TS3Query query = new TS3Query(config);
        query.connect();
        // TODO implement use of config file and some sort of user confirmation (like "EULA" in minecraft config

        Config conf = Config.getInstance();

        if (conf.getProperty("eula").equals("false")) {
            System.out.println("You have not excepted the eula. Please open your config file under <filepath> and change the line eula=false to eula=true");
        }

        final TS3Api api = query.getApi();
        api.login(Config.getInstance().getProperty("USERNAME"), Config.getInstance().getProperty("PASSWORD"));
        api.selectVirtualServerById(1);
        api.setNickname(Config.getInstance().getProperty("USERNAME"));

        api.registerEvent(TS3EventType.TEXT_SERVER, -1);
        api.addTS3Listeners(new VoteListener(api));


    }

}
