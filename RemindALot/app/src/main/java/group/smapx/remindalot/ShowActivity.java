package group.smapx.remindalot;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

public class ShowActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        final LinearLayout listContacts = (LinearLayout) findViewById(R.id.listContacts);
        final MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.activity_show);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
        final FloatingActionButton fabDelete = (FloatingActionButton) findViewById(R.id.fabDelete);
        final TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        final TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        final TextView txtDate = (TextView) findViewById(R.id.txtDate);

        fabEdit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));
        fabDelete.setImageDrawable(getResources().getDrawable(R.drawable.ic_trash));

        fragment.getMapAsync(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ArrayList<String> list = new ArrayList<>();
        list.add("Test person 1");
        list.add("Test person 2");
        list.add("Test person 3");
        list.add("Test person 4");
        list.add("Test person 5");
        list.add("Test person 6");
        list.add("Test person 7");
        list.add("Test person 8");
        list.add("Test person 9");
        list.add("Test person 10");
        adapter.addAll(list);

        Log.d("ShowActivity", "Setting onTouchListener");
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View item = adapter.getView(i, null, listContacts);
            listContacts.addView(item);
        }

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title", txtTitle.getText().toString());
                intent.putExtra("description", txtDescription.getText().toString());
                intent.putExtra("date", txtDate.getText().toString());
                intent.putExtra("contacts", );

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("ShowActivity", "MAP IS GOD DAMN READY! :D");
        LatLng position = new LatLng(56.184702, 10.116348);
        googleMap.addMarker(new MarkerOptions().position(position));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }
}