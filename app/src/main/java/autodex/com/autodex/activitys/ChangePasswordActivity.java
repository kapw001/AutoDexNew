package autodex.com.autodex.activitys;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePasswordActivity extends BaseActivity {
    private static final String TAG = "ChangePasswordActivity";
    private EditText newpassword;
    private EditText confirmpassword;
    private Button changepassword;
    private TextInputLayout newpasswordtxt, confirmpasswordtxt;
    private String token, responsetxt, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_change_password);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_password);

        token = getIntent().getStringExtra("token");
        responsetxt = getIntent().getStringExtra("response");
        number = getIntent().getStringExtra("number");

        init();
        listener();
    }

    private void init() {
        newpassword = (EditText) findViewById(R.id.newpassword);
        confirmpassword = (EditText) findViewById(R.id.confrimpassword);
        changepassword = (Button) findViewById(R.id.changepassword);
        newpasswordtxt = (TextInputLayout) findViewById(R.id.newpasswordtxt);
        confirmpasswordtxt = (TextInputLayout) findViewById(R.id.confrimpasswordtxt);

    }

    @Override
    public void onResponse(JSONObject response) {
        super.onResponse(response);

        Log.e(TAG, "onResponse: Json " + response.toString());

    }

    @Override
    public void onResponse(String response1) {
        super.onResponse(response1);

        Log.e(TAG, "onResponse: string " + response1);


        try {
            JSONObject response = new JSONObject(responsetxt);

            JSONObject jsonObject = response.getJSONObject("userId");

            String userId = jsonObject.getString("id");

            JSONObject jsonObject1 = response.getJSONObject("headers");

            final String token = jsonObject1.getString("Authorization");

            sessionManager.createSession(number, "", userId, token);

            Utility.hideProgress();


            Utility.showProgress(ChangePasswordActivity.this, "Loading");

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


                    Utility.showProgress(ChangePasswordActivity.this, "Loading");
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


        } catch (JSONException e) {
            e.printStackTrace();
        }


        Toast.makeText(this, "Password has been changed successfully...", Toast.LENGTH_SHORT).show();

//        startActivity(new Intent(this, LoginActivity.class));
//        finish();

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        Log.e(TAG, "onErrorResponse: " + error.getMessage());
        Utility.showDialog(ChangePasswordActivity.this, "Error", "Server error");

    }

    @Override
    public void callRequest() {
        super.callRequest();

        Map<Object, Object> params = new HashMap<>();
        params.put("password", newpassword.getText().toString());

        webPostRequest.postJSONPutDataGetNumber(WebUrl.CHANGEPASSWORD, params, token, this);
    }


    private void listener() {


        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate(newpassword.getText().toString(), confirmpassword.getText().toString())) {
                    callRequest();
                }

            }
        });


    }

    private boolean validate(String pnumber, String conPassword) {

        newpasswordtxt.setErrorEnabled(false);
        newpasswordtxt.setError(null);
        confirmpasswordtxt.setErrorEnabled(false);
        confirmpasswordtxt.setError(null);
        if (!Utility.emptyValdate(pnumber)) {
            newpasswordtxt.setErrorEnabled(true);
            newpasswordtxt.setError("Enter a new password.");
            return false;
        } else if (!Utility.emptyValdate(conPassword)) {
            confirmpasswordtxt.setErrorEnabled(true);
            confirmpasswordtxt.setError("Enter a confirm password.");
            return false;
        } else if (!pnumber.equalsIgnoreCase(conPassword)) {
            Toast.makeText(this, "Password is mismatch", Toast.LENGTH_SHORT).show();

            return false;

        } else {
            return true;
        }

    }
}
