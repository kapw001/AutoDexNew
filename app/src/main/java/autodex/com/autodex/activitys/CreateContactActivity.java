package autodex.com.autodex.activitys;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autodex.com.autodex.R;
import autodex.com.autodex.Utility;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.model.Contact;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebUrl;
import de.hdodenhof.circleimageview.CircleImageView;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

public class CreateContactActivity extends BaseActivity {

    private static final String TAG = "CreateContactActivity";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private EditText firstname, middlename, lastname, mobile, home, work, email, birthday, notes, address1, address2, city, state, zipcode;
    private FrameLayout profileimgupdate;
    private boolean edit = false;
    private Uri uri;
    private int idEDIT_CONTACT = 1;
    private String contactID;
    private Contact contact;
    private CircleImageView profile_image;
    private AdView mAdView;
    private Spinner category;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
        MobileAds.initialize(this,
                getResources().getString(R.string.appid));

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(mAdView));
        init();

        if (Build.VERSION.SDK_INT >= 21) {

            // Here, thisActivity is the current activity
            callPermission();
        } else {
            readCon();
        }


    }

    public void callPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.READ_CONTACTS)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
//            }
        } else {
            readCon();
        }
    }

    public void readCon() {
        if (getIntent().getBooleanExtra("edit", false)) {
            edit = true;
            contact = (Contact) getIntent().getSerializableExtra("contact");
            contactID = contact.getId();
            String u = getIntent().getStringExtra("contacturi");
            uri = Uri.parse(u);
            readContacts(contactID);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    readCon();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(this, "Please give permission.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void readContacts(String contactId) {

        ContentResolver cr = getContentResolver();
//        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
//                null, null, null);

        Cursor cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
                ContactsContract.Contacts._ID + " = ?", new String[]{String.valueOf(contactId)}, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur
                        .getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
                String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, contactId};
                Cursor nameCur = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
                while (nameCur.moveToNext()) {
                    String given = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                    String family = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                    String middle = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
                    String display = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                    firstname.setText(given);
                    middlename.setText(middle);
                    lastname.setText(family);
                }
                nameCur.close();


                String image_uri = cur
                        .getString(cur
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                try {
                    Uri uri = Uri.parse(image_uri);
                    profile_image.setImageURI(uri);
                } catch (NullPointerException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage());
                }

                if (Integer
                        .parseInt(cur.getString(cur
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[]{id}, null);

                    while (pCur.moveToNext()) {
                        int phoneType = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        //String isStarred 		= pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //you will get all phone numbers according to it's type as below switch case.
                        //Logs.e will print the phone number along with the name in DDMS. you can use these details where ever you want.
                        switch (phoneType) {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                mobile.setText(phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                home.setText(phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                work.setText(phoneNo);
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:

                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:

                                break;
                            default:
                                break;
                        }
                    }
                    pCur.close();

                }

                Cursor emailCur = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                + " = ?", new String[]{id}, null);
                while (emailCur.moveToNext()) {
                    String emailContact = emailCur
                            .getString(emailCur
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//                    emailType = emailCur
//                            .getString(emailCur
//                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
//                    sb.append("\nEmail:" + emailContact + "Email type:" + emailType);
//                    System.out.println("Email " + emailContact
//                            + " Email Type : " + emailType);
                    email.setText(emailContact);
//
                }
//
                emailCur.close();

// iterate through all Contact's Birthdays and print in log
                Cursor cursor = getContactsBirthdays();
                int bDayColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
                while (cursor.moveToNext()) {
                    String bDay = cursor.getString(bDayColumn);
                    Log.d(TAG, "Birthday: " + bDay);
                    birthday.setText(bDay);
                }
                cursor.close();


//                if (image_uri != null) {
//                    System.out.println(Uri.parse(image_uri));
//                    try {
//                        bitmap = MediaStore.Images.Media
//                                .getBitmap(this.getContentResolver(),
//                                        Uri.parse(image_uri));
//                        sb.append("\n Image in Bitmap:" + bitmap);
//                        System.out.println(bitmap);
//
//                    } catch (FileNotFoundException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                }

            }

            cur.close();

        } else {
            finish();
        }
    }

    private Cursor getContactsBirthdays() {
        Uri uri = ContactsContract.Data.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Event.CONTACT_ID,
                ContactsContract.CommonDataKinds.Event.START_DATE
        };

        String where =
                ContactsContract.Data.MIMETYPE + "= ? AND " +
                        ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY;
        String[] selectionArgs = new String[]{
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
        };
        String sortOrder = null;
        return managedQuery(uri, projection, where, selectionArgs, sortOrder);
    }

    private void init() {

        category = (Spinner) findViewById(R.id.category);
        profileimgupdate = (FrameLayout) findViewById(R.id.profileimgupdate);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        firstname = (EditText) findViewById(R.id.firstname);
        middlename = (EditText) findViewById(R.id.middlename);
        lastname = (EditText) findViewById(R.id.lastname);
        mobile = (EditText) findViewById(R.id.mobile);
        home = (EditText) findViewById(R.id.home);
        work = (EditText) findViewById(R.id.work);
        email = (EditText) findViewById(R.id.email);
        birthday = (EditText) findViewById(R.id.birthday);
        address1 = (EditText) findViewById(R.id.address1);
        address2 = (EditText) findViewById(R.id.address2);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        zipcode = (EditText) findViewById(R.id.zipcode);
        birthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    callDatePicker(birthday);
                }
                return true;
            }
        });
        notes = (EditText) findViewById(R.id.notes);


    }


    private void callDatePicker(EditText birthday) {
        Utility.callDatePicker(CreateContactActivity.this, birthday);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sync, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.tick) {

            saveContact();

        }

        return false;
    }


    private void saveContact() {
        String fN = firstname.getText().toString();
        String mN = middlename.getText().toString();
        String lN = lastname.getText().toString();
        String moN = mobile.getText().toString();
        String hoN = home.getText().toString();
        String woN = work.getText().toString();
        String emN = email.getText().toString();
        String birtN = birthday.getText().toString();
        String noteN = notes.getText().toString();
        String add1 = address1.getText().toString();
        String add2 = address2.getText().toString();
        String citytxt = city.getText().toString();
        String statetxt = state.getText().toString();
        String zipcodetxt = zipcode.getText().toString();

        Map<Object, Object> params = new HashMap<>();
        params.put("profileTag", category.getSelectedItem().toString().toLowerCase());
        params.put("firstName", fN);
        params.put("middleName", mN);
        params.put("lastName", lN);
        params.put("contactUserId", 0);
        params.put("favorite", false);
        params.put("dob", birtN);
        params.put("phoneNumber", moN);
        if (emN.length() > 0) {
            params.put("useEmail", true);
        } else {
            params.put("useEmail", false);
        }
        params.put("address1", add1);
        params.put("address2", add2);
        params.put("city", citytxt);
        params.put("state", statetxt);
        params.put("zip", zipcodetxt);


        Map<Object, Object> homeNumber = new HashMap<>();
        homeNumber.put("name", "home-phone");
        homeNumber.put("value", hoN);

        Map<Object, Object> workNumber = new HashMap<>();
        workNumber.put("name", "work-phone");
        workNumber.put("value", woN);

        Map<Object, Object> email = new HashMap<>();
        email.put("name", "email");
        email.put("value", emN);

        Map<Object, Object> notest = new HashMap<>();
        notest.put("name", "note");
        notest.put("value", noteN);

        List<Map<Object, Object>> contactAttributes = new ArrayList<>();

        contactAttributes.add(homeNumber);
        contactAttributes.add(workNumber);
        contactAttributes.add(email);
        contactAttributes.add(notest);
        params.put("contactAttributes", contactAttributes);


        Map<Object, Object> category1 = new HashMap<>();

        String caname = category.getSelectedItem().toString().toLowerCase();

        if (caname.toLowerCase().equalsIgnoreCase("Personal & Professional".toLowerCase())) {
            caname = "personalprofessional";
        } else if (caname.toLowerCase().equalsIgnoreCase("".toLowerCase())) {
            caname = "Uncategorized";
        }

        category1.put("category", caname);

        List<Map<Object, Object>> contactCategories = new ArrayList<>();
        contactCategories.add(category1);
        params.put("contactCategories", contactCategories);


        Log.e(TAG, "saveContact: " + new JSONObject(params).toString());

        if ((fN.length() > 0 ||
                mN.length() > 0 ||
                lN.length() > 0) &&
                moN.length() > 0
//                hoN.length() > 0 &&
//                woN.length() > 0 &&
//                emN.length() > 0 ||
//                birtN.length() > 0 ||
//                noteN.length() > 0

                )

        {



            if (Build.VERSION.SDK_INT >= 21) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                } else {
                    callReq(params);

                }
            } else {
                callReq(params);

            }

        } else {
            Toast.makeText(this, "Please enter name or mobile number", Toast.LENGTH_SHORT).show();
        }


    }


    private void callReq(Map<Object, Object> params) {
        final String token = sessionManager.getDetails().get(KEY_TOKEN);

        Utility.showProgress(CreateContactActivity.this, "Loading");

        webPostRequest.postJSONDataGetNumber(WebUrl.ADDCONTACT, params, token, new WebResponse() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse:JSONObject " + response);
                Utility.hideProgress();
            }

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: String " + response);
                Utility.hideProgress();
                Gson gson = new Gson();
                GetContactListResponse contact = gson.fromJson(response, GetContactListResponse.class);


                saveContactLocal(contact);


//                Utility.showProgress(CreateContactActivity.this, "Loading");
//                webPostRequest.postGETData(WebUrl.GETCONTACT + response, token, new WebResponse() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Utility.hideProgress();
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        Utility.hideProgress();
//
//
//                    }
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Utility.hideProgress();
//
//                        Utility.showDialog(CreateContactActivity.this, "Error", "Server error");
//                    }
//
//                    @Override
//                    public void callRequest() {
//
//                    }
//                });


            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress();
                Utility.showDialog(CreateContactActivity.this, "Error", getResources().getString(R.string.e));
            }

            @Override
            public void callRequest() {

            }
        });
    }


    private void saveContactLocal(GetContactListResponse newContact) {
        callPermission();
        if (edit) {
            updateContacts(this);
        } else {
            createContacts(this, newContact);
        }
    }


    private void createContacts(Context cntx, GetContactListResponse newContact) {
        {
            Context contetx = cntx; //Application's context or Activity's context
//            String strDisplayName = displayName; // Name of the Person to add
//            String strNumber = number; //number of the person to add with the Contact

            ArrayList<ContentProviderOperation> cntProOper = new ArrayList<ContentProviderOperation>();
            int contactIndex = cntProOper.size();//ContactSize

            //Newly Inserted contact
            // A raw contact will be inserted ContactsContract.RawContacts table in contacts database.

            cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)//Step1
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

            //Display name will be inserted in ContactsContract.Data table
            if (firstname.getText().length() > 0) {
                cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step2
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, firstname.getText().toString()) // Name of the contact
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                                firstname.getText().toString())
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME,
                                middlename.getText().toString())
                        .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                                lastname.getText().toString())
                        .build());
            }

            //Mobile number will be inserted in ContactsContract.Data table
            if (mobile.getText().length() > 0) {
                cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile.getText().toString()) // Number to be added
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
            }
            if (home.getText().length() > 0) {
                cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, home.getText().toString()) // Number to be added
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME).build()); //Type like HOME, MOBILE etc

            }
            if (work.getText().length() > 0) {
                cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, work.getText().toString()) // Number to be added
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK).build()); //Type like HOME, MOBILE etc
            }

            //Email will be inserted in ContactsContract.Data table
            if (email.getText().length() > 0) {
                cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, email.getText().toString())
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .build());
            }

            if (birthday.getText().length() > 0) {
                cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, birthday.getText().toString()) //"26-05-2015"
                        .withValue(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)
                        .build());
            }


            try {
                // We will do batch operation to insert all above data
                //Contains the output of the app of a ContentProviderOperation.
                //It is sure to have exactly one of uri or count set
                ContentProviderResult[] contentProresult = contetx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper); //apply above data insertion into contacts list

                int contactId = Integer.parseInt(contentProresult[0].uri.getLastPathSegment());

                newContact.setLocalContactID(contactId + "");

                Log.e(TAG, "writeContacts: " + contentProresult.toString() + " contactId " + contactId);


                List<GetContactListResponse> contactList = new ArrayList<GetContactListResponse>();
                contactList.add(newContact);
                databaseHandler.addData(contactList);
                for (GetContactListResponse getContactListResponse : contactList
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


                Gson gson = new Gson();

                List<GetContactListResponse> oldContactList;

                String old_list = sharedPreferences.getString("list", "");

                Type type = new TypeToken<List<GetContactListResponse>>() {
                }.getType();
                oldContactList = gson.fromJson(old_list, type);

                Log.e(TAG, "onResponse: before list count" + oldContactList.size());

                oldContactList.add(newContact);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("list", gson.toJson(oldContactList));
                editor.apply();


                Log.e(TAG, "onResponse: After list count" + oldContactList.size());


                Toast.makeText(this, "Contact Saved!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                setResult(HomeActivity.CONTACT_ADDED, intent);

                finish();

            } catch (RemoteException exp) {

                Log.e(TAG, "writeContacts: error adding contact " + exp.getMessage());
                Toast.makeText(this, "Contact not Saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(HomeActivity.CONTACT_ADDED, intent);
                finish();
                //logs;
            } catch (OperationApplicationException exp) {
                //logs
                Log.e(TAG, "writeContacts: error adding contact " + exp.getMessage());
                Toast.makeText(this, "Contact not Saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(HomeActivity.CONTACT_ADDED, intent);
                finish();
            }
        }
    }


    private void updateContacts(Context cntx) {
        {
            Context contetx = cntx; //Application's context or Activity's context
//            String strDisplayName = displayName; // Name of the Person to add
//            String strNumber = number; //number of the person to add with the Contact


            Log.e(TAG, "updateContacts:contact id " + contactID);

            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            String where = ContactsContract.Data.CONTACT_ID + " = ? AND " +
                    ContactsContract.Data.MIMETYPE + " = ? AND " +
                    String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE) + " = ? ";

            String[] params = new String[]{contactID,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                    String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)};
            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                    .withSelection(where, params)
                    .withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(contactID), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE})
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastname.getText().toString())
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME, middlename.getText().toString())
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstname.getText().toString())
                    .build());

            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                    .withSelection(where, params)
                    .withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?" + " AND " + ContactsContract.CommonDataKinds.Organization.TYPE + "=?", new String[]{String.valueOf(contactID), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)})
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile.getText().toString())
                    .build());

//            String where3 = ContactsContract.Data.CONTACT_ID + " = ? AND " +
//                    ContactsContract.Data.MIMETYPE + " = ?";
//            String[] params3 = new String[]{firstname.getText().toString(), "vnd.android.cursor.item/email_v2"};
//            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                    .withSelection(where3, params3)
//                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email.getText().toString())
//                    .build());

//            String selectPhone = ContactsContract.Data.CONTACT_ID + "=? AND " + ContactsContract.Data.MIMETYPE + "='" +
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'" + " AND " + ContactsContract.CommonDataKinds.Phone.TYPE + "=?";
//            String[] phoneArgs = new String[]{contactID, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_WORK)};
//            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                    .withSelection(selectPhone, phoneArgs)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, work.getText().toString())
//                    .build());
//
//            Cursor cursor=managedQuery(ContactsContract.Data.CONTENT_URI,)

            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                    .withSelection(where, params)
                    .withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?" + " AND " + ContactsContract.CommonDataKinds.Organization.TYPE + "=?", new String[]{String.valueOf(contactID), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)})
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, home.getText().toString())
                    .build());
            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                    .withSelection(where, params)
                    .withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?" + " AND " + ContactsContract.CommonDataKinds.Organization.TYPE + "=?", new String[]{String.valueOf(contactID), ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_WORK)})
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, work.getText().toString())
                    .build());


            String whereEmal = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            String[] emailParams = new String[]{contactID, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};

            ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                    .withSelection(whereEmal, emailParams)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email.getText().toString())
                    .build());

            String whereBirth = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            String[] birthParams = new String[]{contactID, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE};

            ops.add(android.content.ContentProviderOperation.newUpdate(android.provider.ContactsContract.Data.CONTENT_URI)
                    .withSelection(whereBirth, birthParams)
                    .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, email.getText().toString())
                    .build());

            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);


            } catch (Exception e) {
                e.printStackTrace();

            }

//            ArrayList<ContentProviderOperation> cntProOper = new ArrayList<ContentProviderOperation>();
//
//
//            String where = ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
//                    ContactsContract.Data.MIMETYPE + " = ? AND " +
//                    String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE) + " = ? ";
//
//            String[] paramsMobile = new String[]{firstname.getText().toString(),
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
//                    String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)};
//
//            cntProOper.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                    .withSelection(where, paramsMobile)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile.getText().toString())
////                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, firstname.getText().toString())
////                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, firstname.getText().toString())
////                    .withValue(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME, middlename.getText().toString())
////                    .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastname.getText().toString())
//                    .build());

//            String[] paramsHome = new String[]{firstname.getText().toString(),
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
//                    String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)};
//
//            cntProOper.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                    .withSelection(where, paramsHome)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, home.getText().toString())
//                    .build());
//
//            String[] paramsWork = new String[]{firstname.getText().toString(),
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
//                    String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_WORK)};
//
//            cntProOper.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                    .withSelection(where, paramsWork)
//                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, work.getText().toString())
//                    .build());
//
//            //email
//            String where3 = ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
//                    ContactsContract.Data.MIMETYPE + " = ?";
//            String[] params3 = new String[]{firstname.getText().toString(), "vnd.android.cursor.item/email_v2"};
//            cntProOper.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                    .withSelection(where3, params3)
//                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email.getText().toString())
//                    .build());
//
//            if (birthday.getText().length() > 0) {
//                cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
//                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
//                        .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, birthday.getText().toString()) //"26-05-2015"
//                        .withValue(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)
//                        .build());
//            }

//            //birthday
//            String whereBirthday = ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
//                    ContactsContract.Data.MIMETYPE + " = ? AND " +
//                    String.valueOf(ContactsContract.CommonDataKinds.Event.TYPE) + " = ? ";
//            String[] paramsBirthday = new String[]{firstname.getText().toString(),
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
//                    String.valueOf(ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)};
//
//            cntProOper.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//                    .withSelection(whereBirthday, paramsBirthday)
//                    .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, birthday.getText().toString())
//                    .build());


//            try {
//                // We will do batch operation to insert all above data
//                //Contains the output of the app of a ContentProviderOperation.
//                //It is sure to have exactly one of uri or count set
//                ContentProviderResult[] contentProresult = contetx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper); //apply above data insertion into contacts list
//
//                Log.e(TAG, "writeContacts: " + contentProresult.toString());
//
//                Toast.makeText(this, "Contact Updated!", Toast.LENGTH_SHORT).show();
//                finish();
//
//            } catch (RemoteException exp) {
//
//                Log.e(TAG, "writeContacts: error adding contact " + exp.getMessage());
//                Toast.makeText(this, "Contact not Updated!", Toast.LENGTH_SHORT).show();
//                finish();
//                //logs;
//            } catch (OperationApplicationException exp) {
//                //logs
//                Log.e(TAG, "writeContacts: error adding contact " + exp.getMessage());
            Toast.makeText(this, "Contact not Updated!", Toast.LENGTH_SHORT).show();
            finish();
//            }

        }
    }
}
