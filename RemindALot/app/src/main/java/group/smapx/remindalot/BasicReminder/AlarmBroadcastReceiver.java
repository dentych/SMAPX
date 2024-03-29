package group.smapx.remindalot.BasicReminder;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import group.smapx.remindalot.database.DatabaseDAO;
import group.smapx.remindalot.model.Reminder;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    MediaPlayer mp;
    Boolean AlarmSounding = false;
    Vibrator vib;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle reminderInfo = intent.getBundleExtra("reminderInfo");
        Reminder reminder = (Reminder) reminderInfo.getSerializable("reminder");

        if (reminder == null)
            return;

        DatabaseDAO db = new DatabaseDAO(context);
        reminder.setNotified(true);
        db.updateReminder(reminder);

        AlarmSounding = true;
        vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        final Thread vibrateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    do {
                        vib.vibrate(1000);
                        Thread.sleep(2000);

                    } while (AlarmSounding);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        vibrateThread.start();
        Log.d("AlarmBroadcastReceiver", "Vibrate On!");

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            mp = MediaPlayer.create(context, notification);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setTitle(reminder.getTitle())
                .setMessage(reminder.getDescription())
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlarmBroadcastReceiver", "Ok");
                        mp.stop();
                        AlarmSounding = false;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(false);

        AlertDialog alertDialog = alertDialogBuilder.create();

        //Allows the alarm to be shown on locked screen
        //***This is not our code -> influenced by this: http://stackoverflow.com/questions/3629179/android-activity-over-default-lock-screen
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //***

        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        alertDialog.show();
    }
}

