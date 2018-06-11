package autodex.com.autodex.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import autodex.com.autodex.R;
import autodex.com.autodex.RuntimePermissionRequest;
import autodex.com.autodex.Util;
import autodex.com.autodex.Utility;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebUrl;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

public class InitScreenActivity extends BaseActivity {
    private static final String TAG = "InitScreenActivity";
    private SharedPreferences sharedPreferences;
    private static final int MLTIPLE_PERMISSION_REQUEST = 1;
    private String[] permission = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WAKE_LOCK};

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_screen);
        sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
//        sessionManager = SessionManager.getInstance(this);
//        databaseHandler = DatabaseHandler.getInstace(this);

        if (RuntimePermissionRequest.checkMultiplePermission(this, permission)) {
            RuntimePermissionRequest.requestPermissionMultiple(this, permission, MLTIPLE_PERMISSION_REQUEST, "Permission", "Need read contacts permission to get contacts ");
        } else {
            new UploadData().execute();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MLTIPLE_PERMISSION_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (RuntimePermissionRequest.verifyPermissions(grantResults)) {


//                    Toast.makeText(this, "All multiple Permission granted", Toast.LENGTH_SHORT).show();
                    new UploadData().execute();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {


                    Toast.makeText(this, "Please provide contact read permissions", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;


        }
    }


    class UploadData extends AsyncTask<Void, Void, List<GetContactListResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Utility.showProgressContactInit(InitScreenActivity.this, "Please");
        }

        @Override
        protected List<GetContactListResponse> doInBackground(Void... params) {
            List<GetContactListResponse> mapList = Util.readContacts(getApplicationContext());

            return mapList;
        }

        @Override
        protected void onPostExecute(List<GetContactListResponse> localContactList) {
//            super.onPostExecute(maps);
            Utility.hideProgress();

            try {
                updateLocalContactToServer(localContactList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            Log.e(TAG, "onPostExecute: " + maps.toString());

        }
    }

    private void updateLocalContactToServer(final List<GetContactListResponse> localContactList) throws JSONException {


        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("list", gson.toJson(localContactList));
                        editor.apply();

                        final List<GetContactListResponse> copyList = new ArrayList<>();
                        copyList.clear();
                        copyList.addAll(localContactList);

                        final String token = sessionManager.getDetails().get(KEY_TOKEN);
//        Utility.showProgressContactInit(InitScreenActivity.this, "Please");
                        webPostRequest.postGETData(WebUrl.GETCONTACTLIST, token, new WebResponse() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Utility.hideProgress();
                                Log.e(TAG, "onResponse: " + response);

                            }

                            @Override
                            public void onResponse(String response) {
                                Gson gson = new Gson();
                                Utility.hideProgress();

                                GetContactListResponse[] serverData = gson.fromJson(response, GetContactListResponse[].class);

                                List<GetContactListResponse> serverList = Arrays.asList(serverData);

                                if (serverList.size() > 0) {

                                    localContactList.removeAll(serverList);
                                    showSyncDialogUploadToServer(localContactList, copyList);

                                } else {
                                    showSyncDialogUploadToServer(localContactList, copyList);
                                }


                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                sessionManager.setIsLogin(false);
                                Utility.showDialog(InitScreenActivity.this, "Error", "Problems on server,please try again", new Utility.DialogButtonListener() {
                                    @Override
                                    public void ok() {

                                        finish();
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    }
                                });

                                Log.e(TAG, "onErrorResponse: server error" + error.getMessage());

                            }

                            @Override
                            public void callRequest() {

                            }
                        });

                    }
                });

            }
        }).start();


    }

    private void showSyncDialogUploadToServer(final List<GetContactListResponse> localContactList, final List<GetContactListResponse> copyList) {

        new Thread(new Runnable() {
            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        final String token = sessionManager.getDetails().get(KEY_TOKEN);
                        ;
//        Utility.showProgressContactInit(InitScreenActivity.this, "Please");
                        try {
                            webPostRequest.postJSONPutDataGetNumberStringJsonArray(WebUrl.SAVEALLCONTACTS, new JSONArray(gson.toJson(localContactList)), token, new WebResponse() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Utility.hideProgress();
                                    Log.e(TAG, "onResponse: " + response);

                                }

                                @Override
                                public void onResponse(String response) {
                                    Utility.hideProgress();
                                    Log.e(TAG, "onResponse: " + response);

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("sync", true);

                                    DateFormat dateFormat = new SimpleDateFormat("EEE hh:mm a MMM d, yyyy");
                                    Date date = new Date();
                                    System.out.println(dateFormat.format(date));
                                    editor.putString("synctime", dateFormat.format(date) + "");
                                    editor.putString("synccount", copyList.size() + "");
                                    editor.commit();

                                    GetAllContactFromServer(copyList);

                                }

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Utility.hideProgress();
                                    Log.e(TAG, "onResponse: " + error.getMessage());
                                    sessionManager.setIsLogin(false);
                                    Utility.showDialog(InitScreenActivity.this, "Error", "Problems on server,please try again", new Utility.DialogButtonListener() {
                                        @Override
                                        public void ok() {

                                            finish();
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        }
                                    });

                                }

                                @Override
                                public void callRequest() {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        }).start();


    }


    private void GetAllContactFromServer(final List<GetContactListResponse> localContactList) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        final String token = sessionManager.getDetails().get(KEY_TOKEN);
//        Utility.showProgressContactInit(InitScreenActivity.this, "Please");
                        webPostRequest.postGETData(WebUrl.GETCONTACTLIST, token, new WebResponse() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Utility.hideProgress();
                                Log.e(TAG, "onResponse: " + response);

                            }

                            @Override
                            public void onResponse(final String response) {

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Gson gson = new Gson();

                                        Log.e(TAG, "onResponse: res  " + response.toString());

                                        databaseHandler.deleteAllContactAttributeData();
                                        databaseHandler.deleteAllContactCategoryData();
                                        databaseHandler.deleteAllData();


                                        GetContactListResponse[] list = gson.fromJson(response.toString(), GetContactListResponse[].class);

                                        Log.e(TAG, "onResponse: server data size " + list.length);

                                        List<GetContactListResponse> serverlIST = Arrays.asList(list);

                                        for (int i = 0; i < localContactList.size(); i++) {

                                            GetContactListResponse localList = localContactList.get(i);

                                            for (int j = 0; j < serverlIST.size(); j++) {

                                                GetContactListResponse serverList = serverlIST.get(j);

                                                if (localList.equals(serverList)) {
                                                    serverlIST.get(j).setLocalContactID(localList.getLocalContactID());

                                                }
                                            }
                                        }
//
//                List<GetContactListResponse> nList = new ArrayList<>();
//
//                for (GetContactListResponse localList : localContactList
//                        ) {
//
//                    for (GetContactListResponse serverList : serverlIST
//                            ) {
//
//                        if (localList.equals(serverList)) {
//                            serverList.setLocalContactID(localList.getLocalContactID());
//                            nList.add(serverList);
//                        }
//
////                        else {
////                            nList.add(serverList);
////                        }
//
//                    }
//
//                }


                                        for (GetContactListResponse getContactListResponse : serverlIST
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
                                        databaseHandler.addData(serverlIST);
                                        sessionManager.setKeySqlliteloading(true);

                                        Utility.hideProgress();

                                        Intent intent = new Intent(InitScreenActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }
                                }).start();


                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Utility.showDialog(InitScreenActivity.this, "Error", "Server error,please try again", new Utility.DialogButtonListener() {
                                    @Override
                                    public void ok() {
                                        finish();
                                    }
                                });

                                Log.e(TAG, "onErrorResponse: server error" + error.getMessage());

                            }

                            @Override
                            public void callRequest() {

                            }
                        });

                    }
                });

            }
        }).start();


    }
}
