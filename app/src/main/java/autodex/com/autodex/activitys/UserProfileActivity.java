package autodex.com.autodex.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import autodex.com.autodex.R;
import autodex.com.autodex.UtilFunctionalitys;
import autodex.com.autodex.Utility;
import autodex.com.autodex.customview.UserCustomView;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.model.webresponse.ProfileDetails;
import autodex.com.autodex.webrequest.WebUrl;
import de.hdodenhof.circleimageview.CircleImageView;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

public class UserProfileActivity extends BaseActivity implements View.OnClickListener {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private static final String TAG = "UserProfileActivity";
    private EditText profileNmae, email;
    private TextView profilename;
    private RadioGroup radioGroup;
    private RadioButton male, female;
    private EditText dob;
    private PopupWindow pw;
    private CircleImageView profile_image;
    private FrameLayout profileimgupdate;
    private LinearLayout profileUpdateLayout;

    private ProfileDetails profileDetails;

    private boolean is_editable = true;
    private ImageView genderprivacyiconchange, dobprivacyiconchange, emailprivacyiconchange, phonenumberprivacyiconchange, usernameprivacyiconchange;

    private UserCustomView firstnamecustomview, middlenamecustomview, lastnamecustomview, phonecustomview, emailcustomview, dobcustomview,
            address1customview, address2customview, citycustomview, statecustomview, zipcodecustomview;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        MobileAds.initialize(this,
                getResources().getString(R.string.appid));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        MobileAds.initialize(this,
                getResources().getString(R.string.appid));
        profileUpdateLayout = (LinearLayout) findViewById(R.id.profileUpdateLayout);

//        profileDetails = (ProfileDetails) getIntent().getSerializableExtra("profileDetails");

        profileDetails = appDatabase.daoProfileAccess().loadProfileByIds();

        init();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(mAdView));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }


    private void init() {
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        profilename = (TextView) findViewById(R.id.profilename);
        firstnamecustomview = (UserCustomView) findViewById(R.id.firstnamecustomview);
        middlenamecustomview = (UserCustomView) findViewById(R.id.middlenamecustomview);
        lastnamecustomview = (UserCustomView) findViewById(R.id.lastnamecustomview);
        phonecustomview = (UserCustomView) findViewById(R.id.phonecustomview);
        emailcustomview = (UserCustomView) findViewById(R.id.emailcustomview);
        dobcustomview = (UserCustomView) findViewById(R.id.dobcustomview);
        address1customview = (UserCustomView) findViewById(R.id.address1customview);
        address2customview = (UserCustomView) findViewById(R.id.address2customview);
        statecustomview = (UserCustomView) findViewById(R.id.statecustomview);
        zipcodecustomview = (UserCustomView) findViewById(R.id.zipcodecustomview);
        citycustomview = (UserCustomView) findViewById(R.id.citycustomview);


        profile_image.setImageBitmap(UtilFunctionalitys.getBitmapdecode64(this, profileDetails.getImage()));
        profilename.setText(profileDetails.getFirstName() + " " + profileDetails.getLastName());

        firstnamecustomview.setText(profileDetails.getFirstName());
        middlenamecustomview.setText(profileDetails.getMiddleName());
        lastnamecustomview.setText(profileDetails.getLastName());
        phonecustomview.setText(profileDetails.getAutoDexNum());
        emailcustomview.setText(profileDetails.getPersonalEmail());
        dobcustomview.setText(profileDetails.getDob());
        address1customview.setText(profileDetails.getAddress1());
        address2customview.setText(profileDetails.getAddress2());
        citycustomview.setText(profileDetails.getCity());
        statecustomview.setText(profileDetails.getState());
        zipcodecustomview.setText(profileDetails.getZip());


//        profileNmae = (EditText) findViewById(R.id.username);
//        email = (EditText) findViewById(R.id.email);
//        dob = (EditText) findViewById(R.id.dob);
        profileimgupdate = (FrameLayout) findViewById(R.id.profileimgupdate);
        profileimgupdate.setOnClickListener(this);
//
//        usernameprivacyiconchange = (ImageView) findViewById(R.id.usernameprivacyiconchange);
//        phonenumberprivacyiconchange = (ImageView) findViewById(R.id.phonenumberprivacyiconchange);
//        emailprivacyiconchange = (ImageView) findViewById(R.id.emailprivacyiconchange);
//        dobprivacyiconchange = (ImageView) findViewById(R.id.dobprivacyiconchange);
//        genderprivacyiconchange = (ImageView) findViewById(R.id.genderprivacyiconchange);
//
//
//        male = (RadioButton) findViewById(R.id.male);
//        female = (RadioButton) findViewById(R.id.female);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.profileimgupdate:
                selectImage();
                Toast.makeText(this, "Profile image update  ", Toast.LENGTH_SHORT).show();

                break;
        }

    }


    private void selectImage() {

//        if (Utility.checkPermission(this)) {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(UserProfileActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
//        }
    }

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
                    if (data != null)
                        onSelectFromGalleryResult(data);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            else if (requestCode == REQUEST_CAMERA)
                try {
                    if (data != null)
                        onCaptureImageResult(data);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
        }
    }

    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
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

        Utility.showProgress(UserProfileActivity.this, "Uploading");

        final ProfileDetails profileDetails = appDatabase.daoProfileAccess().loadProfileByIds();
        profileDetails.setImage(UtilFunctionalitys.encodeTobase64(bitmap));

        String token = sessionManager.getDetails().get(KEY_TOKEN);
        webPostRequest.fileUpload(WebUrl.PROFILEIMAGEUPLOAD, imagePath, token, new WebResponse() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: File Upload " + response);
                Utility.hideProgress();
                appDatabase.daoProfileAccess().update(profileDetails);
            }

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: File Upload " + response);
                Utility.hideProgress();
                appDatabase.daoProfileAccess().update(profileDetails);
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


    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);

    }

    @Override
    public void onResponse(String response) {
        super.onResponse(response);

        Log.e(TAG, "onResponse: " + response);

        if (Boolean.parseBoolean(response)) {

            ProfileDetails profileDetails = appDatabase.daoProfileAccess().loadProfileByIds();
            profileDetails.setFirstName(firstnamecustomview.getName());
            profileDetails.setLastName(lastnamecustomview.getName());
            profileDetails.setMiddleName(middlenamecustomview.getName());
            profileDetails.setDob(dobcustomview.getName());
            profileDetails.setPersonalEmail(emailcustomview.getName());
            profileDetails.setAutoDexNum(phonecustomview.getName());
            profileDetails.setAddress1(address1customview.getName());
            profileDetails.setAddress2(address2customview.getName());
            profileDetails.setCity(citycustomview.getName());
            profileDetails.setState(statecustomview.getName());
            profileDetails.setZip(zipcodecustomview.getName());

            appDatabase.daoProfileAccess().update(profileDetails);

            Log.e(TAG, "onResponse: " + appDatabase.daoProfileAccess().loadProfileByIds().toString());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        Log.e(TAG, "onErrorResponse: " + error.getMessage());
        Utility.showDialog(UserProfileActivity.this, "Error", getResources().getString(R.string.e));

    }

    @Override
    public void callRequest() {
        super.callRequest();

        Map<Object, Object> params = new HashMap<Object, Object>();
        params.put("id", profileDetails.getId());
        params.put("firstName", firstnamecustomview.getName());
        params.put("middleName", middlenamecustomview.getName());
        params.put("lastName", lastnamecustomview.getName());
        params.put("dob", dobcustomview.getName());
        params.put("personalEmail", emailcustomview.getName());
        params.put("address1", address1customview.getName());
        params.put("address2", address2customview.getName());
        params.put("city", citycustomview.getName());
        params.put("state", statecustomview.getName());
        params.put("zip", zipcodecustomview.getName());
        params.put("autoDexNum", phonecustomview.getName());
        params.put("autoDexNumTag", 0);

        String token = sessionManager.getDetails().get(KEY_TOKEN);

        webPostRequest.postJSONPutDataGetNumber(WebUrl.UPDATEPROFILE, params, token, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.userprofilemenu, menu);

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
        } else if (id == R.id.edit) {

            if (is_editable) {
                item.setIcon(getResources().getDrawable(R.drawable.ic_check_black_24dp));
                enable();
                is_editable = false;


            } else {
                callRequest();
                item.setIcon(getResources().getDrawable(R.drawable.ic_edit_black_24dp));
                disable();
                is_editable = true;

            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void enable() {

        int count = profileUpdateLayout.getChildCount();
        Log.e(TAG, "enable: " + count);


        for (int i = 0; i < count; i++) {


            if (!((profileUpdateLayout.getChildAt(i)).findViewById(R.id.phonecustomview) == phonecustomview))
                ((UserCustomView) profileUpdateLayout.getChildAt(i)).enabelEditText();

        }

    }

    private void disable() {


        int count = profileUpdateLayout.getChildCount();

        Log.e(TAG, "enable: " + count);


        for (int i = 0; i < count; i++) {

            ((UserCustomView) profileUpdateLayout.getChildAt(i)).disableEditText();

            Log.e(TAG, "disable: " + ((UserCustomView) profileUpdateLayout.getChildAt(i)).getCategory());

        }

    }


}
