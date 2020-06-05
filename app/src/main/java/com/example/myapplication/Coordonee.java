package com.example.myapplication;

public class Coordonee {
    String longitude;
    String latitude;
    String dateLocalisation;

    public Coordonee() {
    }

    public Coordonee(String longitude, String latitude,String dateLocalisation) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.dateLocalisation=dateLocalisation;
    }

    public String getDateLocalisation() {
        return dateLocalisation;
    }

    public void setDateLocalisation(String dateLocalisation) {
        this.dateLocalisation = dateLocalisation;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}