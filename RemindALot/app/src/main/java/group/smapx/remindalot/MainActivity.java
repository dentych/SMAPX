package group.smapx.remindalot;

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

import group.smapx.remindalot.model.Contact;
import group.smapx.remindalot.model.LocationData;
import group.smapx.remindalot.model.Reminder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ListView listReminder = (ListView) findViewById(R.id.listReminders);
        setSupportActionBar(toolbar);

        final BasicReminder b = new BasicReminder(this);

        FloatingActionButton fab_create = (FloatingActionButton) findViewById(R.id.fab);

        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open creation activity here!
                Intent createReminder = new Intent();
//                startActivityForResult(createReminder,1);

//                //Broadcasting test! Left for clarification until monday
//                String title1 = "GruppeMøde";
//                String desc1 = "med drengene på mandag";
//
//                String title2 = "aksdmakmsd";
//                String desc2 = "aksdoasdpi";
//
//                Calendar c = Calendar.getInstance();
//                c.add(Calendar.SECOND, 5);
//
//                b.setAlarm(title1, desc1, c.getTimeInMillis(), 0);
//                Log.d("Test", String.valueOf(c.getTimeInMillis() + 5000));
//
//                c.add(Calendar.SECOND, 10);
//
//                b.setAlarm(title2, desc2, c.getTimeInMillis(), 1);
//                Log.d("Test", String.valueOf(c.getTimeInMillis() + 10000));
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
}