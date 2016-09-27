package group.smapx.assignment2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import group.smapx.assignment2.DAL.WeatherDAO;
import group.smapx.assignment2.DAL.WeatherDBHelper;
import group.smapx.assignment2.models.WeatherModel;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DBUnitTests {
    Context mMockContext;

    @Test
    public void DBSetup_CallingCtor_ExpectDBIsOpen() throws Exception {
        WeatherDBHelper helper = WeatherDBHelper.getInstance(new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_"));

        SQLiteDatabase db = helper.getWritableDatabase();
        Log.d("Test", "DB Status tested.");
        assertTrue(db.isOpen());
        db.close();
    }

    @Test
    public void DBSetup_CallingCtor_ExpectDBIsClosedOnExit() throws Exception {
        WeatherDBHelper helper = WeatherDBHelper.getInstance(new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_"));

        SQLiteDatabase db = helper.getWritableDatabase();
        db.close();
        Log.d("Test", "DB Status tested.");
        assertTrue(!db.isOpen());

    }
    @Test
    public void DBShit_CreatingWeatherModel_ExpectNoError(){
        WeatherDAO dao  = new WeatherDAO(new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_"));
        long id = dao.save(new WeatherModel(new Date().getTime(), 20.00,0, 105.36, 0, 30.00, 20, 120, "None"));
        Log.d("Test","ID: " + id);

        assertTrue(id != -1);
    }

    @Test
    public void DBShit_GetAllData_ExpectNoError(){
        WeatherDAO dao  = new WeatherDAO(new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_"));
        long id = dao.save(new WeatherModel(new Date().getTime(), 20.00,0, 105.36, 0, 30.00, 20, 120, "None"));
        Log.d("Test", "Rowcount: " + dao.getProfilesCount());
        List<WeatherModel> models = dao.getAllWeather();

        assertTrue(models.size() == 0);

    }

    @Test
    public void DBShit_GetDataWithCondition_ExpectNoError(){
        WeatherDAO dao  = new WeatherDAO(new RenamingDelegatingContext(InstrumentationRegistry.getTargetContext(), "test_"));
        long id = dao.save(new WeatherModel(new Date().getTime(), 1,2,3,4,5,6,7, "None"));
        List<WeatherModel> models = dao.getWeatherForLastDays(2);
        Log.d("Test","Size: " + models.size());

        assertTrue(models.get(0).getId() == 1);
        assertTrue(models.get(0).getTemperature() == 1);
        assertTrue(models.get(0).getHumidity() == 2);
        assertTrue(models.get(0).getPressure() == 3);
        assertTrue(models.get(0).getTemp_min() == 4);
        assertTrue(models.get(0).getTemp_max() == 5);
        assertTrue(models.get(0).getWindspeed() == 6);
        assertTrue(models.get(0).getWinddirection() == 7);
        assertTrue(models.get(0).getClouds().equals("None"));


    }
}

