package group.smapx.remindalot.model;

import java.io.Serializable;

public class LocationData implements Serializable {
    private String lat;
    private String lon;
    private String formattedAddress;

    public LocationData(String lat, String lon, String formattedAddress) {
        this.lat = lat;
        this.lon = lon;
        this.formattedAddress = formattedAddress;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }
}
