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

    public boolean storeSpotifyUser(String code) {
        int tsUser = 0;
        try {
            spotifyAccounts.put(tsUser, getAuthorizationCodeCredentials(code));
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void nextSong() {

    }

    public void previousSong() {

    }

    public void pauseSong() {

    }

    public void resumeSong() {

    }

}
