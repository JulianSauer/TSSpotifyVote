package spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import plugin.Config;

import java.io.IOException;
import java.net.URI;

public class OAuthHandler {

    private final String scope = "user-read-playback-state," +
            "user-modify-playback-state";

    private final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8888/callback");

    protected SpotifyApi spotifyApi;

    public OAuthHandler() {
        Config config = Config.getInstance();
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(config.getProperty("CLIENT_ID"))
                .setClientSecret(config.getProperty("CLIENT_SECRET"))
                .setRedirectUri(redirectUri)
                .build();
    }

    public SpotifyApi getSpotifyApi() {
        return spotifyApi;
    }

    /**
     * Creates a Uri to ask a user for authorization.
     *
     * @return Uri for the user
     */
    public String getAuthorizationCodeUri() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope(scope)
                .show_dialog(true)
                .build();

        final URI uri = authorizationCodeUriRequest.execute();

        return uri.toString();
    }

    /**
     * Creates accesss and refresh tokens using code retrieved from callback.
     *
     * @param callbackCode Callback from {@link OAuthHandler#getAuthorizationCodeUri()}
     * @return Api object containing tokens
     * @throws IOException
     * @throws SpotifyWebApiException
     */
    protected SpotifyApi getAuthorizationCodeCredentials(String callbackCode) throws IOException, SpotifyWebApiException {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(callbackCode).build();
        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
        spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        return spotifyApi;
    }

}
