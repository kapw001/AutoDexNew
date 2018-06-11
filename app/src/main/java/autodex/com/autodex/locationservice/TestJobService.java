package autodex.com.autodex.locationservice;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import autodex.com.autodex.Util;

/**
 * Created by yasar on 8/11/17.
 */

public class TestJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), SimpleWakefulService.class);
        getApplicationContext().startService(service);
        Util.scheduleJob(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
