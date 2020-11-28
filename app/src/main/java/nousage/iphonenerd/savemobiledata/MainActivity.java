package nousage.iphonenerd.savemobiledata;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;

import static nousage.iphonenerd.savemobiledata.autostart.round;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_MODIFY_PHONE_STATE = 123;
    public static int mNotificationId = 1;
    public static int yccmsTicket = 1;
    public static int nphwTicket = 1;
    public static int onoff;
    public static int pushquiet;
    public static double Latitude;
    public static double Longitude;
    public static int saveLoc;
    public static double maxLat;
    public static double minLat;
    public static double maxLong;
    public static double minLong;
    public static double currentLatitude;
    public static double currentLongitude;

    public static int onCreateLaunched;
    public static int onAdLaunched;

    public static InterstitialAd mInterstitialAd;

    ImageButton tBMobileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sharedPrefs = getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE);
        onAdLaunched = sharedPrefs.getInt("onAdLaunched", 0);


        final Switch mSwitch = (Switch) findViewById(R.id.switch1);

        ImageView mEnableText = (ImageView) findViewById(R.id.imageView);
        mEnableText.setVisibility(View.INVISIBLE);
        final ImageView enable = (ImageView) findViewById(R.id.imageView2);
        enable.setVisibility(View.INVISIBLE);


        Intent mServiceIntent = new Intent(this, RSSPullService.class);

        this.startService(mServiceIntent);


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

        mSwitch.setChecked(sharedPrefs.getBoolean("isChecked", false));
        checkSwitch();
        onCreateLaunched = 1;


        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSwitch();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        /*MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });*/

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9180401716986593/9443423441");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_loc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.threepoints:
                startActivity(new Intent(this, LocationActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void checkSwitch(){
        final Switch mSwitch = (Switch) findViewById(R.id.switch1);
        final ImageView enable = (ImageView) findViewById(R.id.imageView2);
        final ImageView pushTextOn = (ImageView) findViewById(R.id.imageView4);
        final ImageView quietTextOn = (ImageView) findViewById(R.id.imageView5);
        final ImageView pushTextOff = (ImageView) findViewById(R.id.imageView6);
        final ImageView quietTextOff = (ImageView) findViewById(R.id.imageView7);

        SharedPreferences.Editor editor = getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE).edit();

        if(mSwitch.isChecked()){
            onoff=1;
            editor.putInt("onoff", 1);
            editor.commit();
            SharedPreferences sharedPrefs = getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE);
            pushquiet = sharedPrefs.getInt("pushquiet", 1);
            final ImageView mEnableText = (ImageView) findViewById(R.id.imageView);
            mEnableText.setVisibility(View.INVISIBLE);
            enable.setVisibility(View.INVISIBLE);
            if (!isAccessibilitySettingsOn(getApplicationContext())) {
                mEnableText.setVisibility(View.VISIBLE);
                enable.setVisibility(View.VISIBLE);
                enable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivityForResult(intent, 0);
                        mEnableText.setVisibility(View.INVISIBLE);
                        enable.setVisibility(View.INVISIBLE);
                    }
                });
            }

            if (pushquiet == 1) {
                pushTextOn.setVisibility(View.VISIBLE);
                quietTextOn.setVisibility(View.INVISIBLE);
                pushTextOff.setVisibility(View.INVISIBLE);
                quietTextOff.setVisibility(View.VISIBLE);
                quietOffClick();
            } else {
                pushTextOn.setVisibility(View.INVISIBLE);
                quietTextOn.setVisibility(View.VISIBLE);
                pushTextOff.setVisibility(View.VISIBLE);
                quietTextOff.setVisibility(View.INVISIBLE);
                pushOffClick();
            }
            editor.putBoolean("isChecked", true);
            editor.commit();
        } else {
            onoff=0;
            editor.putInt("onoff", 0);
            editor.commit();

            ImageView mEnableText = (ImageView) findViewById(R.id.imageView);
            mEnableText.setVisibility(View.INVISIBLE);
            enable.setVisibility(View.INVISIBLE);
            pushTextOn.setVisibility(View.INVISIBLE);
            quietTextOn.setVisibility(View.INVISIBLE);
            pushTextOff.setVisibility(View.VISIBLE);
            quietTextOff.setVisibility(View.VISIBLE);
            editor.putBoolean("isChecked", false);
            editor.commit();
        }
    }


    public void quietOffClick(){
        final ImageView quietTextOff = (ImageView) findViewById(R.id.imageView7);
        final ImageView pushTextOn = (ImageView) findViewById(R.id.imageView4);
        final Switch mSwitch = (Switch) findViewById(R.id.switch1);
        quietTextOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwitch.isChecked()) {
                    final ImageView mEnableText = (ImageView) findViewById(R.id.imageView);
                    mEnableText.setVisibility(View.INVISIBLE);
                    final ImageView enable = (ImageView) findViewById(R.id.imageView2);
                    enable.setVisibility(View.INVISIBLE);
                    if (!isAccessibilitySettingsOn(getApplicationContext())) {
                        mEnableText.setVisibility(View.VISIBLE);
                        enable.setVisibility(View.VISIBLE);
                        enable.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                startActivityForResult(intent, 0);
                                mEnableText.setVisibility(View.INVISIBLE);
                                enable.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    pushquiet=0;
                    SharedPreferences.Editor editor = getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE).edit();
                    editor.putInt("pushquiet", 0);
                    editor.commit();
                    quietTextOff.setVisibility(View.INVISIBLE);
                    pushTextOn.setVisibility(View.INVISIBLE);
                    ImageView quietTextOn = (ImageView) findViewById(R.id.imageView5);
                    quietTextOn.setVisibility(View.VISIBLE);
                    ImageView pushTextOff = (ImageView) findViewById(R.id.imageView6);
                    pushTextOff.setVisibility(View.VISIBLE);
                    pushOffClick();
                }
            }
        });

    }

    public void pushOffClick(){
        final ImageView pushTextOff = (ImageView) findViewById(R.id.imageView6);
        final ImageView quietTextOn = (ImageView) findViewById(R.id.imageView5);
        final Switch mSwitch = (Switch) findViewById(R.id.switch1);
        pushTextOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwitch.isChecked()) {
                    final ImageView mEnableText = (ImageView) findViewById(R.id.imageView);
                    mEnableText.setVisibility(View.INVISIBLE);
                    final ImageView enable = (ImageView) findViewById(R.id.imageView2);
                    enable.setVisibility(View.INVISIBLE);
                    if (!isAccessibilitySettingsOn(getApplicationContext())) {
                        mEnableText.setVisibility(View.VISIBLE);
                        enable.setVisibility(View.VISIBLE);
                        enable.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                startActivityForResult(intent, 0);
                                mEnableText.setVisibility(View.INVISIBLE);
                                enable.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    pushquiet=1;
                    SharedPreferences.Editor editor = getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE).edit();
                    editor.putInt("pushquiet", 1);
                    editor.commit();
                    pushTextOff.setVisibility(View.INVISIBLE);
                    quietTextOn.setVisibility(View.INVISIBLE);
                    ImageView pushTextOn = (ImageView) findViewById(R.id.imageView4);
                    pushTextOn.setVisibility(View.VISIBLE);
                    ImageView quietTextOff = (ImageView) findViewById(R.id.imageView7);
                    quietTextOff.setVisibility(View.VISIBLE);
                    quietOffClick();
                }
            }
        });

    }

    public void setMobileDataState(boolean mobileDataEnabled) {
        try {
            TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod)
            {
                setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
            }
        }
        catch (Exception ex) {
            Log.e(TAG, "Error setting mobile data state", ex);
        }
    }


    public boolean getMobileDataState() {
        try {
            TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            Method getMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("getDataEnabled");

            if (null != getMobileDataEnabledMethod)
            {
                boolean mobileDataEnabled = (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);

                return mobileDataEnabled;
            }
        }
        catch (Exception ex) {
            Log.e(TAG, "Error getting mobile data state", ex);
        }

        return false;
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + NewShit.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }


}
