package group.smapx.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import group.smapx.assignment2.models.WeatherAdaptor;
import group.smapx.assignment2.models.WeatherModel;
import group.smapx.assignment2.service.WeatherServiceConnection;
import group.smapx.assignment2.service.WeatherService;

public class MainActivity extends AppCompatActivity {
    WeatherServiceConnection connection;

    private WeatherAdaptor weatherAdaptor;
    private ListView weatherListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView currentDesc = (TextView)findViewById(R.id.current_description_text);
        final TextView currentTemp = (TextView)findViewById(R.id.current_temperature_text);

        Intent intent = new Intent(getBaseContext(), WeatherService.class);
        startService(intent);

        connection = new WeatherServiceConnection();
        bindService(intent, connection, BIND_AUTO_CREATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WeatherModel wm = connection.getBoundService().getCurrentWeather();
//                currentDesc.setText(wm.getClouds());
//                currentTemp.setText(String.valueOf(wm.getTemperature()));
                Log.d("Main", "" + wm);
            }
        });

        setupAdaptor();
    }

    private void setupAdaptor() {
        weatherAdaptor = new WeatherAdaptor(this, android.R.layout.simple_list_item_1);

        weatherListView = (ListView)findViewById(R.id.listView);

        weatherListView.setAdapter(weatherAdaptor);

//        List<WeatherModel> weatherModelList;
//        weatherModelList = connection.getBoundService().getPastWeather();
//        if(weatherModelList != null) {
//            weatherAdaptor.addAll(weatherModelList);
//        }
    }
}
