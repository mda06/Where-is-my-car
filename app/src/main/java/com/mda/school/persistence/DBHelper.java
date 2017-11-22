package com.mda.school.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mda.school.model.Car;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by michael on 22/11/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "Cars.db";
    public static final String CARS_TABLE_NAME = "cars";
    public static final String CARS_COLUMN_ID = "id";
    public static final String CARS_COLUMN_LATITUDE = "latitude";
    public static final String CARS_COLUMN_LONGITUDE = "longitude";
    public static final String CARS_COLUMN_DATE = "date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CARS_TABLE_NAME +
                "(" + CARS_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + CARS_COLUMN_LATITUDE + " REAL, "
                + CARS_COLUMN_LONGITUDE + " REAL, "
                + CARS_COLUMN_DATE + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CARS_TABLE_NAME);
        onCreate(db);
    }

    public boolean addCar (Car car) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CARS_COLUMN_LATITUDE, car.getLocation().getLatitude());
        contentValues.put(CARS_COLUMN_LONGITUDE, car.getLocation().getLongitude());
        contentValues.put(CARS_COLUMN_DATE, car.getDate().getTime());
        db.insert(CARS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Car getFirstCar() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + CARS_TABLE_NAME + " ORDER BY " + CARS_COLUMN_DATE + " DESC", null);
        res.moveToFirst();
        return getCar(res);
    }

    public void removeCars() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + CARS_TABLE_NAME);
    }

    public ArrayList<Car> getAllCars() {
        ArrayList<Car> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + CARS_TABLE_NAME, null);
        res.moveToFirst();

        while(!res.isAfterLast()){
            list.add(getCar(res));
            res.moveToNext();
        }
        return list;
    }

    private Car getCar(Cursor res) {
        if(res.isAfterLast()) return null;
        Car car = new Car();
        car.setId(res.getInt(res.getColumnIndex(CARS_COLUMN_ID)));
        car.getLocation().setLatitude(res.getDouble(res.getColumnIndex(CARS_COLUMN_LATITUDE)));
        car.getLocation().setLongitude(res.getDouble(res.getColumnIndex(CARS_COLUMN_LONGITUDE)));
        car.setDate(new Date(res.getLong(res.getColumnIndex(CARS_COLUMN_DATE))));
        return car;
    }
}
