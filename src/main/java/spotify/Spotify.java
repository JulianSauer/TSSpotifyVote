package spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.model_objects.miscellaneous.Device;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.player.*;
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

    public boolean previousSong(String tsUser) {

        if (!spotifyAccounts.containsKey(tsUser))
            return false;

        SkipUsersPlaybackToPreviousTrackRequest skipUsersPlaybackToPreviousTrackRequest = spotifyAccounts.get(tsUser)
                .skipUsersPlaybackToPreviousTrack()
                .device_id(getDeviceId(tsUser))
                .build();

        try {
            String result = skipUsersPlaybackToPreviousTrackRequest.execute();
            System.out.println(result);
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean pauseSong(String tsUser) {

        if (!spotifyAccounts.containsKey(tsUser))
            return false;

        PauseUsersPlaybackRequest pauseUsersPlaybackRequest = spotifyAccounts.get(tsUser)
                .pauseUsersPlayback()
                .device_id(getDeviceId(tsUser))
                .build();

        try {
            String result = pauseUsersPlaybackRequest.execute();
            System.out.println(result);
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return true;

    }

    public boolean resumeSong(String tsUser) {

        if (!spotifyAccounts.containsKey(tsUser))
            return false;

        StartResumeUsersPlaybackRequest startResumeUsersPlaybackRequest = spotifyAccounts.get(tsUser)
                .startResumeUsersPlayback()
                .device_id(getDeviceId(tsUser))
                .build();

        try {
            String result = startResumeUsersPlaybackRequest.execute();
            System.out.println(result);
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return true;

    }

    public String getCurrentSong(String tsUser) {

        if (!spotifyAccounts.containsKey(tsUser))
            return "I have no clue";

        GetInformationAboutUsersCurrentPlaybackRequest getInformationAboutUsersCurrentPlaybackRequest = spotifyAccounts.get(tsUser)
                .getInformationAboutUsersCurrentPlayback()
                .build();

        try {
            CurrentlyPlayingContext result = getInformationAboutUsersCurrentPlaybackRequest.execute();

            Track track = result.getItem();
            ArtistSimplified[] artists = track.getArtists();
            StringBuilder songInformation = new StringBuilder();

            for (int i = 0; i < artists.length; i++) {
                if (i == artists.length - 1)
                    songInformation.append(artists[i].getName() + " - ");
                else
                    songInformation.append(artists[i].getName() + ", ");
            }

            songInformation.append(track.getName());
            return songInformation.toString();

        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return "I have no clue";

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
