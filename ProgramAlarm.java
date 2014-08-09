package com.aaps.surfnow;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by adrian on 8/08/14.
 */
 
public class ProgramAlarm extends IntentService {
    public ProgramAlarm() {
        super("ProgramAlarm");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent intent2 = new Intent(this, AlarmIntentReceiver.class);

        AlarmManager am=(AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,  intent2, PendingIntent.FLAG_CANCEL_CURRENT);
        long recurring = (60 * 60000);  // in milliseconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),recurring, pendingIntent);
    }
}
