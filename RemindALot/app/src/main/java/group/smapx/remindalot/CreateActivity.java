package group.smapx.remindalot;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

import group.smapx.remindalot.Create.AddressValidator;
import group.smapx.remindalot.Create.DateDialog;
import group.smapx.remindalot.Create.TimeDialog;
import group.smapx.remindalot.Create.ValidatorThread;
import group.smapx.remindalot.Models.LocationData;

public class CreateActivity extends AppCompatActivity {

    Calendar remindCalender = Calendar.getInstance();
    EditText dateText;
    EditText timeText;
    DateDialog dateDialog;
    TimeDialog timeDialog;
    LocationData data;
    EditText locationText;

  //  DatePickerDialog dateDialog;
/*
    DatePickerDialog.OnDateSetListener datePickDialogListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            remindCalender.set(Calendar.YEAR, year);
            remindCalender.set(Calendar.MONTH, month);
            remindCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            dateText.setText(format.format(remindCalender.getTime()));
        }


    };*/

    private void initLocationListener(){
        locationText = (EditText) findViewById(R.id.placement);

        locationText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("Debug","OnFuckChange called");
                if(!hasFocus && !locationText.getText().toString().matches("")){
                    ValidatorThread thread = new ValidatorThread(locationText, CreateActivity.this,data);
                    thread.execute(locationText.getText().toString());
                }

            }
        });
    }



    private void initDateTimeListeners(){
        dateText = (EditText) findViewById(R.id.datepicker);
        timeText = (EditText) findViewById(R.id.timepicker);

        dateDialog = new DateDialog(CreateActivity.this, dateText);
        timeDialog = new TimeDialog(CreateActivity.this, timeText);

        dateText.setOnClickListener(dateDialog);
        timeText.setOnClickListener(timeDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        initDateTimeListeners();
        initLocationListener();

    }



}
