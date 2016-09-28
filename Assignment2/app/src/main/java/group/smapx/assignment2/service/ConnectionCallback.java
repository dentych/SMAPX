package group.smapx.assignment2.service;

/**
 * Callback interface for executing code when service is connected.
 */
public interface ConnectionCallback {
    /**
     * Called when the service is bound.
     */
    public void connected();
}
