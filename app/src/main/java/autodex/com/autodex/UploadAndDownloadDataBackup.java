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
import java.util.List;

import autodex.com.autodex.activitys.HomeActivity;
import autodex.com.autodex.activitys.MyDialog;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebPostRequest;
import volleywebrequest.com.mysqlitelibrary.DatabaseHandler;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

/**
 * Created by yasar on 8/11/17.
 */

public class UploadAndDownloadDataBackup extends IntentService {

    private static final String TAG = "UploadAndDownloadData";
    private SharedPreferences sharedPreferences;
    private SessionManager sessionManager;
    private WebPostRequest webPostRequest;
    private DatabaseHandler databaseHandler;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UploadAndDownloadDataBackup() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.e(TAG, "onHandleIntent: ");
        sessionManager = SessionManager.getInstance(getApplicationContext());
        webPostRequest = WebPostRequest.getInstance(getApplicationContext());
        databaseHandler = DatabaseHandler.getInstace(getApplicationContext());
        sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
        List<GetContactListResponse> mapList = Util.readContacts(getApplicationContext());

        try {
            updateLocalContactToServer(mapList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateLocalContactToServer(List<GetContactListResponse> mapList) throws JSONException {
        Gson gson = new Gson();

        Log.e(TAG, "updateLocalContactToServer: list size   " + sharedPreferences.getString("list", "").length());

        if (sharedPreferences.getString("list", "").length() <= 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("list", gson.toJson(mapList));
            editor.putString("listtouploadserver", gson.toJson(mapList));
            editor.putString("savetolist", gson.toJson(mapList));
            editor.commit();


            showSyncDialogUploadToServer(mapList, mapList);
        } else {

            List<GetContactListResponse> productFromShared = new ArrayList<>();

            String jsonPreferences = sharedPreferences.getString("list", "");

            Type type = new TypeToken<List<GetContactListResponse>>() {
            }.getType();
            productFromShared = gson.fromJson(jsonPreferences, type);
            List<GetContactListResponse> jsList = new ArrayList<>();
            jsList.clear();
            jsList.addAll(mapList);

            List<GetContactListResponse> l = mapList;

            l.removeAll(productFromShared);

            if (l.size() > 0) {
                showSyncDialogUploadToServer(l, jsList);
            } else {

                if (!sharedPreferences.getBoolean("sync", false)) {
                    showSyncDialogUploadToServer(jsList, jsList);
                    Log.e(TAG, "updateLocalContactToServer: called  " + jsList.size());
                }
            }

            Log.e(TAG, "updateLocalContactToServer: " + l.size() + "  " + productFromShared.size() + " " + mapList.size());

        }

    }

    private void showSyncDialogUploadToServer(final List<GetContactListResponse> list, final List<GetContactListResponse> jsonSaveList) {

        if (HomeActivity.isServiceStarted) {
            Log.e(TAG, "showSyncDialogUploadToServer: first time called " + HomeActivity.isServiceStarted);
            Gson gson = new Gson();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("listtouploadserver", gson.toJson(list));
            editor.putString("savetolist", gson.toJson(jsonSaveList));
            editor.commit();
            Intent intent = new Intent(this, MyDialog.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
