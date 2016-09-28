package group.smapx.assignment2.service;

import android.app.Service;

/**
 * Callback interface for executing code when service is connected.
 */
public interface ConnectionCallback {
    /**
     * Called when the service is bound.
     */
    public void connected(Service service);
}
