package autodex.com.autodex.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by yasar on 7/6/18.
 */

public class App extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);

    }
}
