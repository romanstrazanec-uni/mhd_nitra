package sk.romanstrazanec.mhdnitra.entities;

public class FavouriteLine {
    private long ID;
    private long IDLine;

    public FavouriteLine(long ID, long IDLine) {
        this.ID = ID;
        this.IDLine = IDLine;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getIDLine() {
        return IDLine;
    }

    public void setIDLine(long IDLine) {
        this.IDLine = IDLine;
    }
}
