package com.example.nay.mhdnitra.Entities;

public class LineStop {
    private long ID;
    private long IDLine;
    private long IDStop;
    private int number;

    public LineStop(long ID, long IDLine, long IDStop, int number) {
        this.ID = ID;
        this.IDLine = IDLine;
        this.IDStop = IDStop;
        this.number = number;
    }

    public long getID(){
        return this.ID;
    }

    public long getIDLine(){
        return this.IDLine;
    }

    public long getIDStop(){
        return this.IDStop;
    }

    public int getNumber(){
        return this.number;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setIDLine(long IDLine) {
        this.IDLine = IDLine;
    }

    public void setIDStop(long IDStop) {
        this.IDStop = IDStop;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
