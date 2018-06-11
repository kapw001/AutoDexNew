package autodex.com.autodex.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import autodex.com.autodex.MessageEvent;
import autodex.com.autodex.R;
import autodex.com.autodex.Util;
import autodex.com.autodex.Utility;
import autodex.com.autodex.activitys.HomeActivity;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebUrl;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

/**
 * Created by yasar on 11/9/17.
 */

public class SyncBackupFragment extends BaseFragment {

    private static final String TAG = "SyncBackupFragment";
    private Button pushtocloud;
    private SharedPreferences sharedPreferences;
    private TextView syncCount, lastsync;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_syncbackup, container, false);
        sharedPreferences = getActivity().getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);

        syncCount = (TextView) view.findViewById(R.id.totalcontacts);
        lastsync = (TextView) view.findViewById(R.id.lastsync);

//        if (sharedPreferences.getString("synccount", null) != null) {

//        }

        showCountAndDate();

        pushtocloud = (Button) view.findViewById(R.id.pushtocloud);
        pushtocloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String token = sessionManager.getDetails().get(KEY_TOKEN);
                final Gson gson = new Gson();

                List<GetContactListResponse> productFromShared = new ArrayList<>();
                List<GetContactListResponse> localContactList = new ArrayList<>();


                String jsonPreferences = sharedPreferences.getString("servertoupdatelist", "");
                String localcontact = sharedPreferences.getString("localcontactlist", "");


                Type type = new TypeToken<List<GetContactListResponse>>() {
                }.getType();
                productFromShared = gson.fromJson(jsonPreferences, type);

                localContactList = gson.fromJson(localcontact, type);

                if (productFromShared != null && productFromShared.size() > 0) {

                    Log.e(TAG, "onClick: " + gson.toJson(productFromShared));

                    Utility.showProgress(getActivity(), "Loding");
                    try {
                        final List<GetContactListResponse> finalProductFromShared = productFromShared;
                        final List<GetContactListResponse> finalLocalContactList = localContactList;
                        webPostRequest.postJSONPutDataGetNumberStringJsonArray(WebUrl.SAVEALLCONTACTS, new JSONArray(gson.toJson(productFromShared)), token, new WebResponse() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Utility.hideProgress();

                            }

                            @Override
                            public void onResponse(String response) {
                                Utility.hideProgress();
                                Log.e(TAG, "onResponse: " + response);

                                Toast.makeText(getActivity(), "Successfully synced", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("sync", true);
                                DateFormat dateFormat = new SimpleDateFormat("EEE hh:mm a MMM d, yyyy");
                                Date date = new Date();
                                System.out.println(dateFormat.format(date));
                                editor.putString("synctime", dateFormat.format(date) + "");
                                editor.putString("synccount", finalLocalContactList.size() + "");
                                editor.putString("list", gson.toJson(finalLocalContactList));
                                finalProductFromShared.clear();
                                editor.putString("servertoupdatelist", gson.toJson(finalProductFromShared));
                                editor.commit();
//
                                showCountAndDate();

//                                syncCount.setText(sharedPreferences.getString("synccount", "0"));
//                                lastsync.setText("Last sync : " + sharedPreferences.getString("synctime", "00:00"));

                                EventBus.getDefault().post(new MessageEvent("sysnctime"));

                                GetAllContactFromServer(finalLocalContactList);

                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Utility.hideProgress();
                                Log.e(TAG, "onResponse: " + error.getMessage());

                                Utility.showDialog(getActivity(), "Error", "Server error");

                            }

                            @Override
                            public void callRequest() {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getActivity(), "There is no contacts to sync to server", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        iShowCount.showNotificationCount(HomeActivity.count);

        return view;
    }

    private void showCountAndDate() {
        syncCount.setText(sharedPreferences.getString("synccount", "0"));


        try {
//            Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"
            lastsync.setText(Html.fromHtml("Last synced: <b>" + Util.formatToYesterdayOrToday(sharedPreferences.getString("synctime", "00:00").toString()) + "</b>"));
        } catch (ParseException e) {
            e.printStackTrace();
            lastsync.setText("Last synced: " + sharedPreferences.getString("synctime", "00:00"));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
//        inflater.inflate(R.menu.sync, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tick:
//                Toast.makeText(getActivity(), "Tick", Toast.LENGTH_SHORT).show();
                break;


        }
        return true;

    }

    @Override
    void searchFilter(String s) {

    }

    private void GetAllContactFromServer(final List<GetContactListResponse> localContactList) {
        final String token = sessionManager.getDetails().get(KEY_TOKEN);
        Utility.showProgress(getActivity(), "Loading");
        webPostRequest.postGETData(WebUrl.GETCONTACTLIST, token, new WebResponse() {
            @Override
            public void onResponse(JSONObject response) {

                Utility.hideProgress();
                Log.e(TAG, "onResponse: " + response);

            }

            @Override
            public void onResponse(final String response) {
                final Gson gson = new Gson();

                Log.e(TAG, "onResponse: res  " + response.toString());


                Observable.fromCallable(new Callable<Boolean>() {

                    @Override
                    public Boolean call() throws Exception {

                        databaseHandler.deleteAllContactAttributeData();
                        databaseHandler.deleteAllContactCategoryData();
                        databaseHandler.deleteAllData();

                        GetContactListResponse[] list = gson.fromJson(response.toString(), GetContactListResponse[].class);

                        Log.e(TAG, "onResponse: server data size " + list.length);

                        List<GetContactListResponse> serverlIST = Arrays.asList(list);
//
//                List<GetContactListResponse> nList = new ArrayList<>();


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


                        return true;
                    }
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                        Utility.hideProgress();
                    }
                });


            }

            @Override
            public void onErrorResponse(VolleyError error) {

                Utility.showDialog(getActivity(), "Error", "Server error,please try again");

                Log.e(TAG, "onErrorResponse: server error" + error.getMessage());

            }

            @Override
            public void callRequest() {

            }
        });
    }
}
