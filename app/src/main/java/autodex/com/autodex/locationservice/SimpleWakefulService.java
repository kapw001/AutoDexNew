package autodex.com.autodex.locationservice;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import autodex.com.autodex.R;

/**
 * Created by yasar on 8/11/17.
 */

public class SimpleWakefulService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static String tagName = "SimpleWakefulService";
    private static final String TAG = "SimpleWakefulService";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private LocationRequest mLocationRequest;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SimpleWakefulService() {
        super(tagName);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);


//        if (mLastLocation != null) {
        Log.e(TAG, "onHandleIntent: location " + mLastLocation.getLatitude() + "   " + mLastLocation.getLongitude());
//            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        Toast.makeText(getApplicationContext(), "" + mLastLocation.getLatitude() + "   " + mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
//        }


        Log.e(TAG, "onHandleIntent: ");
//        Toast.makeText(this, "Called", Toast.LENGTH_SHORT).show();
//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        // Define a listener that responds to location updates
//        LocationListener locationListener = new LocationListener() {
//            public void onLocationChanged(Location location) {
//                // Called when a new location is found by the network location provider.
//                String lat = Double.toString(location.getLatitude());
//                String lon = Double.toString(location.getLongitude());
//
//                Toast.makeText(SimpleWakefulService.this, "" + lat + "  " + lon, Toast.LENGTH_SHORT).show();
//
//            }
//
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            public void onProviderEnabled(String provider) {
//            }
//
//            public void onProviderDisabled(String provider) {
//            }
//        };
//        // Register the listener with the Location Manager to receive location updates
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        YourWakefulReceiver.completeWakefulIntent(intent);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
