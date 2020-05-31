package com.example.myapplication;

public class Coordonee {
    String longitude;
    String latitude;

    public Coordonee() {
    }

    public Coordonee(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitue) {
        this.longitude = longitue;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}