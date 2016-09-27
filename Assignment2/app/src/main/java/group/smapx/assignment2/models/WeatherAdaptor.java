package group.smapx.assignment2.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

            //cloud description?
            TextView description = (TextView)convertView.findViewById(R.id.description_text);
            description.setText(wm.getClouds());

            //probably in kelvin..
            TextView temperature = (TextView)convertView.findViewById(R.id.temperature_text);
            temperature.setText(String.valueOf(wm.getTemperature()));

            //should be changed to something like 15-08-2016
            TextView date = (TextView)convertView.findViewById(R.id.date_text);
            date.setText(String.valueOf(wm.getTimestamp()));

            //should be changed to something like 18:00
            TextView time = (TextView)convertView.findViewById(R.id.time_text);
            time.setText(String.valueOf(wm.getTimestamp()));
        }

        return convertView;
    }
}
