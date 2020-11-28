package nousage.iphonenerd.savemobiledata;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static nousage.iphonenerd.savemobiledata.MainActivity.Latitude;
import static nousage.iphonenerd.savemobiledata.MainActivity.Longitude;
import static nousage.iphonenerd.savemobiledata.MainActivity.currentLatitude;
import static nousage.iphonenerd.savemobiledata.MainActivity.currentLongitude;
import static nousage.iphonenerd.savemobiledata.MainActivity.maxLat;
import static nousage.iphonenerd.savemobiledata.MainActivity.maxLong;
import static nousage.iphonenerd.savemobiledata.MainActivity.minLat;
import static nousage.iphonenerd.savemobiledata.MainActivity.minLong;
import static nousage.iphonenerd.savemobiledata.MainActivity.saveLoc;

import static nousage.iphonenerd.savemobiledata.MainActivity.onAdLaunched;
import static nousage.iphonenerd.savemobiledata.MainActivity.mInterstitialAd;


public class LocationActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "LocationActivity";

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final int MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION = 123;

    public static String strLat = Double.toString(Latitude);
    public static String strLong = Double.toString(Longitude);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Log.d(TAG, "onCreate called");
        setInterface();


        // Create the LocationRequest object
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

    }

    public void setInterface() {
        ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
        ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
        TextView first = (TextView) findViewById(R.id.textView2);
        ImageView enable = (ImageView) findViewById(R.id.imageView8);
        ImageView changeLoc = (ImageView) findViewById(R.id.imageView81);
        ImageView deleteLoc = (ImageView) findViewById(R.id.imageView82);

        TextView Lat = (TextView) findViewById(R.id.textView3);
        TextView Lattext = (TextView) findViewById(R.id.textView31);
        TextView Long = (TextView) findViewById(R.id.textView4);
        TextView Longtext = (TextView) findViewById(R.id.textView41);
        Lat.setText(strLat);
        Long.setText(strLong);

        if (saveLoc==1){
            savecurr.setVisibility(View.INVISIBLE);
            locsaved.setVisibility(View.VISIBLE);
            first.setVisibility(View.INVISIBLE);
            enable.setVisibility(View.INVISIBLE);
            Lat.setVisibility(View.VISIBLE);
            Lattext.setVisibility(View.VISIBLE);
            Long.setVisibility(View.VISIBLE);
            Longtext.setVisibility(View.VISIBLE);
            changeLoc.setVisibility(View.INVISIBLE);
            deleteLoc.setVisibility(View.INVISIBLE);

        } else {
            savecurr.setVisibility(View.VISIBLE);
            locsaved.setVisibility(View.INVISIBLE);
            first.setVisibility(View.INVISIBLE);
            enable.setVisibility(View.INVISIBLE);
            Lat.setVisibility(View.INVISIBLE);
            Lattext.setVisibility(View.INVISIBLE);
            Long.setVisibility(View.INVISIBLE);
            Longtext.setVisibility(View.INVISIBLE);
            changeLoc.setVisibility(View.INVISIBLE);
            deleteLoc.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences.Editor editor = getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE).edit();

        if (onAdLaunched == 0) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
                onAdLaunched++;
                editor.putInt("onAdLaunched", onAdLaunched);
                editor.commit();
            }
        } else {
            onAdLaunched++;
            if (onAdLaunched == 3) onAdLaunched = 0;
            editor.putInt("onAdLaunched", onAdLaunched);
            editor.commit();
        }

        /*if (onAdLaunched == 0) {

            Log.d("TAG", "INTERSTITIAL VALUE0");
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    Log.d("TAG", "INTERSTITIAL LOADED");
                    mInterstitialAd.show();
                    onAdLaunched++;
                    SharedPreferences.Editor editor = getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE).edit();
                    editor.putInt("onAdLaunched", onAdLaunched);
                    editor.commit();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.d("TAG", "The interstitial wasn't loaded yet." + errorCode);
                }
            });
        } else {
            Log.d("TAG", "INTERSTITIAL VALUE123");
            onAdLaunched++;
            if (onAdLaunched == 3) onAdLaunched = 0;
            SharedPreferences.Editor editor = getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE).edit();
            editor.putInt("onAdLaunched", onAdLaunched);
            editor.commit();
        }*/

        // connect googleapiclient
        mGoogleApiClient.connect();
        Log.d(TAG, "onStart called"); //THIS IS WORKING

        setInterface();
    }

    @Override
    public void onConnected(Bundle bundle) {
        final SharedPreferences.Editor editor = getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE).edit();

        Log.d(TAG, "PHASE1");
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "PHASE2");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        } else {
            if(saveLoc!=1 && isAccessibilitySettingsOn(getApplicationContext())) {
                final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
                Log.d(TAG, "HERE 1");
                savecurr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveLocation();
                        savecurr.setVisibility(View.INVISIBLE);
                    }
                });
            } else if (!isAccessibilitySettingsOn(getApplicationContext())) {
                final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
                Log.d(TAG, "HERE 2");
                savecurr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final TextView first = (TextView) findViewById(R.id.textView2);
                        final ImageView enable = (ImageView) findViewById(R.id.imageView8);
                        first.setVisibility(View.VISIBLE);
                        enable.setVisibility(View.VISIBLE);
                        enable.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                startActivityForResult(intent, 0);
                                first.setVisibility(View.INVISIBLE);
                                enable.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });


            } else {
                ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
                locsaved.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ImageView changeLoc = (ImageView) findViewById(R.id.imageView81);
                        final ImageView deleteLoc = (ImageView) findViewById(R.id.imageView82);
                        changeLoc.setVisibility(View.VISIBLE);
                        deleteLoc.setVisibility(View.VISIBLE);
                        changeLoc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeLoc.setVisibility(View.INVISIBLE);
                                deleteLoc.setVisibility(View.INVISIBLE);
                                saveLoc=2;
                                editor.putInt("saveLoc", 2);
                                editor.commit();
                                saveLocation();
                            }
                        });
                        deleteLoc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changeLoc.setVisibility(View.INVISIBLE);
                                deleteLoc.setVisibility(View.INVISIBLE);
                                saveLoc=2;
                                editor.putInt("saveLoc", 2);
                                editor.commit();
                                ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
                                locsaved.setVisibility(View.INVISIBLE);
                                TextView Lat = (TextView) findViewById(R.id.textView3);
                                TextView Lattext = (TextView) findViewById(R.id.textView31);
                                TextView Long = (TextView) findViewById(R.id.textView4);
                                TextView Longtext = (TextView) findViewById(R.id.textView41);
                                Lat.setVisibility(View.INVISIBLE);
                                Lattext.setVisibility(View.INVISIBLE);
                                Long.setVisibility(View.INVISIBLE);
                                Longtext.setVisibility(View.INVISIBLE);
                                final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
                                savecurr.setVisibility(View.VISIBLE);
                                savecurr.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        saveLocation();
                                        savecurr.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }

    public void beforeAccepting(){
        final SharedPreferences.Editor editor = getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE).edit();

        Log.d(TAG, "HERE 5");
        if(saveLoc!=1 && isAccessibilitySettingsOn(getApplicationContext())) {
            final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
            Log.d(TAG, "HERE 1");
            savecurr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveLocation();
                    savecurr.setVisibility(View.INVISIBLE);
                }
            });
        } else if (!isAccessibilitySettingsOn(getApplicationContext())) {
            final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
            Log.d(TAG, "HERE 2");
            savecurr.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //HERE IS THE GAAAAAAME
                    if(saveLoc!=1 && isAccessibilitySettingsOn(getApplicationContext())) {
                        final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
                        Log.d(TAG, "HERE 3");
                        saveLocation();
                        savecurr.setVisibility(View.INVISIBLE);

                    } else if (!isAccessibilitySettingsOn(getApplicationContext())) {
                        final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
                        Log.d(TAG, "HERE 4");
                        savecurr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final TextView first = (TextView) findViewById(R.id.textView2);
                                final ImageView enable = (ImageView) findViewById(R.id.imageView8);
                                first.setVisibility(View.VISIBLE);
                                enable.setVisibility(View.VISIBLE);
                                enable.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                        startActivityForResult(intent, 0);
                                        first.setVisibility(View.INVISIBLE);
                                        enable.setVisibility(View.INVISIBLE);
                                        beforeAccepting();
                                    }
                                });
                            }
                        });


                    } else {
                        ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
                        locsaved.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final ImageView changeLoc = (ImageView) findViewById(R.id.imageView81);
                                final ImageView deleteLoc = (ImageView) findViewById(R.id.imageView82);
                                changeLoc.setVisibility(View.VISIBLE);
                                deleteLoc.setVisibility(View.VISIBLE);
                                changeLoc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changeLoc.setVisibility(View.INVISIBLE);
                                        deleteLoc.setVisibility(View.INVISIBLE);
                                        saveLoc=2;
                                        editor.putInt("saveLoc", 2);
                                        editor.commit();
                                        saveLocation();
                                    }
                                });
                                deleteLoc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changeLoc.setVisibility(View.INVISIBLE);
                                        deleteLoc.setVisibility(View.INVISIBLE);
                                        saveLoc=2;
                                        editor.putInt("saveLoc", 2);
                                        editor.commit();
                                        ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
                                        locsaved.setVisibility(View.INVISIBLE);
                                        TextView Lat = (TextView) findViewById(R.id.textView3);
                                        TextView Lattext = (TextView) findViewById(R.id.textView31);
                                        TextView Long = (TextView) findViewById(R.id.textView4);
                                        TextView Longtext = (TextView) findViewById(R.id.textView41);
                                        Lat.setVisibility(View.INVISIBLE);
                                        Lattext.setVisibility(View.INVISIBLE);
                                        Long.setVisibility(View.INVISIBLE);
                                        Longtext.setVisibility(View.INVISIBLE);
                                        final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
                                        savecurr.setVisibility(View.VISIBLE);
                                        savecurr.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                saveLocation();
                                                savecurr.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                }
            });


        } else {
            ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
            locsaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ImageView changeLoc = (ImageView) findViewById(R.id.imageView81);
                    final ImageView deleteLoc = (ImageView) findViewById(R.id.imageView82);
                    changeLoc.setVisibility(View.VISIBLE);
                    deleteLoc.setVisibility(View.VISIBLE);
                    changeLoc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeLoc.setVisibility(View.INVISIBLE);
                            deleteLoc.setVisibility(View.INVISIBLE);
                            saveLoc=2;
                            editor.putInt("saveLoc", 2);
                            editor.commit();
                            saveLocation();
                        }
                    });
                    deleteLoc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeLoc.setVisibility(View.INVISIBLE);
                            deleteLoc.setVisibility(View.INVISIBLE);
                            saveLoc=2;
                            editor.putInt("saveLoc", 2);
                            editor.commit();
                            ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
                            locsaved.setVisibility(View.INVISIBLE);
                            TextView Lat = (TextView) findViewById(R.id.textView3);
                            TextView Lattext = (TextView) findViewById(R.id.textView31);
                            TextView Long = (TextView) findViewById(R.id.textView4);
                            TextView Longtext = (TextView) findViewById(R.id.textView41);
                            Lat.setVisibility(View.INVISIBLE);
                            Lattext.setVisibility(View.INVISIBLE);
                            Long.setVisibility(View.INVISIBLE);
                            Longtext.setVisibility(View.INVISIBLE);
                            final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
                            savecurr.setVisibility(View.VISIBLE);
                            savecurr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    saveLocation();
                                    savecurr.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        final SharedPreferences.Editor editor = getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE).edit();

        Log.d(TAG, "PHASE3");

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCES_FINE_LOCATION: {

                Log.d(TAG, "PHASE4");

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if(saveLoc!=1 && isAccessibilitySettingsOn(getApplicationContext())) {
                        final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
                        Log.d(TAG, "HERE 3");
                        savecurr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                saveLocation();
                                savecurr.setVisibility(View.INVISIBLE);
                            }
                        });
                    } else if (!isAccessibilitySettingsOn(getApplicationContext())) {
                        final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
                        Log.d(TAG, "HERE 4");
                        savecurr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final TextView first = (TextView) findViewById(R.id.textView2);
                                final ImageView enable = (ImageView) findViewById(R.id.imageView8);
                                first.setVisibility(View.VISIBLE);
                                enable.setVisibility(View.VISIBLE);
                                enable.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                        startActivityForResult(intent, 0);
                                        first.setVisibility(View.INVISIBLE);
                                        enable.setVisibility(View.INVISIBLE);
                                        beforeAccepting();
                                    }
                                });
                            }
                        });


                    } else {
                        ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
                        locsaved.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final ImageView changeLoc = (ImageView) findViewById(R.id.imageView81);
                                final ImageView deleteLoc = (ImageView) findViewById(R.id.imageView82);
                                changeLoc.setVisibility(View.VISIBLE);
                                deleteLoc.setVisibility(View.VISIBLE);
                                changeLoc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changeLoc.setVisibility(View.INVISIBLE);
                                        deleteLoc.setVisibility(View.INVISIBLE);
                                        saveLoc=2;
                                        editor.putInt("saveLoc", 2);
                                        editor.commit();
                                        saveLocation();
                                    }
                                });
                                deleteLoc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changeLoc.setVisibility(View.INVISIBLE);
                                        deleteLoc.setVisibility(View.INVISIBLE);
                                        saveLoc=2;
                                        editor.putInt("saveLoc", 2);
                                        editor.commit();
                                        ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
                                        locsaved.setVisibility(View.INVISIBLE);
                                        TextView Lat = (TextView) findViewById(R.id.textView3);
                                        TextView Lattext = (TextView) findViewById(R.id.textView31);
                                        TextView Long = (TextView) findViewById(R.id.textView4);
                                        TextView Longtext = (TextView) findViewById(R.id.textView41);
                                        Lat.setVisibility(View.INVISIBLE);
                                        Lattext.setVisibility(View.INVISIBLE);
                                        Long.setVisibility(View.INVISIBLE);
                                        Longtext.setVisibility(View.INVISIBLE);
                                        final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
                                        savecurr.setVisibility(View.VISIBLE);
                                        savecurr.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                saveLocation();
                                                savecurr.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Log.d(TAG, "PHASE8");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void saveLocation(){
        Log.d(TAG, "PHASE5");

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            Log.d(TAG, "PHASE6");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this); //PROBLEM HERE, NEED 2 REQUEST PERMISSION AT RUNTIME
        }
        else {
            Log.d(TAG, "PHASE7");
            handleNewLocation(location);
            ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
            locsaved.setVisibility(View.VISIBLE);
        }
    }

    private void handleNewLocation(Location location) {
        final SharedPreferences.Editor editor = getSharedPreferences("nousage.savemobiledata", MODE_PRIVATE).edit();

        Log.d(TAG, location.toString());
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        editor.putFloat("currentLatitude", (float)currentLatitude);
        editor.commit();
        editor.putFloat("currentLongitude", (float)currentLongitude);
        editor.commit();

        if(saveLoc != 1){
            Latitude = currentLatitude;
            Longitude = currentLongitude;
            editor.putFloat("Latitude", (float)currentLatitude);
            editor.commit();
            editor.putFloat("Longitude", (float)currentLongitude);
            editor.commit();


            strLat = Double.toString(Latitude);
            strLong = Double.toString(Longitude);
            TextView Lat = (TextView) findViewById(R.id.textView3);
            TextView Lattext = (TextView) findViewById(R.id.textView31);
            TextView Long = (TextView) findViewById(R.id.textView4);
            TextView Longtext = (TextView) findViewById(R.id.textView41);
            Lat.setText(strLat);
            Long.setText(strLong);
            Lat.setVisibility(View.VISIBLE);
            Lattext.setVisibility(View.VISIBLE);
            Long.setVisibility(View.VISIBLE);
            Longtext.setVisibility(View.VISIBLE);


            maxLat = Latitude + 0.000363;
            minLat = Latitude - 0.000246;
            maxLong = Longitude + 0.000482;
            minLong = Longitude - 0.000423;
            editor.putFloat("maxLat", (float)maxLat);
            editor.commit();
            editor.putFloat("minLat", (float)minLat);
            editor.commit();
            editor.putFloat("maxLong", (float)maxLong);
            editor.commit();
            editor.putFloat("minLong", (float)minLong);
            editor.commit();


            saveLoc = 1;
            editor.putInt("saveLoc", 1);
            editor.commit();
            ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
            locsaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ImageView changeLoc = (ImageView) findViewById(R.id.imageView81);
                    final ImageView deleteLoc = (ImageView) findViewById(R.id.imageView82);
                    changeLoc.setVisibility(View.VISIBLE);
                    deleteLoc.setVisibility(View.VISIBLE);
                    changeLoc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeLoc.setVisibility(View.INVISIBLE);
                            deleteLoc.setVisibility(View.INVISIBLE);
                            saveLoc=2;
                            editor.putInt("saveLoc", 2);
                            editor.commit();
                            saveLocation();
                        }
                    });
                    deleteLoc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeLoc.setVisibility(View.INVISIBLE);
                            deleteLoc.setVisibility(View.INVISIBLE);
                            saveLoc=2;
                            editor.putInt("saveLoc", 2);
                            editor.commit();
                            ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
                            locsaved.setVisibility(View.INVISIBLE);
                            TextView Lat = (TextView) findViewById(R.id.textView3);
                            TextView Lattext = (TextView) findViewById(R.id.textView31);
                            TextView Long = (TextView) findViewById(R.id.textView4);
                            TextView Longtext = (TextView) findViewById(R.id.textView41);
                            Lat.setVisibility(View.INVISIBLE);
                            Lattext.setVisibility(View.INVISIBLE);
                            Long.setVisibility(View.INVISIBLE);
                            Longtext.setVisibility(View.INVISIBLE);
                            final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
                            savecurr.setVisibility(View.VISIBLE);
                            savecurr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    saveLocation();
                                    savecurr.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });
                }
            });

        } else if (saveLoc == 1){
            ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
            locsaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ImageView changeLoc = (ImageView) findViewById(R.id.imageView81);
                    final ImageView deleteLoc = (ImageView) findViewById(R.id.imageView82);
                    changeLoc.setVisibility(View.VISIBLE);
                    deleteLoc.setVisibility(View.VISIBLE);
                    changeLoc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeLoc.setVisibility(View.INVISIBLE);
                            deleteLoc.setVisibility(View.INVISIBLE);
                            saveLoc=2;
                            editor.putInt("saveLoc", 2);
                            editor.commit();
                            saveLocation();
                        }
                    });
                    deleteLoc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            changeLoc.setVisibility(View.INVISIBLE);
                            deleteLoc.setVisibility(View.INVISIBLE);
                            saveLoc=2;
                            editor.putInt("saveLoc", 2);
                            editor.commit();
                            ImageView locsaved = (ImageView) findViewById(R.id.imageView30);
                            locsaved.setVisibility(View.INVISIBLE);
                            TextView Lat = (TextView) findViewById(R.id.textView3);
                            TextView Lattext = (TextView) findViewById(R.id.textView31);
                            TextView Long = (TextView) findViewById(R.id.textView4);
                            TextView Longtext = (TextView) findViewById(R.id.textView41);
                            Lat.setVisibility(View.INVISIBLE);
                            Lattext.setVisibility(View.INVISIBLE);
                            Long.setVisibility(View.INVISIBLE);
                            Longtext.setVisibility(View.INVISIBLE);
                            final ImageView savecurr = (ImageView) findViewById(R.id.imageView3);
                            savecurr.setVisibility(View.VISIBLE);
                            savecurr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    saveLocation();
                                    savecurr.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });
                }
            });
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
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

    public boolean onTouchEvent(MotionEvent event) {
        if(saveLoc==1){
            ImageView changeLoc = (ImageView) findViewById(R.id.imageView81);
            ImageView deleteLoc = (ImageView) findViewById(R.id.imageView82);
            changeLoc.setVisibility(View.INVISIBLE);
            deleteLoc.setVisibility(View.INVISIBLE);
        }

        return true;

    }


    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

}