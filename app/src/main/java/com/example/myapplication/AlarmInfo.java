package com.example.myapplication;

import java.io.Serializable;
import java.util.Calendar;

public class AlarmInfo implements Serializable {

    private int id;
    private Calendar calendar;
    private boolean repeat;
    private String message;
    private String status;

    public AlarmInfo(){}

    public AlarmInfo(Calendar calendar, boolean repeat, String message, String status) {
        this.calendar = calendar;
        this.repeat = repeat;
        this.message = message;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
