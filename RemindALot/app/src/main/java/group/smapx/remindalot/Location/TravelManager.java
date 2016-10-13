package group.smapx.remindalot.Location;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import group.smapx.remindalot.Create.HTTPRequestExecutor;
import group.smapx.remindalot.model.LocationData;
import group.smapx.remindalot.model.TravelInfo;

public class TravelManager {

    String BaseURL = "http://maps.googleapis.com/maps/api/directions/json?origin=";
    String DestionationURL = "&destination=";
    String TransportationURL = "&sensor=false&mode=";


    public void getTravelInfo(final String type, final LocationData from, final LocationData to, final TravelinfoReceier receier) {

        new Thread() {
            public void run() {
                String travelRequestURL = BaseURL + from.getLat() + "+" + from.getLon() +
                        DestionationURL + to.getLat() + "+" + to.getLon() +
                        TransportationURL + type;

                TravelInfo info = new TravelInfo();
                String response = null;
                try {
                    response = new HTTPRequestExecutor().get(travelRequestURL);
                    info = parseJSONData(response);
                } catch (Exception e) {
                    receier.onException(e);
                    return;
                }

                info.setFrom(from);
                info.setTo(to);
                info.setTravelType(type);
                receier.onTravelInfoReady(info);
            }
        }.start();

    }

    public TravelInfo parseJSONData(String responeData) throws ParseException {
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(responeData);
        JSONObject responseObject = (JSONObject) obj;


        JSONArray resultsArray = (JSONArray) responseObject.get("routes");

        if (resultsArray.size() == 0)
            throw new ParseException(0);
        JSONObject routesObject = (JSONObject) resultsArray.get(0);
        JSONArray legsarray = (JSONArray) routesObject.get("legs");
        JSONObject legsobject = (JSONObject) legsarray.get(0);

        JSONObject distance = (JSONObject) legsobject.get("distance");
        JSONObject duration = (JSONObject) legsobject.get("duration");

        String dist = distance.get("value").toString();
        String dur = duration.get("value").toString();

        TravelInfo travelInfo = new TravelInfo();
        travelInfo.setDistance(Integer.parseInt(dist));
        travelInfo.setSecondsOfTravel(Long.parseLong(dur));

        return travelInfo;

    }


}
