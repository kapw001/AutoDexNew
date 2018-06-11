package autodex.com.autodex.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.error.VolleyError;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autodex.com.autodex.R;
import autodex.com.autodex.Utility;
import autodex.com.autodex.interfacecallback.CameraAndGalleryCallBack;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebUrl;
import de.hdodenhof.circleimageview.CircleImageView;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

//53347e31-3980-4ced-9220-245ee93e82a9

public class UpdateContactActivity extends BaseActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private static final String TAG = "CreateContactActivity";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private EditText firstname, middlename, lastname, mobile, home, work, email, birthday, notes, address1, address2, city, state, zipcode;
    private FrameLayout profileimgupdate;
    private boolean edit = false;
    private Uri uri;
    private int idEDIT_CONTACT = 1;
    private String contactID;
    private GetContactListResponse contact;
    private CircleImageView profile_image;
    private Spinner category;
    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpBackEnabled();
        sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);

        init();

        if (Build.VERSION.SDK_INT >= 21) {

            // Here, thisActivity is the current activity
            callPermission();
        } else {
            readCon();
        }


        profileimgupdate.setOnClickListener(v -> selectImage());


        List<GetContactListResponse> list1 = databaseHandler.getAllDatas(String.valueOf(contact.getId()));

        Utility.setSpinnerToValue(category, list1.get(0).getProfileTag());


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(mAdView));

    }

    private void selectImage() {

        Utility.callCameraAndGallery(this, cameraAndGalleryCallBack);
    }


    private CameraAndGalleryCallBack cameraAndGalleryCallBack = new CameraAndGalleryCallBack() {
        @Override
        public void callCamera(String s) {
            userChoosenTask = s;
            cameraIntent();
        }

        @Override
        public void callGallery(String s) {
            userChoosenTask = s;
            galleryIntent();
        }
    };

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                try {
                    onSelectFromGalleryResult(data);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            else if (requestCode == REQUEST_CAMERA)
                try {
                    onCaptureImageResult(data);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void onCaptureImageResult(Intent data) throws URISyntaxException {


        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profile_image.setImageBitmap(thumbnail);

        Uri tempUri = getImageUri(getApplicationContext(), thumbnail);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File finalFile = new File(getRealPathFromURI(tempUri));

        imageUpload(thumbnail, getRealPathFromURI(tempUri));

        Log.e(TAG, "onCaptureImageResult: " + getRealPathFromURI(tempUri));
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) throws URISyntaxException {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        profile_image.setImageBitmap(bm);

        Uri tempUri = getImageUri(getApplicationContext(), bm);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File finalFile = new File(getRealPathFromURI(tempUri));

        imageUpload(bm, getRealPathFromURI(tempUri));

        Log.e(TAG, "onSelectFromGalleryResult: " + getRealPathFromURI(tempUri));
    }


    private void imageUpload(Bitmap bitmap, final String imagePath) {

        Utility.showProgress(UpdateContactActivity.this, "Uploading");

        String token = sessionManager.getDetails().get(KEY_TOKEN);
        webPostRequest.fileUpload(WebUrl.CONTACTPROFILEIMAGEUPLOAD + "/" + contact.getId(), imagePath, token, new WebResponse() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: File Upload " + response);
                Utility.hideProgress();

            }

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: File Upload " + response);
                Utility.hideProgress();

            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onResponse: File Upload " + error.getMessage());
                Utility.hideProgress();
            }

            @Override
            public void callRequest() {

            }
        });


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
            contact = (GetContactListResponse) getIntent().getSerializableExtra("contact");

            String contactId = contact.getLocalContactID();

            List<GetContactListResponse> getContactListResponse = databaseHandler.getAllDatas(String.valueOf(contact.getId()));
            GetContactListResponse getContactListResponse1 = getContactListResponse.get(0);
            getContactListResponse1.setLocalContactID(contactId);
            contact = getContactListResponse1;


            Log.e(TAG, "readCon: uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu " + contact.getLocalContactID());

            showDataIntoEditText(contact);
        }
    }


    private void showDataIntoEditText(GetContactListResponse contact) {

        firstname.setText(contact.getFirstName());
        middlename.setText(contact.getMiddleName());
        lastname.setText(contact.getLastName());
        mobile.setText(contact.getPhoneNumber());
        birthday.setText(contact.getDob());
        address1.setText(contact.getAddress1());
        address2.setText(contact.getAddress2());
        city.setText(contact.getCity());
        state.setText(contact.getState());
        zipcode.setText(contact.getZip());

        List<ContactCategory> list1 = databaseHandler.getAllContactCategoryDatas(String.valueOf(contact.getId()));
        List<ContactAttribute> list = databaseHandler.getAllContactAttributeDatas(String.valueOf(contact.getId()));


        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ContactAttribute contactAttribute = list.get(i);

                try {
                    if (contactAttribute.getName().toLowerCase().contains("home-phone")) {
                        home.setText(contactAttribute.getValue());
                    }
                } catch (NullPointerException e) {

                }
                try {
                    if (contactAttribute.getName().toLowerCase().contains("work-phone")) {
                        work.setText(contactAttribute.getValue());

                    }
                } catch (NullPointerException e) {

                }

                try {
                    if (contactAttribute.getName().toLowerCase().contains("email")) {
                        email.setText(contactAttribute.getValue());
                    }
                } catch (NullPointerException e) {

                }

                try {
                    if (contactAttribute.getName().toLowerCase().contains("note")) {
                        notes.setText(contactAttribute.getValue());
                    }
                } catch (NullPointerException e) {

                }
            }
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
        birthday.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                callDatePicker(birthday);
            }
            return true;
        });
        notes = (EditText) findViewById(R.id.notes);


    }


    private void callDatePicker(EditText birthday) {
        Utility.callDatePicker(UpdateContactActivity.this, birthday);
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

            updateContact();

        }

        return false;
    }


    private void updateContact() {
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
        params.put("id", contact.getId());
        params.put("firstName", fN);
        params.put("middleName", mN);
        params.put("lastName", lN);
        params.put("contactUserId", 0);
        params.put("favorite", false);
        params.put("dob", birtN);
        params.put("favorite", contact.getFavorite());
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

        List<ContactAttribute> contactAttributeList = databaseHandler.getAllContactAttributeDatas(String.valueOf(contact.getId()));

        int home_phone_Id = 0;
        int work_phone_Id = 0;
        int email_phone_Id = 0;
        int note_phone_Id = 0;

        if (contactAttributeList != null && contactAttributeList.size() > 0) {
            for (int i = 0; i < contactAttributeList.size(); i++) {
                ContactAttribute contactAttribute = contactAttributeList.get(i);

                try {
                    if (contactAttribute.getName().toLowerCase().contains("home-phone")) {
                        home_phone_Id = contactAttribute.getId();
                    }
                } catch (NullPointerException e) {

                }
                try {
                    if (contactAttribute.getName().toLowerCase().contains("work-phone")) {
                        work_phone_Id = contactAttribute.getId();

                    }
                } catch (NullPointerException e) {

                }

                try {
                    if (contactAttribute.getName().toLowerCase().contains("email")) {
                        email_phone_Id = contactAttribute.getId();
                    }
                } catch (NullPointerException e) {

                }

                try {
                    if (contactAttribute.getName().toLowerCase().contains("note")) {
                        note_phone_Id = contactAttribute.getId();
                    }
                } catch (NullPointerException e) {

                }
            }
        }

        Map<Object, Object> homeNumber = new HashMap<>();
        homeNumber.put("id", home_phone_Id);
        homeNumber.put("name", "home-phone");
        homeNumber.put("value", hoN);

        Map<Object, Object> workNumber = new HashMap<>();
        workNumber.put("id", work_phone_Id);
        workNumber.put("name", "work-phone");
        workNumber.put("value", woN);

        Map<Object, Object> email = new HashMap<>();
        email.put("id", email_phone_Id);
        email.put("name", "email");
        email.put("value", emN);

        Map<Object, Object> notest = new HashMap<>();
        notest.put("id", note_phone_Id);
        notest.put("name", "note");
        notest.put("value", noteN);

        List<Map<Object, Object>> contactAttributes = new ArrayList<>();

        contactAttributes.add(homeNumber);
        contactAttributes.add(workNumber);
        contactAttributes.add(email);
        contactAttributes.add(notest);
        params.put("contactAttributes", contactAttributes);


        List<ContactCategory> contactCategory = databaseHandler.getAllContactCategoryDatas(String.valueOf(contact.getId()));
        Map<Object, Object> category1 = new HashMap<>();
        if (contactCategory.size() > 0) {
            int contactCategoryId = contactCategory.get(0).getId();
            category1.put("id", contactCategoryId);
        }

        String caname = category.getSelectedItem().toString().toLowerCase();

        if (caname.toLowerCase().equalsIgnoreCase("Personal & Professional".toLowerCase())) {
            caname = "personalprofessional";
        } else if (caname.toLowerCase().equalsIgnoreCase("".toLowerCase())) {
            caname = "Uncategorized";
        }

        category1.put("category", caname);

        params.put("profileTag", category.getSelectedItem().toString().toLowerCase());

        List<Map<Object, Object>> contactCategories = new ArrayList<>();
        contactCategories.add(category1);
        params.put("contactCategories", contactCategories);


        Log.e(TAG, "Update contact : " + new JSONObject(params).toString());

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

        Utility.showProgress(UpdateContactActivity.this, "Loading");

        webPostRequest.postJSONPutDataGetNumber(WebUrl.ADDCONTACT, params, token, new WebResponse() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse:JSONObject " + response);
                Utility.hideProgress();
                Toast.makeText(UpdateContactActivity.this, "Response " + response, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: String " + response);
                Utility.hideProgress();

                Gson gson = new Gson();

                Log.e(TAG, "onResponse: res  " + response.toString());


                contactID = contact.getLocalContactID();

                GetContactListResponse contact = gson.fromJson(response.toString(), GetContactListResponse.class);
                contact.setLocalContactID(contactID);
                List<GetContactListResponse> contactList = new ArrayList<GetContactListResponse>();
                contactList.add(contact);

//                contactList.get(0).setLocalContactID(contact.getLocalContactID());
//                        databaseHandler.deleteAllDataRow(String.valueOf(contactList.get(0).getId()));
                databaseHandler.deleteAllContactAttributeDataRow(String.valueOf(contactList.get(0).getId()));
                databaseHandler.deleteAllContactCategoryDataRow(String.valueOf(contactList.get(0).getId()));
                databaseHandler.updateData(contactList.get(0));

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


                saveContactLocal();

//                Toast.makeText(UpdateContactActivity.this, "Response " + response, Toast.LENGTH_SHORT).show();
//
//                Utility.showProgress(UpdateContactActivity.this, "Loading");
//                webPostRequest.postGETData(WebUrl.GETCONTACT + contact.getId(), token, new WebResponse() {
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
//                        Log.e(TAG, "onErrorResponse: " + error.getMessage());
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

                Utility.showDialog(UpdateContactActivity.this, "Error", "Server error");

            }

            @Override
            public void callRequest() {

            }
        });
    }


    private void saveContactLocal() {
//        callPermission();
        updateContacts(this);

    }


    private void updateContacts(Context cntx) {
        {
            Context contetx = cntx; //Application's context or Activity's context
//            String strDisplayName = displayName; // Name of the Person to add
//            String strNumber = number; //number of the person to add with the Contact

            contactID = contact.getLocalContactID();

            Log.e(TAG, "updateContacts: con  id up date " + contactID);


            List<GetContactListResponse> oldContactList;

            String old_list = sharedPreferences.getString("list", "");

            Type type = new TypeToken<List<GetContactListResponse>>() {
            }.getType();
            oldContactList = gson.fromJson(old_list, type);

            for (int i = 0; i < oldContactList.size(); i++) {
                GetContactListResponse getContactListResponse = oldContactList.get(i);
                if (getContactListResponse.getLocalContactID().equalsIgnoreCase(contactID)) {

                    oldContactList.get(i).setFirstName(firstname.getText().toString());
                    oldContactList.get(i).setLastName(lastname.getText().toString());
                    oldContactList.get(i).setMiddleName(middlename.getText().toString());
                    oldContactList.get(i).setPhoneNumber(mobile.getText().toString());

                    Log.e(TAG, "updateContacts:  Updated list in json " + gson.toJson(oldContactList.get(i)));

                }
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("list", gson.toJson(oldContactList));
            editor.apply();


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

            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(whereEmal, emailParams)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email.getText().toString())
                    .build());

            String whereBirth = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            String[] birthParams = new String[]{contactID, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE};

            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(whereBirth, birthParams)
                    .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, email.getText().toString())
                    .build());

            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                Intent intent = new Intent();
                setResult(ContactDetailsActivity.EDIT_DATA, intent);

//                Toast.makeText(this, "Contact Updated!", Toast.LENGTH_SHORT).show();
                finish();

            } catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(this, "Contact not Updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(ContactDetailsActivity.EDIT_DATA, intent);
                finish();

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

//            }

        }
    }
}
