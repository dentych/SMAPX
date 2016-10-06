package group.smapx.remindalot.Create;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by benla on 10/5/2016.
 */

public class HTTPRequestExecutor {

    HttpURLConnection connection;
    URL url;

    private boolean isOnline() {
        return true;
    }

    public String get(String req) throws IOException {
        if (!isOnline())
            throw new IOException("Not online");

        url = new URL(req);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);

        BufferedReader bufferedReader = new BufferedReader(reader);
        String response = "";

        String inputLine;
        while ((inputLine = bufferedReader.readLine()) != null) {
            response += inputLine;
        }

        return response;

    }
}
