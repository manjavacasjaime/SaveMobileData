package nousage.iphonenerd.savemobiledata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import static nousage.iphonenerd.savemobiledata.MainActivity.onoff;
import static nousage.iphonenerd.savemobiledata.MainActivity.pushquiet;

import static nousage.iphonenerd.savemobiledata.MainActivity.Latitude;
import static nousage.iphonenerd.savemobiledata.MainActivity.Longitude;
import static nousage.iphonenerd.savemobiledata.MainActivity.currentLatitude;
import static nousage.iphonenerd.savemobiledata.MainActivity.currentLongitude;
import static nousage.iphonenerd.savemobiledata.MainActivity.maxLat;
import static nousage.iphonenerd.savemobiledata.MainActivity.maxLong;
import static nousage.iphonenerd.savemobiledata.MainActivity.minLat;
import static nousage.iphonenerd.savemobiledata.MainActivity.minLong;
import static nousage.iphonenerd.savemobiledata.MainActivity.saveLoc;
import static nousage.iphonenerd.savemobiledata.MainActivity.onCreateLaunched;

import static android.content.Context.MODE_PRIVATE;

public class autostart extends BroadcastReceiver {

    private static final String TAG = "AUTOSTART";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (onCreateLaunched != 1) {

            SharedPreferences sharedPrefs = context.getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE);

            onoff = sharedPrefs.getInt("onoff", 0);
            pushquiet = sharedPrefs.getInt("pushquiet", 1);
            saveLoc = sharedPrefs.getInt("saveLoc", 0);
            if (saveLoc == 1) {
                currentLatitude = round((double) sharedPrefs.getFloat("currentLatitude", 0), 7);
                currentLongitude = round((double) sharedPrefs.getFloat("currentLongitude", 0), 7);
                Latitude = round((double) sharedPrefs.getFloat("Latitude", 0), 7);
                Longitude = round((double) sharedPrefs.getFloat("Longitude", 0), 7);
                maxLat = round((double) sharedPrefs.getFloat("maxLat", 0), 7);
                maxLong = round((double) sharedPrefs.getFloat("maxLong", 0), 7);
                minLat = round((double) sharedPrefs.getFloat("minLat", 0), 7);
                minLong = round((double) sharedPrefs.getFloat("minLong", 0), 7);
            }

            Intent mServiceIntent = new Intent(context, RSSPullService.class);

            context.startService(mServiceIntent);
        }
    }



    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
