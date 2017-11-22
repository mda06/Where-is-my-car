package com.mda.school.async;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeocodingAsyncTask extends AsyncTask<Location, Void, String> {

    private final String TAG = getClass().getSimpleName();

    public AsyncResponse<String> delegate = null;

    public GeocodingAsyncTask(AsyncResponse<String> response) {
        delegate = response;
    }

    @Override
    protected String doInBackground(Location... params) {
        if(params.length != 1) return null;
        Location loc = params[0];
        String key = "AIzaSyCGpv6ko1ieBPYj00r0WGeBDdZcS9DkUOE";
        String path = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
        path += loc.getLongitude() + "," + loc.getLatitude() + "&sensor=true";
        path += "&key=" + key;

        try{
            JSONObject jsonObject = getJSONObjectFromURL(path);
            JSONArray arr = jsonObject.getJSONArray("results");
            if(arr.length() > 0) {
                JSONObject obj = arr.getJSONObject(0);
                String adr = obj.getString("formatted_address");
                Log.d(TAG, "Address found: " + adr);
                return adr;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    //https://stackoverflow.com/questions/34691175/how-to-send-httprequest-and-get-json-response-in-android
    private static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        return new JSONObject(jsonString);
    }

    @Override
    protected void onPostExecute(String adr)
    {
        super.onPostExecute(adr);
        if(delegate != null)
            delegate.processFinish(adr);
    }
}
