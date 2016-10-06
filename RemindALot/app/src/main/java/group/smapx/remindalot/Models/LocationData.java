package group.smapx.remindalot.Models;

/**
 * Created by benla on 10/6/2016.
 */

public class LocationData {

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

    String lat;
    String lon;
    String formattedAddress;
}
