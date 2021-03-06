package com.mda.school.model;

import android.location.Location;

import java.util.Date;

/**
 * Created by michael on 22/11/17.
 */

public class Car {
    private int id;
    private Location location;
    private String address;
    private Date date;

    public Car() {
        location = new Location("");
        date = new Date();
    }

    public Car(Location loc, Date date) {
        this.location = loc;
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", location=" + location +
                ", date=" + date +
                '}';
    }
}
