package autodex.com.autodex.locationservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.android.gms.location.LocationResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import autodex.com.autodex.Util;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebPostRequest;
import autodex.com.autodex.webrequest.WebUrl;

/**
 * Created by yasar on 21/11/17.
 */

public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
    private WebPostRequest webPostRequest;
    private SessionManager sessionManager;
    private static final String TAG = "LUBroadcastReceiver";

    public static final String ACTION_PROCESS_UPDATES =
            "autodex.com.autodex.locationservice.action" +
                    ".PROCESS_UPDATES";
    private SharedPreferences sharedPreferences;
    private SharedPreferences preference;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            sharedPreferences = context.getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                preference = PreferenceManager.getDefaultSharedPreferences(context);
                webPostRequest = WebPostRequest.getInstance(context);
                sessionManager = SessionManager.getInstance(context);
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {

                    Location location = result.getLastLocation();
                    if (sharedPreferences.getString("lat", null) == null) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("lat", String.valueOf(location.getLatitude()));
                        editor.putString("long", String.valueOf(location.getLongitude()));
                        editor.apply();
                    } else {
                        Location B = new Location("B");
                        B.setLatitude(Double.parseDouble(sharedPreferences.getString("lat", null)));
                        B.setLongitude(Double.parseDouble(sharedPreferences.getString("long", null)));
//                        double distance = distance(B.getLatitude(), B.getLongitude(), location.getLatitude(), location.getLongitude());
                        double distance = distance(location.getLatitude(), location.getLongitude(), B.getLatitude(), B.getLongitude(), "m");
                        int disToCover = preference.getInt("seekBar", 1);
                        if (distance > disToCover) {
                            Util.sendNotification(context, "Location traveled is  " + distance);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("lat", String.valueOf(location.getLatitude()));
                            editor.putString("long", String.valueOf(location.getLongitude()));
                            editor.apply();
                        }

//                        Toast.makeText(context, "" + distance, Toast.LENGTH_SHORT).show();
                    }

                    updateLocation(location);

                }
            }
        }
    }

    private void updateLocation(Location location) {
        Map<Object, Object> params = new HashMap<>();
        params.put("latitude", location.getLatitude());
        params.put("longtitude", location.getLongitude());

        String token = sessionManager.getKeyToken();

//                    0f4f3cef-970b-4276-a712-dde570b83b32

        webPostRequest.postJSONDataGetNumber(WebUrl.GEOLOCATION, params, token, new WebResponse() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: location " + response);
            }

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: location " + response);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onResponse: location " + error.getMessage());
            }

            @Override
            public void callRequest() {

            }
        });

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, String units) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        double miles = d / 1.609344;

        if (units.equalsIgnoreCase("km")) {
            return d;
        } else {
            return miles;
        }
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
