package group.smapx.remindalot.Create;

import android.location.Geocoder;
import android.util.Log;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;

import group.smapx.remindalot.model.LocationData;

/**
 * Created by benla on 10/5/2016.
 */


public class AddressValidator {

    private LocationData locData;
    HTTPRequestExecutor requestExecutor = new HTTPRequestExecutor();

    String APIConnector = "&key=";
    String API = "AIzaSyBr22PX72IGaOkU7SRQ6LDGQIcal9U2fFs";
    String AddressLookup = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    public boolean validate(String address) throws Exception {
//        Log.d("Debug","Searching for address " + address);
        String addressRequest = address.replaceAll("\\s+","+");
        String req = AddressLookup + addressRequest + APIConnector + API;
        String responeData = "";

        try {
            responeData = requestExecutor.get(req);
        } catch (IOException e) {
            throw new Exception("Error while downloading data: " + e.getMessage());
        }
        System.out.println(responeData);
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(responeData);
        JSONObject responeObj =  (JSONObject)obj;


        JSONArray jsonObject1 = (JSONArray) responeObj.get("results");
        if(jsonObject1.size() == 0)
            return false;
        JSONObject jsonObject2 = (JSONObject)jsonObject1.get(0);
        JSONObject jsonObject3 = (JSONObject)jsonObject2.get("geometry");

        JSONObject location = (JSONObject) jsonObject3.get("location");


        locData = new LocationData(location.get("lat").toString(),
                location.get("lng").toString(),
                jsonObject2.get("formatted_address").toString());

        return true;

    }

    public LocationData getLocationData() {
        return locData;
    }


}


