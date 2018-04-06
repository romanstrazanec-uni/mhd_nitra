package com.example.nay.mhdnitra.Entities;

public class Time {
    private long ID;
    private long IDLineStop;
    private String time;
    private int weekend;
    private int holidays;

    public Time(long ID, long IDLineStop, String time, int weekend, int holidays) {
        this.ID = ID;
        this.IDLineStop = IDLineStop;
        this.time = time;
        this.weekend = weekend;
        this.holidays = holidays;
    }

    public long getID() {
        return ID;
    }

    public long getIDLineStop() {
        return IDLineStop;
    }

    public String getTime() {
        return time;
    }

    public int getWeekend() {
        return weekend;
    }

    public int getHolidays() {
        return holidays;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setIDLineStop(long IDLineStop) {
        this.IDLineStop = IDLineStop;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setWeekend(int weekend) {
        this.weekend = weekend;
    }

    public void setHolidays(int holidays) {
        this.holidays = holidays;
    }
}
