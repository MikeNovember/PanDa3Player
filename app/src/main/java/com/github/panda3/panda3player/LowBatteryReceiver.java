package com.github.panda3.panda3player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class LowBatteryReceiver extends BroadcastReceiver {
    Boolean low_battery = false;
    public LowBatteryReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // an Intent broadcast.
        if(intent.getAction().equals(Intent.ACTION_BATTERY_LOW))
        {
            low_battery = true;

        }
        if(low_battery)
        {
            showLowBatteryAlert(context);
        }


    }
    private void showLowBatteryAlert(Context context)
    {
        Toast.makeText(context, R.string.low_battery, Toast.LENGTH_LONG).show();

    }
}
