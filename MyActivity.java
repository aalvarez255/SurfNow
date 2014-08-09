package com.aaps.surfnow;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONObject;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SimpleTimeZone;


/**
 * Main user activity. Switch button to enable/disable the alarms and log of the last waves requests.
 */
 
 
public class MyActivity extends ActionBarActivity {

    Switch switch1;
    SharedPreferences prefs;
    SharedPreferences logs;

    AlarmManager am = null;
    PendingIntent pendingIntent = null;
    TextView textView;


    public void clear(View v) {
        SharedPreferences.Editor edit = logs.edit();
        edit.clear();
        edit.commit();

        textView.setText("");
    }

    public void send(View v) {
        JSONObject json;
        String height = "";
        try {
            json = new HttpConnection().execute().get();
            height = json.getString("height");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("TEST",height);

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Olas:");
        alertDialog.setMessage(height + "m");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.show();

        Set<String> log = logs.getStringSet("log_waves",null);
        if (log != null) {
            log.add(height);
        }
        else {
            String[] array = {};
            Set<String> mySet = new HashSet<String>(Arrays.asList(array));
            log = mySet;
            log.add(height);
        }

        SharedPreferences.Editor edit = logs.edit();
        edit.putStringSet("log_waves",log);
        edit.commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        textView = (TextView) findViewById(R.id.textView);

        logs = getApplicationContext().getSharedPreferences("log",MODE_PRIVATE);
        prefs = getApplicationContext().getSharedPreferences("Configuration", getApplicationContext().MODE_PRIVATE);

        Set<String> log_set = logs.getStringSet("log_waves",null);
        String[] array;

        if (log_set != null){
            array = log_set.toArray(new String[log_set.size()]);
        }
        else {
            array = new String[0];
        }

        for (int i = 0; i < array.length; ++i) {
            textView.append(array[i]);
        }

        boolean notifications = prefs.getBoolean("notifications",true);

        switch1 = (Switch) findViewById(R.id.switch1);
        if (notifications) switch1.setChecked(true);
        else switch1.setChecked(false);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkSwitch();
            }
        });

        checkSwitch();
    }


    public void checkSwitch() {
        if (switch1.isChecked()) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("notifications", true);
            editor.commit();

            Intent intent = new Intent(this, AlarmIntentReceiver.class);
            am=(AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            pendingIntent = PendingIntent.getBroadcast(this, 0,  intent, PendingIntent.FLAG_CANCEL_CURRENT);
            long recurring = (60 * 60000);  // in milliseconds
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),recurring, pendingIntent);
        }
        else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("notifications", false);
            editor.commit();

            if (am != null) {
                am.cancel(pendingIntent);
            }
        }

    }
}
