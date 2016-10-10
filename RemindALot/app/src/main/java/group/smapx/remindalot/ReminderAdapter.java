package group.smapx.remindalot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;

import group.smapx.remindalot.model.Reminder;

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    public ReminderAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.reminder_list_layout, null);
        }

        TextView titleText = (TextView) v.findViewById(R.id.title_text);
        TextView timeText = (TextView) v.findViewById(R.id.time_text);
//        TextView placeText = (TextView) v.findViewById(R.id.place_text);
        Reminder reminder = getItem(position);

        if (reminder != null) {
            titleText.setText(reminder.getTitle());
            timeText.setText(new Date(reminder.getDate()).toString());
//            placeText.setText(reminder.getLocationData().getFormattedAddress());
        }

        return v;
    }
}