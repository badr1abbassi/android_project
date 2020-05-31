package com.example.myapplication;

import android.net.Uri;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Calendar;

public class AlarmInfo implements Serializable {

    private int id;
    private Date date;
    private boolean repeat;
    private String message;
    private String status;
    private String imageUrl;


    public AlarmInfo(){}

    public AlarmInfo(int id, Date date, boolean repeat, String message, String status,String imageUrl) {
        this.id = id;
        this.date = date;
        this.repeat = repeat;
        this.message = message;
        this.status = status;
        this.imageUrl=imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    @Exclude
    public Calendar getCalendar() {
        Calendar dateCal=Calendar.getInstance();
        dateCal.set(Calendar.YEAR,this.date.getYear());
        dateCal.set(Calendar.MONTH,this.date.getMonth());
        dateCal.set(Calendar.DAY_OF_MONTH,this.date.getDayOfMonth());
        dateCal.set(Calendar.HOUR,this.date.getHour());
        dateCal.set(Calendar.MINUTE,this.date.getMinute());
        dateCal.set(Calendar.SECOND,0);
        return  dateCal;
    }


}