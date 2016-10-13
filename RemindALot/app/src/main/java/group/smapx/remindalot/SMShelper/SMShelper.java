package group.smapx.remindalot.SMShelper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.List;

import group.smapx.remindalot.R;
import group.smapx.remindalot.model.Contact;

public class SMShelper {
    private Context context;
    private String LOG_TAG = "SMShelper";
    private boolean permissionsGranted = false;

    public SMShelper(Context context) {
        this.context = context;
        getPermissions();
        Log.d(LOG_TAG, "Create SMShelper");
    }

    private void getPermissions() {
        Log.d(LOG_TAG, "Getting permissions");
        int permission = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            permissionsGranted = true;
        }
    }

    public void sendSMS(List<Contact> contactList, String delay) {

        SmsManager smsManager = SmsManager.getDefault();

        if (permissionsGranted) {

            for (Contact contact : contactList) {

                String phoneNumber = contact.getPhoneNumber();

                if (phoneNumber != null) {

                    String message = context.getString(R.string.sms_part1) + " " + delay + " " +
                            context.getString(R.string.sms_part2);

                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    Log.d(LOG_TAG, "Send SMS to: " + phoneNumber);
                }
            }
        }
    }
}
