package group.smapx.remindalot.Create;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import group.smapx.remindalot.CreateActivity;
import group.smapx.remindalot.Models.LocationData;

/**
 * Created by benla on 10/6/2016.
 */

public class ValidatorThread extends AsyncTask<String, Integer, LocationData> {

    private Context context;
    private EditText edittext;
    private LocationData locationData;

    public ValidatorThread(EditText txt, Context contex, LocationData data){
        this.context = contex;
        this.edittext = txt;
        this.locationData = data;
    }

    boolean err = false;
    protected LocationData doInBackground(String... locaton) {

        AddressValidator validator = new AddressValidator();
        try {
            if(validator.validate(locaton[0])){
                locationData = validator.getLocationData();
            }
        } catch (Exception e) {
            err = true;
            return null;
        };
        return locationData;
    }

    protected void onPostExecute(LocationData result) {
        if(err){
            Toast.makeText(context, "Could not get data from google - are you online?",Toast.LENGTH_LONG).show();
            locationData = null;
        }
        else{
            this.edittext.setText(result.getFormattedAddress());
        }
    }
}
