package group.smapx.remindalot.model;

public class TravelInfo {
    public static class TravelType{
        public static final String WALKING = "walking";
        public static final String DRIVING = "driving";
        public static final String PUBLIC_TRANSPORTATION = "transit";
        public static final String BIKING = "bicycling";
    };

    private LocationData from;
    private LocationData to;

    public LocationData getFrom() {
        return from;
    }

    public void setFrom(LocationData from) {
        this.from = from;
    }

    public LocationData getTo() {
        return to;
    }

    public void setTo(LocationData to) {
        this.to = to;
    }

    public long getSecondsOfTravel() {
        return secondsOfTravel;
    }

    public void setSecondsOfTravel(long secondsOfTravel) {
        this.secondsOfTravel = secondsOfTravel;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType;
    }

    long secondsOfTravel;
    int distance;
    String travelType;

}
