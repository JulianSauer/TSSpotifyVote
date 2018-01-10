import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;

public class Main {

    public static void main(String[] args) {

        System.out.println("Starting Bot...");

        final TS3Config config = new TS3Config();
        config.setHost(CredentialsHolder.IP.toString());

        final TS3Query query = new TS3Query(config);
        query.connect();

        // TODO extend or replace CredentialHolder with Config file

        Config conf=new Config();
        System.out.println(conf.getProperty("IP"));     //DEBUG

        final TS3Api api = query.getApi();
        api.login(CredentialsHolder.USERNAME.toString(), CredentialsHolder.PASSWORD.toString());
        api.selectVirtualServerById(1);
        api.setNickname("Vote Bot");

        api.registerEvent(TS3EventType.TEXT_SERVER, -1);
        api.addTS3Listeners(new VoteListener(api));

    }

}
