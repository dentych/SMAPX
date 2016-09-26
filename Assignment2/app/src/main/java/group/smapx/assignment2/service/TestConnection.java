package group.smapx.assignment2.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class TestConnection implements ServiceConnection {
    private TestService boundService = null;
    private boolean bound = false;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        TestService.LocalBinder binder = (TestService.LocalBinder) service;
        boundService = binder.getService();
        bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bound = false;
    }

    public TestService getBoundService() {
        return boundService;
    }

    public boolean isBound() {
        return bound;
    }
}
