package group.smapx.remindalot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import group.smapx.remindalot.Create.ReciverInterfaces.ContactReceiver;
import group.smapx.remindalot.Create.ClickListeners.DateDialog;
import group.smapx.remindalot.Create.ReciverInterfaces.DateTimeReceiver;
import group.smapx.remindalot.Create.ClickListeners.DescriptionBtnListner;
import group.smapx.remindalot.Create.ReciverInterfaces.DescriptionReceiver;
import group.smapx.remindalot.Create.ReciverInterfaces.LocationDataReceiver;
import group.smapx.remindalot.Create.ClickListeners.PeopleButtonListener;
import group.smapx.remindalot.Create.ClickListeners.TimeDialog;
import group.smapx.remindalot.Create.ValidatorThread;
import group.smapx.remindalot.Permitter.PermissionCallback;
import group.smapx.remindalot.Permitter.PermissionManager;
import group.smapx.remindalot.model.LocationData;
import group.smapx.remindalot.model.Reminder;

public class CreateActivity extends AppCompatActivity implements PermissionCallback, ContactReceiver, DescriptionReceiver, DateTimeReceiver, LocationDataReceiver {

    EditText dateText, titleText, timeText, locationText;
    DateDialog dateDialog;
    TimeDialog timeDialog;
    ContactsAdapter adapter;
    LocationData locationData;
    long TSE;
    Button okBtn, cancelBtn, descrButton, contactButton;
    Reminder reminder;
    boolean isPermissionReadContacts = false;
    PermissionManager permissionManager = new PermissionManager();
    ListView contactList;
    PeopleButtonListener peoplebuttonListener;
    private int hour, minute, day, month, year = 0;

    public CreateActivity(Reminder reminder) {
        this.reminder = reminder;
        if (reminder != null) {
            titleText.setText(reminder.getTitle());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(reminder.getDate());
            setDateText(calendar);
            setTimeText(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            for (Contact contact : reminder.getContacts()) {
                adapter.add(contact);
            }
            locationText.setText(reminder.getLocationData().getFormattedAddress());

        } else this.reminder = new Reminder();
    }

    public CreateActivity() {
        reminder = new Reminder();

    }

    private void initLocationListener() {
        locationText = (EditText) findViewById(R.id.placement);

        locationText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !locationText.getText().toString().matches("")) {
                    ValidatorThread thread = new ValidatorThread(CreateActivity.this, CreateActivity.this);
                    thread.execute(locationText.getText().toString());
                }
            }
        });
    }


    private void initDateTimeListeners() {
        dateText = (EditText) findViewById(R.id.datepicker);
        timeText = (EditText) findViewById(R.id.timepicker);

        dateDialog = new DateDialog(CreateActivity.this, this);
        timeDialog = new TimeDialog(CreateActivity.this, this);

        dateText.setOnClickListener(dateDialog);
        timeText.setOnClickListener(timeDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        permissionManager.getPermission(CreateActivity.this, android.Manifest.permission.READ_CONTACTS, this);

        this.peoplebuttonListener = new PeopleButtonListener(this, this);
        this.titleText = (EditText) findViewById(R.id.titleText);
        initDateTimeListeners();
        initLocationListener();
        initContactList();
        initPeopleBtn();
        initDescriptionDialog();

        adapter = new ContactsAdapter(this);
        contactList.setAdapter(adapter);

        contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CreateActivity.this, "Contact deleted", Toast.LENGTH_LONG);
                adapter.remove(adapter.getItem(position));
                return true;
            }
        });
    }

    private void initDescriptionDialog() {
        this.descrButton = (Button) findViewById(R.id.descButton);
        this.descrButton.setOnClickListener(new DescriptionBtnListner(this, this));
    }

    private void initContactList() {
        this.contactList = (ListView) findViewById(R.id.contactList);

    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        peoplebuttonListener.onActivityResult(reqCode, resultCode, data);
    }

    private void initPeopleBtn() {
        this.contactButton = (Button) findViewById(R.id.peopleBtn);

        if (permissionManager.checkPermission(this, android.Manifest.permission.READ_CONTACTS))
            this.contactButton.setOnClickListener(peoplebuttonListener);
    }

    private void initButtons() {
        this.okBtn = (Button) findViewById(R.id.OKBtn);
        this.cancelBtn = (Button) findViewById(R.id.cancelBtn);

        this.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    ArrayList<Contact> contacts = new ArrayList<Contact>();

                    for (int i = 0; i < adapter.getCount(); i++) {
                        contacts.add(adapter.getItem(i));
                    }
                    reminder.setDate(getTSE());

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("reminder", reminder);
                    setResult(RESULT_OK, returnIntent);
                    finish();

                }
            }
        });

        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    private boolean validateInput() {
        return true;
    }

    private long getTSE() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, this.month, this.day, this.hour, this.minute);

        return calendar.getTimeInMillis();
    }

    @Override
    public void onPermissionGranted() {
        isPermissionReadContacts = true;
    }

    @Override
    public void onPermissionDenied() {
        isPermissionReadContacts = false;
    }

    @Override
    public void onContactChosen(String name, String phonenumbr) {
        this.adapter.add(new Contact(name, phonenumbr));
    }

    @Override
    public void onDescriptionEntered(String descr) {
        reminder.setDescription(descr);
        Log.d("Debug", "Setting initial desc in receiver: " + descr);
    }

    @Override
    public String getInitialDescription() {
        Log.d("Debug", "Setting inital to " + reminder.getDescription());
        return reminder.getDescription();
    }

    @Override
    public void onTimeChosen(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        setTimeText(hour, minute);

    }

    @Override
    public void onDateChosen(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
        Calendar remindCalender = Calendar.getInstance();
        remindCalender.set(Calendar.YEAR, year);
        remindCalender.set(Calendar.MONTH, month);
        remindCalender.set(Calendar.DAY_OF_MONTH, day);
        setDateText(remindCalender);

    }

    private void setDateText(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        dateText.setText(format.format(calendar.getTime()));
    }

    private void setTimeText(int hour, int minute) {
        timeText.setText((hour > 9 ? hour : "0" + hour) + ":" + (minute > 9 ? minute : "0" + minute));
    }

    @Override
    public void onLocationDataReady(LocationData locationData) {
        reminder.setLocationData(locationData);
        locationText.setText(locationData.getFormattedAddress());
    }
}