package autodex.com.autodex.activitys;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.android.volley.error.VolleyError;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import autodex.com.autodex.MessageEvent;
import autodex.com.autodex.R;
import autodex.com.autodex.UploadAndDownloadData;
import autodex.com.autodex.Utility;
import autodex.com.autodex.interfacecallback.WebResponse;
import autodex.com.autodex.sessionmanagement.SessionManager;
import autodex.com.autodex.webrequest.WebUrl;
import volleywebrequest.com.mysqlitelibrary.GetContactListResponse;

import static autodex.com.autodex.sessionmanagement.SessionManager.KEY_TOKEN;

public class MyDialog extends BaseActivity {
    private static final String TAG = "MyDialog";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_dialog);


        sharedPreferences = getSharedPreferences(SessionManager.PREF_NAME, SessionManager.PRIVATE_MODE);
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MyDialog.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MyDialog.this);
        }
        builder.setTitle("Sync contact")
                .setMessage("Are you sure you want to sync contacts?")
                .setCancelable(false)
//                .setCanceledOnTouchOutside(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        EventBus.getDefault().post(new MessageEvent("uploadtoserver"));


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();

                        finish();

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)

                .setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            finish();
                            arg0.dismiss();
                        }
                        return true;
                    }
                })


                .show();
    }


}
