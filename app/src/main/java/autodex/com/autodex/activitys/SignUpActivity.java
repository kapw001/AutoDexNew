package autodex.com.autodex.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.android.volley.misc.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import autodex.com.autodex.R;
import autodex.com.autodex.RuntimePermissionRequest;
import autodex.com.autodex.Utility;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.webrequest.WebPostRequest;
import autodex.com.autodex.webrequest.WebUrl;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

/**
 * Created by yasar on 7/9/17.
 */

public class SignUpActivity extends BaseActivity {

    private static final String TAG = "SignUpActivity";
    private static final int MLTIPLE_PERMISSION_REQUEST = 1;
    private String[] permission = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS};
    private EditText name, phonenumber, lastname, password, confirmpassword, middlename;
    private TextView signin, termsTxt;
    private Button signup;
    private CheckBox terms;
    private TextInputLayout phonenumbertxt, nametxt, lastnametxt, passwordtxt, confirmpasswordtxt, middlenametxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signupnew);
        init();
        listener();

        if (RuntimePermissionRequest.checkMultiplePermission(this, permission)) {
            RuntimePermissionRequest.requestPermissionMultiple(this, permission, MLTIPLE_PERMISSION_REQUEST, "Permission", "Please allow sms read permission to procced ");
        } else {
            setSIMNumber();
        }


    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        Log.e(TAG, "onResume: " + isTaskRoot());
//
//        if (!isTaskRoot()) {
//            onBackPressed();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MLTIPLE_PERMISSION_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (RuntimePermissionRequest.verifyPermissions(grantResults)) {

//                    Toast.makeText(this, "All multiple Permission granted", Toast.LENGTH_SHORT).show();
                    setSIMNumber();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    Toast.makeText(this, "Please allow sms read permission to procced", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;


        }
    }

    private void init() {
        terms = (CheckBox) findViewById(R.id.terms);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        name = (EditText) findViewById(R.id.name);
        lastname = (EditText) findViewById(R.id.lastname);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        middlename = (EditText) findViewById(R.id.middlename);
        phonenumbertxt = (TextInputLayout) findViewById(R.id.phonenumbertxt);
        nametxt = (TextInputLayout) findViewById(R.id.nametxt);
        lastnametxt = (TextInputLayout) findViewById(R.id.lastnametxt);
        passwordtxt = (TextInputLayout) findViewById(R.id.passwordtxt);
        confirmpasswordtxt = (TextInputLayout) findViewById(R.id.confirmpasswordtxt);
        middlenametxt = (TextInputLayout) findViewById(R.id.middletxt);
        signup = (Button) findViewById(R.id.signup);
        signin = (TextView) findViewById(R.id.signin);
        termsTxt = (TextView) findViewById(R.id.termsTxt);

        SpannableString SpanString = new SpannableString(
                "I agree with all terms & conditions");

        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

//                Toast.makeText(SignUpActivity.this, "Ok", Toast.LENGTH_SHORT).show();

            }
        };

        SpanString.setSpan(teremsAndCondition, 17, SpanString.length(), 0);
        SpanString.setSpan(new ForegroundColorSpan(Color.BLUE), 17, SpanString.length(), 0);
        SpanString.setSpan(new UnderlineSpan(), 17, SpanString.length(), 0);
        termsTxt.setMovementMethod(LinkMovementMethod.getInstance());
        termsTxt.setText(SpanString, TextView.BufferType.SPANNABLE);
        termsTxt.setSelected(true);

    }

    private void setSIMNumber() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        phonenumber.setText(mPhoneNumber);
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

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate(phonenumber.getText().toString(), name.getText().toString(), middlename.getText().toString(), lastname.getText().toString(), password.getText().toString(), confirmpassword.getText().toString())) {
                    callRequest();
                }
            }
        });


    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);
        Log.e(TAG, "onResponse: JSONObject " + response.toString());
//        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//        startActivity(intent);
//        finish();

    }

    @Override
    public void onResponse(String response) {
        super.onResponse(response);
        Log.e(TAG, "onResponse: String " + response.toString());

//        Toast.makeText(this, "Registered successfully...,\nPlease login", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), OTPValidation.class);
        intent.putExtra("signup", true);
        intent.putExtra("number", phonenumber.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        Log.e(TAG, "onErrorResponse: " + error.getMessage());

        if (error.networkResponse.statusCode == 400) {
            Utility.showDialog(SignUpActivity.this, "Error", "This number is already existed.");
        } else {
            Utility.showDialog(SignUpActivity.this, "Error", getResources().getString(R.string.e));
        }


    }


    @Override
    public void callRequest() {
        super.callRequest();

        Map<Object, Object> params = new HashMap<Object, Object>();
        params.put("autoDexNum", phonenumber.getText().toString());
        params.put("firstName", name.getText().toString());
        params.put("lastName", lastname.getText().toString());
        params.put("middleName", "");
        params.put("password", password.getText().toString());
        params.put("address1", "");
        params.put("state", "");
        params.put("zip", "");
        params.put("dob", "");

        String token = sessionManager.getDetails().get(KEY_TOKEN);

        webPostRequest.postJSONDataGetNumber1(WebUrl.CREATEPROFILE, params, token, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);

    }

    private boolean validate(String pnumber, String n, String middleName, String lastName, String password, String confirmpassword) {

//        phonenumbertxt.setError(null);
//        nametxt.setError(null);
//        lastnametxt.setError(null);
//        passwordtxt.setError(null);
//        phonenumbertxt.setErrorEnabled(false);
//        nametxt.setErrorEnabled(false);
//        lastnametxt.setErrorEnabled(false);
//        passwordtxt.setErrorEnabled(false);
        if (!Utility.emptyValdate(n)) {
//            nametxt.setError("First Name cannot be empty.");
//            nametxt.setErrorEnabled(true);
            showToast("First Name cannot be empty.");
            return false;
        }
        if (!Utility.emptyValdate(middleName)) {
//            nametxt.setError("First Name cannot be empty.");
//            nametxt.setErrorEnabled(true);
            showToast("Middle Name cannot be empty.");
            return false;
        } else if (!Utility.emptyValdate(lastName)) {
//            lastnametxt.setError("Last Name cannot be empty.");
//            lastnametxt.setErrorEnabled(true);
            showToast("Last Name cannot be empty.");
            return false;
        } else if (!Utility.emptyValdate(pnumber)) {
//            phonenumbertxt.setError("Enter a valid mobile number.");
//            phonenumbertxt.setErrorEnabled(true);
            showToast("Enter a valid mobile number.");
            return false;
        } else if (!Utility.emptyValdate(password)) {
//            passwordtxt.setError("Password cannot be empty.");
//            passwordtxt.setErrorEnabled(true);
            showToast("Password cannot be empty.");
            return false;
        } else if (!Utility.emptyValdate(confirmpassword)) {
//            passwordtxt.setError("Password cannot be empty.");
//            passwordtxt.setErrorEnabled(true);
            showToast("Password cannot be empty.");
            return false;
        } else if (!password.equalsIgnoreCase(confirmpassword)) {
//            passwordtxt.setError("Password cannot be empty.");
//            passwordtxt.setErrorEnabled(true);
            showToast("Password is mismatch.");
            return false;
        } else if (!terms.isChecked()) {
            showToast("Please accept terms and conditions");
            return false;
        } else {
            return true;
        }

    }

    private void showError(String msg) {
        Utility.showDialog(SignUpActivity.this, "Error", msg);
    }

    private void showToast(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }
}
