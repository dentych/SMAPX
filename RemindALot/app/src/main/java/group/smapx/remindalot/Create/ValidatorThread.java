package group.smapx.remindalot.Create;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import group.smapx.remindalot.Create.ReciverInterfaces.LocationDataReceiver;
import group.smapx.remindalot.model.LocationData;

/**
 * Created by benla on 10/6/2016.
 */

public class ValidatorThread extends AsyncTask<String, Integer, LocationData> {

    boolean err;
    private final Context context;
    private final LocationDataReceiver receiver;
    private LocationData locationData;

    public ValidatorThread(Context contex, LocationDataReceiver receiver) {
        context = contex;
        this.receiver = receiver;
    }

    @Override
    protected LocationData doInBackground(String... locaton) {

        AddressValidator validator = new AddressValidator();
        try {
            if (validator.validate(locaton[0])) {
                this.locationData = validator.getLocationData();
            }
        } catch (Exception e) {
            this.err = true;
            return null;
        }
        return this.locationData;
    }

    @Override
    protected void onPostExecute(LocationData result) {
        if (this.err) {
            Toast.makeText(this.context, "Could not get data from google - are you online?", Toast.LENGTH_LONG).show();
            this.locationData = null;
        } else {
            this.receiver.onLocationDataReady(result);
        }
    }
}
