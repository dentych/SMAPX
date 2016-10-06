package group.smapx.remindalot;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    Ringtone r;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Yay, got it!", Toast.LENGTH_SHORT).show();

        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(2000);

        Log.d("Test", "Vibrate'n'shite");
        Log.d("Test", String.valueOf(context.getApplicationContext()));

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }



        AlertDialog.Builder ab = new AlertDialog.Builder(context)
                .setTitle("Wow")
                .setMessage("Alarm god damnit")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // continue do stuff
                        Log.d("Test", "Yes");
                        r.stop();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                        Log.d("Test", "No");
                        r.stop();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);


        AlertDialog ad = ab.create();

        //Allows the alarm to be shown on locked screen
        //***Heavily influenced by this: http://stackoverflow.com/questions/3629179/android-activity-over-default-lock-screen
        ad.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        //***
        ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);






        ad.show();



    }
}

