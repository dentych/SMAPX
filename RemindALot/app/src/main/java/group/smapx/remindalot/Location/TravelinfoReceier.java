package group.smapx.remindalot.Location;

import group.smapx.remindalot.model.TravelInfo;

/**
 * Created by benla on 10/13/2016.
 */

public interface TravelinfoReceier {

    public void onTravelInfoReady(TravelInfo info);
    public void onException(Exception e);
}
