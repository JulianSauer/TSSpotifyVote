package plugin;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import teamspeak.VoteListener;

public class Main {

    public static void main(String[] args) {

        System.out.println("Starting Bot...");

        final TS3Config config = new TS3Config();
        config.setHost(CredentialsHolder.IP.toString());

        final TS3Query query = new TS3Query(config);
        query.connect();

        final TS3Api api = query.getApi();
        api.login(CredentialsHolder.USERNAME.toString(), CredentialsHolder.PASSWORD.toString());
        api.selectVirtualServerById(1);
        api.setNickname("Vote Bot");
        api.sendServerMessage("Vote for the next song by typing !next in chat");

        api.registerEvent(TS3EventType.TEXT_SERVER, -1);
        api.addTS3Listeners(new VoteListener(api));

    }

}
