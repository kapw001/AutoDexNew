package autodex.com.autodex.activitys;


import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import autodex.com.autodex.MessageEvent;
import autodex.com.autodex.R;
import autodex.com.autodex.RuntimePermissionRequest;
import autodex.com.autodex.UploadAndDownloadData;
import autodex.com.autodex.Util;
import autodex.com.autodex.UtilFunctionalitys;
import autodex.com.autodex.Utility;
import autodex.com.autodex.customview.BadgeDrawable;
import autodex.com.autodex.firebase.Config;
import autodex.com.autodex.firebase.NotificationUtils;
import autodex.com.autodex.fragments.BaseFragment;
import autodex.com.autodex.fragments.ContactsAndFavouriteFragments;
import autodex.com.autodex.fragments.FavouritesFragment;
import autodex.com.autodex.fragments.GroupsFragment;
import autodex.com.autodex.fragments.NotificationFragment;
import autodex.com.autodex.fragments.SettingsFragment;
import autodex.com.autodex.fragments.SyncBackupFragment;
import autodex.com.autodex.interfacecallback.IShowCount;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.locationservice.LocationUpdatesBroadcastReceiver;
import autodex.com.autodex.model.NotificationModel;
import autodex.com.autodex.model.webresponse.ProfileDetails;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebUrl;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, IShowCount {
    private static final String TAG = "HomeActivity";

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL = 60000; // Every 60 seconds.

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value, but they may be less frequent.
     */
    private static final long FASTEST_UPDATE_INTERVAL = 30000; // Every 30 seconds

    /**
     * The max time before batched results are delivered by location services. Results may be
     * delivered sooner than this interval.
     */
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL + 10; // Every 5 minutes.

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    private static final int MLTIPLE_PERMISSION_REQUEST = 1;
    private String[] permission = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WAKE_LOCK};

    public static final int CONTACT_ADDED = 3;
    private TextView notificationcount, syncbackup;
    private RelativeLayout notify;
    private TextView badgeCount, navProfileName, navProfilePhoneNumber, navProfileAddress;
    private ImageView phoneImg, locationImg;
    private CircleImageView profileImage;
    private BaseFragment baseFragment;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    private SharedPreferences sharedPreferences;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private View profiledetails;

    private int INSERT_CONTACT_REQUEST = 2;

    private boolean isF = true;

    public static boolean isServiceStarted = true;
    private AdView mAdView;
    public int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        MobileAds.initialize(this,
                getResources().getString(R.string.appid));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(getApplicationContext(), CreateContactActivity.class), CONTACT_ADDED);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        notificationcount = (TextView) navigationView.getMenu().findItem(R.id.notifications).getActionView().findViewById(R.id.badge_notification_1);
        syncbackup = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.syncbackup));


        initializeCountDrawer();
        navigationView.getMenu().findItem(R.id.contact).setChecked(true);
        callFragment(R.id.contact);

        View view = navigationView.getHeaderView(0);
        phoneImg = (ImageView) view.findViewById(R.id.phoneimg);
        locationImg = (ImageView) view.findViewById(R.id.locationimg);
        navProfileName = (TextView) view.findViewById(R.id.navProfileName);
        navProfilePhoneNumber = (TextView) view.findViewById(R.id.navProfilePhoneNumber);
        navProfileAddress = (TextView) view.findViewById(R.id.navProfileAddress);
        profiledetails = (LinearLayout) view.findViewById(R.id.profiledetails);
        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        profiledetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ProfileDetails profileDetails = appDatabase.daoProfileAccess().loadProfileByIds();
//                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class).putExtra("profileDetails", profileDetails));
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
//                transitionActivity();
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (RuntimePermissionRequest.checkMultiplePermission(this, permission)) {
            RuntimePermissionRequest.requestPermissionMultiple(this, permission, MLTIPLE_PERMISSION_REQUEST, "Permission", "Need read contacts permission to get contacts ");
        } else {
//            new UploadData().execute();
            callUploadData();
            createLocationRequest();
            requestLocationUpdates(null);
        }

        if (!checkPermissions()) {
            requestPermissions();
        }


//        callLocation();
//
//        Util.scheduleJob(getApplicationContext());

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
//                    // new push notification is received
//
//                    String message = intent.getStringExtra("message");
//
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//
//                    txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(view) {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.


            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });


//        showNotificationCount(0);


        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            String someData = extras.getString("app");
//            String someData2 = extras.getString("someData2");
            Toast.makeText(this, "" + someData, Toast.LENGTH_SHORT).show();
        }


        Log.e(TAG, "onCreate: token " + SessionManager.getInstance(this).getKeyToken());
//        checkSessionTimeout();

    }


//    private void checkSessionTimeout() {
//
//        Utility.showProgress(this, "Loading");
//
//        final String token = sessionManager.getDetails().get(KEY_TOKEN);
////        Utility.showProgressContactInit(InitScreenActivity.this, "Please");
//        webPostRequest.postGETData(WebUrl.GETCONTACTLIST, token, new WebResponse() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                Utility.hideProgress();
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//
//                Utility.hideProgress();
//
//            }
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                if (error.networkResponse.statusCode == 401) {
//
//
//                    final String pW = SessionManager.getInstance(getApplicationContext()).getKeyPassword();
//                    final String pN = SessionManager.getInstance(getApplicationContext()).getKeyPhonenumber();
//                    Map<Object, Object> params = new HashMap<Object, Object>();
//                    params.put("autoDexNum", pN);
//                    params.put("password", pW);
//                    webPostRequest.postJSONData(WebUrl.LOGIN, params, "", new WebResponse() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Utility.hideProgress();
//                            try {
//
//                                JSONObject jsonObject = response.getJSONObject("userId");
//
//                                String userId = jsonObject.getString("id");
//
//                                JSONObject jsonObject1 = response.getJSONObject("headers");
//
//                                final String token = jsonObject1.getString("Authorization");
//
//                                sessionManager.createSession(pN, pW, userId, token);
//
//                            } catch (JSONException e) {
//
//                                Log.e(TAG, "onResponse: " + e.getMessage());
//                            }
//
//                        }
//
//                        @Override
//                        public void onResponse(String response) {
//                            Utility.hideProgress();
//                        }
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Utility.hideProgress();
//                        }
//
//                        @Override
//                        public void callRequest() {
//                            Utility.hideProgress();
//                        }
//                    });
//
//
////                    Toast.makeText(HomeActivity.this, "Session Time out", Toast.LENGTH_SHORT).show();
//
//                }
//
//
//            }
//
//            @Override
//            public void callRequest() {
//
//            }
//        });
//
//
//    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        final String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {

            Map<Object, Object> params = new HashMap<>();
            params.put("deviceId", regId);
            params.put("deviceType", "android");

            String token = sessionManager.getKeyToken();
            Log.e(TAG, "displayFirebaseRegId: token " + token);
            webPostRequest.postJSONDataGetNumber(WebUrl.ADDUSERDEVICE, params, token, new WebResponse() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e(TAG, "onResponse: Device Added  " + response + "  Device ID   " + regId);
                }

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "onResponse: Device Added  " + response + "  Device ID   " + regId);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    HomeActivity.super.onErrorResponse(error);
                    Log.e(TAG, "onResponse: Device Added Error  " + error.getMessage() + "  Device ID   " + regId);
                }

                @Override
                public void callRequest() {

                }
            });

        }

//        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
//            txtRegId.setText("Firebase Reg Id is not received yet!");
    }


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        // Note: apps running on "O" devices (regardless of targetSdkVersion) may receive updates
        // less frequently than this interval when the app is no longer in the foreground.
        mLocationRequest.setInterval(UPDATE_INTERVAL);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
//        mLocationRequest.setSmallestDisplacement(10);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
    }

    private PendingIntent getPendingIntent() {
        // Note: for apps targeting API level 25 ("Nougat") or lower, either
        // PendingIntent.getService() or PendingIntent.getBroadcast() may be used when requesting
        // location updates. For apps targeting API level O, only
        // PendingIntent.getBroadcast() should be used. This is due to the limits placed on services
        // started in the background in "O".

        // TODO(developer): uncomment to use PendingIntent.getService().
//        Intent intent = new Intent(this, LocationUpdatesIntentService.class);
//        intent.setAction(LocationUpdatesIntentService.ACTION_PROCESS_UPDATES);
//        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.snackbarlayout),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(HomeActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void callUploadData() {
//        Log.e(TAG, "callUploadData: Services calllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll");
        isServiceStarted = true;
        if (isF) {
            turnGPSOn();
            Intent intent = new Intent(this, UploadAndDownloadData.class);
            startService(intent);
            isF = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.e(TAG, "onMessageEvent: ");
//        Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();

        if (event.message.equalsIgnoreCase("uploadtoserver")) {
            callFragment(R.id.syncbackup);
        } else if (event.message.equalsIgnoreCase("sysnctime")) {
//            syncbackup.setText(sharedPreferences.getString("synctime", "00:00"));

            try {
                syncbackup.setText(Util.formatToYesterdayOrToday(sharedPreferences.getString("synctime", "00:00").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
                syncbackup.setText(sharedPreferences.getString("synctime", "00:00"));
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        isServiceStarted = true;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
        isServiceStarted = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isServiceStarted = false;
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
        isServiceStarted = false;
        Log.e(TAG, "onPause: ");
//        Intent intent = new Intent(this, UploadAndDownloadData.class);
//        stopService(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MLTIPLE_PERMISSION_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (RuntimePermissionRequest.verifyPermissions(grantResults)) {

                    callUploadData();
                    requestLocationUpdates(null);
//                    Toast.makeText(this, "All multiple Permission granted", Toast.LENGTH_SHORT).show();
//                    new UploadData().execute();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
//
//                    if (!sessionManager.getKeySqlliteloading()) {
//                        GetAllContactFromServer();
//                    } else {
//                        callFragment(R.id.contact);
//                    }
                    Toast.makeText(this, "Please provide contact read permissions", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;


        }
    }

    public void requestLocationUpdates(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, getPendingIntent());

    }

    public void removeLocationUpdates(View view) {

        mFusedLocationClient.removeLocationUpdates(getPendingIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        updateNavProfile();
        Log.e(TAG, "onResume: called ");

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

    }


    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            showSettingsAlert();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Location");

        // Setting Dialog Message
        alertDialog.setMessage("Location is not enabled. Do you want enable location?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void updateNavProfile() {
        try {
            ProfileDetails profileDetails = appDatabase.daoProfileAccess().loadProfileByIds();

//        Log.e(TAG, "onCreate: image " + profileDetails.getImage());

            profileImage.setImageBitmap(UtilFunctionalitys.getBitmapdecode64(this, profileDetails.getImage()));
            navProfileName.setText(profileDetails.getFirstName() + " " + profileDetails.getLastName() + " " + profileDetails.getMiddleName());

            navProfilePhoneNumber.setText(profileDetails.getAutoDexNum());

            navProfileAddress.setText(profileDetails.getCity() + "," + profileDetails.getState());
        } catch (NullPointerException e) {
            Log.e(TAG, "updateNavProfile: " + e.getMessage());
        }

    }


    private void setFillColor(ImageView imageView) {
        imageView.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);
    }


    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }


    public void getNotificationCount() {
        String token = sessionManager.getKeyToken();
        webPostRequest.postGETData(WebUrl.NOTIFICATION_LIST, token, new WebResponse() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e(TAG, "onResponse: " + response);

            }

            @Override
            public void onResponse(String response) {
                Utility.hideProgress();
                Gson gson = new Gson();

                NotificationModel[] lNotificationModels = gson.fromJson(response, NotificationModel[].class);

                for (NotificationModel model : lNotificationModels) {
                    if (!model.getRead()) {
                        count++;
                    }
                }

//                count++;

                showNotificationCount(count);


            }

            @Override
            public void onErrorResponse(VolleyError error) {
                HomeActivity.super.onErrorResponse(error);
            }

            @Override
            public void callRequest() {
                Utility.hideProgress();
            }
        });
    }

    @Override
    public void showNotificationCount(int count) {

//        Toast.makeText(this, "" + count, Toast.LENGTH_SHORT).show();
//        notificationcount.setVisibility(View.GONE);
//        notify.setVisibility(View.GONE);
//        badgeCount.setText(count + "");
//        badgeCount.setVisibility(View.GONE);


//        try {
//            notificationcount.setVisibility(View.VISIBLE);
//            notify.setVisibility(View.VISIBLE);
//            notificationcount.setVisibility(View.VISIBLE);
//            badgeCount.setVisibility(View.VISIBLE);
//            if (count > 0) {
//                notificationcount.setText(count + "");
//                badgeCount.setText(count + "");
//                notify.setVisibility(View.VISIBLE);
//                notificationcount.setVisibility(View.VISIBLE);
//            } else if (count > 99) {
//                notificationcount.setText(count + "+");
//                badgeCount.setText(count + "+");
//                notify.setVisibility(View.VISIBLE);
//                notificationcount.setVisibility(View.VISIBLE);
//            } else if (count <= 0) {
//                notificationcount.setVisibility(View.VISIBLE);
//                notify.setVisibility(View.VISIBLE);
//                notificationcount.setText(count + "");
//                badgeCount.setText(count + "");
//                Log.e(TAG, "showNotificationCount: comming  ");
//            } else {
//                notificationcount.setVisibility(View.GONE);
//                notify.setVisibility(View.GONE);
//                notificationcount.setVisibility(View.GONE);
//                badgeCount.setVisibility(View.GONE);
//                notificationcount.setText(count + "");
//                badgeCount.setText(count + "");
//                Log.e(TAG, "showNotificationCount: comming  ");
//            }
//        } catch (NullPointerException e) {
//            Log.e(TAG, "showNotificationCount: " + e.getMessage());
//        }

        invalidateOptionsMenu();


    }


    private void initializeCountDrawer() {
//        notificationcount.setText("99+");
        //Gravity property aligns the text
//        notificationcount.setGravity(Gravity.CENTER_VERTICAL);
//        notificationcount.setTypeface(null, Typeface.BOLD);
//        notificationcount.setTextColor(getResources().getColor(R.color.white));
//        notificationcount.setText("99+");
//        notificationcount.setBackgroundResource(R.drawable.countcircle);
//        notificationcount.setWidth(20);
//        notificationcount.setHeight(20);
//        notificationcount.setGravity(Gravity.CENTER_VERTICAL);
//        notificationcount.setTypeface(null, Typeface.BOLD);
//        notificationcount.setTextColor(getResources().getColor(R.color.colorAccent));
////count is added
//        notificationcount.setText("7");

//        syncbackup.setGrFavity(Gravity.CENTER);
//        syncbackup.setTypeface(null, Typeface.BOLD);
//        syncbackup.setTextColor(getResources().getColor(R.color.black));
//        lastsync.setText("Last sync : " + sharedPreferences.getString("synctime", "00:00"));
//        syncbackup.setText(sharedPreferences.getString("synctime", "00:00"));

        try {
            syncbackup.setText(Util.formatToYesterdayOrToday(sharedPreferences.getString("synctime", "00:00").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
            syncbackup.setText(sharedPreferences.getString("synctime", "00:00"));
        }

        syncbackup.setTextSize(getResources().getDimension(R.dimen._3ssp));
        syncbackup.setGravity(Gravity.CENTER_VERTICAL);
//        syncbackup.setTypeface(null, Typeface.BOLD);
//        syncbackup.setTextColor(getResources().getColor(R.color.colorAccent));

        getNotificationCount();




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == INSERT_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "Added_Succesfully", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
//                Log.e(TAG, "onActivityResult: " + data.toString());
                Toast.makeText(getApplicationContext(), "Contacts Adding Error", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CONTACT_ADDED) {

            if (baseFragment instanceof ContactsAndFavouriteFragments) {

                ((ContactsAndFavouriteFragments) baseFragment).callDB();
            }

//            Toast.makeText(this, "Called", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        } else

        {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);

        View count1 = menu.findItem(R.id.notification).getActionView();
        notify = (RelativeLayout) count1.findViewById(R.id.notify);
        badgeCount = (TextView) count1.findViewById(R.id.hotlist_hot);


        if (count > 0) {

            notificationcount.setText(count + "");
            badgeCount.setText(count + "");

            notificationcount.setVisibility(View.VISIBLE);
            notify.setVisibility(View.VISIBLE);
            badgeCount.setVisibility(View.VISIBLE);

        } else {

            notificationcount.setVisibility(View.GONE);
            notify.setVisibility(View.GONE);
            badgeCount.setVisibility(View.GONE);
        }


        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                badgeCount.setText(String.valueOf("56"));
                callFragment(R.id.notifications);
            }
        });

//        return true;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.notification) {
//
//            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show();
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        callFragment(id);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void callFragment(int id) {
        getSupportActionBar().setTitle("AutoDex");
        switch (id) {

            case R.id.contact:
                baseFragment = new ContactsAndFavouriteFragments();
                fab.show();
//                showNotificationCount(count);
                break;

            case R.id.groups:
                baseFragment = new GroupsFragment();
                fab.hide();
//                showNotificationCount(count);
                break;

            case R.id.favorites:
                baseFragment = FavouritesFragment.getFavouritesFragment(true);
                fab.hide();
//                showNotificationCount(count);
                break;

            case R.id.notifications:

                baseFragment = new NotificationFragment();
                getSupportActionBar().setTitle("Notifications");

                fab.hide();
                count = 0;
                showNotificationCount(count);

                break;
            case R.id.syncbackup:

                baseFragment = new SyncBackupFragment();
                getSupportActionBar().setTitle("Sync Backup");
                fab.hide();
//                showNotificationCount(count);

                break;
            case R.id.settings:
                baseFragment = new SettingsFragment();
                getSupportActionBar().setTitle("Settings");
                fab.hide();
//                showNotificationCount(count);
                break;

            case R.id.logout:
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                final String regId = pref.getString("regId", null);


                String token = SessionManager.getInstance(this).getKeyToken();
                Map<Object, Object> params = new HashMap<>();
                params.put("deviceId", regId);
                Utility.showProgress(this, "Logout");
                webPostRequest.postJSONData(WebUrl.LOGOUT, params, token, new WebResponse() {
                    @Override
                    public void onResponse(JSONObject response) {
                        logout();
                        Log.e(TAG, "onResponse: " + response.toString());

                    }

                    @Override
                    public void onResponse(String response) {
                        logout();
                        Log.e(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        logout();
                        Log.e(TAG, "onErrorResponse: " + error.getMessage());
                    }

                    @Override
                    public void callRequest() {

                    }
                });


                return;
        }

        navigationView.getMenu().findItem(id).setChecked(true);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();


        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        if (id == R.id.contact) {
//            fragmentTransaction.replace(R.id.container, baseFragment).;
//        } else {
//        fragmentTransaction.replace(R.id.container, baseFragment).setReorderingAllowed(true).addToBackStack(null);
        fragmentTransaction.replace(R.id.container, baseFragment);
//        }
        fragmentTransaction.commitAllowingStateLoss();

        invalidateOptionsMenu();

//        showNotificationCount(count);

    }

    private void logout() {

        Utility.hideProgress();

        removeLocationUpdates(null);
        sharedPreferences.edit().clear();
        sharedPreferences.edit().commit();

        sessionManager.claerData();
//                ProfileDetails profileDetails = appDatabase.daoProfileAccess().loadProfileByIds();
//                appDatabase.daoProfileAccess().delete(profileDetails);
        appDatabase.daoProfileAccess().deleteAll();
        databaseHandler.deleteAllData();
        databaseHandler.deleteAllContactCategoryData();
        databaseHandler.deleteAllContactAttributeData();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
