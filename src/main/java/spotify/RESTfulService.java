package spotify;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import plugin.CredentialsHolder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RESTfulService {

    public String get(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        get.addHeader("Authorization", "Bearer " + CredentialsHolder.SPOTIFY_TOKEN);

        HttpResponse response = client.execute(get);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }

    public int post(String url, String token) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        post.setHeader("Authorization", "Bearer " + token);

        HttpResponse response = client.execute(post);

        return response.getStatusLine().getStatusCode();
    }

}
