package group.smapx.remindalot.Create.ReciverInterfaces;

import group.smapx.remindalot.model.LocationData;

public interface LocationDataReceiver {

    public void onLocationDataReady(LocationData locationData);

}
