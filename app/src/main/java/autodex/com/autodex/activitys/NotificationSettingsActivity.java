package autodex.com.autodex.activitys;


import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.error.VolleyError;
import com.pavelsikun.seekbarpreference.SeekBarPreference;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import autodex.com.autodex.R;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.model.SaveSettings;
import autodex.com.autodex.webrequest.WebPostRequest;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class NotificationSettingsActivity extends AppCompatPreferenceActivity {

    private static final String TAG = "NotificationSettingsAct";


    public static SaveSettings saveSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        saveSettings = (SaveSettings) getIntent().getSerializableExtra("savesettings");


        getFragmentManager().beginTransaction().replace(android.R.id.content, new NotificationPreferenceFragment()).commit();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.home, menu);
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.notisave, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
//            if (!super.onMenuItemSelected(featureId, item)) {
//                NavUtils.navigateUpFromSameTask(this);
//            }
            return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);

            SwitchPreference pushnotification = (SwitchPreference) findPreference("pushnotification");
            SwitchPreference birtnotification = (SwitchPreference) findPreference("birtnotification");
            SwitchPreference newautodexuser = (SwitchPreference) findPreference("newautodexuser");
            SwitchPreference contactnearme = (SwitchPreference) findPreference("contactnearme");
//            pushnotification.setChecked(false);

            final ListPreference seekBar = (ListPreference) findPreference("seekBar22");
//            SwitchPreference mkm = (SwitchPreference) findPreference("mkm");


            if (saveSettings != null) {

                if (saveSettings.getShowNotificationAlert()) {
                    pushnotification.setChecked(true);
                } else {
                    pushnotification.setChecked(false);
                }

                if (saveSettings.getBirthdayNotify()) {
                    birtnotification.setChecked(true);
                } else {
                    birtnotification.setChecked(false);
                }

                if (saveSettings.getNewAutoDexUserNotify()) {
                    newautodexuser.setChecked(true);
                } else {
                    newautodexuser.setChecked(false);
                }

                if (saveSettings.getContactNearMeNotify()) {
                    contactnearme.setChecked(true);
                } else {
                    contactnearme.setChecked(false);
                }
                seekBar.setValue(String.valueOf(saveSettings.getNotifyMiles()));


            }


//            Log.e(TAG, "onCreate: " + seekBar.getValue());

//            boolean b = mkm.isChecked();
//            if (b) {
//                seekBar.setMeasurementUnit("M");
//            } else {
//                seekBar.setMeasurementUnit("KM");
//            }
//
//            mkm.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object o) {
//                    boolean isVibrateOn = (Boolean) o;
//                    if (isVibrateOn) {
//                        seekBar.setMeasurementUnit("M");
//                    } else {
//                        seekBar.setMeasurementUnit("KM");
//                    }
//
//
//                    return true;
//                }
//            });


        }

    }


}
