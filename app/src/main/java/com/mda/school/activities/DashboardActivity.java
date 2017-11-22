package com.mda.school.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mda.school.fragments.CurrentPositionFragment;
import com.mda.school.fragments.LastPositionFragment;
import com.mda.school.model.Car;
import com.mda.school.persistence.DBHelper;

import java.util.Date;

public class DashboardActivity extends FragmentActivity implements CurrentPositionFragment.OnFragmentInteractionListener,
                                                                    LastPositionFragment.OnFragmentInteractionListener{

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1080;
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
        db = new DBHelper(this);
        currentLocation = null;
        findLastKnowPosition();
    }

    private void findLastKnowPosition() {
        Car car = db.getFirstCar();
        LastPositionFragment lastPos = (LastPositionFragment)getFragmentManager().findFragmentById(R.id.frag_last_pos);
        if(car == null)
            lastPos.getTvLastPosition().setText(getString(R.string.tv_empty_pos));
        else
            lastPos.getTvLastPosition().setText(car.getLocation().toString());
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, "New location found -> " + location.toString());
        currentLocation = location;

        CurrentPositionFragment curPos = (CurrentPositionFragment)getFragmentManager().findFragmentById(R.id.frag_current_pos);
        curPos.getTvCurrentPosition().setText(location.toString());
    }

    public void onSaveButtonClicked() {
        if(currentLocation == null) {
            Toast.makeText(this, getString(R.string.toast_current_location_null), Toast.LENGTH_SHORT).show();
            return;
        }
        Car c = new Car();
        c.setLocation(currentLocation);
        c.setDate(new Date(System.currentTimeMillis()));
        db.addCar(c);
        findLastKnowPosition();
    }

    public Car getLastKnowCar() {
        return db.getFirstCar();
    }

    private void requestLocation() {
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

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //TODO Explain user -> asynchrony
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_FINE_LOCATION);
            }
            return;
        }
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
            default: break;
        }
    }

    private void dummy() {
        //db.removeCars();
        //addDummyCars();
        printAllCars();
        printFirstCar();
    }

    private void printFirstCar() {
        Car car = db.getFirstCar();
        if(car == null)
            Log.d(TAG, "There is no car");
        else
            Log.d(TAG, "Is the first car: " + car.toString());
    }

    private void addDummyCars() {
        addDummyCar(50.85045, 4.24878, 70000000);
        addDummyCar(50.84045, 4.35788, 500005000);
        addDummyCar(50.81045, 4.04878, 330000000);
        addDummyCar(50.87045, 4.55878, 80050000);
    }

    private void addDummyCar(double lat, double lon, long timeOffset) {
        Car car = new Car(new Location(""), new Date(System.currentTimeMillis() - timeOffset));
        car.getLocation().setLatitude(lat);
        car.getLocation().setLongitude(lon);
        db.addCar(car);
    }

    private void printAllCars() {
        for(Car c : db.getAllCars()) {
            Log.d(TAG, c.toString());
        }
    }
}
