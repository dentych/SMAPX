package group.smapx.assignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import group.smapx.assignment2.service.WeatherService;

public class BootCompletedServiceStarter extends BroadcastReceiver {
    public BootCompletedServiceStarter() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent i = new Intent(context, WeatherService.class);
            context.startService(i);
        }
    }
}
