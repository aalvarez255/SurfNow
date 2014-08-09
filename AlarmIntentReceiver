package com.aaps.surfnow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by adrian on 7/08/14.
 */
 
/** 
 * Receiver to capture the alarm programmed to check the waves forecast. 
 * Its purpose is invoque the service "PushNotification".
 */
 
public class AlarmIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent e = new Intent(context, PushNotification.class);
        context.startService(e);
    }

}
