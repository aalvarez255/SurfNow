package com.aaps.surfnow;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by adrian on 7/08/14.
 */
public class PushNotification extends IntentService {

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public PushNotification() {
        super("PushNotification");

    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        JSONObject json;
        String height = "";
        try {
            json = new HttpConnection().execute().get();
            height = json.getString("height");
        } catch (Exception e) {
            e.printStackTrace();
        }

        double waves_height = Double.parseDouble(height);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("log",MODE_PRIVATE);
        Set<String> log = prefs.getStringSet("log_waves",null);
        if (log != null) {
            log.add(height);
        }
        else {
            String[] array = {};
            Set<String> mySet = new HashSet<String>(Arrays.asList(array));
            log = mySet;
            log.add(height);
        }

        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet("log_waves",log);
        edit.commit();

        if (waves_height >= 0.8) {

            long whenTo = System.currentTimeMillis();

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.yii)
                            .setContentTitle("Olitaaas!!")
                            .setContentText("Olas de "+height+"m")
                            .setWhen(whenTo)
                            .setDefaults(-1);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(121, mBuilder.build());
        }
    }
}
