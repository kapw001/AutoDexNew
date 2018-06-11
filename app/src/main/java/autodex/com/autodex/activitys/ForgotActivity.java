package autodex.com.autodex.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import autodex.com.autodex.webrequest.WebPostRequest;
import autodex.com.autodex.webrequest.WebUrl;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_PHONENUMBER;

/**
 * Created by yasar on 7/9/17.
 */

public class ForgotActivity extends BaseActivity {
    private static final int MLTIPLE_PERMISSION_REQUEST = 1;
    private String[] permission = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WAKE_LOCK};

    private static final String TAG = "ForgotActivity";
    private EditText phonenumber;
    private TextView signin;
    private Button forgotpassword;
    private TextInputLayout phonenumbertxt;
    private SharedPreferences sharedPreferences;
    private RelativeLayout loginlayout;

    private boolean isChangeNumber = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgotpassword);
        sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
        loginlayout = (RelativeLayout) findViewById(R.id.rlayout);
        init();
        listener();
//        callActivity();

        if (getIntent() != null) {

            isChangeNumber = getIntent().getBooleanExtra("changenumber", false);
        }
    }

    private void callActivity() {
        if (sessionManager.isLoggedIn() && !sharedPreferences.getBoolean("sync", false) && !sessionManager.getKeySqlliteloading()) {
            Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
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
                    callLogin();

                } else {

                    Utility.showDialog(ForgotActivity.this, "Permission", "Please provide contact read permission");

                }
                return;


        }
    }


    private void init() {
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        forgotpassword = (Button) findViewById(R.id.forgotpassword);
        phonenumbertxt = (TextInputLayout) findViewById(R.id.phonenumbertxt);
        signin = (TextView) findViewById(R.id.signin);
    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);

        Log.e(TAG, "onResponse: Json " + response.toString());

    }

    @Override
    public void onResponse(String response) {
        super.onResponse(response);

        Log.e(TAG, "onResponse: string " + response);

        startActivity(new Intent(this, OTPValidation.class).putExtra("number", phonenumber.getText().toString()).putExtra("changenumber", isChangeNumber));
        finish();

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        Log.e(TAG, "onErrorResponse: " + error.getMessage());
        Utility.showDialog(ForgotActivity.this, "Error", getResources().getString(R.string.e));

    }

    @Override
    public void callRequest() {
        super.callRequest();


        if (isChangeNumber) {

            String token = sessionManager.getKeyToken();

            webPostRequest.postGETDataOTP(WebUrl.GENERATEOTP + "/" + phonenumber.getText().toString(), token, this);

        } else {
            webPostRequest.postGETDataOTP(WebUrl.GENERATEOTP + "/" + phonenumber.getText().toString(), "", this);
        }

    }


    private void listener() {

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);
                onBackPressed();
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (RuntimePermissionRequest.checkMultiplePermission(ForgotActivity.this, permission)) {

                    RuntimePermissionRequest.requestPermissionMultiple(ForgotActivity.this, permission, MLTIPLE_PERMISSION_REQUEST, "Permission", "Need read contacts permission to get contacts ");
                } else {
                    callLogin();
                }


//                callLogin();
            }
        });


    }

    private void callLogin() {

//                Toast.makeText(LoginActivity.this, "Sign In", Toast.LENGTH_SHORT).show();
        if (validate(phonenumber.getText().toString())) {

            callRequest();
//                    showT("Success");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        startActivity(new Intent(this, LoginActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);
//        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);

    }

    private boolean validate(String pnumber) {

        phonenumbertxt.setErrorEnabled(false);
        phonenumbertxt.setError(null);
        if (!Utility.emptyValdate(pnumber)) {
            phonenumbertxt.setErrorEnabled(true);
            phonenumbertxt.setError("Enter a valid mobile number.");
            return false;
        } else {
            return true;
        }

    }
}
