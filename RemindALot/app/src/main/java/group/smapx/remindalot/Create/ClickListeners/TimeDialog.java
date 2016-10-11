package group.smapx.remindalot.Create.ClickListeners;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.TimeZone;

import group.smapx.remindalot.Create.ReciverInterfaces.DateTimeReceiver;

public class TimeDialog implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    private final Context context;
    private final DateTimeReceiver receiver;
    private int hour;
    private int minute;

    public TimeDialog(Context context, DateTimeReceiver receiver) {
        this.context = context;
        this.receiver = receiver;
    }


    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        TimePickerDialog dialog = new TimePickerDialog(this.context, this, 12, 0, true);
        dialog.setTitle("");
        dialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        this.receiver.onTimeChosen(hourOfDay, minute);
    }
}
