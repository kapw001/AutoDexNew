package autodex.com.autodex.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import autodex.com.autodex.R;
import autodex.com.autodex.customview.UserCustomView;

public class CustomViewActivity extends AppCompatActivity {

    private UserCustomView namecustomview, emailcustomview;

    private LinearLayout test;

    private static final String TAG = "CustomViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);

        namecustomview = (UserCustomView) findViewById(R.id.namecustomview);
        emailcustomview = (UserCustomView) findViewById(R.id.emailcustomview);

        namecustomview.setText("Hey");
        test = (LinearLayout) findViewById(R.id.test);

        disable();

    }

    boolean isF = true;

    public void onChange(View view) {

        if (isF) {
            isF = false;
            enable();
//            namecustomview.enabelEditText();
        } else {
            isF = true;
            disable();
//            namecustomview.disableEditText();
        }

    }

    private void enable() {


        Log.e(TAG, "enable: " + test.getChildCount());


        for (int i = 0; i < test.getChildCount(); i++) {

            ((UserCustomView) test.getChildAt(i)).enabelEditText();

        }

    }

    private void disable() {


        Log.e(TAG, "enable: " + test.getChildCount());


        for (int i = 0; i < test.getChildCount(); i++) {

            ((UserCustomView) test.getChildAt(i)).disableEditText();

            Log.e(TAG, "disable: " + ((UserCustomView) test.getChildAt(i)).getCategory());

        }

    }


}
