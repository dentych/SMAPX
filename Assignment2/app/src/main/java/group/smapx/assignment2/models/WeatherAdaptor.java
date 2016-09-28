package group.smapx.assignment2.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import group.smapx.assignment2.MainActivity;
import group.smapx.assignment2.R;

public class WeatherAdaptor extends ArrayAdapter<WeatherModel> {

    public WeatherAdaptor(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.weather_adapter_model,null);
        }

        WeatherModel wm = getItem(position);

        if(wm != null) {

            //Image
            ImageView weatherImage = (ImageView)convertView.findViewById(R.id.weather_pic);
            weatherImage.setImageResource(setupPicture(wm.getClouds()));

            //Weather description
            TextView description = (TextView)convertView.findViewById(R.id.description_text);
            description.setText(wm.getClouds());

            //Temp text
            TextView temperature = (TextView)convertView.findViewById(R.id.temperature_text);
            String formattedTemp = String.valueOf(wm.getTemperature()) + " \u00b0C";
            temperature.setText(formattedTemp);

            //should be changed to form 15-08-2016
            TextView date = (TextView)convertView.findViewById(R.id.date_text);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(wm.getTimestamp());

            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            String month = String.valueOf(cal.get(Calendar.MONTH));
            String year = String.valueOf(cal.get(Calendar.YEAR));

            date.setText(day + "-" + month + "-" + year);

            //should be changed to form 18:00
            TextView time = (TextView)convertView.findViewById(R.id.time_text);
            String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
            String minute = String.valueOf(cal.get(Calendar.MINUTE));

            time.setText(hour + ":" + minute);
        }

        return convertView;
    }

    public int setupPicture(String description) {

        int weatherIcon = R.drawable.default_weather_pic;
        switch (description) {
            case "Sun":
                weatherIcon = R.drawable.ic_sun;
                break;
            case "Cloud":
                weatherIcon = R.drawable.ic_cloud;
                break;
            case "Rain":
                weatherIcon = R.drawable.ic_rain;
                break;
            case "Snow":
                weatherIcon = R.drawable.ic_snow;
                break;
        }

        return weatherIcon;
    }
}
