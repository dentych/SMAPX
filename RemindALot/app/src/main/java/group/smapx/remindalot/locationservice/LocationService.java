package group.smapx.remindalot.locationservice;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;

import group.smapx.remindalot.Location.TravelManager;
import group.smapx.remindalot.database.DatabaseDAO;
import group.smapx.remindalot.model.LocationData;
import group.smapx.remindalot.model.Reminder;
import group.smapx.remindalot.model.TravelInfo;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private TravelManager travelManager;
    private DatabaseDAO db;
    private GoogleApiClient googleApiClient;
    private boolean googleApiConnected = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Debug","Created");
        travelManager = new TravelManager();
        db = new DatabaseDAO(getBaseContext());
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
        googleApiClient.connect();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("Debug","Destroy");
        super.onDestroy();
        stopLocationUpdates();
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
        Log.d("Debug","On connected, starting shit");
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
        Log.d("Debug","New location: " +location.getLatitude() + " / " + location.getLongitude() );

        Reminder reminder = db.getFirstReminder();

        double lat = location.getLatitude();
        double lon = location.getLongitude();
        LocationData from = new LocationData(
                String.valueOf(lat),
                String.valueOf(lon),
                ""
        );

        TravelInfo travelInfo = null;
        try {
            travelInfo = travelManager.getTravelInfo(TravelInfo.TravelType.DRIVING, from, reminder.getLocationData());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (travelInfo == null) {
            return;
        }

        long timeLeft = reminder.getDate() - Calendar.getInstance().getTimeInMillis();
        long secondsOfTravel = travelInfo.getSecondsOfTravel();

        if (timeLeft < secondsOfTravel) {
            // TODO: Send en fucking SMS.
        }
    }
}
