package autodex.com.autodex.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import com.android.volley.error.VolleyError;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import autodex.com.autodex.R;
import autodex.com.autodex.Utility;
import autodex.com.autodex.database.AppDatabase;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebPostRequest;
import autodex.com.autodex.webrequest.WebUrl;
import volleywebrequest.com.mysqlitelibrary.DatabaseHandler;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

/**
 * Created by yasar on 7/9/17.
 */

public class BaseActivity extends AppCompatActivity implements WebResponse {

    private static final String TAG = "BaseActivity";

    public WebPostRequest webPostRequest;
    public SessionManager sessionManager;
    public DatabaseHandler databaseHandler;
    public AppDatabase appDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webPostRequest = WebPostRequest.getInstance(this);
        sessionManager = SessionManager.getInstance(this);
        databaseHandler = DatabaseHandler.getInstace(this);
        appDatabase = AppDatabase.getAppDatabase(this);
        MobileAds.initialize(this,
                getResources().getString(R.string.appid));
    }

    public void setUpBackEnabled() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.ic_logo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }
    }

    public void showT(String msg) {
        Utility.showMsg(getApplicationContext(), msg);
    }


    public void transitionActivity() {
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);
    }


    @Override
    public void onResponse(JSONObject response) {
        Utility.hideProgress();

    }

    @Override
    public void onResponse(String response) {
        Utility.hideProgress();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Utility.hideProgress();

        if (error.networkResponse.statusCode == 401) {
            reLogin();
        }


    }


    @Override
    public void callRequest() {
        Utility.showProgress(BaseActivity.this, "Loading");
    }


    public void reLogin() {

        checkSessionTimeout();
    }

    private void checkSessionTimeout() {


//        Toast.makeText(this, "CCCCCCCCCCCCCCCCCCC", Toast.LENGTH_SHORT).show();

        Utility.showProgress(this, "Loading");

        final String token = sessionManager.getDetails().get(KEY_TOKEN);

        final String pW = SessionManager.getInstance(getApplicationContext()).getKeyPassword();
        final String pN = SessionManager.getInstance(getApplicationContext()).getKeyPhonenumber();
        Map<Object, Object> params = new HashMap<Object, Object>();
        params.put("autoDexNum", pN);
        params.put("password", pW);
        webPostRequest.postJSONData(WebUrl.LOGIN, params, "", new WebResponse() {
            @Override
            public void onResponse(JSONObject response) {
                Utility.hideProgress();
                try {

                    JSONObject jsonObject = response.getJSONObject("userId");

                    String userId = jsonObject.getString("id");

                    JSONObject jsonObject1 = response.getJSONObject("headers");

                    final String token = jsonObject1.getString("Authorization");

                    sessionManager.createSession(pN, pW, userId, token);

                } catch (JSONException e) {

                    Log.e(TAG, "onResponse: " + e.getMessage());
                }

            }

            @Override
            public void onResponse(String response) {
                Utility.hideProgress();
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress();
            }

            @Override
            public void callRequest() {
                Utility.hideProgress();
            }
        });


    }


    public static class AdListener extends com.google.android.gms.ads.AdListener {

        private View mAdView;

        public AdListener(View view) {
            this.mAdView = view;
        }

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
    }

}
