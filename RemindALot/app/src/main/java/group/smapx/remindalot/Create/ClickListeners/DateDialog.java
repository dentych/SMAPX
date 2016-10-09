package group.smapx.remindalot.Create.ClickListeners;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.TimeZone;

import group.smapx.remindalot.Create.ReciverInterfaces.DateTimeReceiver;

/**
 * Created by benla on 10/5/2016.
 */

public class DateDialog implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private final Context context;
    private final DateTimeReceiver receiver;
    private int day;
    private int month;
    private int year;

    public DateDialog(Context context, DateTimeReceiver receiver) {
        this.receiver = receiver;
        this.context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        receiver.onDateChosen(dayOfMonth, month, year);
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(this.context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
