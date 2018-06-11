package autodex.com.autodex.activitys;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import autodex.com.autodex.R;
import autodex.com.autodex.RuntimePermissionRequest;
import autodex.com.autodex.Util;
import autodex.com.autodex.UtilFunctionalitys;
import autodex.com.autodex.Utility;
import autodex.com.autodex.customview.BadgeDrawable;
import autodex.com.autodex.fragments.BaseFragment;
import autodex.com.autodex.fragments.ContactsAndFavouriteFragments;
import autodex.com.autodex.fragments.FavouritesFragment;
import autodex.com.autodex.fragments.GroupsFragment;
import autodex.com.autodex.fragments.NotificationFragment;
import autodex.com.autodex.fragments.SettingsFragment;
import autodex.com.autodex.fragments.SyncBackupFragment;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.locationservice.YourWakefulReceiver;
import autodex.com.autodex.model.webresponse.ProfileDetails;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebUrl;
import de.hdodenhof.circleimageview.CircleImageView;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

public class HomeActivityBackup extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HomeActivity";

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

    private View profiledetails;

    private int INSERT_CONTACT_REQUEST = 2;

    private boolean isF = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);

//        new AsyncTask() {
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                Document doc = null;
//                try {
//                    doc = Jsoup.connect("https://edtest.pappaya.co.uk/videos/").get();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Elements newsHeadlines = doc.select("a");
//
//                for (Element metaElem : newsHeadlines) {
//                    String name = metaElem.attr("name");
//                    String content = metaElem.attr("content");
//                    Log.e(TAG, "onCreate: Jsoup " + name + "      " + content + "   " + metaElem.html());
//                }
//
//                return null;
//            }
//        }.execute();

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.ic_logo);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                final Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
//                startActivity(intent);
//                Intent intent = new Intent(Intent.ACTION_INSERT);
//                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
//                intent.putExtra("finishActivityOnSaveCompleted", true);
//                startActivityForResult(intent, INSERT_CONTACT_REQUEST);
                startActivityForResult(new Intent(getApplicationContext(), CreateContactActivity.class), CONTACT_ADDED);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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
//        callFragment(R.id.contact);

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
                ProfileDetails profileDetails = appDatabase.daoProfileAccess().loadProfileByIds();
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class).putExtra("profileDetails", profileDetails));
//                transitionActivity();
            }
        });

//        updateNavProfile();

//        if (isF) {
//            isF = false;
//            if (!sharedPreferences.getBoolean("sync", false)) {

//            } else {
//                callFragment(R.id.contact);
//            }
//        }


        if (RuntimePermissionRequest.checkMultiplePermission(this, permission)) {
            RuntimePermissionRequest.requestPermissionMultiple(this, permission, MLTIPLE_PERMISSION_REQUEST, "Permission", "Need read contacts permission to get contacts ");
        } else {
            new UploadData().execute();
        }

        callLocation();

        Util.scheduleJob(getApplicationContext());
    }


    private void callLocation() {

        Calendar calander = Calendar.getInstance();
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(HomeActivityBackup.this, YourWakefulReceiver.class);
        boolean flag = (PendingIntent.getBroadcast(HomeActivityBackup.this, 0,
                intent, PendingIntent.FLAG_NO_CREATE) == null);
/*Register alarm if not registered already*/
        if (flag) {
            PendingIntent alarmIntent = PendingIntent.getBroadcast(HomeActivityBackup.this, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

// Create Calendar obj called calendar

/* Setting alarm for every one hour from the current time.*/
//            int intervalTimeMillis = 1000 * 60 * 60; // 1 hour
            int intervalTimeMillis = 1000 * 60; // 1 hour
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                    calander.getTimeInMillis(), intervalTimeMillis,
                    alarmIntent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MLTIPLE_PERMISSION_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (RuntimePermissionRequest.verifyPermissions(grantResults)) {

//                    Toast.makeText(this, "All multiple Permission granted", Toast.LENGTH_SHORT).show();
                    new UploadData().execute();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    if (!sessionManager.getKeySqlliteloading()) {
                        GetAllContactFromServer();
                    } else {
                        callFragment(R.id.contact);
                    }
                    Toast.makeText(this, "Please provide multiple permissions", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateNavProfile();
        Log.e(TAG, "onResume: called ");

    }


    private void showSyncDialogUploadToServer(final List<GetContactListResponse> list, final List<GetContactListResponse> jsonSaveList) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(HomeActivityBackup.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(HomeActivityBackup.this);
        }
        builder.setTitle("Sync contact")
                .setMessage("Are you sure you want to sync contacts?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        final String token = sessionManager.getDetails().get(KEY_TOKEN);
                        final Gson gson = new Gson();
                        Utility.showProgress(HomeActivityBackup.this, "Loding");
                        try {
                            webPostRequest.postJSONPutDataGetNumberStringJsonArray(WebUrl.SAVEALLCONTACTS, new JSONArray(gson.toJson(list)), token, new WebResponse() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Utility.hideProgress();
                                    Log.e(TAG, "onResponse: " + response);
                                    callFragment(R.id.contact);
                                }

                                @Override
                                public void onResponse(String response) {
                                    Utility.hideProgress();
                                    Log.e(TAG, "onResponse: " + response);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("sync", true);
                                    editor.putString("list", gson.toJson(jsonSaveList));
                                    editor.commit();

                                    GetAllContactFromServer();

                                }

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Utility.hideProgress();
                                    Log.e(TAG, "onResponse: " + error.getMessage());
                                    callFragment(R.id.contact);
                                }

                                @Override
                                public void callRequest() {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();

                        if (!sessionManager.getKeySqlliteloading()) {
                            GetAllContactFromServer();
                        } else {
                            callFragment(R.id.contact);
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void GetAllContactFromServer() {
        final String token = sessionManager.getDetails().get(KEY_TOKEN);
        Utility.showProgress(HomeActivityBackup.this, "Loading");
        webPostRequest.postGETData(WebUrl.GETCONTACTLIST, token, new WebResponse() {
            @Override
            public void onResponse(JSONObject response) {

                Utility.hideProgress();
                Log.e(TAG, "onResponse: " + response);

            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Utility.hideProgress();
                Log.e(TAG, "onResponse: res  " + response.toString());

                GetContactListResponse[] list = gson.fromJson(response.toString(), GetContactListResponse[].class);

                List<GetContactListResponse> l = Arrays.asList(list);

                for (GetContactListResponse getContactListResponse : l
                        ) {

                    List<ContactAttribute> cAList = getContactListResponse.getContactAttributes();
                    List<ContactCategory> cCList = getContactListResponse.getContactCategories();

                    String forignId = String.valueOf(getContactListResponse.getId());

                    for (int i = 0; i < cAList.size(); i++) {
                        cAList.get(i).setForignID(forignId);
                    }

                    for (int i = 0; i < cCList.size(); i++) {
                        cCList.get(i).setForignID(forignId);
                    }


                    databaseHandler.addContactAttributeData(cAList);
                    databaseHandler.addContactCategoryData(cCList);

                }
//
//        databaseHandler.deleteAllData();
                databaseHandler.addData(l);
                sessionManager.setKeySqlliteloading(true);

                callFragment(R.id.contact);

            }

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "onErrorResponse: server error" + error.getMessage());
                callFragment(R.id.contact);
            }

            @Override
            public void callRequest() {

            }
        });
    }


    class UploadData extends AsyncTask<Void, Void, List<GetContactListResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(HomeActivityBackup.this, "Loading");
        }

        @Override
        protected List<GetContactListResponse> doInBackground(Void... params) {
            List<GetContactListResponse> mapList = Util.readContacts(getApplicationContext());
            return mapList;
        }

        @Override
        protected void onPostExecute(List<GetContactListResponse> maps) {
//            super.onPostExecute(maps);
            Utility.hideProgress();

            try {
                updateLocalContactToServer(maps);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Log.e(TAG, "onPostExecute: " + maps.toString());

        }
    }
//
//    public static void d(String TAG, String message) {
//        int maxLogSize = 2000;
//        for(int i = 0; i <= message.length() / maxLogSize; i++) {
//            int start = i * maxLogSize;
//            int end = (i+1) * maxLogSize;
//            end = end > message.length() ? message.length() : end;
//            android.util.Log.e(TAG, message.substring(start, end));
//        }
//    }


    private void updateLocalContactToServer(List<GetContactListResponse> mapList) throws JSONException {
        Gson gson = new Gson();
        if (sharedPreferences.getString("list", "").length() <= 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("list", gson.toJson(mapList));
            editor.commit();


            showSyncDialogUploadToServer(mapList, mapList);
        } else {

            List<GetContactListResponse> productFromShared = new ArrayList<>();

            String jsonPreferences = sharedPreferences.getString("list", "");

            Type type = new TypeToken<List<GetContactListResponse>>() {
            }.getType();
            productFromShared = gson.fromJson(jsonPreferences, type);
            List<GetContactListResponse> jsList = new ArrayList<>();
            jsList.clear();
            jsList.addAll(mapList);

            List<GetContactListResponse> l = mapList;

            l.removeAll(productFromShared);

            if (l.size() > 0) {
                showSyncDialogUploadToServer(l, jsList);
            } else {

                if (!sharedPreferences.getBoolean("sync", false)) {
                    showSyncDialogUploadToServer(jsList, jsList);
                    Log.e(TAG, "updateLocalContactToServer: called  " + jsList.size());
                } else {
                    if (!sessionManager.getKeySqlliteloading()) {
                        GetAllContactFromServer();
                    } else {
                        callFragment(R.id.contact);
                    }
                }
            }

            Log.e(TAG, "updateLocalContactToServer: " + l.size() + "  " + productFromShared.size() + " " + mapList.size());

        }

    }


    private void updateNavProfile() {
        ProfileDetails profileDetails = appDatabase.daoProfileAccess().loadProfileByIds();

//        Log.e(TAG, "onCreate: image " + profileDetails.getImage());

        profileImage.setImageBitmap(UtilFunctionalitys.getBitmapdecode64(this, profileDetails.getImage()));
        navProfileName.setText(profileDetails.getFirstName() + " " + profileDetails.getLastName() + " " + profileDetails.getMiddleName());

        navProfilePhoneNumber.setText(profileDetails.getAutoDexNum());

        navProfileAddress.setText(profileDetails.getCity() + "," + profileDetails.getState());
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


    private void initializeCountDrawer() {
        notificationcount.setText("99+");
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

        syncbackup.setGravity(Gravity.CENTER_VERTICAL);
//        syncbackup.setTypeface(null, Typeface.BOLD);
//        syncbackup.setTextColor(getResources().getColor(R.color.black));
        syncbackup.setText("12 Aug 2017");
        syncbackup.setTextSize(8f);
//        syncbackup.setGravity(Gravity.CENTER_VERTICAL);
//        syncbackup.setTypeface(null, Typeface.BOLD);
//        syncbackup.setTextColor(getResources().getColor(R.color.colorAccent));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == INSERT_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {

                Log.e(TAG, "onActivityResult: " + data.toString());


                Uri uri = Uri.parse(data.getData().toString());

                Log.e(TAG, "onActivityResult: uri " + uri.toString());

                Cursor cur = getContentResolver().query(uri, null,
                        null, null, null);


//        Toast.makeText(this, "Cur " + cur.getCount(), Toast.LENGTH_SHORT).show();
                ContentResolver cr = getContentResolver();
                String phone = null;
                String emailContact = null;
                String emailType = null;
                String image_uri = "";
                Bitmap bitmap = null;
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur
                                .getString(cur
                                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (Integer
                                .parseInt(cur.getString(cur
                                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            System.out.println("name : " + name + ", ID : " + id);

                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = ?", new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                phone = pCur
                                        .getString(pCur
                                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            }
                            pCur.close();

                            Log.e(TAG, "onActivityResult: Name " + name + "  ID " + id + "  Number " + phone);
                        }
                    }
                }


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

        View count = menu.findItem(R.id.notification).getActionView();
        notify = (RelativeLayout) count.findViewById(R.id.notify);
        badgeCount = (TextView) count.findViewById(R.id.hotlist_hot);


        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                badgeCount.setText(String.valueOf("56"));
                callFragment(R.id.notifications);
            }
        });

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
                break;

            case R.id.groups:
                baseFragment = new GroupsFragment();
                fab.hide();
                break;

            case R.id.favorites:
                baseFragment = new FavouritesFragment();
                fab.hide();
                break;

            case R.id.notifications:
                baseFragment = new NotificationFragment();
                getSupportActionBar().setTitle("Notifications");

                fab.hide();

                break;
            case R.id.syncbackup:

                baseFragment = new SyncBackupFragment();
                getSupportActionBar().setTitle("Sync Backup");
                fab.hide();

                break;
            case R.id.settings:
                baseFragment = new SettingsFragment();
                getSupportActionBar().setTitle("Settings");
                fab.hide();
                break;

            case R.id.logout:

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

                return;
        }

        navigationView.getMenu().findItem(id).setChecked(true);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        if (id == R.id.contact) {
//            fragmentTransaction.replace(R.id.container, baseFragment).;
//        } else {
        fragmentTransaction.replace(R.id.container, baseFragment).addToBackStack(null);
//        }
        fragmentTransaction.commit();


    }


}
