package group.smapx.remindalot;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_create = (FloatingActionButton) findViewById(R.id.fab);

        fab_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open creation activity here!
                Intent createReminder = new Intent();
//                startActivityForResult(createReminder,1);
            }
        });


    }
}
