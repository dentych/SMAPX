package group.smapx.assignment2.DAL;

import android.provider.BaseColumns;

public class WeatherContract {
    protected WeatherContract() {}

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +
                    WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    WeatherEntry.TIMESTAMP + " INTEGER ," +
                    WeatherEntry.TEMPERATURE + " REAL," +
                    WeatherEntry.HUMIDITY + " REAL," +
                    WeatherEntry.PRESSURE + " REAL," +
                    WeatherEntry.TEMP_MIN + " REAL," +
                    WeatherEntry.TEMP_MAX + " REAL," +
                    WeatherEntry.WIDNSPEED + " REAL," +
                    WeatherEntry.WINDDIRECTION + " REAL," +
                    WeatherEntry.DESCRIPTION + " TEXT" + " )";

    public static class WeatherEntry implements BaseColumns {
        public static final String TABLE_NAME = "WEATHER_INFO";
        public static final String TIMESTAMP = "TIMESTAMP";
        public static final String TEMPERATURE = "TEMPERATURE";
        public static final String HUMIDITY = "HUMIDITY";
        public static final String PRESSURE = "PRESSURE";
        public static final String TEMP_MIN = "TEMP_MIN";
        public static final String TEMP_MAX = "TEMP_MAX";
        public static final String WIDNSPEED = "WIDNSPEED";
        public static final String WINDDIRECTION = "WINDDIRECTION";
        public static final String DESCRIPTION = "DESCRIPTION";
    }
}
