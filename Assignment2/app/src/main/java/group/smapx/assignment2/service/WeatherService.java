package group.smapx.assignment2.service;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import group.smapx.assignment2.DAL.WeatherDAO;
import group.smapx.assignment2.R;
import group.smapx.assignment2.models.WeatherModel;

public class WeatherService extends Service {
    private static final String LOG_TAG = "WeatherService";
    public static final String BROADCAST_ACTION = "group.smapx.assignment2.service.WeatherServiceBroadcast";
    public static final int MSG_NEW_WEATHER = 1;
    private IBinder binder = new LocalBinder();
    private Timer timer = null;
    private WeatherDAO weatherDAO;
    private static final long WAIT_TIME_MILLIS = 30 * 60 * 1000;
    private WeatherModel currentWeather = null;

    /*
    PUBLIC METHODS
     */

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
        weatherDAO = new WeatherDAO(getBaseContext());
        List<WeatherModel> weatherPastDay = weatherDAO.getWeatherForLastDays(1);
        if (weatherPastDay.size() > 0) {
            currentWeather = weatherPastDay.get(weatherPastDay.size() - 1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TAG, "onDestroy!");
        timer.cancel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand!");

        if (timer == null) {
            timer = new Timer();
        }

        // Setup timer for getting weather data.
        timer.scheduleAtFixedRate(new WeatherTimerTask(), 1000, WAIT_TIME_MILLIS);

        return START_STICKY;
    }

    /**
     * Get the current weather in Aarhus.
     *
     * @return WeatherModel object containing information about the weather.
     */
    public WeatherModel getCurrentWeather() {
        return currentWeather;
    }

    /**
     * Get the weather report for the past five days.
     * The information is gathered over time by the phone and stored in a
     * local database.
     *
     * @return List of WeatherModel objects.
     */
    public List<WeatherModel> getPastWeather() {
        return getPastWeather(5);
    }

    /**
     * Get the weather report for the specified number of days.
     * The information is gathered over time by the phone and stored in a
     * local database.
     *
     * @param days Amount of days to get weather data for.
     * @return List of WeatherModel objects.
     */
    public List<WeatherModel> getPastWeather(int days) {
        return weatherDAO.getWeatherForLastDays(days);
    }

    /*
    PRIVATE METHODS
     */
    private URL tryGetUrl(String urlToParse) {
        URL url = null;
        try {
            url = new URL(urlToParse);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error when trying to parse the weather URL: " + urlToParse, e);
        }

        return url;
    }

    private WeatherModel parseResult(String result) {
        Log.d(LOG_TAG, "Parsing weather info from result: " + result);

        long timestamp = Calendar.getInstance().getTime().getTime();
        Gson gson = new Gson();
        WeatherJsonObject weatherObject = gson.fromJson(result, WeatherJsonObject.class);

        WeatherModel weatherInfo = new WeatherModel(
                timestamp,
                weatherObject.getTemperature(),
                weatherObject.getHumidity(),
                weatherObject.getPressure(),
                weatherObject.getTempMin(),
                weatherObject.getTempMax(),
                weatherObject.getWindSpeed(),
                weatherObject.getWindDirection(),
                weatherObject.getClouds()
        );

        return weatherInfo;
    }

    /**
     * Used to bind the service from an Activity
     */
    private boolean isConnectedToInternet() {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /*
    NESTED CLASSES
     */
    public class LocalBinder extends Binder {
        public WeatherService getService() {
            return WeatherService.this;
        }
    }

    private class WeatherJsonObject {
        // long timestamp, double temperature, double humidity, double pressure, double temp_min, double temp_max, double windspeed, double winddirection, String clouds
        private JsonObject coord;
        private JsonArray weather;
        private String base;
        private JsonObject main;
        private JsonObject wind;
        private JsonObject clouds;
        private JsonObject rain;
        private int dt;
        private JsonObject sys;
        private int id;
        private String name;
        private int cod;

        double getTemperature() {
            return main.get("temp").getAsDouble();
        }

        double getHumidity() {
            return main.get("humidity").getAsDouble();
        }

        double getPressure() {
            return main.get("pressure").getAsDouble();
        }

        double getTempMin() {
            return main.get("temp_min").getAsDouble();
        }

        double getTempMax() {
            return main.get("temp_max").getAsDouble();
        }

        double getWindSpeed() {
            return wind.get("speed").getAsDouble();
        }

        double getWindDirection() {
            return wind.get("deg").getAsDouble();
        }

        String getClouds() {
            return "";
        }
    }

    private class WeatherTimerTask extends TimerTask {
        @Override
        public void run() {
            fetchCurrentWeather();
        }

        private void fetchCurrentWeather() {
            if (!isConnectedToInternet()) {
                return;
            }
            URL url = tryGetUrl("http://api.openweathermap.org/data/2.5/weather?q=Aarhus,DK&units=metric&APPID=" +
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
                is = con.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                WeatherModel weatherInfo = parseResult(result.toString());
                long id = weatherDAO.save(weatherInfo);
                sendWeatherBroadcast();
                Log.d(LOG_TAG, "WeatherModel object saved in database with ID: " + id);
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

        private void sendWeatherBroadcast() {
            Intent intent = new Intent(BROADCAST_ACTION);
            intent.putExtra("message", MSG_NEW_WEATHER);
            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
        }
    }
}
