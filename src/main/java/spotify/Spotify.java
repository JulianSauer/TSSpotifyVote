package spotify;

import plugin.CredentialsHolder;

import java.io.IOException;

public class Spotify extends RESTfulService {

    public int nextSong() throws IOException {
        return post("https://api.spotify.com/v1/me/player/next");
    }

    public int previousSong() throws IOException {
        return post("https://api.spotify.com/v1/me/player/previous");
    }

    public String currentSong() throws IOException {
        return get("https://api.spotify.com/v1/me/player");
    }

    private int post(String url) throws IOException {
        int responseCode = post(url, CredentialsHolder.SPOTIFY_TOKEN.toString());
        System.out.println("Response Code : " + responseCode);
        System.out.println("URL: " + url);
        return responseCode;
    }

}
