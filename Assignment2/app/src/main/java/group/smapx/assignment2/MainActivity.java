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

import java.util.Calendar;
import java.util.List;

import group.smapx.assignment2.models.WeatherAdaptor;
import group.smapx.assignment2.models.WeatherModel;
import group.smapx.assignment2.service.ConnectionCallback;
import group.smapx.assignment2.service.WeatherService;
import group.smapx.assignment2.service.WeatherServiceConnection;

public class MainActivity extends AppCompatActivity {
    WeatherServiceConnection connection;

    private static WeatherAdaptor weatherAdaptor;
    private ListView weatherListView;
    private static WeatherModel wm = null;

    private TextView currentDesc;
    private TextView currentTemp;
    private ImageView currentWeatherPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentDesc = (TextView) findViewById(R.id.current_description_text);
        currentTemp = (TextView) findViewById(R.id.current_temperature_text);
        currentWeatherPic = (ImageView) findViewById(R.id.current_weather_pic);

        Intent intent = new Intent(getBaseContext(), WeatherService.class);
        startService(intent);

        connection = new WeatherServiceConnection(new ConnectionCallback() {
            @Override
            public void connected(WeatherService service) {
                setupAdaptor();
                wm = service.getCurrentWeather();
                updateCurrentWeatherBar();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCurrentWeatherBar();
            }
        });

        // Example on how to register the broadcast from WeatherService.
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(WeatherService.BROADCAST_ACTION));

        Log.d("Main", "OnCreate finished");
    }

    private void updateCurrentWeatherBar() {
        if (wm != null) {
            currentDesc.setText(wm.getDescription());
            String temperature = wm.getTemperature() + " \u00b0C";
            currentTemp.setText(String.valueOf(temperature));
            currentWeatherPic.setImageResource(weatherAdaptor.setupPicture(wm.getDescription()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "RESUMING!");
        if (!connection.isBound()) {
            Intent intent = new Intent(getBaseContext(), WeatherService.class);
            bindService(intent, connection, BIND_AUTO_CREATE);
            Log.d("MainActivity", "Weather service was bound!");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (connection.isBound()) {
            unbindService(connection);
            connection.setBound(false);
            Log.d("MainActivity", "Weather service was unbound!");
        }
    }

    private void setupAdaptor() {
        weatherAdaptor = new WeatherAdaptor(this, android.R.layout.simple_list_item_1);

        weatherListView = (ListView) findViewById(R.id.listView);

        weatherListView.setAdapter(weatherAdaptor);

        List<WeatherModel> weatherModelList;
        weatherModelList = connection.getBoundService().getPastWeather(1);
        if (weatherModelList != null) {
            weatherAdaptor.addAll(weatherModelList);
            weatherAdaptor.notifyDataSetChanged();
        }
    }

    // This is an example of how to use the broadcasting from WeatherService.
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int message = intent.getIntExtra("message", 0);

            if (message == 1 && connection.isBound()) {
                WeatherModel weatherModel = connection.getBoundService().getCurrentWeather();

                updateCurrentWeatherBar();

                weatherAdaptor.insert(connection.getBoundService().getCurrentWeather(), 0);

                long currentTime = Calendar.getInstance().getTimeInMillis();
                long dayInMillis = 24 * 60 * 60 * 1000;
                long timePassed;
                do {
                    int cursor = weatherAdaptor.getCount() - 1;
                    WeatherModel weather = weatherAdaptor.getItem(cursor);
                    if (weather != null) {
                        timePassed = currentTime - weather.getTimestamp();
                    } else {
                        timePassed = 0;
                    }
                    if (timePassed > dayInMillis) {
                        weatherAdaptor.remove(weather);
                    }
                } while (timePassed > dayInMillis);
                Log.d("Main", "Placed current info");
            }
        }
    };
}
