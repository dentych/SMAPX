package group.smapx.remindalot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.jar.Manifest;

import group.smapx.remindalot.Create.ContactReceiver;
import group.smapx.remindalot.Create.DateDialog;
import group.smapx.remindalot.Create.PeopleButtonListener;
import group.smapx.remindalot.Create.TimeDialog;
import group.smapx.remindalot.Create.ValidatorThread;
import group.smapx.remindalot.Models.LocationData;
import group.smapx.remindalot.Permitter.PermissionCallback;
import group.smapx.remindalot.Permitter.PermissionManager;

public class CreateActivity extends AppCompatActivity implements PermissionCallback, ContactReceiver {

    Calendar remindCalender = Calendar.getInstance();
    EditText dateText;
    EditText timeText;
    DateDialog dateDialog;
    TimeDialog timeDialog;
    ContactsAdapter adapter;
    LocationData data;
    EditText locationText;
    EditText contactButton;
    boolean isPermissionReadContacts = false;
    PermissionManager permissionManager = new PermissionManager();
    private PeopleButtonListener peoplebuttonListener;
    ListView contactList;
    List<Contact> chosenContacts = new ArrayList<>();
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

    private void initLocationListener() {
        locationText = (EditText) findViewById(R.id.placement);

        locationText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("Debug", "OnFuckChange called");
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

        dateDialog = new DateDialog(CreateActivity.this, dateText);
        timeDialog = new TimeDialog(CreateActivity.this, timeText);

        dateText.setOnClickListener(dateDialog);
        timeText.setOnClickListener(timeDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        boolean isDone = false;
        permissionManager.getPermission(CreateActivity.this, android.Manifest.permission.READ_CONTACTS, this);

        this.peoplebuttonListener =new PeopleButtonListener(this,this);

        initDateTimeListeners();
        initLocationListener();
        initContactList();
        initPeopleBtn();

        adapter = new ContactsAdapter(this);
        contactList.setAdapter(adapter);

        contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.remove(adapter.getItem(position));
                return true;
            }
        });
    }

    private void initContactList(){
        this.contactList = (ListView)findViewById(R.id.contactList);

    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode,resultCode,data);
        peoplebuttonListener.onActivityResult(reqCode,resultCode,data);
    }

        private void initPeopleBtn(){
        this.contactButton = (EditText) findViewById(R.id.peoplebtn);
            contactButton.setKeyListener(null);

        if(permissionManager.checkPermission(this, android.Manifest.permission.READ_CONTACTS))
            this.contactButton.setOnClickListener(peoplebuttonListener);
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

        this.adapter.add(new Contact(name,phonenumbr));
       // contactButton.setText(contactButton.getText().toString() + "\n" + name);
        // Set variable.
        Log.d("Debug", "Name: " + name);
        Log.d("Debug", "Number:" + phonenumbr);
    }
}
