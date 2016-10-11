package group.smapx.remindalot.Create;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequestExecutor {

    HttpURLConnection connection;
    URL url;

    private boolean isOnline() {
        return true;
    }

    public String get(String req) throws IOException {
        if (!isOnline())
            throw new IOException("Not online");

        this.url = new URL(req);
        this.connection = (HttpURLConnection) this.url.openConnection();
        this.connection.setRequestMethod("GET");

        InputStream inputStream = this.connection.getInputStream();
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
