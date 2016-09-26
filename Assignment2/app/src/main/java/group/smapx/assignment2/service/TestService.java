package group.smapx.assignment2.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import group.smapx.assignment2.NotImplementedException;
import group.smapx.assignment2.R;

public class TestService extends Service {
    private static final String LOG_TAG = "TestService";

    private IBinder binder = new LocalBinder();

    private Timer timer = null;

    /**
     * Empty constructor
     */
    public TestService() {
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
                Calendar cal = Calendar.getInstance();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext())
                        .setContentTitle("Service executed!")
                        .setContentText("Time: " +
                                String.format("%02d:%02d:%02d",
                                        cal.get(Calendar.HOUR_OF_DAY),
                                        cal.get(Calendar.MINUTE),
                                        cal.get(Calendar.SECOND)))
                        .setSmallIcon(R.mipmap.ic_launcher);
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(25, builder.build());
            }
        }, 1000, 10000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TAG, "onDestroy!");
        timer.cancel();
    }

    /**
     * Used to bind the service from an Activity
     */
    public class LocalBinder extends Binder {
        public TestService getService() {
            return TestService.this;
        }
    }

    /**
     * Get the current weather for the city you're in
     * @return Weather information object
     */
    public Object getCurrentWeather() {
        throw new NotImplementedException();
    }

    /**
     * Get the weather report for the past days, which is stored on the phone.
     * @return List of weather information objects
     */
    public List<Object> getPastWeather() {
        throw new NotImplementedException();
    }
}
