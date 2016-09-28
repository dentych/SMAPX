package group.smapx.assignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import group.smapx.assignment2.models.WeatherAdaptor;
import group.smapx.assignment2.models.WeatherModel;
import group.smapx.assignment2.service.ConnectionCallback;
import group.smapx.assignment2.service.WeatherService;
import group.smapx.assignment2.service.WeatherServiceConnection;

public class MainActivity extends AppCompatActivity {
    WeatherServiceConnection connection;

    private WeatherAdaptor weatherAdaptor;
    private ListView weatherListView;
    private WeatherModel wm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView currentDesc = (TextView)findViewById(R.id.current_description_text);
        final TextView currentTemp = (TextView)findViewById(R.id.current_temperature_text);
        final ImageView currentWeatherPic = (ImageView)findViewById(R.id.current_weather_pic);

        Intent intent = new Intent(getBaseContext(), WeatherService.class);
        startService(intent);

        connection = new WeatherServiceConnection(new ConnectionCallback() {
            @Override
            public void connected() {
                setupAdaptor();

                wm = connection.getBoundService().getCurrentWeather();

                Log.d("Main", "" + wm.getTemperature() );
                Log.d("Main", "Wow, dennis er en lille luder");
            }
        });
        bindService(intent, connection, BIND_AUTO_CREATE);

        //if data was received, insert data.
        if(wm != null) {
            currentDesc.setText(wm.getClouds());
            currentTemp.setText(String.valueOf(wm.getTemperature()));
            currentWeatherPic.setImageResource(weatherAdaptor.setupPicture(wm.getClouds()));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(connection != null) {
//                    wm = connection.getBoundService().getCurrentWeather();
//                }

                if(wm != null) {
                    currentDesc.setText(wm.getClouds());
                    currentTemp.setText(String.valueOf(wm.getTemperature()));
                    currentWeatherPic.setImageResource(weatherAdaptor.setupPicture(wm.getClouds()));
                }

                Log.d("Main", "" + wm);
            }
        });

        // Example on how to register the broadcast from WeatherService.
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(WeatherService.BROADCAST_ACTION));
    }

    private void setupAdaptor() {
        weatherAdaptor = new WeatherAdaptor(this, android.R.layout.simple_list_item_1);

        weatherListView = (ListView)findViewById(R.id.listView);

        weatherListView.setAdapter(weatherAdaptor);

        List<WeatherModel> weatherModelList;
        weatherModelList = connection.getBoundService().getPastWeather();
        if(weatherModelList != null) {
            weatherAdaptor.addAll(weatherModelList);
        }
    }

    // This is an example of how to use the broadcasting from WeatherService.
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int message = intent.getIntExtra("message", 0);

            if (message == 1 && connection.isBound()) {
                WeatherModel weatherModel = connection.getBoundService().getCurrentWeather();
                // TODO: Do stuff in UI with weatherModel
            }
        }
    };
}
