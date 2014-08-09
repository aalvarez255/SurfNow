package com.aaps.surfnow;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by adrian on 8/08/14.
 */
 
/**
 * Class to establish connection with the www.magicseaweed.com's API (JSON data).
 * Configured to use GET requests and with Cadiz spot (id=184).
 * Optimization to select only the Height of the waves.
 */
 
public class HttpConnection extends AsyncTask<Void, Void, JSONObject> {


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        InputStream is = null;
        JSONArray jsonObject = null;
        JSONObject js = null;

        // HTTP
        try {
            HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
            HttpResponse response;
            String responseString = null;

            response = httpclient.execute(new HttpGet("http://magicseaweed.com/api/APIKEY/forecast/?spot_id=184&units=eu"));

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
            jsonObject = new JSONArray(responseString);
            js = jsonObject.getJSONObject(jsonObject.length() - 1);
            js = js.getJSONObject("swell");
            js = js.getJSONObject("components");
            js = js.getJSONObject("combined");

        } catch (Exception e) {
            Log.v("TEST",e.getMessage());
        }

        return js;
    }


    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
    }
}
