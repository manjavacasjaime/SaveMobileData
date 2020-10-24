package nousage.iphonenerd.savemobiledata;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static nousage.iphonenerd.savemobiledata.MainActivity.currentLatitude;
import static nousage.iphonenerd.savemobiledata.MainActivity.currentLongitude;
import static nousage.iphonenerd.savemobiledata.MainActivity.mNotificationId;
import static nousage.iphonenerd.savemobiledata.MainActivity.maxLat;
import static nousage.iphonenerd.savemobiledata.MainActivity.maxLong;
import static nousage.iphonenerd.savemobiledata.MainActivity.minLat;
import static nousage.iphonenerd.savemobiledata.MainActivity.minLong;
import static nousage.iphonenerd.savemobiledata.MainActivity.nphwTicket;
import static nousage.iphonenerd.savemobiledata.MainActivity.onoff;
import static nousage.iphonenerd.savemobiledata.MainActivity.pushquiet;
import static nousage.iphonenerd.savemobiledata.MainActivity.saveLoc;
import static nousage.iphonenerd.savemobiledata.MainActivity.yccmsTicket;

public class NewShit extends AccessibilityService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "NewShitActivity";
    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;
    @Override
    protected void onServiceConnected(){
        super.onServiceConnected();
        Log.d(TAG, "FALUYA 81 IS WORKING");
        //Configure these here for compatibility with API 13 and below.
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        if (Build.VERSION.SDK_INT >= 16)
            //Just in case this helps
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "FALUYA 82 IS WORKING");
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() != null && event.getClassName() != null) {
                ComponentName componentName = new ComponentName(
                        event.getPackageName().toString(),
                        event.getClassName().toString()
                );

                ActivityInfo activityInfo = tryGetActivity(componentName);
                boolean isActivity = activityInfo != null;
                if (isActivity) {


                    //Create the LocationRequest object
                    mLocationRequest = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                            .setFastestInterval(1 * 1000); // 1 second, in milliseconds

                    // Create an instance of GoogleAPIClient.
                    if (mGoogleApiClient == null) {
                        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(LocationServices.API)
                                .build();
                        Log.d(TAG, "workinggg"); //THIS IS WORKING
                    }
                    mGoogleApiClient.connect();

                    if (saveLoc == 1) {
                        Log.d(TAG, "LOCATION BEING OBSERVED");
                        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (location == null) {
                            Log.d(TAG, "PHASE6");
                            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this); //PROBLEM HERE, NEED 2 REQUEST PERMISSION AT RUNTIME
                        } else {
                            Log.d(TAG, "PHASE7");
                            handleNewLocation(location);
                        }
                    }







                    Log.i("CurrentActivity", componentName.flattenToShortString()); //boolean startsWith(String pr)
                    boolean apps1 = false;
                    boolean apps2 = false;
                    boolean apps3 = false;
                    boolean dataUsing = false;

                    dataUsing = isOnline();

                    if (!(isOnline())){
                        mNotificationId = 1;
                        yccmsTicket = 1;
                        nphwTicket = 1;
                    }

                    boolean Instagram = componentName.flattenToShortString().startsWith("com.instagram.android");
                    boolean Facebook = componentName.flattenToShortString().startsWith("com.facebook.katana");
                    boolean Twitter = componentName.flattenToShortString().startsWith("com.twitter.android");
                    boolean Snapchat = componentName.flattenToShortString().startsWith("com.snapchat.android");
                    if (Instagram || Facebook || Twitter || Snapchat){
                        apps1 = true;
                    }

                    boolean Youtube = componentName.flattenToShortString().startsWith("com.google.android.youtube");
                    boolean ClashRoyale = componentName.flattenToShortString().startsWith("com.supercell.clashroyale");
                    boolean ClashOfClans = componentName.flattenToShortString().startsWith("com.supercell.clashofclans");
                    boolean Marca = componentName.flattenToShortString().startsWith("com.iphonedroid.marca");
                    boolean Skype = componentName.flattenToShortString().startsWith("com.skype.raider");
                    if (Youtube || ClashRoyale || ClashOfClans || Marca || Skype){
                        apps2 = true;
                    }

                    boolean Netflix = componentName.flattenToShortString().startsWith("com.netflix.mediaclient");
                    boolean PlayMX = componentName.flattenToShortString().startsWith("com.nymeros.playmx");
                    boolean HBO = componentName.flattenToShortString().startsWith("com.hbo.android.app");
                    boolean Wattpad = componentName.flattenToShortString().startsWith("wp.wattpad");
                    boolean Twitch = componentName.flattenToShortString().startsWith("tv.twitch.android.app");
                    if (Netflix || PlayMX || HBO || Wattpad || Twitch){
                        apps3 = true;
                    }

                    if (dataUsing && (mNotificationId < 3 || (mNotificationId > 15 && mNotificationId < 17) || (mNotificationId > 67 && mNotificationId < 69)) && (apps1 || apps2 || apps3) && onoff==1
                            && ((saveLoc == 1 && currentLatitude>=minLat && currentLatitude<=maxLat && currentLongitude>=minLong && currentLongitude<=maxLong)
                            || saveLoc != 1)) {
                        if (apps2 && yccmsTicket == 1){
                            mNotificationId = 15;
                            yccmsTicket++;
                        }
                        if (apps3 && nphwTicket == 1){
                            mNotificationId = 67;
                            nphwTicket++;
                        }


                        if (mNotificationId == 1 || mNotificationId == 15 || mNotificationId == 67){
                            if (pushquiet==1) {
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(this, "mychannelIdpush")
                                                .setSmallIcon(R.drawable.exclamationn)
                                                .setContentTitle("Data Usage Warning")
                                                .setContentText("Beware of an excessive use")
                                                .setPriority(Notification.PRIORITY_MAX)
                                                .setDefaults(NotificationCompat.DEFAULT_VIBRATE);

                                // Sets an ID for the notification
                                // Gets an instance of the NotificationManager service
                                NotificationManager mNotifyMgr =
                                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    String channelId = "mychannelIdpush";
                                    NotificationChannel channel = new NotificationChannel(
                                            channelId,
                                            "Channel human readable title",
                                            NotificationManager.IMPORTANCE_HIGH);
                                    mNotifyMgr.createNotificationChannel(channel);
                                    mBuilder.setChannelId(channelId);
                                }

                                // Builds the notification and issues it.
                                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                                mNotificationId++;

                            } else {
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(this, "mychannelIdquiet")
                                                .setSmallIcon(R.drawable.exclamationn)
                                                .setContentTitle("Data Usage Warning")
                                                .setContentText("Beware of an excessive use")
                                                .setPriority(Notification.PRIORITY_MAX);

                                // Sets an ID for the notification
                                // Gets an instance of the NotificationManager service
                                NotificationManager mNotifyMgr =
                                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    String channelId = "mychannelIdquiet";
                                    NotificationChannel channel = new NotificationChannel(
                                            channelId,
                                            "Channel human readable title",
                                            NotificationManager.IMPORTANCE_LOW);
                                    mNotifyMgr.createNotificationChannel(channel);
                                    mBuilder.setChannelId(channelId);
                                }

                                // Builds the notification and issues it.
                                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                                mNotificationId++;
                            }

                        } /*else {
                            if(pushquiet==1) {
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(this)
                                                .setSmallIcon(R.drawable.exclamationn)
                                                .setContentTitle("Data Usage Warning")
                                                .setContentText("Data decreasing")
                                                .setPriority(Notification.PRIORITY_MAX)
                                                .setDefaults(NotificationCompat.DEFAULT_VIBRATE);
                                // Sets an ID for the notification
                                // Gets an instance of the NotificationManager service
                                NotificationManager mNotifyMgr =
                                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                // Builds the notification and issues it.
                                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                                mNotificationId++;
                            } else {
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(this)
                                                .setSmallIcon(R.drawable.exclamationn)
                                                .setContentTitle("Data Usage Warning")
                                                .setContentText("Data decreasing")
                                                .setPriority(Notification.PRIORITY_MAX);
                                // Sets an ID for the notification
                                // Gets an instance of the NotificationManager service
                                NotificationManager mNotifyMgr =
                                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                // Builds the notification and issues it.
                                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                                mNotificationId++;
                            }
                        }*/

                    } else {
                        mNotificationId++;
                        if ((yccmsTicket > 1 || nphwTicket > 1) && (!(apps2 || apps3))){
                            yccmsTicket=1;
                            nphwTicket=1;
                            mNotificationId=3;
                        }
                    }
                    if(mNotificationId == 14){ //Instagram, Facebook, Twitter and Snapchat
                        mNotificationId = 1;
                    }
                    if (mNotificationId == 66){ //YouTube, Clash Royale, Clash of Clans, Marca and Skype
                        mNotificationId = 1;
                        yccmsTicket=1;
                    }
                    if (mNotificationId == 238){ //Netflix, PlayMX, HBO and Wattpad
                        mNotificationId = 1;
                        nphwTicket=1;
                    }
                    if (!(apps1 || apps2 || apps3)){
                        mNotificationId=1;
                        yccmsTicket=1;
                        nphwTicket=1;
                    }

                }
            }
        }
    }

    Context context=this;

    protected boolean isOnline() {
        Log.d(TAG, "FALUYA 83 IS WORKING");
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean netInfo = cm.isActiveNetworkMetered();
        if (netInfo) {
            return true;
        } else {
            return false;
        }

    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        Log.d(TAG, "FALUYA 84 IS WORKING");
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "FALUYA 6 IS WORKING");
        if (saveLoc == 1) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                Log.d(TAG, "PHASE6");
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this); //PROBLEM HERE, NEED 2 REQUEST PERMISSION AT RUNTIME
            } else {
                Log.d(TAG, "PHASE7");
                handleNewLocation(location);
            }
        }
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, "FALUYA 7 IS WORKING");
        Log.d(TAG, location.toString());
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
