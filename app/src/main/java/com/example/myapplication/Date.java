package com.example.myapplication;

import androidx.annotation.NonNull;

public class Date {
    private int minute=0;
    private int hour=0;
    private int dayOfMonth=0;
    private int month=0;
    private int year=0;

    public Date() {}

    public Date(int minute, int hour, int dayOfMonth, int month, int year) {
        this.minute = minute;
        this.hour = hour;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return dayOfMonth+"/"+month+"/"+year+"  "+hour+":"+minute;
    }
}
