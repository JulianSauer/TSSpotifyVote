package teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import spotify.Spotify;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DescriptionUpdater {

    private final Spotify spotify;

    private final TS3Api ts3Api;

    private Map<String, ScheduledFuture> futureTasks;

    ScheduledExecutorService service;

    public DescriptionUpdater(Spotify spotify, VoteListener voteListener) {
        this.spotify = spotify;
        this.ts3Api = voteListener.getTS3Api();
        futureTasks = new HashMap<>();
        service = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Updates the description of a bot in newDelay milliseconds with current song info.
     *
     * @param musicBot Description for this bot will be updated
     * @param newDelay Time to wait until update in milliseconds
     */
    public void updateScheduleFor(Client musicBot, int newDelay) {
        String id = musicBot.getUniqueIdentifier();
        ScheduledFuture task;
        if (futureTasks.containsKey(id)) {
            task = futureTasks.get(id);
            if (task != null)
                task.cancel(false);
        }

        CurrentlyPlayingContext currentContext = spotify.getSongContext(musicBot);
        if (newDelay < 0 || currentContext == null || !currentContext.getIs_playing()) {
            futureTasks.put(musicBot.getUniqueIdentifier(), null);
            return;
        }

        task = service.scheduleAtFixedRate(new UpdateRunnable(musicBot), newDelay, 1000, TimeUnit.MILLISECONDS);
        futureTasks.put(musicBot.getUniqueIdentifier(), task);
    }

    /**
     * Pauses update thread.
     *
     * @param musicBot Description for this bot is not updated
     */
    public void pauseSchedule(Client musicBot) {
        String id = musicBot.getUniqueIdentifier();
        if (!futureTasks.containsKey(id) || futureTasks.get(id) == null)
            return;
        futureTasks.get(id).cancel(false);
        futureTasks.put(id, null);
    }

    /**
     * Updates a description with song information.
     */
    class UpdateRunnable implements Runnable {

        private final Client musicBot;

        UpdateRunnable(Client musicBot) {
            this.musicBot = musicBot;
        }

        @Override
        public void run() {
            Map<ClientProperty, String> nameProperty = new HashMap<>(1);
            nameProperty.put(ClientProperty.CLIENT_DESCRIPTION, spotify.getCurrentSongInfo(musicBot));
            ts3Api.editClient(musicBot.getId(), nameProperty);
            updateScheduleFor(musicBot, spotify.getRemainingMS(musicBot));
        }
    }

}
