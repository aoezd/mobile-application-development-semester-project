package com.aoezdemir.todoapp.model;

public class LatLng {

    private double lat;
    private double lng;

    public LatLng() {
    }

    public LatLng(long lat, long lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}