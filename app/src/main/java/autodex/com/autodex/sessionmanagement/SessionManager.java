package autodex.com.autodex.sessionmanagement;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import autodex.com.autodex.BuildConfig;

/**
 * Created by yasar on 5/10/17.
 */

public class SessionManager {

    public static SessionManager sessionManager;
    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Context
    private Context _context;

    // Shared pref mode
    public static int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String PREF_NAME = BuildConfig.APPLICATION_ID;

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    // Email address (make variable public to access from outside)
    public static final String KEY_PHONENUMBER = "number";
    public static final String KEY_USERID = "userid";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_SQLLITELOADING = "sqlloading";

    public boolean getKeySqlliteloading() {
        return pref.getBoolean(KEY_SQLLITELOADING, false);
    }

    public void setKeySqlliteloading(boolean b) {
        editor.putBoolean(KEY_SQLLITELOADING, b);
        editor.commit();
    }

    // Constructor
    private SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static SessionManager getInstance(Context _context) {
        if (sessionManager == null) {
            sessionManager = new SessionManager(_context);
        }

        return sessionManager;
    }

    /**
     * Create login session
     */
    public void createSession(String number, String password, String userID, String token) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_PASSWORD, password);

        // Storing email in pref
        editor.putString(KEY_PHONENUMBER, number);

        editor.putString(KEY_USERID, userID);
        editor.putString(KEY_TOKEN, token);

        // commit changes
        editor.commit();
    }

    public void changeNumber(String number) {

        // Storing email in pref
        editor.putString(KEY_PHONENUMBER, number);


        // commit changes
        editor.commit();
    }

    public void changeToken(String token) {

        editor.putString(KEY_TOKEN, token);

        editor.apply();
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // user email id
        user.put(KEY_PHONENUMBER, pref.getString(KEY_PHONENUMBER, null));
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));

        // return user
        return user;
    }

    public String getKeyPassword() {
        return pref.getString(KEY_PASSWORD, null);
    }

    public String getKeyPhonenumber() {
        return pref.getString(KEY_PHONENUMBER, null);
    }

    public String getKeyToken() {

        return pref.getString(KEY_TOKEN, null);

    }

    /**
     * Clear session details
     */
    public void claerData() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();


    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setIsLogin(boolean b) {
        editor.putBoolean(IS_LOGIN, b);
        editor.commit();
    }
}
