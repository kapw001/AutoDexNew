package autodex.com.autodex.activitys;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import autodex.com.autodex.R;
import autodex.com.autodex.Util;
import autodex.com.autodex.Utility;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

public class TestActivity extends BaseActivity {
    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


//        List<GetContactListResponse> li = new ArrayList<>();
//
//        GetContactListResponse sd = new GetContactListResponse();
//        sd.setFirstName("Ka");
//        sd.setMiddleName("");
//
//        li.add(sd);
//
//
//        List<GetContactListResponse> li1 = new ArrayList<>();
//
//        GetContactListResponse sd1 = new GetContactListResponse();
//        sd1.setFirstName("Ka");
//        sd1.setMiddleName("");
//
//        li1.add(sd1);
//
//
//        li.removeAll(li1);
//
//        Log.e(TAG, "onCreate: " + li.size());


//        new LoadData().execute();
        enumerateColumns();
    }

    private void enumerateColumns() {
        Cursor cursor = getApplicationContext().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.d("myActivity", cursor.getColumnName(i) + " : " + cursor.getString(i));
                }
            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    public static void d(String TAG, String message) {
        int maxLogSize = 2000;
        for (int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            android.util.Log.e(TAG, message.substring(start, end));
        }
    }

    class LoadData extends AsyncTask<Void, Void, List<GetContactListResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utility.showProgress(TestActivity.this, "Loading");
        }

        @Override
        protected List<GetContactListResponse> doInBackground(Void... params) {

            List<GetContactListResponse> list = Util.readContacts(getApplicationContext());

//            d(TAG, "doInBackground: " + new Gson().toJson(list));

            List<GetContactListResponse> serverlIST = getSerVerData(getApplicationContext());
//
            List<GetContactListResponse> nList = new ArrayList<>();

            for (GetContactListResponse localList : list
                    ) {

                for (GetContactListResponse serverList : serverlIST
                        ) {

                    if (localList.equals(serverList)) {
                        serverList.setLocalContactID(localList.getLocalContactID());
                        nList.add(serverList);
                    }

                }

            }
//
//            List<GetContactListResponse> localUpdatedList = new ArrayList<>(list);
//            List<GetContactListResponse> localToServerUpdateList = new ArrayList<>();
//
//            localUpdatedList.removeAll(serverlIST);
//
//            for (GetContactListResponse toUpdate : localUpdatedList
//                    ) {
//                for (GetContactListResponse nist : nList
//                        ) {
//
//                    if (toUpdate.getLocalContactID().equalsIgnoreCase(nist.getLocalContactID())) {
//                        Log.e(TAG, "doInBackground: "+toUpdate.getLocalContactID()+ "    "+ nist.getLocalContactID());
//                        toUpdate.setId(nist.getId());
//                        localToServerUpdateList.add(toUpdate);
//                    }
//                }
//            }
//
//
//            Log.e(TAG, "doInBackground: sizes  " + " local :" + list.size() + "  localUpdatedList  :" + localUpdatedList.size() + "   server  :" + serverlIST.size() + "    newSize  " + nList.size()+"   localToServerUpdateList   "+localToServerUpdateList.size());


            return list;
        }

        @Override
        protected void onPostExecute(List<GetContactListResponse> list) {
            super.onPostExecute(list);

            Utility.hideProgress();

            Gson gson = new Gson();

            for (GetContactListResponse l : list
                    ) {
                Log.e(TAG, "onPostExecute: " + gson.toJson(l));
            }


        }
    }


    public static List<GetContactListResponse> getSerVerData(Context context) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<GetContactListResponse>>() {
        }.getType();
        return gson.fromJson(loadJSONFromAsset(context), listType);
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
