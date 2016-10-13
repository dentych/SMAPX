package group.smapx.remindalot.Create;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import group.smapx.remindalot.Create.ReciverInterfaces.LocationDataReceiver;
import group.smapx.remindalot.R;
import group.smapx.remindalot.model.LocationData;

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
            else this.err = true;
        } catch (Exception e) {
            this.err = true;
            return null;
        }
        return this.locationData;
    }

    @Override
    protected void onPostExecute(LocationData result) {
        if (this.err) {
            Toast.makeText(this.context, R.string.address_network_validation_error, Toast.LENGTH_LONG).show();
            this.locationData = null;
        } else {
            this.receiver.onLocationDataReady(result);
        }
    }
}
