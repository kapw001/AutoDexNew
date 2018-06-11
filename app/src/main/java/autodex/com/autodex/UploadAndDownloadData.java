package autodex.com.autodex;

import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.error.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import autodex.com.autodex.activitys.HomeActivity;
import autodex.com.autodex.activitys.MyDialog;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebPostRequest;
import autodex.com.autodex.webrequest.WebUrl;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.DatabaseHandler;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

/**
 * Created by yasar on 8/11/17.
 */

public class UploadAndDownloadData extends IntentService {

    private static final String TAG = "UploadAndDownloadData";
    private SharedPreferences sharedPreferences;
    private SessionManager sessionManager;
    private WebPostRequest webPostRequest;
    private DatabaseHandler databaseHandler;
    private Gson gson = new Gson();


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UploadAndDownloadData() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.e(TAG, "onHandleIntent: Called ");
        sessionManager = SessionManager.getInstance(getApplicationContext());
        webPostRequest = WebPostRequest.getInstance(getApplicationContext());
        databaseHandler = DatabaseHandler.getInstace(getApplicationContext());
        sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);


//        Observable.fromCallable(new Callable<List<GetContactListResponse>>() {
//
//            @Override
//            public List<GetContactListResponse> call() throws Exception {
//                return Util.readContacts(getApplicationContext());
//            }
//        }).distinct()
//                .flatMap(new Function<List<GetContactListResponse>, ObservableSource<List<GetContactListResponse>>>() {
//                    @Override
//                    public ObservableSource<List<GetContactListResponse>> apply(@NonNull final List<GetContactListResponse> getContactListResponses) throws Exception {
//                        return Observable.fromCallable(new Callable<List<GetContactListResponse>>() {
//                            @Override
//                            public List<GetContactListResponse> call() throws Exception {
//
//
//                                return getContactListResponses;
//                            }
//                        });
//                    }
//                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<List<GetContactListResponse>>() {
//            @Override
//            public void onSubscribe(@NonNull Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(@NonNull List<GetContactListResponse> getContactListResponses) {
//
//                for (GetContactListResponse response : getContactListResponses) {
//
//                    Log.e(TAG, "onNext: " + new Gson().toJson(response));
//
//                }
//
//            }
//
//            @Override
//            public void onError(@NonNull Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });


        List<GetContactListResponse> localContactList = Util.readContacts(getApplicationContext());

        Log.e(TAG, "onHandleIntent size   " + localContactList.size());

        try {
            updateLocalContactToServer(localContactList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateLocalContactToServer(List<GetContactListResponse> localContactList) throws JSONException {

        List<GetContactListResponse> copyOfLocalContactList = new ArrayList<>();
        copyOfLocalContactList.clear();
        copyOfLocalContactList.addAll(localContactList);


//        List<GetContactListResponse> removedIFPhoneNumberNot = new ArrayList<>();
//
//        for (int i = 0; i < copyOfLocalContactList.size(); i++) {
//
//            if (copyOfLocalContactList.get(i).getPhoneNumber() != null) {
//                removedIFPhoneNumberNot.add(copyOfLocalContactList.get(i));
//            }
//        }
//
//        localContactList.clear();
//        localContactList.addAll(removedIFPhoneNumberNot);


//        Log.e(TAG, "updateLocalContactToServer: copyOfLocalContactList test  " + gson.toJson(copyOfLocalContactList));

        for (GetContactListResponse response : copyOfLocalContactList
                ) {

            Log.e(TAG, "local " + gson.toJson(response));
        }

        List<GetContactListResponse> oldContactList;

        if (sharedPreferences.getString("list", null) == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("list", gson.toJson(copyOfLocalContactList));
            editor.apply();
        }

        String old_list = sharedPreferences.getString("list", "");

        Type type = new TypeToken<List<GetContactListResponse>>() {
        }.getType();
        oldContactList = gson.fromJson(old_list, type);

//        removedIFPhoneNumberNot.clear();
//
//        for (int i = 0; i < oldContactList.size(); i++) {
//
//            if (oldContactList.get(i).getPhoneNumber() != null) {
//                removedIFPhoneNumberNot.add(oldContactList.get(i));
//            }
//        }
//
//        oldContactList.clear();
//        oldContactList.addAll(removedIFPhoneNumberNot);

//        Log.e(TAG, "updateLocalContactToServer: list contact test  " + gson.toJson(oldContactList));


        localContactList.removeAll(oldContactList);


//        List<GetContactListResponse> serverData = new ArrayList<>();

//
//        for (int i = 0; i < oldContactList.size(); i++) {
//
//
//            Log.e(TAG, "old  " + gson.toJson(oldContactList.get(i)));
//
//            localContactList.remove(oldContactList.get(i));
//
//        }


        Log.e(TAG, "updateLocalContactToServer: Size " + localContactList.size());

//        Log.e(TAG, " server data : " + gson.toJson(localContactList));
//
//
//        Log.e(TAG, "updateLocalContactToServer: " + localContactList.size());


////        oldContactList.removeAll(copyOfLocalContactList);
//
//
//        Log.e(TAG, "updateLocalContactToServer: local contact   " + gson.toJson(localContactList));
//        Log.e(TAG, "------------------------------------------------------------------------------------");
//        Log.e(TAG, "updateLocalContactToServer: old " + gson.toJson(oldContactList));
//
////
//        if (localContactList.size() > 0) {
//
////            SharedPreferences.Editor editor = sharedPreferences.edit();
////            editor.putString("list", gson.toJson(copyOfLocalContactList));
////            editor.apply();
//
//
//            Log.e(TAG, "updateLocalContactToServer: " + localContactList.size() + "     " + gson.toJson(localContactList));
//        } else {
//
//            Log.e(TAG, "There is no contact to update");
//        }


//        List<GetContactListResponse> copyOfLocalContactList = new ArrayList<>();
//        copyOfLocalContactList.clear();
//        copyOfLocalContactList.addAll(localContactList);

        List<GetContactListResponse> dataBaseContactList = databaseHandler.getAllDatas();
        for (int i = 0; i < dataBaseContactList.size(); i++) {
            GetContactListResponse cl = dataBaseContactList.get(i);

            List<ContactAttribute> contactAttributeList = databaseHandler.getAllContactAttributeDatas(String.valueOf(cl.getId()));
            dataBaseContactList.get(i).setContactAttributes(contactAttributeList);
        }
//
//        localContactList.removeAll(dataBaseContactList);
//
//
        if (localContactList.size() > 0) {
            List<GetContactListResponse> toUpdateServerList = new ArrayList<>();
            for (GetContactListResponse lo : localContactList
                    ) {

                for (GetContactListResponse server : dataBaseContactList
                        ) {

                    if (lo.getLocalContactID().equalsIgnoreCase(server.getLocalContactID())) {

                        lo.setId(server.getId());
                        toUpdateServerList.add(lo);
                    }

                }
            }


            Set<GetContactListResponse> removeDupliacate = new HashSet<>();
            removeDupliacate.addAll(toUpdateServerList);
            toUpdateServerList.clear();
            toUpdateServerList.addAll(removeDupliacate);

            localContactList.removeAll(toUpdateServerList);
            toUpdateServerList.addAll(localContactList);

            Log.e(TAG, "updateLocalContactToServer: json  " + gson.toJson(toUpdateServerList));

            showSyncDialogUploadToServer(toUpdateServerList, copyOfLocalContactList);

        }


    }

    private void showSyncDialogUploadToServer(final List<GetContactListResponse> jsonSaveList, final List<GetContactListResponse> localContactList) {

        if (HomeActivity.isServiceStarted) {
            Log.e(TAG, "showSyncDialogUploadToServer: first time called " + HomeActivity.isServiceStarted);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("servertoupdatelist", gson.toJson(jsonSaveList));
            editor.putString("localcontactlist", gson.toJson(localContactList));
            editor.apply();
            Intent intent = new Intent(this, MyDialog.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
