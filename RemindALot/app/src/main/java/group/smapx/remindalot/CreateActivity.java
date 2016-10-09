package group.smapx.remindalot;

<<<<<<< HEAD
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
=======
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
>>>>>>> d0c31c5431f598130f1c1957942d9b24ae30d073

    Calendar remindCalender = Calendar.getInstance();
    EditText dateText;
    EditText timeText;
    DateDialog dateDialog;
    TimeDialog timeDialog;
<<<<<<< HEAD
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
=======
    LocationData data;
    EditText locationText;

  //  DatePickerDialog dateDialog;
>>>>>>> d0c31c5431f598130f1c1957942d9b24ae30d073
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

<<<<<<< HEAD
    private void initLocationListener() {
=======
    private void initLocationListener(){
>>>>>>> d0c31c5431f598130f1c1957942d9b24ae30d073
        locationText = (EditText) findViewById(R.id.placement);

        locationText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
<<<<<<< HEAD
                Log.d("Debug", "OnFuckChange called");
                if (!hasFocus && !locationText.getText().toString().matches("")) {
                    ValidatorThread thread = new ValidatorThread(locationText, CreateActivity.this, data);
=======
                Log.d("Debug","OnFuckChange called");
                if(!hasFocus && !locationText.getText().toString().matches("")){
                    ValidatorThread thread = new ValidatorThread(locationText, CreateActivity.this,data);
>>>>>>> d0c31c5431f598130f1c1957942d9b24ae30d073
                    thread.execute(locationText.getText().toString());
                }

            }
        });
    }


<<<<<<< HEAD
    private void initDateTimeListeners() {
=======

    private void initDateTimeListeners(){
>>>>>>> d0c31c5431f598130f1c1957942d9b24ae30d073
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
<<<<<<< HEAD
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
=======

        initDateTimeListeners();
        initLocationListener();

    }



>>>>>>> d0c31c5431f598130f1c1957942d9b24ae30d073
}
