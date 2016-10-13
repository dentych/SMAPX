package group.smapx.remindalot.BasicReminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import group.smapx.remindalot.model.Reminder;

public class BasicReminder {

    private Context context;
    private AlarmManager am;

    public BasicReminder(Context context) {
        this.context = context;
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(Reminder reminder, long time) {

        Bundle reminderInfo = new Bundle();
        reminderInfo.putString("title", reminder.getTitle());
        reminderInfo.putString("description", reminder.getDescription());

        Intent setAlarm = new Intent(context, AlarmBroadcastReceiver.class);
        setAlarm.putExtra("reminderInfo", reminderInfo);

        PendingIntent pendingAlarm = PendingIntent.getBroadcast(context, (int) reminder.getId(), setAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= 19) {
            am.setExact(AlarmManager.RTC_WAKEUP, time, pendingAlarm);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, time, pendingAlarm);
        }

        Log.d("BasicReminder", "Setting Alarm");
        Log.d("BasicReminder", String.valueOf(reminder.getDate()));

    }

    public void deleteAlarm(Reminder reminder) {
        Intent deleteAlarm = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingAlarm = PendingIntent.getBroadcast(context, (int) reminder.getId(), deleteAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        am.cancel(pendingAlarm);
    }

}
