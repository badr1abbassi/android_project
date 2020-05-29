package com.example.myapplication;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.net.URI;
import java.util.Calendar;

public class AlarmInfo implements Serializable {

    private int id;
    private Calendar calendar;
    private boolean repeat;
    private String message;
    private String status;
    private Uri image;
    private String imageUrl;


    public AlarmInfo(){}

    public AlarmInfo(int id, Calendar calendar, boolean repeat, String message, String status, Uri image,String imageUrl) {
        this.id = id;
        this.calendar = calendar;
        this.repeat = repeat;
        this.message = message;
        this.status = status;
        this.image = image;
        this.imageUrl=imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    @Exclude
    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Exclude
    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}