package group.smapx.remindalot.Permitter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

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
            case 192: {
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
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                return 2;
            default:
                return 0;

        }
    }

    public void getPermission(Activity activity, String permission, PermissionCallback callback) {
        this.callback = callback;
        if (ContextCompat.checkSelfPermission(activity,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)) {

            } else {

                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        getPermissionCode(permission));

            }
        }
    }
}
