package sk.romanstrazanec.mhdnitra.entities;

public class Time {
    private long ID;
    private long IDLineStop;
    private int hour;
    private int minute;
    private int weekend;
    private int holidays;

    public Time(long ID, long IDLineStop, int hour, int minute, int weekend, int holidays) {
        this.ID = ID;
        this.IDLineStop = IDLineStop;
        this.hour = hour;
        this.minute = minute;
        this.weekend = weekend;
        this.holidays = holidays;
    }

    public long getID() {
        return ID;
    }

    public long getIDLineStop() {
        return IDLineStop;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
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

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setWeekend(int weekend) {
        this.weekend = weekend;
    }

    public void setHolidays(int holidays) {
        this.holidays = holidays;
    }
}
