package autodex.com.autodex.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import autodex.com.autodex.Utility;
import autodex.com.autodex.activitys.ChangeNumberActivity;
import autodex.com.autodex.activitys.ForgotActivity;
import autodex.com.autodex.activitys.HomeActivity;
import autodex.com.autodex.activitys.NotificationSettingsActivity;
import autodex.com.autodex.R;
import autodex.com.autodex.activitys.UserProfileActivity;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.model.SaveSettings;
import autodex.com.autodex.model.webresponse.ProfileDetails;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebUrl;

/**
 * Created by yasar on 11/9/17.
 */

public class SettingsFragment extends BaseFragment {

    private static final String TAG = "SettingsFragment";
    private RelativeLayout notificationlay, changenumber, manageuser;

    private SaveSettings saveSettings;
    private String token;

    private Gson gson = new Gson();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_settings, container, false);

        token = sessionManager.getKeyToken();

        notificationlay = (RelativeLayout) view.findViewById(R.id.notificationlay);
        changenumber = (RelativeLayout) view.findViewById(R.id.changenumber);
        changenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callProfileActivity();
            }
        });
        manageuser = (RelativeLayout) view.findViewById(R.id.manageuser);

        manageuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileDetails profileDetails = appDatabase.daoProfileAccess().loadProfileByIds();
//        startActivity(new Intent(getActivity(), UserProfileActivity.class).putExtra("profileDetails", profileDetails));
                Bundle bundle = new Bundle();
                bundle.putSerializable("profileDetails", profileDetails);
                Utility.startAct(getActivity(), UserProfileActivity.class, bundle);
            }
        });

        notificationlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (saveSettings != null) {

                    getActivity().startActivity(new Intent(getActivity(), NotificationSettingsActivity.class).putExtra("savesettings", saveSettings));
                } else {
                    getActivity().startActivity(new Intent(getActivity(), NotificationSettingsActivity.class));
                }
            }
        });

        callSettingRequest();

//        iShowCount.showNotificationCount(HomeActivity.count);

        return view;
    }

    private void callProfileActivity() {
//        ProfileDetails profileDetails = appDatabase.daoProfileAccess().loadProfileByIds();
////        startActivity(new Intent(getActivity(), UserProfileActivity.class).putExtra("profileDetails", profileDetails));
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("profileDetails", profileDetails);
//        Utility.startAct(getActivity(), UserProfileActivity.class, bundle);


        startActivity(new Intent(getActivity(), ForgotActivity.class).putExtra("changenumber",true));
//        startActivity(new Intent(getActivity(), ChangeNumberActivity.class));

    }


    private void callSettingRequest() {
        Utility.showProgress(getActivity(), "Loading");

        webPostRequest.postGETData(WebUrl.SAVEPREFERENCE, token, new WebResponse() {
            @Override
            public void onResponse(JSONObject response) {
                Utility.hideProgress();
            }

            @Override
            public void onResponse(String response) {
                Utility.hideProgress();
                saveSettings = gson.fromJson(response, SaveSettings.class);

            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.hideProgress();
                Utility.showDialog(getActivity(), "Error", "Server error");

            }

            @Override
            public void callRequest() {

            }
        });
    }

    @Override
    public void onResponse(JSONObject response) {

        super.onResponse(response);
        Log.e(TAG, "onResponse: " + response);

    }

    @Override
    public void onResponse(String response) {
        super.onResponse(response);
        Log.e(TAG, "onResponse: " + response);
        callSettingRequest();
        Toast.makeText(getActivity(), "Settings successfully saved.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        super.onErrorResponse(error);
        Utility.showDialog(getActivity(), "Error", "Server error");
    }

    //142119c4-f94d-4d1f-889f-ac51a5862b6f
    @Override
    public void callRequest() {
        super.callRequest();

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Map<Object, Object> prefrenceMap = new HashMap<>();
        prefrenceMap.put("contactNearMeNotify", preference.getBoolean("contactnearme", true));
        prefrenceMap.put("birthdayNotify", preference.getBoolean("birtnotification", true));
        prefrenceMap.put("newAutoDexUserNotify", preference.getBoolean("newautodexuser", true));
        prefrenceMap.put("showNotificationAlert", preference.getBoolean("pushnotification", true));
//        prefrenceMap.put("vibrateOnNotificationAlert", "");
//        prefrenceMap.put("popUpNotify", "");
//        prefrenceMap.put("changeNumWithCountryCode", "");
//        prefrenceMap.put("enableOtpNotification", "");
//        prefrenceMap.put("manageBlockUnBlockContacts", "");
//        prefrenceMap.put("manageUserInfo", "");
//        prefrenceMap.put("showFirstLastNameWithImage", "");
        prefrenceMap.put("notifyMiles", preference.getInt("seekBar", 10));


        webPostRequest.postJSONPutDataGetNumber(WebUrl.SAVEPREFERENCE, prefrenceMap, token, this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.notisave, menu);
//        Drawable drawable = menu.getItem(0).getIcon();
//        if (drawable != null) {
//            drawable.mutate();
//            drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:

                callRequest();
//                Toast.makeText(getActivity(), "setting", Toast.LENGTH_SHORT).show();
                break;


        }
        return true;

    }


    @Override
    void searchFilter(String s) {

    }
}
