package spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Spotify extends OAuthHandler {

    // <TS Client ID, Token>
    private Map<Integer, SpotifyApi> spotifyAccounts;

    public Spotify() {
        spotifyAccounts = new HashMap();
    }

    public boolean storeSpotifyUser(String code, int tsUser) {
        try {
            spotifyAccounts.put(tsUser, getAuthorizationCodeCredentials(code));
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void nextSong(int tsUser) {
        System.out.println("Next song for Spotify account of " + tsUser);
    }

    public void previousSong(int tsUser) {
        System.out.println("Previous song for Spotify account of " + tsUser);
    }

    public void pauseSong(int tsUser) {
        System.out.println("Pause song for Spotify account of " + tsUser);
    }

    public void resumeSong(int tsUser) {
        System.out.println("Resume song for Spotify account of " + tsUser);
    }

    public String getCurrentSong(int tsUser) {
        System.out.println("Current song for Spotify account of " + tsUser);
        return "idk";
    }

}
