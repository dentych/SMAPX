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
    }
}
