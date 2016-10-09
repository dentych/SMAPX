package group.smapx.remindalot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;
import java.util.Locale;

import group.smapx.remindalot.Create.ContactReceiver;
import group.smapx.remindalot.Create.DateDialog;
import group.smapx.remindalot.Create.DescriptionBtnListner;
import group.smapx.remindalot.Create.DescriptionReceiver;
import group.smapx.remindalot.Create.DateTimeReceiver;
import group.smapx.remindalot.Create.PeopleButtonListener;
import group.smapx.remindalot.Create.TimeDialog;
import group.smapx.remindalot.Create.ValidatorThread;
import group.smapx.remindalot.model.LocationData;
import group.smapx.remindalot.Permitter.PermissionCallback;
import group.smapx.remindalot.Permitter.PermissionManager;
import group.smapx.remindalot.model.Reminder;

public class CreateActivity extends AppCompatActivity implements PermissionCallback, ContactReceiver, DescriptionReceiver, DateTimeReceiver{

    Calendar remindCalender = Calendar.getInstance();
    EditText dateText;
    EditText titleText;
    EditText timeText;
    DateDialog dateDialog;
    TimeDialog timeDialog;
    ContactsAdapter adapter;
    LocationData data;
    EditText locationText;
    Button contactButton;
    long TSE;
    int hour, minute, day, month, year = 0;
    Button okBtn;
    Button cancelBtn;
    Button descrButton;
    Reminder reminder;
    String description;

    boolean isPermissionReadContacts = false;
    PermissionManager permissionManager = new PermissionManager();
    private PeopleButtonListener peoplebuttonListener;
    ListView contactList;


    private void initLocationListener() {
        locationText = (EditText) findViewById(R.id.placement);

        locationText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !locationText.getText().toString().matches("")) {
                    ValidatorThread thread = new ValidatorThread(locationText, CreateActivity.this, data);
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
        this.titleText = (EditText)findViewById(R.id.titleText);
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
                Toast.makeText(CreateActivity.this,"Contact deleted", Toast.LENGTH_LONG);
                adapter.remove(adapter.getItem(position));
                return true;
            }
        });
    }

    private void initDescriptionDialog(){
        this.descrButton = (Button) findViewById(R.id.descButton);
        this.descrButton.setOnClickListener(new DescriptionBtnListner(this,this));
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

    private void initButtons(){
        this.okBtn = (Button)findViewById(R.id.OKBtn);
        this.cancelBtn = (Button) findViewById(R.id.cancelBtn);

        this.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()){
                    ArrayList<Contact> contacts = new ArrayList<Contact>();

                    for(int i = 0; i < adapter.getCount(); i++){
                        contacts.add(adapter.getItem(i));
                    }
                    reminder = new Reminder(
                           titleText.getText().toString(),
                            description,
                            getTSE(),
                            contacts);

                    reminder.setLocationData(data);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("reminder",reminder);
                    setResult(RESULT_OK,returnIntent);
                    finish();

                }
            }
        });

        this.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED,returnIntent);
                finish();
            }
        });
    }

    private boolean validateInput(){
        return  true;
    }

    private long getTSE(){

        Calendar calendar = Calendar.getInstance();
        calendar.set(this.year,this.month,this.day,this.hour,this.minute);

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
        this.description = descr;
    }

    @Override
    public void onTimeChosen(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        timeText.setText((hour > 9 ? hour : "0" + hour) + ":" + (minute > 9 ? minute : "0" + minute));

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
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        dateText.setText(format.format(remindCalender.getTime()));
    }
}
