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

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    MediaPlayer mp;
    Boolean AlarmSounding = false;
    Vibrator vib;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Yay, got it!", Toast.LENGTH_SHORT).show();

        Bundle reminderInfo = intent.getBundleExtra("reminderInfo");
        String title = reminderInfo.getString("title");
        String description = reminderInfo.getString("description");

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
                .setTitle(title)
                .setMessage(description)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlarmBroadcastReceiver", "Ok");
                        mp.stop();
                        AlarmSounding = false;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info);

        AlertDialog alertDialog = alertDialogBuilder.create();

        //Allows the alarm to be shown on locked screen
        //***Heavily influenced by this: http://stackoverflow.com/questions/3629179/android-activity-over-default-lock-screen
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //***

        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        alertDialog.show();

        keepDialog(alertDialog);
    }

    //Not my code, solution found here: http://stackoverflow.com/questions/7557265/prevent-dialog-dismissal-on-screen-rotation-in-android
    private static void keepDialog(AlertDialog alertDialog) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(lp);
    }
    //***
}

