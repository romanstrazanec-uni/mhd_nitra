package sk.romanstrazanec.mhdnitra.java.entities;

public class Line {
    private long ID;
    private String line;

    public Line(long ID, String line) {
        this.ID = ID;
        this.line = line;
    }

    public long getID() {
        return this.ID;
    }

    public String getLine() {
        return this.line;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
