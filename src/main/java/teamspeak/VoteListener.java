package teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class VoteListener extends TS3EventAdapter {

    private final TS3Api api;
    private final int clientId;

    private int voteCounter;

    public VoteListener(TS3Api api) {
        this.api = api;
        clientId = api.whoAmI().getId();
        voteCounter = 0;
    }

    @Override
    public void onTextMessage(TextMessageEvent e) {
        if (e.getTargetMode() == TextMessageTargetMode.SERVER && e.getInvokerId() != clientId) {
            String message = e.getMessage().toLowerCase();

            if (message.equals("!next")) {
                voteCounter++;
                api.sendServerMessage("Votes: " + voteCounter);
            }
        }
    }

}
