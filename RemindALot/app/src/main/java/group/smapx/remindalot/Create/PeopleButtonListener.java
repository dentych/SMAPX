package group.smapx.remindalot.Create;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import group.smapx.remindalot.R;


/**
 * Created by benla on 10/6/2016.
 */

public class PeopleButtonListener  implements View.OnClickListener {
    static final int PICK_CONTACT=1;
    private final ContactReceiver receiver;
    private Activity activity;

    public PeopleButtonListener(ContactReceiver receiver, Activity activity){
        this.receiver = receiver;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        activity.startActivityForResult(intent, PICK_CONTACT);
    }


    public void onActivityResult(int reqCode, int resultCode, Intent data) {

        String cNumber = null;
        String cName;
        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  activity.managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = activity.getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                            cNumber = phones.getString(phones.getColumnIndex("data1"));
                        }
                        cName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        receiver.onContactChosen(cName,cNumber == null ? "" : cNumber);


                    }
                }
                break;
        }
    }
}
