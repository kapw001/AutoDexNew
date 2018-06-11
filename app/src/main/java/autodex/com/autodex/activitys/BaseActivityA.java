package autodex.com.autodex.activitys;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import autodex.com.autodex.R;
import autodex.com.autodex.Utility;

/**
 * Created by yasar on 7/9/17.
 */

public class BaseActivityA extends Activity {


    public void showT(String msg) {
        Utility.showMsg(getApplicationContext(), msg);
    }


    public void transitionActivity() {
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);
    }


}
