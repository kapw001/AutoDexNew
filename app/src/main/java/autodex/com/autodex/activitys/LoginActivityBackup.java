package autodex.com.autodex.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import autodex.com.autodex.R;
import autodex.com.autodex.RuntimePermissionRequest;
import autodex.com.autodex.Utility;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.model.webresponse.ProfileDetails;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebUrl;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_PHONENUMBER;

/**
 * Created by yasar on 7/9/17.
 */

public class LoginActivityBackup extends BaseActivity {
    private static final int MLTIPLE_PERMISSION_REQUEST = 1;
    private String[] permission = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WAKE_LOCK};

    private EditText phonenumber, password;
    private TextView signup, forgotPassword;
    private Button signin;
    private TextInputLayout phonenumbertxt, passwordtxt;
    private ImageView testimg;
    private SharedPreferences sharedPreferences;

    private static final String TAG = "LoginActivity";
    private RelativeLayout loginlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signin);
        sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
        loginlayout = (RelativeLayout) findViewById(R.id.loginlayout);
        init();
        listener();
        loginlayout.setVisibility(View.GONE);
//        if (RuntimePermissionRequest.checkMultiplePermission(this, permission)) {
//            loginlayout.setVisibility(View.GONE);
//            RuntimePermissionRequest.requestPermissionMultiple(this, permission, MLTIPLE_PERMISSION_REQUEST, "Permission", "Need read contacts permission to get contacts ");
//        } else {
//            callActivity();
//        }
        callActivity();

    }

    private void callActivity() {
        if (sessionManager.isLoggedIn() && !sharedPreferences.getBoolean("sync", false) && !sessionManager.getKeySqlliteloading()) {

            Intent intent = new Intent(getApplicationContext(), InitScreenActivity.class);
            startActivity(intent);
            finish();
        } else if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();

        } else {
            loginlayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MLTIPLE_PERMISSION_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (RuntimePermissionRequest.verifyPermissions(grantResults)) {
                    callActivity();
//                    loginlayout.setVisibility(View.VISIBLE);

                } else {

                    callActivity();
//                    loginlayout.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Please provide multiple permissions", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;


        }
    }

    private void init() {
        testimg = (ImageView) findViewById(R.id.testimg);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        password = (EditText) findViewById(R.id.password);
        phonenumbertxt = (TextInputLayout) findViewById(R.id.phonenumbertxt);
        passwordtxt = (TextInputLayout) findViewById(R.id.passwordtxt);

        signup = (TextView) findViewById(R.id.signup);
        forgotPassword = (TextView) findViewById(R.id.forgotpassword);

        signin = (Button) findViewById(R.id.signin);
    }


    private void listener() {

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(LoginActivity.this, "Sign In", Toast.LENGTH_SHORT).show();
                if (validate(phonenumber.getText().toString(), password.getText().toString())) {
                    callRequest();
//                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                startActivity(intent);
//                finish();
//                transitionActivity();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
//                transitionActivity();


            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivity(intent);
//                transitionActivity();
            }
        });

    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
        final String pW = password.getText().toString();
        final String pN = phonenumber.getText().toString();

        Log.e(TAG, "onResponse: " + response.toString());

        try {

            JSONObject jsonObject = response.getJSONObject("userId");

            String userId = jsonObject.getString("id");

            JSONObject jsonObject1 = response.getJSONObject("headers");

            final String token = jsonObject1.getString("Authorization");

            sessionManager.createSession(pN, pW, userId, token);

            Utility.hideProgress();


            Utility.showProgress(LoginActivityBackup.this, "Loading");

//            String token = sessionManager.getDetails().get(KEY_TOKEN);
            final String phonenumber = sessionManager.getDetails().get(KEY_PHONENUMBER);
            Log.e(TAG, "callRequest: " + token);
            webPostRequest.postGETData(WebUrl.GETPROFILEBYPHONENUMBER + phonenumber, token, new WebResponse() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.e(TAG, "onResponse:JSONObject " + response);
                    Utility.hideProgress();

                }

                @Override
                public void onResponse(String response) {
                    Utility.hideProgress();
                    Log.e(TAG, "onResponse:Str " + response);
                    Gson gson = new Gson();

                    ProfileDetails profileDetails = gson.fromJson(response.toString(), ProfileDetails.class);
                    appDatabase.daoProfileAccess().deleteAll();
                    appDatabase.daoProfileAccess().insert(profileDetails);


                    Utility.showProgress(LoginActivityBackup.this, "Loading");
                    webPostRequest.postGETData(WebUrl.GETPROFILEIMAGE, token, new WebResponse() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utility.hideProgress();

                        }

                        @Override
                        public void onResponse(String response) {
                            Utility.hideProgress();

                            ProfileDetails profileDetails1 = appDatabase.daoProfileAccess().loadProfileByIds();
                            profileDetails1.setImage(response);
                            appDatabase.daoProfileAccess().update(profileDetails1);

                            if (sessionManager.isLoggedIn()) {
                                Intent intent = new Intent(getApplicationContext(), InitScreenActivity.class);
                                startActivity(intent);
                                finish();

                            }

                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Utility.hideProgress();
                            if (sessionManager.isLoggedIn()) {
                                Intent intent = new Intent(getApplicationContext(), InitScreenActivity.class);
                                startActivity(intent);
                                finish();

                            }
//                            Utility.showDialog(LoginActivity.this, "Error", "Server error");

                        }

                        @Override
                        public void callRequest() {

                        }
                    });


                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Utility.hideProgress();
//                    Utility.showDialog(LoginActivity.this, "Error", "Server error");
                    if (sessionManager.isLoggedIn()) {
                        Intent intent = new Intent(getApplicationContext(), InitScreenActivity.class);
                        startActivity(intent);
                        finish();

                    }

                }

                @Override
                public void callRequest() {

                }
            });

//            if (sessionManager.isLoggedIn()) {
//                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                startActivity(intent);
//                finish();
//
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response) {
        super.onResponse(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        Log.e(TAG, "onErrorResponse: " + error.getMessage());
        Utility.showDialog(LoginActivityBackup.this, "Error", "Enter a valid credentials");

    }

    @Override
    public void callRequest() {
        super.callRequest();

        final String pW = password.getText().toString();
        final String pN = phonenumber.getText().toString();
        Map<Object, Object> params = new HashMap<Object, Object>();
        params.put("autoDexNum", pN);
        params.put("password", pW);
        webPostRequest.postJSONData(WebUrl.LOGIN, params, "", this);
    }

    private boolean validate(String pnumber, String pword) {

        phonenumbertxt.setError(null);
        passwordtxt.setError(null);
        if (!Utility.emptyValdate(pnumber)) {
            phonenumbertxt.setError("Enter a valid mobile number.");
            return false;
        } else if (!Utility.emptyValdate(pword)) {
            passwordtxt.setError("Password cannot be empty.");
            return false;
        } else {
            return true;
        }

    }

    public static void setErrorMsg(String msg, EditText viewId) {
        //Osama ibrahim 10/5/2013
        int ecolor = Color.BLACK; // whatever color you want
        String estring = msg;
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
        ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
        viewId.setError(ssbuilder);

    }
}
