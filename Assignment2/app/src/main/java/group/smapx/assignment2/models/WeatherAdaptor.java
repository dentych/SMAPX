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
import java.util.TimeZone;

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
            weatherImage.setImageResource(setupPicture(wm.getDescription()));

            //Weather description
            TextView description = (TextView)convertView.findViewById(R.id.description_text);
            description.setText(wm.getDescription());

            //Temp text
            TextView temperature = (TextView)convertView.findViewById(R.id.temperature_text);
            String formattedTemp = String.valueOf(wm.getTemperature()) + " \u00b0C";
            temperature.setText(formattedTemp);

            //should be changed to form 15-08-2016
            TextView date = (TextView)convertView.findViewById(R.id.date_text);
            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
            cal.setTimeInMillis(wm.getTimestamp());

            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            //month +1 since the calendar api count the twelve months from 0-11 instead of 1-12
            String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
            String year = String.valueOf(cal.get(Calendar.YEAR));

            date.setText(day + "-" + month + "-" + year);

            //should be changed to form 18:00
            TextView time = (TextView)convertView.findViewById(R.id.time_text);
            String hour = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY));
            String minute = String.format("%02d", cal.get(Calendar.MINUTE));
            time.setText(hour + ":" + minute);
        }

        return convertView;
    }

    public int setupPicture(String description) {

        int weatherIcon = R.drawable.default_weather_pic;
        switch (description.toLowerCase()) {
            case "sun":
            case "clear":
            case "atmosphere":
                weatherIcon = R.drawable.ic_sun;
                break;
            case "clouds":
            case "extreme":
                weatherIcon = R.drawable.ic_cloud;
                break;
            case "rain":
            case "thunderstorm":
            case "drizzle":
                weatherIcon = R.drawable.ic_rain;
                break;
            case "snow":
                weatherIcon = R.drawable.ic_snow;
                break;
        }

        return weatherIcon;
    }
}
