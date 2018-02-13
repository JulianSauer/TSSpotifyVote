package spotify;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.exceptions.detailed.UnauthorizedException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.model_objects.miscellaneous.Device;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.player.*;
import plugin.Config;
import teamspeak.DescriptionUpdater;
import teamspeak.VoteListener;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Spotify {

    private final int UPDATE_DELAY = 2000;

    // <TS Client ID, Token>
    private Map<String, SpotifyApi> spotifyAccounts;

    private final String scope = "user-read-playback-state," +
            "user-modify-playback-state";

    private final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8888/callback");

    private Config config;

    private final TS3Api ts3Api;

    private DescriptionUpdater descriptionUpdater;

    public Spotify(VoteListener voteListener) {
        spotifyAccounts = new HashMap<>();
        config = Config.getInstance();
        ts3Api = voteListener.getTS3Api();
        descriptionUpdater = new DescriptionUpdater(this, voteListener);
    }

    public boolean storeSpotifyUser(String code, Client musicBot) {
        SpotifyApi api = getAuthorizationCodeCredentials(code);
        return storeSpotifyUser(musicBot, api);
    }

    public boolean loadUser(Client musicBot) {
        String id = musicBot.getUniqueIdentifier();
        String accessToken = config.getProperty(id + ".AccessToken");
        String refreshToken = config.getProperty(id + ".RefreshToken");
        if (accessToken == null || refreshToken == null
                || "".equals(accessToken) || "".equals(refreshToken))
            return false;

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(config.getProperty("CLIENT_ID"))
                .setClientSecret(config.getProperty("CLIENT_SECRET"))
                .build();

        spotifyApi.setAccessToken(accessToken);
        spotifyApi.setRefreshToken(refreshToken);
        spotifyAccounts.put(id, spotifyApi);
        descriptionUpdater.updateScheduleFor(musicBot, 0);

        return true;
    }

    public boolean nextSong(Client musicBot) {

        if (!spotifyAccounts.containsKey(musicBot.getUniqueIdentifier()))
            return false;

        SkipUsersPlaybackToNextTrackRequest skipUsersPlaybackToNextTrackRequest = spotifyAccounts.get(musicBot.getUniqueIdentifier())
                .skipUsersPlaybackToNextTrack()
                .device_id(getDeviceId(musicBot))
                .build();

        try {
            descriptionUpdater.updateScheduleFor(musicBot, UPDATE_DELAY);
            skipUsersPlaybackToNextTrackRequest.execute();
        } catch (UnauthorizedException e) {
            updateTokens(musicBot);
            return nextSong(musicBot);
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean previousSong(Client musicBot) {

        if (!spotifyAccounts.containsKey(musicBot.getUniqueIdentifier()))
            return false;

        SkipUsersPlaybackToPreviousTrackRequest skipUsersPlaybackToPreviousTrackRequest = spotifyAccounts.get(musicBot.getUniqueIdentifier())
                .skipUsersPlaybackToPreviousTrack()
                .device_id(getDeviceId(musicBot))
                .build();

        try {
            descriptionUpdater.updateScheduleFor(musicBot, UPDATE_DELAY);
            skipUsersPlaybackToPreviousTrackRequest.execute();
        } catch (UnauthorizedException e) {
            updateTokens(musicBot);
            return previousSong(musicBot);
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean pauseSong(Client musicBot) {

        CurrentlyPlayingContext currentContex = getSongContext(musicBot);
        if (!spotifyAccounts.containsKey(musicBot.getUniqueIdentifier())
                || currentContex == null
                || !currentContex.getIs_playing())
            return false;

        PauseUsersPlaybackRequest pauseUsersPlaybackRequest = spotifyAccounts.get(musicBot.getUniqueIdentifier())
                .pauseUsersPlayback()
                .device_id(getDeviceId(musicBot))
                .build();

        try {
            descriptionUpdater.pauseSchedule(musicBot);
            pauseUsersPlaybackRequest.execute();
        } catch (UnauthorizedException e) {
            updateTokens(musicBot);
            return pauseSong(musicBot);
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return true;

    }

    public boolean resumeSong(Client musicBot) {

        CurrentlyPlayingContext currentContex = getSongContext(musicBot);
        if (!spotifyAccounts.containsKey(musicBot.getUniqueIdentifier())
                || currentContex == null
                || currentContex.getIs_playing())
            return false;

        StartResumeUsersPlaybackRequest startResumeUsersPlaybackRequest = spotifyAccounts.get(musicBot.getUniqueIdentifier())
                .startResumeUsersPlayback()
                .device_id(getDeviceId(musicBot))
                .build();

        try {
            descriptionUpdater.updateScheduleFor(musicBot, 0);
            startResumeUsersPlaybackRequest.execute();
        } catch (UnauthorizedException e) {
            updateTokens(musicBot);
            return resumeSong(musicBot);
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return true;

    }

    public String getCurrentSongInfo(Client musicBot) {
        if (!spotifyAccounts.containsKey(musicBot.getUniqueIdentifier()))
            return "I have no clue";

        CurrentlyPlayingContext context = getSongContext(musicBot);
        if (context == null)
            return "I have no clue";
        Track track = context.getItem();

        ArtistSimplified[] artists = track.getArtists();
        StringBuilder songInformation = new StringBuilder();

        for (int i = 0; i < artists.length; i++) {
            if (i == artists.length - 1) {
                songInformation.append(artists[i].getName());
                songInformation.append(" - ");
            } else {
                songInformation.append(artists[i].getName());
                songInformation.append(", ");
            }
        }

        songInformation.append(track.getName());
        return songInformation.toString();
    }

    /**
     * Returns the time in milliseconds until the current song ends. If no song is being played a default value is returned.
     *
     * @param musicBot Reference for Spotify account
     * @return Remaining time in milliseconds
     */
    public int getRemainingMS(Client musicBot) {
        CurrentlyPlayingContext context = getSongContext(musicBot);
        if (context == null || !context.getIs_playing())
            return 20000;
        Track track = context.getItem();
        int progress = context.getProgress_ms();
        int duration = track.getDurationMs();
        return duration - progress + UPDATE_DELAY;
    }

    /**
     * Returns information about current playback.
     *
     * @param musicBot Reference for Spotify account
     * @return Playback information
     */
    public CurrentlyPlayingContext getSongContext(Client musicBot) {
        if (!spotifyAccounts.containsKey(musicBot.getUniqueIdentifier()))
            return null;

        GetInformationAboutUsersCurrentPlaybackRequest getInformationAboutUsersCurrentPlaybackRequest = spotifyAccounts.get(musicBot.getUniqueIdentifier())
                .getInformationAboutUsersCurrentPlayback()
                .build();

        try {
            return getInformationAboutUsersCurrentPlaybackRequest.execute();
        } catch (UnauthorizedException e) {
            updateTokens(musicBot);
            return getSongContext(musicBot);
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns currently used device.
     *
     * @param musicBot Reference for Spotify account
     * @return Device ID
     */
    private String getDeviceId(Client musicBot) {

        if (!spotifyAccounts.containsKey(musicBot.getUniqueIdentifier()))
            return "";

        GetUsersAvailableDevicesRequest getUsersAvailableDevicesRequest = spotifyAccounts.get(musicBot.getUniqueIdentifier())
                .getUsersAvailableDevices()
                .build();
        try {
            Device[] devices = getUsersAvailableDevicesRequest.execute();
            for (Device device : devices)
                if (device.getIs_active())
                    return device.getId();
        } catch (UnauthorizedException e) {
            updateTokens(musicBot);
            return getDeviceId(musicBot);
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return "";

    }

    /**
     * Creates a Uri to ask a user for authorization.
     *
     * @return Uri for the user
     */
    public String getAuthorizationCodeUri() {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(config.getProperty("CLIENT_ID"))
                .setClientSecret(config.getProperty("CLIENT_SECRET"))
                .setRedirectUri(redirectUri)
                .build();
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope(scope)
                .show_dialog(true)
                .build();

        final URI uri = authorizationCodeUriRequest.execute();

        return uri.toString();
    }

    /**
     * Creates access and refresh tokens using code retrieved from callback.
     *
     * @param callbackCode Callback from {@link Spotify#getAuthorizationCodeUri()}
     * @return Api object containing tokens
     */
    private SpotifyApi getAuthorizationCodeCredentials(String callbackCode) {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(config.getProperty("CLIENT_ID"))
                .setClientSecret(config.getProperty("CLIENT_SECRET"))
                .setRedirectUri(redirectUri)
                .build();
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(callbackCode).build();

        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return spotifyApi;
    }

    private boolean updateTokens(Client musicBot) {
        String id = musicBot.getUniqueIdentifier();
        if (!spotifyAccounts.containsKey(id))
            return false;

        SpotifyApi spotifyApi = spotifyAccounts.get(id);
        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi
                .authorizationCodeRefresh()
                .build();
        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            return storeSpotifyUser(musicBot, spotifyApi);
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean storeSpotifyUser(Client musicBot, SpotifyApi api) {
        if (api == null)
            return false;
        String id = musicBot.getUniqueIdentifier();
        spotifyAccounts.put(id, api);
        if (api.getAccessToken() == null) {
            String error = "Could not obtain new access token. Try updating manually using !auth.";
            System.err.println(error);
            ts3Api.sendChannelMessage(musicBot.getChannelId(), error);
        } else {
            config.setProperty(id + ".AccessToken", api.getAccessToken());
        }
        if (api.getRefreshToken() == null) {
            String error = "Could not obtain new refresh token. Try updating manually using !auth.";
            System.err.println(error);
            ts3Api.sendChannelMessage(musicBot.getChannelId(), error);
        } else {
            config.setProperty(id + ".RefreshToken", api.getRefreshToken());
        }
        config.saveConfig();
        return true;
    }

}
