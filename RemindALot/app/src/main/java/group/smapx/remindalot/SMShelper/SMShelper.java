package group.smapx.remindalot.SMShelper;

import android.Manifest;
import android.app.Activity;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.List;

import group.smapx.remindalot.Permitter.PermissionCallback;
import group.smapx.remindalot.Permitter.PermissionManager;
import group.smapx.remindalot.model.Contact;

public class SMShelper implements PermissionCallback {

    private String LOG_TAG = "SMShelper";
    private Activity activity;
    private PermissionManager permissionManager = new PermissionManager();
    private boolean permissionsGranted = false;

    public SMShelper(Activity receivedActivity) {
        activity = receivedActivity;
        getPermissions();
        Log.d(LOG_TAG, "Create SMShelper");
    }

    private void getPermissions() {
        Log.d(LOG_TAG, "Getting permissions");
        permissionManager.getPermission(activity, Manifest.permission.SEND_SMS, this);
    }

    public void sendSMS(List<Contact> contactList, String delay) {

        SmsManager smsManager = SmsManager.getDefault();

        if (permissionsGranted) {

            for (Contact contact : contactList) {

                String phoneNumber = contact.getPhoneNumber();

                if (phoneNumber != null) {

                    String message = "Im sorry to say that i will be " + delay + " minutes late, see you soon!";

                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    Log.d(LOG_TAG, "Send SMS to: " + phoneNumber);
                }
            }
        }
    }

    @Override
    public void onPermissionGranted() {
        permissionsGranted = true;
        Log.d(LOG_TAG, "Permission granted");
    }

    @Override
    public void onPermissionDenied() {
        permissionsGranted = false;
        Log.d(LOG_TAG, "Permission denied");
    }
}
