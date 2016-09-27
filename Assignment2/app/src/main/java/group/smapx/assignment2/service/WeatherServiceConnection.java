package group.smapx.assignment2.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class WeatherServiceConnection implements ServiceConnection {
    private WeatherService boundService = null;
    private boolean bound = false;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        WeatherService.LocalBinder binder = (WeatherService.LocalBinder) service;
        boundService = binder.getService();
        bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bound = false;
    }

    public WeatherService getBoundService() {
        return boundService;
    }

    public boolean isBound() {
        return bound;
    }
}
