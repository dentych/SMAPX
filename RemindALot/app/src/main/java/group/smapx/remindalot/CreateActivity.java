package group.smapx.remindalot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import java.util.Date;
import java.util.Locale;

import group.smapx.remindalot.Create.ClickListeners.DateDialog;
import group.smapx.remindalot.Create.ClickListeners.DescriptionBtnListner;
import group.smapx.remindalot.Create.ClickListeners.PeopleButtonListener;
import group.smapx.remindalot.Create.ClickListeners.TimeDialog;
import group.smapx.remindalot.Create.ReciverInterfaces.ContactReceiver;
import group.smapx.remindalot.Create.ReciverInterfaces.DateTimeReceiver;
import group.smapx.remindalot.Create.ReciverInterfaces.DescriptionReceiver;
import group.smapx.remindalot.Create.ReciverInterfaces.LocationDataReceiver;
import group.smapx.remindalot.Create.ValidatorThread;
import group.smapx.remindalot.Permitter.PermissionCallback;
import group.smapx.remindalot.Permitter.PermissionManager;
import group.smapx.remindalot.adapter.ContactsAdapter;
import group.smapx.remindalot.model.Contact;
import group.smapx.remindalot.model.LocationData;
import group.smapx.remindalot.model.Reminder;

public class CreateActivity extends AppCompatActivity implements PermissionCallback, ContactReceiver, DescriptionReceiver, DateTimeReceiver, LocationDataReceiver {
    public static final int RESULT_CREATE = 100;
    EditText dateText, titleText, timeText, locationText;
    DateDialog dateDialog;
    TimeDialog timeDialog;
    ContactsAdapter adapter;
    boolean addressValidated = false;
    LocationData locationData;
    Button okBtn, cancelBtn, descrButton, contactButton;
    Reminder reminder;
    boolean isPermissionReadContacts = false;
    PermissionManager permissionManager = new PermissionManager();
    ListView contactList;
    PeopleButtonListener peoplebuttonListener;
    private int hour, minute, day, month, year = 0;

    private void initLocationListener() {
        locationText = (EditText) findViewById(R.id.placement);

        locationText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !locationText.getText().toString().matches("")) {
                    validateAddress();
                }
            }
        });
    }

    private void validateAddress() {
        ValidatorThread thread = new ValidatorThread(CreateActivity.this, CreateActivity.this);
        thread.execute(locationText.getText().toString());
    }

    private void initDateTimeListeners() {
        dateText = (EditText) findViewById(R.id.datepicker);
        timeText = (EditText) findViewById(R.id.timepicker);

        dateDialog = new DateDialog(CreateActivity.this, this);
        timeDialog = new TimeDialog(CreateActivity.this, this);

        dateText.setOnClickListener(dateDialog);
        timeText.setOnClickListener(timeDialog);
    }

    private void populateFields() {
        if (reminder == null)
            return;

        if (reminder.getTitle() != null) {
            titleText.setText(reminder.getTitle());
        }
        if (reminder.getDate() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(reminder.getDate());
            setDateText(calendar);
            setTimeText(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        }
        if (reminder.getContacts() != null) {
            for (Contact contact : reminder.getContacts()) {
                adapter.add(contact);
            }
        }
        if (reminder.getLocationData() != null) {
            locationText.setText(reminder.getLocationData().getFormattedAddress());
        }
    }

    private void initSearchbtn() {
        Button searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAddress();
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

    private void getPermissions() {
        permissionManager.getPermission(CreateActivity.this, android.Manifest.permission.READ_CONTACTS, this);
    }

    private void initPeopleBtn() {
        this.contactButton = (Button) findViewById(R.id.peopleBtn);
        this.contactButton.setOnClickListener(peoplebuttonListener);
    }

    private void initButtons() {
        this.okBtn = (Button) findViewById(R.id.OKBtn);
        this.cancelBtn = (Button) findViewById(R.id.cancelBtn);

        this.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addressValidated)
                    validateAddress();

                if (validateInput()) {
                    ArrayList<Contact> contacts = new ArrayList<Contact>();

                    for (int i = 0; i < adapter.getCount(); i++) {
                        contacts.add(adapter.getItem(i));
                    }
                    reminder.setTitle(titleText.getText().toString());
                    reminder.setDate(getTSE());
                    reminder.setContacts(contacts);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("reminder", reminder);
                    setResult(CreateActivity.RESULT_CREATE, returnIntent);
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

    private boolean isNullOrWhiteSpace(String s) {
        if (s == null || s.equals("") || s.isEmpty() || s.trim().isEmpty())
            return true;
        return false;
    }

    private boolean validateInput() {
        Log.d("HEJ!", "GetTSE: " + getTSE() + ". New date: " + new Date().getTime());
        if (isNullOrWhiteSpace(titleText.getText().toString())) {
            Toast.makeText(this, "Title can not be empty", Toast.LENGTH_LONG).show();
            Log.d("Debug", "Title");
            return false;
        } else if (isNullOrWhiteSpace(locationText.getText().toString())) {
            Toast.makeText(this, "Location can not be empty", Toast.LENGTH_LONG).show();
            Log.d("Debug", "Location");
            return false;
        } else if (reminder.getLocationData() == null) {
            Toast.makeText(this, "There is a problem with the address.", Toast.LENGTH_LONG).show();
            return false;
        } else if (getTSE() < new Date().getTime()) {
            Toast.makeText(this, "Can not schedule a reminder in to the past. We're not that good.", Toast.LENGTH_LONG).show();
            Log.d("Debug", "Timeinmill");
            return false;
        } else if (getTSE() == 0) {
            Toast.makeText(this, "Please select time and date", Toast.LENGTH_LONG).show();
            Log.d("Debug", "Datetime");
            return false;
        }

        return true;

    }

    private long getTSE() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year, this.month, this.day, this.hour, this.minute);
        if (this.year == 0 || this.month == 0 || this.day == 0)
            return 0;
        return calendar.getTimeInMillis();
    }

    private void setDateText(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        dateText.setText(format.format(calendar.getTime()));
    }

    private void setTimeText(int hour, int minute) {
        timeText.setText((hour > 9 ? hour : "0" + hour) + ":" + (minute > 9 ? minute : "0" + minute));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        getPermissions();
        this.peoplebuttonListener = new PeopleButtonListener(this, this);
        this.titleText = (EditText) findViewById(R.id.titleText);
        initDateTimeListeners();
        initLocationListener();
        initContactList();
        initPeopleBtn();
        initDescriptionDialog();
        initSearchbtn();
        adapter = new ContactsAdapter(this);
        contactList.setAdapter(adapter);

        contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Toast.makeText(CreateActivity.this, "Contact deleted", Toast.LENGTH_LONG);

                new AlertDialog.Builder(CreateActivity.this)
                        .setTitle("Remove contact")
                        .setMessage("Are you sure you want to remove the contact?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.remove(adapter.getItem(position));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.mipmap.ic_launcher)
                        .show();

                return true;
            }
        });

        if (getIntent() != null && getIntent().hasExtra("reminder")) {
            reminder = (Reminder) getIntent().getSerializableExtra("reminder");
            populateFields();
        } else if (savedInstanceState != null) {
            reminder = (Reminder) savedInstanceState.getSerializable("reminder");
            populateFields();
        } else {
            reminder = new Reminder();
        }

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(reminder.getDate());
        setDateFields(c);

        initButtons();
    }

    private void setDateFields(Calendar c) {
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<Contact> contacts = new ArrayList<>();
        if (adapter.getCount() > 0) {
            for (int i = 0; i < adapter.getCount(); i++) {
                contacts.add(adapter.getItem(i));
            }
        }
        reminder.setContacts(contacts);
        outState.putSerializable("reminder", reminder);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        peoplebuttonListener.onActivityResult(reqCode, resultCode, data);
    }

    @Override
    public void onPermissionGranted() {
        Log.d("Debug", "Permission granted");
        isPermissionReadContacts = true;
        initPeopleBtn();
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

    @Override
    public void onLocationDataReady(LocationData locationData) {
        reminder.setLocationData(locationData);
        locationText.setText(locationData.getFormattedAddress());
    }
}
