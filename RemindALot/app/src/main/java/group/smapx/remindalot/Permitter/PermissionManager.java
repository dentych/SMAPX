package group.smapx.remindalot.Permitter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class PermissionManager extends AppCompatActivity {


    private boolean permissionGranted;
    private boolean isDone;
    private PermissionCallback callback;


    public boolean checkPermission(Activity activity, String permission) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity,
                permission);

        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.callback.onPermissionGranted();
                } else {
                    this.callback.onPermissionDenied();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private int getPermissionCode(String permission) {
        switch (permission) {
            case Manifest.permission.READ_CONTACTS:
                return 1;
            default:
                return 0;

        }
    }

    public void getPermission(Activity activity, String permission, PermissionCallback callback) {
        this.callback = callback;
        Log.d("Debug","Get perm is called");
        if (ContextCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Debug","Permission not in place, trying to get.");

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)) {

            } else {

                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        getPermissionCode(permission));

            }
        }
        else{
            Log.d("Debug","Already in place");
            callback.onPermissionGranted();
        }
    }
}
