package spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.Device;
import com.wrapper.spotify.requests.data.player.GetUsersAvailableDevicesRequest;
import com.wrapper.spotify.requests.data.player.SkipUsersPlaybackToNextTrackRequest;
import plugin.Config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Spotify {

    // <TS Client ID, Token>
    private Map<String, SpotifyApi> spotifyAccounts;

    private Config config;

    public Spotify() {
        spotifyAccounts = new HashMap<>();
        config = Config.getInstance();
    }

    public boolean storeSpotifyUser(String code, String tsUser) {
        try {
            OAuthHandler oAuth = new OAuthHandler();
            SpotifyApi api = oAuth.getAuthorizationCodeCredentials(code);
            spotifyAccounts.put(tsUser, api);
            config.setProperty(tsUser + ".AccessToken", api.getAccessToken());
            config.setProperty(tsUser + ".RefreshToken", api.getRefreshToken());
            config.saveConfig();
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean loadUser(String tsUser) {
        String accessToken = config.getProperty(tsUser + ".AccessToken");
        String refreshToken = config.getProperty(tsUser + ".RefreshToken");
        if (accessToken == null || refreshToken == null
                || "".equals(accessToken) || "".equals(refreshToken))
            return false;

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(config.getProperty("CLIENT_ID"))
                .setClientSecret(config.getProperty("CLIENT_SECRET"))
                .build();

        spotifyApi.setAccessToken(accessToken);
        spotifyApi.setRefreshToken(refreshToken);
        spotifyAccounts.put(tsUser, spotifyApi);

        return true;
    }

    public boolean nextSong(String tsUser) {

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

    public void previousSong(String tsUser) {
        System.out.println("Previous song for Spotify account of " + tsUser);
    }

    public void pauseSong(String tsUser) {
        System.out.println("Pause song for Spotify account of " + tsUser);
    }

    public void resumeSong(String tsUser) {
        System.out.println("Resume song for Spotify account of " + tsUser);
    }

    public String getCurrentSong(String tsUser) {
        System.out.println("Current song for Spotify account of " + tsUser);
        return "idk";
    }

    private String getDeviceId(String tsUser) {

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
