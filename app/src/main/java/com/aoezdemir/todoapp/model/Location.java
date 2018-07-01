package com.aoezdemir.todoapp.model;

public class Location {

    private String name;

    private LatLng latlng;

    public Location() {
    }

    public Location(String name, LatLng latlng) {
        this.name = name;
        this.latlng = latlng;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}