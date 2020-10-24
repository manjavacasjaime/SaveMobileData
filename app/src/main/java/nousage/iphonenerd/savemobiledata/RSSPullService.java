package nousage.iphonenerd.savemobiledata;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static nousage.iphonenerd.savemobiledata.MainActivity.mNotificationId;
import static nousage.iphonenerd.savemobiledata.MainActivity.nphwTicket;
import static nousage.iphonenerd.savemobiledata.MainActivity.yccmsTicket;

public class RSSPullService extends IntentService {

    private static final String TAG = "FirstIntentActivity";

    public RSSPullService() {
        super("RSSPullService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        //String dataString = workIntent.getDataString();

        // Do work here, based on the contents of dataString

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                myLopp();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, new Date(), 7000);

    }

    public void myLopp(){
        if(isOnline()==true) {
            getTasks3();
        } else {
            mNotificationId = 1;
            yccmsTicket = 1;
            nphwTicket = 1;
        }
    }

    Context context=this;

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean netInfo = cm.isActiveNetworkMetered();
        if (netInfo) {
            return true;
        } else {
            return false;
        }

    }

    public void getTasks3(){
        Log.d(TAG, "FALUYA PRE-1 IS WORKING");

        Intent mServiceIntent2 = new Intent(this, NewShit.class);
        //mServiceIntent.setData(Uri.parse(dataUrl));
        this.startService(mServiceIntent2);
    }
}
