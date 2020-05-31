package com.example.myapplication;

public class Coordonee {
    String longitue;
    String latitude;

    public Coordonee() {
    }

    public Coordonee(String longitue, String latitude) {
        this.longitue = longitue;
        this.latitude = latitude;
    }

    public String getLongitue() {
        return longitue;
    }

    public void setLongitue(String longitue) {
        this.longitue = longitue;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}