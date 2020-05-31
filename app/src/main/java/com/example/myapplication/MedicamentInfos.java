package com.example.myapplication;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MedicamentInfos implements Serializable {

    private int id;
    private Date date;
    private boolean repeat;
    private String message;
    private String status;
    private String imageUrl;


    public MedicamentInfos(){}

    public MedicamentInfos(int id, Date date, boolean repeat, String message, String status, String imageUrl) {
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
    public Calendar getCalendar() throws ParseException {
        String src=this.date.getYear()+"-"+this.date.getMonth()+"-"+this.date.getDayOfMonth()+" "+this.date.getHour()+":"+this.date.getMinute();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        java.util.Date myDate = format.parse(src);
        Calendar dateCal=Calendar.getInstance();
        dateCal.setTime(myDate);
        return  dateCal;
    }
    @Exclude
    public Calendar getTime() throws ParseException{
        String src=this.date.getHour()+":"+this.date.getMinute();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        java.util.Date myDate = format.parse(src);
        Calendar dateCal=Calendar.getInstance();
        dateCal.setTime(myDate);
        return dateCal;
    }


}