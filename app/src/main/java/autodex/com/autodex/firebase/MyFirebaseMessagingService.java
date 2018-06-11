package autodex.com.autodex.firebase;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import autodex.com.autodex.MessageEvent;
import autodex.com.autodex.Utility;
import autodex.com.autodex.activitys.HomeActivity;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebPostRequest;
import autodex.com.autodex.webrequest.WebUrl;
import volleywebrequest.com.mysqlitelibrary.ContactAttribute;
import volleywebrequest.com.mysqlitelibrary.ContactCategory;
import volleywebrequest.com.mysqlitelibrary.DatabaseHandler;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

/**
 * Created by yasar on 7/12/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

        try {
            if (intent.getExtras() != null) {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");

                for (String key : intent.getExtras().keySet()) {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }

                onMessageReceived(builder.build());
            } else {
                super.handleIntent(intent);
            }
        } catch (Exception e) {
            super.handleIntent(intent);
        }

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
//            handleNotification(remoteMessage.getNotification().getBody());
//        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());


            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json, remoteMessage.getNotification().getBody());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }


    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json, String msg) {
        Log.e(TAG, "push json: " + json.toString());

        // TODO: 23/3/18  {app={"content-available":1}, operationType=updateContacts, contactIds=123323}
        // TODO: 23/3/18  {"app":{"content-available":1},"operationType":"updateContacts","contactIds":[49683]}
        try {


//            JSONObject data = json.getJSONObject("data");
            JSONObject app = json.getJSONObject("app");

            int value = app.getInt("content-available");

            if (value == 1) {

                final WebPostRequest webPostRequest = WebPostRequest.getInstance(getApplicationContext());

                final SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());

                final DatabaseHandler databaseHandler = DatabaseHandler.getInstace(getApplicationContext());

                String token = sessionManager.getKeyToken();

                JSONArray jsonArray = json.getJSONArray("contactIds");

                for (int i = 0; i < jsonArray.length(); i++) {


                    long contactId = jsonArray.getInt(i);

                    Log.e(TAG, "handleDataMessage: " + contactId);


                    webPostRequest.postGETData(WebUrl.GETCONTACT + "/" + contactId, token, new WebResponse() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.e(TAG, "onResponse: JSONObject " + response.toString());
                        }

                        @Override
                        public void onResponse(String response) {

                            Gson gson = new Gson();

                            GetContactListResponse updateData = gson.fromJson(response, GetContactListResponse.class);

                            List<ContactAttribute> attributes = updateData.getContactAttributes();
                            List<ContactCategory> categories = updateData.getContactCategories();

                            databaseHandler.updateData(updateData);
                            databaseHandler.updateContactAttributeData(attributes);
                            databaseHandler.updateContactCategoryData(categories);

                            EventBus.getDefault().post(new MessageEvent("Contact Updated"));

                            Log.e(TAG, "onResponse: JSONObject " + response.toString());
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "onResponse: error " + error.getMessage());
                        }

                        @Override
                        public void callRequest() {

                        }
                    });


                }


            } else {

                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", msg);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            }

//            String title = data.getString("title");
//            String message = data.getString("message");
//            boolean isBackground = data.getBoolean("is_background");
//            String imageUrl = data.getString("image");
//            String timestamp = data.getString("timestamp");
//            JSONObject payload = data.getJSONObject("payload");
//
//            Log.e(TAG, "title: " + title);
//            Log.e(TAG, "message: " + message);
//            Log.e(TAG, "isBackground: " + isBackground);
//            Log.e(TAG, "payload: " + payload.toString());
//            Log.e(TAG, "imageUrl: " + imageUrl);
//            Log.e(TAG, "timestamp: " + timestamp);


//            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();
//            } else {
//                // app is in background, show the notification in notification tray
//                Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
//                resultIntent.putExtra("message", message);
//
//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }
//            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}