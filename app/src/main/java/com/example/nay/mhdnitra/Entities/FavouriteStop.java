package com.example.nay.mhdnitra.Entities;

public class FavouriteStop {
    private long ID;
    private long IDStop;

    public FavouriteStop(long ID, long IDStop) {
        this.ID = ID;
        this.IDStop = IDStop;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getIDStop() {
        return IDStop;
    }

    public void setIDStop(long IDStop) {
        this.IDStop = IDStop;
    }
}
