package group.smapx.remindalot.Create;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by benla on 10/5/2016.
 */

public class DateDialog implements View.OnClickListener, DatePickerDialog.OnDateSetListener{
    private Context context;
    private EditText dateText;
    private int day;
    private int month;
    private int year;

    public DateDialog(Context context, EditText dateText){
        this.context = context;
        this.dateText = dateText;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar remindCalender = Calendar.getInstance();
        remindCalender.set(Calendar.YEAR, year);
        remindCalender.set(Calendar.MONTH, month);
        remindCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        dateText.setText(format.format(remindCalender.getTime()));
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
