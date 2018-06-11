package autodex.com.autodex.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import autodex.com.autodex.MessageEvent;
import autodex.com.autodex.R;
import autodex.com.autodex.UtilFunctionalitys;
import autodex.com.autodex.Utility;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebUrl;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

public class ContactDetailsActivity extends BaseActivity {

    private static final String TAG = "ContactDetailsActivity";
    public static final int EDIT_DATA = 1;
    private boolean isFromFav = false;
    private TextView mobile;
    private ImageView profileImg, phoneCall, favorite, msg;
    private Uri uri;
    private int idEDIT_CONTACT = 1;
    private String contactID;
    private Map<String, String> details = new LinkedHashMap<>();
    private GetContactListResponse contact;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_contact_details);
        MobileAds.initialize(this,
                getResources().getString(R.string.appid));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        profileImg = (ImageView) findViewById(R.id.profile_img);
        phoneCall = (ImageView) findViewById(R.id.phonecall);
        favorite = (ImageView) findViewById(R.id.favorite);
        mobile = (TextView) findViewById(R.id.mobile);
        msg = (ImageView) findViewById(R.id.msg);

        contact = (GetContactListResponse) getIntent().getSerializableExtra("contact");


        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile.getText().length() > 0) {
                    Uri number = Uri.parse("tel:" + mobile.getText());
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    startActivity(callIntent);
                } else {
                    Toast.makeText(ContactDetailsActivity.this, "There is no dial number available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
//
        showDetails();
        show();


        isFromFav = getIntent().getBooleanExtra("isFromFav", false);

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + contact.getPhoneNumber()));
//                sendIntent.setData(Uri.parse("sms:"));
                startActivity(sendIntent);
            }
        });


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GetContactListResponse> getContactListResponse = databaseHandler.getAllDatas(String.valueOf(contact.getId()));


                if (getContactListResponse.get(0).getFavorite()) {
                    getContactListResponse.get(0).setFavorite(false);
                } else {
//                    getContactListResponse1.setFavorite(true);
                    getContactListResponse.get(0).setFavorite(true);
                }

                databaseHandler.updateData(getContactListResponse.get(0));
//                databaseHandler.updateDataFavourite(true,getContactListResponse.get(0).getId());
//                databaseHandler.deleteAllDataRow(String.valueOf(contact.getId()));
//                databaseHandler.addData(getContactListResponse);
                List<GetContactListResponse> getContactListResponse2 = databaseHandler.getAllDatas(String.valueOf(contact.getId()));
                Log.e(TAG, "onClick: f " + getContactListResponse2.get(0).getFavorite());
                showDetails();

                final GetContactListResponse res = getContactListResponse.get(0);
                List<ContactAttribute> CAList = databaseHandler.getAllContactAttributeDatas(String.valueOf(res.getId()));
                List<ContactCategory> CCList = databaseHandler.getAllContactCategoryDatas(String.valueOf(res.getId()));
                res.setContactAttributes(CAList);
                res.setContactCategories(CCList);
                Utility.showProgress(ContactDetailsActivity.this, "Loading");
                Gson gson = new Gson();
                String jsonObject = gson.toJson(res);
                try {
                    JSONObject jsonObject1 = new JSONObject(jsonObject);
                    final String token = sessionManager.getDetails().get(KEY_TOKEN);
                    webPostRequest.postJSONPutDataGetNumberStringJson(WebUrl.ADDCONTACT, jsonObject1, token, new WebResponse() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utility.hideProgress();
                            Log.e(TAG, "onResponse: fave updated  " + response);

                            if (isFromFav) {
                                finish();
                            }
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "onResponse: fave updated  " + response);
                            Utility.hideProgress();
                            if (isFromFav) {
                                finish();
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onResponse: fave updated  " + error.getMessage());
                            Utility.hideProgress();
                            if (isFromFav) {
                                finish();
                            }
                        }

                        @Override
                        public void callRequest() {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "onClick: JSON  " + gson.toJson(res).toString());


            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(mAdView));

//        Toast.makeText(this, "Adview", Toast.LENGTH_SHORT).show();

    }

    private void showDetails() {
        Gson gson = new Gson();


        List<GetContactListResponse> getContactListResponse = databaseHandler.getAllDatas(String.valueOf(contact.getId()));

//        List<ContactAttribute> list = databaseHandler.getAllContactAttributeDatas(String.valueOf(contact.getId()));
//        List<ContactCategory> list1 = databaseHandler.getAllContactCategoryDatas(String.valueOf(contact.getId()));

        GetContactListResponse getContactListResponse1 = getContactListResponse.get(0);

        getSupportActionBar().setTitle(getContactListResponse1.getFirstName() + " " + getContactListResponse1.getMiddleName() + " " + getContactListResponse1.getLastName());

        mobile.setText(getContactListResponse1.getPhoneNumber());


        if (getContactListResponse1.getFavorite()) {
            favorite.setColorFilter(ContextCompat.getColor(this, R.color.red));
        } else {
            favorite.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
        }


        Log.e(TAG, "showDetails: favourite " + getContactListResponse1.getFavorite());
//        String jsonInString = gson.toJson(getContactListResponse1);
//        String jsonInString1 = gson.toJson(list);
//        String jsonInString2 = gson.toJson(list1);
//
//
//        Log.e(TAG, "onCreate: " + jsonInString);
//        Log.e(TAG, "onCreate: " + jsonInString1);
//        Log.e(TAG, "onCreate: " + jsonInString2);
//
//        Toast.makeText(this, "" + getContactListResponse1.getFirstName(), Toast.LENGTH_SHORT).show();
    }


    private void show() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.contactdetailsmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        } else if (item.getItemId() == R.id.edit) {


            Intent intent = new Intent(this, UpdateContactActivity.class);
            intent.putExtra("contact", (Serializable) contact);
            intent.putExtra("edit", true);
            startActivityForResult(intent, EDIT_DATA);


//            Intent intent = new Intent(Intent.ACTION_EDIT, uri);
//            intent.putExtra("finishActivityOnSaveCompleted", true);
////            startActivity(intent);
//
//            startActivityForResult(intent, idEDIT_CONTACT);
        } else if (item.getItemId() == R.id.share) {
            UtilFunctionalitys.share(this, "", contact.getFullName() + ": " + contact.getPhoneNumber());
        } else if (item.getItemId() == R.id.delete) {
            Utility.showProgress(ContactDetailsActivity.this, "Loading");
            String token = sessionManager.getDetails().get(SessionManager.KEY_TOKEN);
            webPostRequest.postDelete(WebUrl.ADDCONTACT + "/" + contact.getId(), token, new WebResponse() {
                @Override
                public void onResponse(JSONObject response) {
                    Utility.hideProgress();
                    Log.e(TAG, "onResponse: delete id " + response);
                    deleteLocalContact();
                }

                @Override
                public void onResponse(String response) {
                    Utility.hideProgress();
                    Log.e(TAG, "onResponse: delete id " + response);
                    deleteLocalContact();

                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Utility.hideProgress();
                    Utility.showDialog(ContactDetailsActivity.this, "Error", "Server Error");
                    Log.e(TAG, "onResponse: delete id " + error.getMessage());

                }

                @Override
                public void callRequest() {

                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteLocalContact() {
        databaseHandler.deleteAllDataRow(String.valueOf(contact.getId()));
        databaseHandler.deleteAllContactAttributeDataRow(String.valueOf(contact.getId()));
        databaseHandler.deleteAllContactCategoryDataRow(String.valueOf(contact.getId()));


        try {
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contact.getLocalContactID());
            int deleted = getContentResolver().delete(uri, null, null);
            Log.e(TAG, "deleteLocalContact: " + deleted);
        } catch (UnsupportedOperationException e) {

        }


        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_DATA) {

            EventBus.getDefault().post(new MessageEvent("Contact updated"));

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }


}
