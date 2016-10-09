package group.smapx.remindalot.Create;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by benla on 10/5/2016.
 */

public class TimeDialog implements View.OnClickListener, TimePickerDialog.OnTimeSetListener{
    private Context context;
    private DateTimeReceiver receiver;
    private int hour;
    private int minute;

    public TimeDialog(Context context, DateTimeReceiver receiver){
        this.context = context;
        this.receiver = receiver;
    }


    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        TimePickerDialog dialog = new TimePickerDialog(context,this,12,0,true);
        dialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        receiver.onTimeChosen(hourOfDay,minute);
    }
}
