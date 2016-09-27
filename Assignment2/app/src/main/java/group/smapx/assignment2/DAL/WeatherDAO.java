package group.smapx.assignment2.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;



import group.smapx.assignment2.models.WeatherModel;

/**
 * Created by DSLTEST-UDVIKLER on 27-09-2016.
 */

public class WeatherDAO {

    private WeatherDBHelper connection;

    public WeatherDAO(Context context){
        connection = WeatherDBHelper.getInstance(context);
    }


    public void Save(WeatherModel model){
        SQLiteDatabase db = connection.getWritableDatabase();

        ContentValues vals = new ContentValues();
        vals.put(WeatherContract.WeatherEntry.TIMESTAMP, model.getTimestamp());
        vals.put(WeatherContract.WeatherEntry.TEMPERATURE, model.getTemperature());
        vals.put(WeatherContract.WeatherEntry.HUMIDITY, model.getHumidity());
        vals.put(WeatherContract.WeatherEntry.PRESSURE, model.getPressure());
        vals.put(WeatherContract.WeatherEntry.TEMP_MIN, model.getTemp_min());
        vals.put(WeatherContract.WeatherEntry.TEMP_MAX, model.getTemp_max());
        vals.put(WeatherContract.WeatherEntry.WIDNSPEED, model.getWindspeed());
        vals.put(WeatherContract.WeatherEntry.WINDDIRECTION, model.getWinddirection());
        vals.put(WeatherContract.WeatherEntry.CLOUDS, model.getClouds());

        long id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, vals);
        Log.d("DAL", "Saved model with ID: " + id);
        db.close();
    }


    public List<WeatherModel> getWeatherForLastDays(int days){


        SQLiteDatabase db = connection.getReadableDatabase();
        Cursor c = db.query(
                WeatherContract.WeatherEntry.TABLE_NAME,
                getCollumns(),
                WeatherContract.WeatherEntry.TIMESTAMP + " > ?",
                getCondition(days),
                null,
                null,
                null
        );

        List<WeatherModel> models = new ArrayList<>();
        if(c.moveToFirst()){
            while(c.moveToNext()){
                WeatherModel model = new WeatherModel(
                        Long.parseLong(c.getString(1)),
                        Double.parseDouble(c.getString(2)),
                        Double.parseDouble(c.getString(3)),
                        Double.parseDouble(c.getString(4)),
                        Double.parseDouble(c.getString(5)),
                        Double.parseDouble(c.getString(6)),
                        Double.parseDouble(c.getString(7)),
                        Double.parseDouble(c.getString(8)),
                        c.getString(9));
                model.setId(Integer.parseInt(c.getString(0)));

                models.add(model);
            }
        }
        db.close();
        Log.d("DAL", "Get on condition returning " + models.size() + " models.");

        return models;
    }

    public void Delete(WeatherModel model) {
        if(model.getId() == 0)
            return;

        SQLiteDatabase db = connection.getWritableDatabase();

        db.delete(WeatherContract.WeatherEntry.TABLE_NAME,
                WeatherContract.WeatherEntry._ID+" = ?",
                new String[] { String.valueOf(model.getId()) });

        db.close();
        Log.d("DAL", "Model with ID " + model.getId()  + " Deleted.");
    }


    private String[] getCondition(int days){
        long now = new Date().getTime();
        String[] args = { Long.toString(now - (86400000*days)) };
        return  args;
    }


    private String[] getCollumns(){
        String[] projection ={
                WeatherContract.WeatherEntry._ID,
                WeatherContract.WeatherEntry.TIMESTAMP,
                WeatherContract.WeatherEntry.TEMPERATURE,
                WeatherContract.WeatherEntry.CLOUDS,
                WeatherContract.WeatherEntry.HUMIDITY,
                WeatherContract.WeatherEntry.PRESSURE,
                WeatherContract.WeatherEntry.TEMP_MAX,
                WeatherContract.WeatherEntry.TEMP_MIN,
                WeatherContract.WeatherEntry.WIDNSPEED,
                WeatherContract.WeatherEntry.WINDDIRECTION };
        return projection;


    }
}
