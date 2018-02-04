package spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.Device;
import com.wrapper.spotify.requests.data.player.GetUsersAvailableDevicesRequest;
import com.wrapper.spotify.requests.data.player.SkipUsersPlaybackToNextTrackRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Spotify {

    // <TS Client ID, Token>
    private Map<Integer, SpotifyApi> spotifyAccounts;

    public Spotify() {
        spotifyAccounts = new HashMap<>();
    }

    public boolean storeSpotifyUser(String code, int tsUser) {
        try {
            OAuthHandler oAuth = new OAuthHandler();
            spotifyAccounts.put(tsUser, oAuth.getAuthorizationCodeCredentials(code));
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean nextSong(int tsUser) {

        if (!spotifyAccounts.containsKey(tsUser))
            return false;

        SkipUsersPlaybackToNextTrackRequest skipUsersPlaybackToNextTrackRequest = spotifyAccounts.get(tsUser)
                .skipUsersPlaybackToNextTrack()
                .device_id(getDeviceId(tsUser))
                .build();

        try {
            String result = skipUsersPlaybackToNextTrackRequest.execute();
            System.out.println(result);
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
            return false;
        }

        return true;

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

    private String getDeviceId(int tsUser) {

        if (!spotifyAccounts.containsKey(tsUser))
            return "";

        GetUsersAvailableDevicesRequest getUsersAvailableDevicesRequest = spotifyAccounts.get(tsUser)
                .getUsersAvailableDevices()
                .build();
        try {
            Device[] devices = getUsersAvailableDevicesRequest.execute();
            for (Device device : devices)
                if (device.getIs_active())
                    return device.getId();
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return "";

    }

}
