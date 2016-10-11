package group.smapx.remindalot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import group.smapx.remindalot.adapter.ContactsAdapter;
import group.smapx.remindalot.model.Contact;
import group.smapx.remindalot.model.LocationData;
import group.smapx.remindalot.model.Reminder;

public class ShowActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final int RESULT_DELETE = 101;
    private LinearLayout listContacts;
    private MapFragment fragment;
    private ScrollView scrollView;
    private View mapScrollDisableView;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabDelete;
    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtDate;
    private TextView txtLocation;
    private ContactsAdapter adapter;
    private LocationData locationData;
    private Reminder originalReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        findViews();

        fabEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
        fabDelete.setImageDrawable(getResources().getDrawable(R.drawable.ic_trash));

        if (savedInstanceState != null) {
            originalReminder = (Reminder) savedInstanceState.getSerializable("reminder");
        } else if (getIntent() != null) {
            Intent intent = getIntent();
            originalReminder = (Reminder) intent.getSerializableExtra("reminder");
        }
        populateFields(originalReminder);

        fragment.getMapAsync(this);

        // Disable scrollView scrolling when using map.
        mapScrollDisableView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        fillContactList();

        clearContactList();

        setupFabClickListeners();
    }

    private void clearContactList() {
        adapter.clear();
        listContacts.removeAllViews();
    }

    private void findViews() {
        listContacts = (LinearLayout) findViewById(R.id.listContacts);
        fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        scrollView = (ScrollView) findViewById(R.id.activity_show);
        mapScrollDisableView = findViewById(R.id.imageView);
        fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
        fabDelete = (FloatingActionButton) findViewById(R.id.fabDelete);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        adapter = new ContactsAdapter(this);
    }

    private void setupFabClickListeners() {
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowActivity.this, CreateActivity.class);
                intent.putExtra("reminder", originalReminder);
                startActivityForResult(intent, CreateActivity.RESULT_CREATE);
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("reminder", originalReminder);
                setResult(RESULT_DELETE, intent);
                finish();
            }
        });
    }

    private void fillContactList() {
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View item = adapter.getView(i, null, listContacts);
            listContacts.addView(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("reminder", originalReminder);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("ShowActivity", "MAP IS GOD DAMN READY! :D");
        double lat = Double.parseDouble(locationData.getLat());
        double lon = Double.parseDouble(locationData.getLon());
        LatLng position = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions().position(position));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }

    public ArrayList<Contact> getContactsFromAdapter() {
        ArrayList<Contact> contacts = new ArrayList<>();
        if (adapter != null && adapter.getCount() > 0) {
            int count = adapter.getCount();
            for (int i = 0; i < count; i++) {
                contacts.add(adapter.getItem(i));
            }
        }
        return contacts;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == CreateActivity.RESULT_CREATE) {
            originalReminder = (Reminder) data.getSerializableExtra("reminder");

            populateFields(originalReminder);

            Intent intent = new Intent(MainActivity.ACTION_REMINDER_EDITED);
            intent.putExtra("reminder", originalReminder);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            fragment.getMapAsync(this);
        }
    }

    private void populateFields(Reminder reminder) {
        if (reminder == null) {
            return;
        }
        txtTitle.setText(reminder.getTitle());
        txtDescription.setText(reminder.getDescription());

        //format timestamp
        SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        String time = format.format(new Date(reminder.getDate()));
        txtDate.setText(time);

        txtLocation.setText(reminder.getLocationData().getFormattedAddress());
        locationData = reminder.getLocationData();
        clearContactList();
        adapter.addAll(reminder.getContacts());
        fillContactList();
    }
}