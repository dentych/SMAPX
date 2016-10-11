package group.smapx.remindalot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import group.smapx.remindalot.BasicReminder.BasicReminder;
import group.smapx.remindalot.adapter.ReminderAdapter;
import group.smapx.remindalot.database.DatabaseDAO;
import group.smapx.remindalot.model.Reminder;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    public static final String ACTION_REMINDER_EDITED = "group.smapx.remindalot.REMINDER_EDITED";
    private ReminderAdapter adapter;
    private BasicReminder basicReminder;
    private TextView reminderCount;
    private DatabaseDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ListView listReminder = (ListView) findViewById(R.id.listReminders);
        setSupportActionBar(toolbar);

        basicReminder = new BasicReminder(this);
         reminderCount = (TextView)findViewById(R.id.reminder_count);


        FloatingActionButton fab_create = (FloatingActionButton) findViewById(R.id.fab);
        db = new DatabaseDAO(this);

        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open creation activity here!
                Intent createReminder = new Intent(MainActivity.this, CreateActivity.class);
                startActivityForResult(createReminder, CreateActivity.RESULT_CREATE);
            }
        });

        adapter = new ReminderAdapter(this);
        List<Reminder> reminders = db.getAllReminders();
        adapter.addAll(reminders);

        listReminder.setAdapter(adapter);

        reminderCount.setText(String.valueOf(adapter.getCount()));

        listReminder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reminder item = (Reminder) listReminder.getItemAtPosition(position);

                Intent intent = new Intent(view.getContext(), ShowActivity.class);
                intent.putExtra("reminder", item);
                startActivityForResult(intent, ShowActivity.RESULT_DELETE);
            }
        });

        listReminder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Reminder item = (Reminder) listReminder.getItemAtPosition(position);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Reminder")
                        .setMessage("Are you sure you want to delete the reminder?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteReminder(item);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.mipmap.ic_launcher)
                        .show();

                return true;
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(ACTION_REMINDER_EDITED));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == CreateActivity.RESULT_CREATE) {
            Log.d(LOG_TAG, "Creating reminder in list and database...");
            Reminder reminder = (Reminder) data.getSerializableExtra("reminder");
            adapter.add(reminder);

            adapter.notifyDataSetChanged();

            Log.d(LOG_TAG, "Adapter count: " + adapter.getCount());
            db.insertReminder(reminder);

            basicReminder.setAlarm(reminder);
            reminderCount.setText(String.valueOf(adapter.getCount()));

        } else if (resultCode == ShowActivity.RESULT_DELETE) {
            Reminder reminder = (Reminder) data.getSerializableExtra("reminder");
            Log.d(LOG_TAG, "Trying to delete reminder with ID " + reminder.getId());
            deleteReminder(reminder);

        }
    }

    private void deleteReminder(Reminder reminder) {
        long id = reminder.getId();

        for (int i = 0; i < adapter.getCount(); i++) {
            Reminder item = adapter.getItem(i);
            if (item != null && item.getId() == id) {
                adapter.remove(item);
                db.deleteReminder(id);
                basicReminder.deleteAlarm(reminder);
                reminderCount.setText(String.valueOf(adapter.getCount()));
                return;
            }
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "Received broadcast");
            if (intent != null && intent.hasExtra("reminder")) {
                Log.d(LOG_TAG, "Intent has reminder in it.");
                Reminder reminder = (Reminder) intent.getSerializableExtra("reminder");

                long id = reminder.getId();

                Log.d(LOG_TAG, "Trying to find the item to modify");
                for (int i = 0; i < adapter.getCount(); i++) {
                    Reminder item = adapter.getItem(i);
                    if (item != null && item.getId() == id) {
                        adapter.remove(item);
                        adapter.insert(reminder, i);

                        break;
                    }
                }

                db.updateReminder(reminder);
                basicReminder.setAlarm(reminder);
                Log.d(LOG_TAG, "Done editing.");
            }
        }
    };
}