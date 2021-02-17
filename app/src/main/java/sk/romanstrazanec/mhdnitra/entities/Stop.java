package sk.romanstrazanec.mhdnitra.entities;

public class Stop {
    private long ID;
    private String name;

    public Stop(long ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public long getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }
}
