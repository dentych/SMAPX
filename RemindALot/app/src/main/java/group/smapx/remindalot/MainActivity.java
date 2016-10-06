package group.smapx.remindalot;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final BasicReminder b = new BasicReminder(this);
        AlarmBroadcastReceiver br = new AlarmBroadcastReceiver();


        FloatingActionButton fab_create = (FloatingActionButton) findViewById(R.id.fab);

        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open creation activity here!
                Intent createReminder = new Intent();
//                startActivityForResult(createReminder,1);

                Calendar c = Calendar.getInstance();
                c.add(Calendar.SECOND, 5);

                b.setAlarm(c.getTimeInMillis());
                Log.d("Test", String.valueOf(c.getTimeInMillis() + 5000));

            }
        });
    }
}
