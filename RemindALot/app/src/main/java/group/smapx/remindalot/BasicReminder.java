package group.smapx.remindalot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class BasicReminder {

    private Context context;
    private AlarmManager am;

    public BasicReminder(Context context) {
        this.context = context;
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(String title, String description, long time, int requestCode) {

        Bundle reminderInfo = new Bundle();
        reminderInfo.putString("title", title);
        reminderInfo.putString("description", description);

        Intent setAlarm = new Intent(context, AlarmBroadcastReceiver.class);
        setAlarm.putExtra("reminderInfo", reminderInfo);

        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, setAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= 19) {
            am.setExact(AlarmManager.RTC_WAKEUP, time, pi);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, time, pi);
        }

        Log.d("BasicReminder", "Setting Alarm");
    }
}
