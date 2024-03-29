package group.smapx.remindalot.locationservice;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import group.smapx.remindalot.BasicReminder.BasicReminder;
import group.smapx.remindalot.Location.TravelManager;
import group.smapx.remindalot.Location.TravelinfoReceier;
import group.smapx.remindalot.MainActivity;
import group.smapx.remindalot.SMShelper.SMShelper;
import group.smapx.remindalot.database.DatabaseDAO;
import group.smapx.remindalot.model.LocationData;
import group.smapx.remindalot.model.Reminder;
import group.smapx.remindalot.model.TravelInfo;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, TravelinfoReceier {
    private static final String LOG_TAG = "LocationService";
    private static final long LAST_LOCATION_UPDATE_INTERVAL = 60000;
    private static final int TRIGGER_DISTANCE = 50;
    private SMShelper smShelper;
    private TravelManager travelManager;
    private DatabaseDAO db;
    private GoogleApiClient googleApiClient;
    private Reminder latestReminder;
    private Location lastLocation;
    private long lastLocationTime;
    private BasicReminder basicReminder;
    private boolean requestingUpdates = false;
    private boolean isMoving = false;

    @Override
    public void onCreate() {
        super.onCreate();
        basicReminder = new BasicReminder(getBaseContext());
        Log.d(LOG_TAG, "Created");
        travelManager = new TravelManager();
        db = new DatabaseDAO(getBaseContext());
        smShelper = new SMShelper(getBaseContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long delay = 60000;
        if (googleApiClient == null || !googleApiClient.isConnected()) {
            googleApiClient = new GoogleApiClient.Builder(getBaseContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        if (!requestingUpdates) {
            if (googleApiClient.isConnected()) {
                startLocationUpdates();
            } else {
                googleApiClient.connect();
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "Destroy");
        super.onDestroy();
        stopLocationUpdates();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient,
                this
        );
    }

    private void startLocationUpdates() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setMaxWaitTime(300000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestingUpdates = false;
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                locationRequest,
                this
        );

        requestingUpdates = true;
    }

    private void updateMovingState(Location location) {
        long timeNow = Calendar.getInstance().getTimeInMillis();
        if (lastLocation == null) {
            lastLocation = location;
            lastLocationTime = timeNow;
            isMoving = false;
        } else {
            long timeSinceUpdated = timeNow - lastLocationTime;
            if (timeSinceUpdated > LAST_LOCATION_UPDATE_INTERVAL) {
                isMoving = lastLocation.distanceTo(location) > TRIGGER_DISTANCE;
                Log.d(LOG_TAG, "Updated move state: " + isMoving);
                lastLocation = location;
                lastLocationTime = timeNow;
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(LOG_TAG, "On connected, starting shit");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG_TAG, "New location: " + location.getLatitude() + " / " + location.getLongitude());

        updateMovingState(location);

        latestReminder = getFirstReminder();
        if (latestReminder == null) {
            return;
        }

        double lat = location.getLatitude();
        double lon = location.getLongitude();
        LocationData from = new LocationData(
                String.valueOf(lat),
                String.valueOf(lon),
                ""
        );

        travelManager.getTravelInfo(latestReminder.getMeansOfTransportation(), from, latestReminder.getLocationData(), this);
    }

    @Override
    public void onTravelInfoReady(TravelInfo travelInfo) {

        if (travelInfo == null || latestReminder == null) {
            Log.d(LOG_TAG, "TravelInfo or Reminder null, aborting TravelInfoReady");
            return;
        }

        long timeLeft = latestReminder.getDate() - Calendar.getInstance().getTimeInMillis();
        long millisecondsOfTravel = travelInfo.getSecondsOfTravel() * 1000;
        if (!latestReminder.isNotified()) {
            long basicReminderTime = latestReminder.getDate() - millisecondsOfTravel - TimeUnit.MINUTES.toMillis(15);
            basicReminder.setAlarm(latestReminder, basicReminderTime);
        }

        Log.d(LOG_TAG, "Milliseconds of travel: " + millisecondsOfTravel);
        Log.d(LOG_TAG, "Timeleft to reminder: " + timeLeft);
        Log.d(LOG_TAG, "Delay: " + (millisecondsOfTravel - timeLeft));
        if (timeLeft < millisecondsOfTravel && !latestReminder.isSmsSent() && isMoving) {
            Log.d("Debug", "IN IF: " + (millisecondsOfTravel - timeLeft));
            String delay = Long.toString((TimeUnit.MILLISECONDS.toMinutes(millisecondsOfTravel - timeLeft)));
            smShelper.sendSMS(latestReminder.getContacts(), delay);
            latestReminder.setSmsSent(true);
            db.updateReminder(latestReminder);
        }
    }

    @Override
    public void onException(Exception e) {
        e.printStackTrace();
    }

    public Reminder getFirstReminder() {
        Reminder firstReminder = db.getFirstReminder();
        if (firstReminder != null) {
            if (Calendar.getInstance().getTimeInMillis() > firstReminder.getDate()) {
                db.deleteReminder(firstReminder.getId());
                Intent intent = new Intent(MainActivity.ACTION_REMINDER_DELETED);
                intent.putExtra("reminder", firstReminder);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                firstReminder = db.getFirstReminder();
            }
        }
        return firstReminder;
    }
}
