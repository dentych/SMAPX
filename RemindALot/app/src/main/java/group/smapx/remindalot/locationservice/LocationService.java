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
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import group.smapx.remindalot.Location.TravelManager;
import group.smapx.remindalot.Location.TravelinfoReceier;
import group.smapx.remindalot.SMShelper.SMShelper;
import group.smapx.remindalot.database.DatabaseDAO;
import group.smapx.remindalot.model.LocationData;
import group.smapx.remindalot.model.Reminder;
import group.smapx.remindalot.model.TravelInfo;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, TravelinfoReceier {
    private static final String LOG_TAG = "LocationService";
    SMShelper smShelper;
    private TravelManager travelManager;
    private DatabaseDAO db;
    private GoogleApiClient googleApiClient;
    Reminder latestReminder;

    private boolean googleApiConnected = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Created");
        travelManager = new TravelManager();
        db = new DatabaseDAO(getBaseContext());
        smShelper = new SMShelper(getBaseContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long delay = 60000;
        if (!googleApiConnected) {
            googleApiClient = new GoogleApiClient.Builder(getBaseContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
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
        locationRequest.setInterval(2500);
        locationRequest.setFastestInterval(1000);
        locationRequest.setMaxWaitTime(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                locationRequest,
                this
        );
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(LOG_TAG, "On connected, starting shit");
        googleApiConnected = true;
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiConnected = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        googleApiConnected = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG_TAG, "New location: " + location.getLatitude() + " / " + location.getLongitude());

        latestReminder = db.getFirstReminder();
        if (latestReminder == null) {
            return;
        }
        if (latestReminder.isSmsSent()) {
            return; // Kunne gøre fancy snask, but no.
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

        if (travelInfo == null) {
            return;
        }

        long timeLeft = latestReminder.getDate() - Calendar.getInstance().getTimeInMillis();
        long secondsOfTravel = travelInfo.getSecondsOfTravel();

        Log.d(LOG_TAG, "Seconds of " + secondsOfTravel);
        Log.d(LOG_TAG, "Timeleft to reminder : " + timeLeft);
        Log.d(LOG_TAG, "Delay: " + (secondsOfTravel * 1000 - timeLeft));
        if (timeLeft < secondsOfTravel * 1000) {
            Log.d("Debug", "IN IF: " + (secondsOfTravel * 1000 - timeLeft));
          //  String delay = Long.toString((((secondsOfTravel * 1000 - timeLeft) / (1000 * 60)) % 60));
            String delay = Long.toString( TimeUnit.MILLISECONDS.toMinutes(secondsOfTravel * 1000 - timeLeft));

            smShelper.sendSMS(latestReminder.getContacts(), delay);
            latestReminder.setSmsSent(true);
            db.updateReminder(latestReminder);
        }
    }

    @Override
    public void onException(Exception e) {
        Log.d("Error", "Exception caught: " + e.getMessage());
    }
}
