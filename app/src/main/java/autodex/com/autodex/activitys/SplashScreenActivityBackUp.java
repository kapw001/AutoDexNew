package autodex.com.autodex.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.error.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import autodex.com.autodex.NetworkHelper;
import autodex.com.autodex.R;
import autodex.com.autodex.Utility;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebUrl;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivityBackUp extends BaseActivity {

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        setContentView(R.layout.activity_splash_screen);


//        SessionManager.getInstance(this).changeToken("sdfsdfsdfsd");


        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (sessionManager.isLoggedIn()) {

                            if (NetworkHelper.isNetworkAvailable(getApplicationContext())) {


//                Toast.makeText(this, "Called", Toast.LENGTH_SHORT).show();

                                final String token = sessionManager.getDetails().get(KEY_TOKEN);
//        Utility.showProgressContactInit(InitScreenActivity.this, "Please");
                                webPostRequest.postGETData(WebUrl.NOTIFICATION_LIST, token, new WebResponse() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        callActivity();


                                    }

                                    @Override
                                    public void onResponse(String response) {

                                        callActivity();

                                    }

                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                        if (error.networkResponse.statusCode == 401) {

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
                                                        callActivity();

                                                    } catch (JSONException e) {

                                                        Log.e("splash", "onResponse: " + e.getMessage());
                                                        callActivity();
                                                    }

                                                }

                                                @Override
                                                public void onResponse(String response) {
                                                    callActivity();
                                                }

                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    callActivity();
                                                }

                                                @Override
                                                public void callRequest() {
                                                    callActivity();
                                                }
                                            });


//                    Toast.makeText(HomeActivity.this, "Session Time out", Toast.LENGTH_SHORT).show();

                                        } else {
                                            callActivity();
                                        }


                                    }

                                    @Override
                                    public void callRequest() {

                                    }
                                });
                            } else {
                                callActivity();
                            }


                        } else {
                            callActivity();

                        }

                    }
                });

            }
        }).start();


    }


    private void callActivity() {
//        Toast.makeText(getApplicationContext(), "FIne", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }

}
