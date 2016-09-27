package group.smapx.assignment2.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by DSLTEST-UDVIKLER on 27-09-2016.
 */

public class WeatherDBHelper extends SQLiteOpenHelper {

    private static WeatherDBHelper instance;

    private static final String NAME = "WeatherDB.db";
    private static final int VERSION = 1;


    public static synchronized WeatherDBHelper getInstance(Context context){
        if(instance == null)
            instance = new WeatherDBHelper(context,NAME,null,VERSION);

        return instance;
    }


    private WeatherDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DAL", "Creating database with " + WeatherContract.SQL_CREATE_ENTRIES);
        db.execSQL(WeatherContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
