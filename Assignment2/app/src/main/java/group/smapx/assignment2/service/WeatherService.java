package group.smapx.assignment2.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import group.smapx.assignment2.NotImplementedException;
import group.smapx.assignment2.R;

public class WeatherService extends Service {
    private static final String LOG_TAG = "WeatherService";
    private IBinder binder = new LocalBinder();
    private Timer timer = null;

    /**
     * Empty constructor
     */
    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate!");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand!");

        if (timer == null) {
            timer = new Timer();
        }

        // Setup timer for getting weather data.
        timer.scheduleAtFixedRate(new TimerTask() {
            // Run should implement getting weather data.
            @Override
            public void run() {
                if (isConnectedToInternet()) {
                    URL url = tryGetUrl("http://api.openweathermap.org/data/2.5/weather?q=Aarhus&APPID=" +
                            getString(R.string.api_key));
                    if (url == null) {
                        return;
                    }

                    InputStream is = null;
                    try {
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setReadTimeout(10000);
                        con.setConnectTimeout(15000);
                        con.setRequestMethod("GET");
                        con.connect();
                        int reponseCode = con.getResponseCode();
                        is = con.getInputStream();

                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            result.append(line);
                        }
                        Log.d(LOG_TAG, "Result: " + result);
                        // TODO: Convert to Java Object and put in DB
                        // Use JsonObject maybe?
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error when trying to fetch weather data", e);
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                Log.e(LOG_TAG, "Couldn't close input stream...", e);
                            }
                        }
                    }
                }
            }

            private URL tryGetUrl(String urlToParse) {
                URL url = null;
                try {
                    url = new URL(urlToParse);
                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, "Error when trying to parse the weather URL: " + urlToParse, e);
                }

                return url;
            }
        }, 1000, 1000000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TAG, "onDestroy!");
        timer.cancel();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Used to bind the service from an Activity
     */
    public class LocalBinder extends Binder {
        public WeatherService getService() {
            return WeatherService.this;
        }
    }

    /**
     * Get the current weather for the city you're in
     *
     * @return Weather information object
     */
    public Object getCurrentWeather() {
        throw new NotImplementedException();
    }

    /**
     * Get the weather report for the past days, which is stored on the phone.
     *
     * @return List of weather information objects
     */
    public List<Object> getPastWeather() {
        throw new NotImplementedException();
    }
}
