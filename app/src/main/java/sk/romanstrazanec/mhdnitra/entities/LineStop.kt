package sk.romanstrazanec.mhdnitra.entities;

public class LineStop {
    private long ID;
    private long IDLine;
    private long IDStop;
    private int number;
    private int direction;

    public LineStop(long ID, long IDLine, long IDStop, int number, int direction) {
        this.ID = ID;
        this.IDLine = IDLine;
        this.IDStop = IDStop;
        this.number = number;
        this.direction = direction;
    }

    public long getID() {
        return this.ID;
    }

    public long getIDLine() {
        return this.IDLine;
    }

    public long getIDStop() {
        return this.IDStop;
    }

    public int getNumber() {
        return this.number;
    }

    public int getDirection() {
        return direction;
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

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
