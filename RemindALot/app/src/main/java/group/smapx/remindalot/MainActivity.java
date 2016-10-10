package group.smapx.remindalot;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import group.smapx.remindalot.Permitter.PermissionCallback;
import group.smapx.remindalot.Permitter.PermissionManager;
import group.smapx.remindalot.model.LocationData;
import group.smapx.remindalot.model.Reminder;

public class MainActivity extends AppCompatActivity implements PermissionCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ListView listReminder = (ListView) findViewById(R.id.listReminders);
        setSupportActionBar(toolbar);

        final BasicReminder b = new BasicReminder(this);

        FloatingActionButton fab_create = (FloatingActionButton) findViewById(R.id.fab);

        PermissionManager permissionManager = new PermissionManager();

        permissionManager.getPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW, this);


        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open creation activity here!
                Intent createReminder = new Intent();
//              startActivityForResult(createReminder,1);

            }
        });

        ArrayAdapter<Reminder> adapter = new ArrayAdapter<Reminder>(this, android.R.layout.simple_list_item_1);
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Annedreas", "25010203"));
        contacts.add(new Contact("Abekat", "12312302"));

        Reminder reminder = new Reminder("Titel", "Forklaring", new Date().getTime(), contacts);
        adapter.add(reminder);
        reminder.setLocationData(new LocationData("50", "10", "Some address in tyskland"));
        listReminder.setAdapter(adapter);

        listReminder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reminder item = (Reminder) listReminder.getItemAtPosition(position);

                Intent intent = new Intent(view.getContext(), ShowActivity.class);
                intent.putExtra("reminder", item);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPermissionGranted() {

    }

    @Override
    public void onPermissionDenied() {

    }
}