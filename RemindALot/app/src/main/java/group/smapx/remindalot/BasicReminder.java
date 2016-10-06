package group.smapx.remindalot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class BasicReminder {

    private Context context;
    private AlarmManager am;

    public BasicReminder(Context context) {
        this.context = context;
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(long time) {

        Intent setAlarm = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context,0, setAlarm, 0);

        if (Build.VERSION.SDK_INT >= 19) {
            am.setExact(AlarmManager.RTC_WAKEUP, time, pi);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, time, pi);

        }
        Log.d("BasicReminder", "Setting Alarm");
    }
}
