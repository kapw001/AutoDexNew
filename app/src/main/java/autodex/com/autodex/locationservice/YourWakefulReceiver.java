package autodex.com.autodex.locationservice;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

/**
 * Created by yasar on 8/11/17.
 */

public class YourWakefulReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "YourWakefulReceiver Called", Toast.LENGTH_SHORT).show();
        Intent service = new Intent(context, SimpleWakefulService.class);
        startWakefulService(context, service);
    }

}