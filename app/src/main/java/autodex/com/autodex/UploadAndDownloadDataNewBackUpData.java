package autodex.com.autodex;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import autodex.com.autodex.activitys.HomeActivity;
import autodex.com.autodex.activitys.MyDialog;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebPostRequest;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.DatabaseHandler;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

/**
 * Created by yasar on 8/11/17.
 */

public class UploadAndDownloadDataNewBackUpData extends IntentService {

    private static final String TAG = "UploadAndDownloadData";
    private SharedPreferences sharedPreferences;
    private SessionManager sessionManager;
    private WebPostRequest webPostRequest;
    private DatabaseHandler databaseHandler;
    private Gson gson = new Gson();


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UploadAndDownloadDataNewBackUpData() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.e(TAG, "onHandleIntent: Called ");
        sessionManager = SessionManager.getInstance(getApplicationContext());
        webPostRequest = WebPostRequest.getInstance(getApplicationContext());
        databaseHandler = DatabaseHandler.getInstace(getApplicationContext());
        sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
        List<GetContactListResponse> localContactList = Util.readContacts(getApplicationContext());

        try {
            updateLocalContactToServer(localContactList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateLocalContactToServer(List<GetContactListResponse> localContactList) throws JSONException {

        Log.e(TAG, "updateLocalContactToServer: local " + gson.toJson(localContactList));

        List<GetContactListResponse> copyOfLocalContactList = new ArrayList<>();
        copyOfLocalContactList.clear();
        copyOfLocalContactList.addAll(localContactList);

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


        localContactList.removeAll(oldContactList);

////        oldContactList.removeAll(copyOfLocalContactList);
//
//
//        Log.e(TAG, "updateLocalContactToServer: local contact   " + gson.toJson(localContactList));
//        Log.e(TAG, "------------------------------------------------------------------------------------");
//        Log.e(TAG, "updateLocalContactToServer: old " + gson.toJson(oldContactList));
//
//
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
            editor.commit();
            Intent intent = new Intent(this, MyDialog.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
