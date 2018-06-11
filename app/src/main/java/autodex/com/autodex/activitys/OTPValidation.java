package autodex.com.autodex.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class OTPValidation extends BaseActivity {
    private static final String TAG = "OTPValidation";
    private static final int MLTIPLE_PERMISSION_REQUEST = 1;
    private String[] permission = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WAKE_LOCK};

    private EditText otp;
    private TextView signin;
    private Button validation;
    private TextInputLayout phonenumbertxt;
    private String number;
    private boolean isSignUp = false;
    private SharedPreferences sharedPreferences;
    private boolean isChangenumber = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpvalidation);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_otpvalidation);
        sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
        number = getIntent().getStringExtra("number");
        isSignUp = getIntent().getBooleanExtra("signup", false);
        isChangenumber = getIntent().getBooleanExtra("changenumber", false);

        init();
        listener();
    }


    private void init() {
        otp = (EditText) findViewById(R.id.otp);
        validation = (Button) findViewById(R.id.validate);
        phonenumbertxt = (TextInputLayout) findViewById(R.id.phonenumbertxt);
        signin = (TextView) findViewById(R.id.signin);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MLTIPLE_PERMISSION_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (RuntimePermissionRequest.verifyPermissions(grantResults)) {
                    callRequest();

                } else {

                    Utility.showDialog(OTPValidation.this, "Permission", "Please allow contact permission to procced");

                }
                return;


        }
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
        final String pW = "";//password.getText().toString();
        final String pN = number;//phonenumber.getText().toString();

        Log.e(TAG, "onResponse: OTP Validation " + response.toString());

        try {

            JSONObject jsonObject = response.getJSONObject("userId");

            String userId = jsonObject.getString("id");

            JSONObject jsonObject1 = response.getJSONObject("headers");

            final String token = jsonObject1.getString("Authorization");

            if (isSignUp) {


                sessionManager.createSession(pN, pW, userId, token);

                Utility.hideProgress();


                Utility.showProgress(OTPValidation.this, "Loading");

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


                        Utility.showProgress(OTPValidation.this, "Loading");
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

            } else if (isChangenumber) {


                startActivity(new Intent(this, ChangeNumberActivity.class).putExtra("token", token).putExtra("userId", userId).putExtra("response", response.toString()).putExtra("number", number));
                finish();


            } else {
                startActivity(new Intent(this, ChangePasswordActivity.class).putExtra("token", token).putExtra("userId", userId).putExtra("response", response.toString()).putExtra("number", number));
                finish();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Utility.hideProgress();


    }

    @Override
    public void onResponse(String response) {
        super.onResponse(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        Log.e(TAG, "onErrorResponse: " + error.getMessage());
        Utility.showDialog(OTPValidation.this, "Error", getResources().getString(R.string.e));

    }

    @Override
    public void callRequest() {
        super.callRequest();


        final String pW = "";//password.getText().toString();
        final String pN = "";//phonenumber.getText().toString();


        if (isChangenumber) {

            Map<Object, Object> params = new HashMap<>();
            params.put("autoDexNum", sessionManager.getKeyPhonenumber());
            params.put("otp", otp.getText().toString());
            String token = sessionManager.getKeyToken();

            webPostRequest.postJSONData(WebUrl.VALIDATEOTP, params, token, this);

//            Toast.makeText(this, "Called", Toast.LENGTH_SHORT).show();


        } else {

            Map<Object, Object> params = new HashMap<>();
            params.put("autoDexNum", number);
            params.put("otp", otp.getText().toString());

            webPostRequest.postJSONData(WebUrl.VALIDATEOTP, params, "", this);
        }
    }


    private void listener() {

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);
//                onBackPressed();
            }
        });

        validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (validate(otp.getText().toString())) {

                    if (RuntimePermissionRequest.checkMultiplePermission(OTPValidation.this, permission)) {

                        RuntimePermissionRequest.requestPermissionMultiple(OTPValidation.this, permission, MLTIPLE_PERMISSION_REQUEST, "Permission", "Please allow contacts permission to proceed ");
                    } else {
                        callRequest();
                    }


                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);

    }

    private boolean validate(String pnumber) {

        phonenumbertxt.setErrorEnabled(false);
        phonenumbertxt.setError(null);
        if (!Utility.emptyValdate(pnumber)) {
            phonenumbertxt.setErrorEnabled(true);
            phonenumbertxt.setError("Enter a OTP.");
            return false;
        } else {
            return true;
        }

    }
}
