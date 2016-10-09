package group.smapx.remindalot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends ArrayAdapter<Contact> {
    public ContactsAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.view_contact, null);
        }

        TextView name = (TextView) v.findViewById(R.id.name);
        TextView phoneNumber = (TextView) v.findViewById(R.id.phoneNumber);
        Contact contact = getItem(position);

        if (contact != null) {
            name.setText(contact.getName());
            phoneNumber.setText(contact.getPhoneNumber());
        }

        return v;
    }
}
