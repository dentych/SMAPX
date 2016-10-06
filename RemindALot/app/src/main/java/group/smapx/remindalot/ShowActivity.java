package group.smapx.remindalot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;

public class ShowActivity extends AppCompatActivity implements OnMapReadyCallback {
    private LinearLayout listContacts;
    private MapFragment fragment;
    private ScrollView scrollView;
    private View mapScrollDisableView;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabDelete;
    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtDate;
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        listContacts = (LinearLayout) findViewById(R.id.listContacts);
        fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        scrollView = (ScrollView) findViewById(R.id.activity_show);
        mapScrollDisableView = findViewById(R.id.imageView);
        fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
        fabDelete = (FloatingActionButton) findViewById(R.id.fabDelete);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtDate = (TextView) findViewById(R.id.txtDate);
        adapter = new ContactsAdapter(this);

        fabEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
        fabDelete.setImageDrawable(getResources().getDrawable(R.drawable.ic_trash));

        if (savedInstanceState != null) {
            txtTitle.setText(savedInstanceState.getString("title"));
            txtDescription.setText(savedInstanceState.getString("description"));
            txtDate.setText(savedInstanceState.getString("date"));
            ArrayList<Contact> contacts = (ArrayList<Contact>) savedInstanceState.getSerializable("contacts");
            if (contacts != null)
                adapter.addAll(contacts);
        } else if (getIntent() != null) {
            Intent intent = getIntent();
            Reminder reminder = (Reminder) intent.getSerializableExtra("reminder");

            txtTitle.setText(reminder.getTitle());
            txtDescription.setText(reminder.getDescription());
            txtDate.setText(new Date(reminder.getDate()).toString());
            ArrayList<Contact> contacts = reminder.getContacts();
            if (contacts != null)
                adapter.addAll(contacts);
        }

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

        setupFabClickListeners();
    }

    private void setupFabClickListeners() {
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title", txtTitle.getText().toString());
                intent.putExtra("description", txtDescription.getText().toString());
                intent.putExtra("date", txtDate.getText().toString());

                Toast.makeText(ShowActivity.this, "You clicked edit! This should open edit activity when it's created.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement delete
                Toast.makeText(ShowActivity.this, "You clicked DELETE! This is not yet implemented",
                        Toast.LENGTH_SHORT).show();
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

        outState.putString("title", "Titel");
        outState.putString("description", "forklaring");
        outState.putString("date", "01-02-2003");
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Andreas", "12345678"));
        outState.putSerializable("contacts", contacts);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("ShowActivity", "MAP IS GOD DAMN READY! :D");
        LatLng position = new LatLng(56.184702, 10.116348);
        googleMap.addMarker(new MarkerOptions().position(position));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }
}