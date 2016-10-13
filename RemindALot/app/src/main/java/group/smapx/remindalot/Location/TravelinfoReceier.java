package group.smapx.remindalot.Location;

import group.smapx.remindalot.model.TravelInfo;

public interface TravelinfoReceier {

    public void onTravelInfoReady(TravelInfo info);
    public void onException(Exception e);
}
