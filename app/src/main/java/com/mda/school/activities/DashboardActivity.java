package com.mda.school.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mda.school.async.AsyncResponse;
import com.mda.school.async.GeocodingAsyncTask;
import com.mda.school.fragments.CurrentPositionFragment;
import com.mda.school.fragments.LastPositionFragment;
import com.mda.school.model.Car;
import com.mda.school.persistence.DBHelper;

import java.util.Date;

public class DashboardActivity extends AppCompatActivity implements CurrentPositionFragment.OnFragmentInteractionListener,
        LastPositionFragment.OnFragmentInteractionListener {

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1080;
    private static final int PERMISSION_REQUEST_INTERNET = 1070;
    public static final String CAR_DATABASE = "CAR_DATABASE_DASHBOARD";
    private final String TAG = getClass().getSimpleName();
    private Location currentLocation;
    private DBHelper db;

    @Override
    protected void onResume() {
        super.onResume();
        requestLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBHelper(this);
        currentLocation = null;
        findLastKnowPosition();
        requestLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void findLastKnowPosition() {
        Car car = db.getFirstCar();
        LastPositionFragment lastPos = (LastPositionFragment) getFragmentManager().findFragmentById(R.id.frag_last_pos);
        if (car == null || car.getAddress() == null && car.getLocation() == null) {
            lastPos.getTvLastPosition().setText(getString(R.string.tv_empty_pos));
            lastPos.getmBtnNavigate().setEnabled(false);
            lastPos.getmBtnSharePos().setEnabled(false);
        } else {
            if (car.getAddress() == null)
                lastPos.getTvLastPosition().setText("Latitude: " + car.getLocation().getLatitude()
                        + "\nLongitude: " + car.getLocation().getLongitude());
            else
                lastPos.getTvLastPosition().setText(car.getAddress());

            lastPos.getmBtnNavigate().setEnabled(true);
            lastPos.getmBtnSharePos().setEnabled(true);
        }
    }

    private void handleNewLocation(final Location location) {
        if (location == null) return;
        Log.d(TAG, "New location found: " + location.toString());
        currentLocation = location;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                //TODO Explain user -> asynchrony
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                        PERMISSION_REQUEST_INTERNET);
            }
            Log.d(TAG, "handleNewLocation");
            return;
        }

        new GeocodingAsyncTask(new AsyncResponse<String>() {
            @Override
            public void processFinish(String output) {
                CurrentPositionFragment curPos = (CurrentPositionFragment) getFragmentManager().findFragmentById(R.id.frag_current_pos);
                if (curPos == null) return;
                if (output != null)
                    curPos.getTvCurrentPosition().setText(output);
                else if (location != null)
                    curPos.getTvCurrentPosition().setText("Latitude: " + location.getLatitude()
                            + "\nLongitude: " + location.getLongitude());
                else
                    curPos.getTvCurrentPosition().setText(getString(R.string.tv_empty_pos));
            }
        }).execute(currentLocation);
    }

    public void onSaveButtonClicked() {
        if (currentLocation == null) {
            Toast.makeText(this, getString(R.string.toast_current_location_null), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "Save location : " + currentLocation.toString());
        new GeocodingAsyncTask(new AsyncResponse<String>() {
            @Override
            public void processFinish(String output) {
                Car c = new Car();
                c.setLocation(currentLocation);
                c.setDate(new Date(System.currentTimeMillis()));
                c.setAddress(output);

                db.addCar(c);
                findLastKnowPosition();
            }
        }).execute(currentLocation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                Intent intent = new Intent(DashboardActivity.this, HistoryActivity.class);
                //TODO addExtra...
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public Car getLastKnowCar() {
        return db.getFirstCar();
    }

    @Override
    public void onRefreshLocationClicked() {
        requestLocation();
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //TODO Explain user -> asynchrony
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    PERMISSION_REQUEST_FINE_LOCATION);
            }
            return;
        }
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //Handle last know location before update the new location
        handleNewLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                handleNewLocation(location);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Access granted for fine_location");
                    requestLocation();
                } else {
                    Log.d(TAG, "Access not granted for fine_location");
                }
                return;
            }
            case PERMISSION_REQUEST_INTERNET: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Access granted for internet");
                    handleNewLocation(currentLocation);
                } else {
                    Log.d(TAG, "Access not granted for internet");
                }
                return;
            }
            default:
                Log.d(TAG, "onRequestPermissionsResult requestcode not handled for: " + requestCode);
                break;
        }
    }
}
