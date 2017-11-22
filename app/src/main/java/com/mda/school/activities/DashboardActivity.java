package com.mda.school.activities;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mda.school.model.Car;
import com.mda.school.persistence.DBHelper;

import java.util.Date;

public class DashboardActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        db = new DBHelper(this);
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
