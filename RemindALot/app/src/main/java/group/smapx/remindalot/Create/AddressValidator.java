package group.smapx.remindalot.Create;

import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;

import group.smapx.remindalot.model.LocationData;

/**
 * Created by benla on 10/5/2016.
 */


public class AddressValidator {

    final String APIConnector = "&key=";
    final String API = "AIzaSyBr22PX72IGaOkU7SRQ6LDGQIcal9U2fFs";
    final String AddressLookup = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    HTTPRequestExecutor requestExecutor = new HTTPRequestExecutor();
    private LocationData locData;

    public boolean validate(String address) throws Exception {
//        Log.d("Debug","Searching for address " + address);
        String addressRequest = address.replaceAll("\\s+", "+");
        String req = this.AddressLookup + addressRequest + this.APIConnector + this.API;
        String responeData = "";

        try {
            responeData = this.requestExecutor.get(req);
        } catch (IOException e) {
            throw new Exception("Error while downloading data: " + e.getMessage());
        }
              JSONParser parser = new JSONParser();

        Object obj = parser.parse(responeData);
        JSONObject responseObject = (JSONObject) obj;


        JSONArray resultsArray = (JSONArray) responseObject.get("results");
        if (resultsArray.size() == 0)
            return false;
        JSONObject jsonObject2 = (JSONObject) resultsArray.get(0);
        JSONObject jsonObject3 = (JSONObject) jsonObject2.get("geometry");

        JSONObject location = (JSONObject) jsonObject3.get("location");


        this.locData = new LocationData(location.get("lat").toString(),
                location.get("lng").toString(),
                jsonObject2.get("formatted_address").toString());

        return true;

    }

    public LocationData getLocationData() {
        return this.locData;
    }


}


