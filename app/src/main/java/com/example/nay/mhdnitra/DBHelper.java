package com.example.nay.mhdnitra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nay.mhdnitra.Entities.Line;
import com.example.nay.mhdnitra.Entities.LineStop;
import com.example.nay.mhdnitra.Entities.Stop;
import com.example.nay.mhdnitra.Entities.Time;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "mhd";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String I = "INTEGER";
        final String T = "TEXT";
        final String PK = "INTEGER PRIMARY KEY AUTOINCREMENT";

        createTable(db, MyContract.Line.TABLE_NAME, new String[]{MyContract.Line.COLUMN_ID, MyContract.Line.COLUMN_LINE}, new String[]{PK, T});
        createTable(db, MyContract.Stop.TABLE_NAME, new String[]{MyContract.Stop.COLUMN_ID, MyContract.Stop.COLUMN_NAME}, new String[]{PK, T});
        createTable(db, MyContract.LineStop.TABLE_NAME,
                new String[]{MyContract.LineStop.COLUMN_ID,
                        MyContract.LineStop.COLUMN_ID_LINE,
                        MyContract.LineStop.COLUMN_ID_STOP,
                        MyContract.LineStop.COLUMN_NUMBER},
                new String[]{PK, I, I, I});
        createTable(db, MyContract.Time.TABLE_NAME,
                new String[]{MyContract.Time.COLUMN_ID,
                        MyContract.Time.COLUMN_ID_LINESTOP,
                        MyContract.Time.COLUMN_TIME,
                        MyContract.Time.COLUMN_WEEKEND,
                        MyContract.Time.COLUMN_HOLIDAYS},
                new String[]{PK, I, T, I, I});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        dropTable(db, MyContract.Line.TABLE_NAME);
        dropTable(db, MyContract.Stop.TABLE_NAME);
        dropTable(db, MyContract.LineStop.TABLE_NAME);
        dropTable(db, MyContract.Time.TABLE_NAME);
        onCreate(db);
    }

    private void dropTable(SQLiteDatabase db, String tableName){
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    private void createTable(SQLiteDatabase db, String tableName, String[] columns, String[] types){
        if (columns.length != types.length) return;

        StringBuilder c = new StringBuilder();
        for(int i = 0; i < columns.length; i++) {
            c.append(columns[i]).append(" ").append(types[i]);
            if (i != columns.length - 1) c.append(", ");
        }

        String query = "CREATE TABLE " + tableName +  " (" + c + ")";
        db.execSQL(query);
    }

    public Cursor getCursor(String tableName){
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + tableName;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        db.close();
        return c;
    }

    public void addLine(Line l){
        ContentValues values = new ContentValues();
        values.put(MyContract.Line.COLUMN_LINE, l.getLine());

        SQLiteDatabase db = getWritableDatabase();
        long newRow = db.insert(MyContract.Line.TABLE_NAME, null, values);
        db.close();
    }

    public void addStop(Stop s){
        ContentValues values = new ContentValues();
        values.put(MyContract.Stop.COLUMN_NAME, s.getName());

        SQLiteDatabase db = getWritableDatabase();
        long newRow = db.insert(MyContract.Stop.TABLE_NAME, null, values);
        db.close();
    }

    public void addLineStop(LineStop ls){
        ContentValues values = new ContentValues();
        values.put(MyContract.LineStop.COLUMN_ID_LINE, ls.getIDLine());
        values.put(MyContract.LineStop.COLUMN_ID_STOP, ls.getIDStop());
        values.put(MyContract.LineStop.COLUMN_NUMBER, ls.getNumber());

        SQLiteDatabase db = getWritableDatabase();
        long newRow = db.insert(MyContract.LineStop.TABLE_NAME, null, values);
        db.close();
    }

    public void addTime(Time t){
        ContentValues values = new ContentValues();
        values.put(MyContract.Time.COLUMN_ID_LINESTOP, t.getIDLineStop());
        values.put(MyContract.Time.COLUMN_TIME, t.getTime());
        values.put(MyContract.Time.COLUMN_WEEKEND, t.getWeekend());
        values.put(MyContract.Time.COLUMN_HOLIDAYS, t.getHolidays());

        SQLiteDatabase db = getWritableDatabase();
        long newRow = db.insert(MyContract.Time.TABLE_NAME, null, values);
        db.close();
    }

    public void updateLine(Line l){
        ContentValues values = new ContentValues();
        values.put(MyContract.Line.COLUMN_LINE, l.getLine());

        SQLiteDatabase db = getWritableDatabase();
        db.update(MyContract.Line.TABLE_NAME, values, MyContract.Line.COLUMN_ID + " = ?", new String[]{String.valueOf(l.getID())});
        db.close();
    }

    public void updateStop(Stop s){
        ContentValues values = new ContentValues();
        values.put(MyContract.Stop.COLUMN_NAME, s.getName());

        SQLiteDatabase db = getWritableDatabase();
        db.update(MyContract.Stop.TABLE_NAME, values, MyContract.Stop.COLUMN_ID + " = ?", new String[]{String.valueOf(s.getID())});
        db.close();
    }

    public void updateLineStop(LineStop ls){
        ContentValues values = new ContentValues();
        values.put(MyContract.LineStop.COLUMN_ID_LINE, ls.getIDLine());
        values.put(MyContract.LineStop.COLUMN_ID_STOP, ls.getIDStop());
        values.put(MyContract.LineStop.COLUMN_NUMBER, ls.getNumber());

        SQLiteDatabase db = getWritableDatabase();
        db.update(MyContract.LineStop.TABLE_NAME, values, MyContract.LineStop.COLUMN_ID + " = ?", new String[]{String.valueOf(ls.getID())});
        db.close();
    }

    public void updateTime(Time t){
        ContentValues values = new ContentValues();
        values.put(MyContract.Time.COLUMN_ID_LINESTOP, t.getIDLineStop());
        values.put(MyContract.Time.COLUMN_TIME, t.getTime());
        values.put(MyContract.Time.COLUMN_WEEKEND, t.getWeekend());
        values.put(MyContract.Time.COLUMN_HOLIDAYS, t.getHolidays());

        SQLiteDatabase db = getWritableDatabase();
        db.update(MyContract.Time.TABLE_NAME, values, MyContract.Time.COLUMN_ID + " = ?", new String[]{String.valueOf(t.getID())});
        db.close();
    }

    public void deleteLine(long ID){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MyContract.Line.TABLE_NAME, MyContract.Line.COLUMN_ID + " = ?", new String[]{String.valueOf(ID)});
        db.close();
    }

    public void deleteStop(long ID){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MyContract.Stop.TABLE_NAME, MyContract.Stop.COLUMN_ID + " = ?", new String[]{String.valueOf(ID)});
        db.close();
    }

    public void deleteLineStop(long ID){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MyContract.LineStop.TABLE_NAME, MyContract.LineStop.COLUMN_ID + " = ?", new String[]{String.valueOf(ID)});
        db.close();
    }

    public void deleteTime(long ID){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MyContract.Time.TABLE_NAME, MyContract.Time.COLUMN_ID + " = ?", new String[]{String.valueOf(ID)});
        db.close();
    }

    private void MHDNitra() {
        String[] lines = new String[]{"1", "2", "4", "6", "7", "8", "9", "10", "11", "12", "13", "14",
                "15", "16", "17", "18", "19", "21", "22", "24", "25", "26", "27", "30", "32", "33", "C35"};
        for (int i = 0; i < lines.length; i++) addLine(new Line(i + 1, lines[i]));

        String[] stops = new String[]{
                // Drážovce
                "Belopotockého",
                "Drážovce",
                "PD Drážovce",
                "Pri kríži",
                "Priemyselný park I",
                "Priemyselný park II",
                "Priemyselný park III",
                "Priemyselný park IV",
                "Priemyselný park V",
                "Rázcestie priemyselný park",

                // Chrenová, Janíkovce
                "Atletický štadión",
                "Bohúňová",
                "Ďurčanského",
                "Gorazdova",
                "Chrenovský cintorín",
                "Janíkovce",
                "Janíkovská cesta",
                "Letecká",
                "Levická",
                "Lomnická",
                "Malé Janíkovce I",
                "Malé Janíkovce II",
                "Mikov dvor",
                "Plynárenská",
                "Poliklinika Chrenová",
                "Sitnianska",
                "Slamkova",
                "Vinohrady Chrenová",
                "Výstavisko",
                "ZŠ Janíkovce",

                // Čermáň
                "Cabajská",
                "Červeňova",
                "Dolnočermánska",
                "Golianova",
                "Hanulova",
                "Hattalova",
                "Kostolná",
                "NAD",
                "Nedbalova",
                "Nový cintorín",
                "SEC",
                "Stavebná škola",
                "Tehelná",
                "Vodohospodárske stavby",
                "ZŠ Škultétyho",
                "Železničiarska",

                // Klokočina, Diely
                "Bizetova",
                "Čajkovského",
                "Kmeťova",
                "Mestská hala",
                "Mikovíniho",
                "Murániho",
                "Nitrianska",
                "Partizánska",
                "Poliklinika Klokočina",
                "Popradská",
                "Považská",
                "Pražská",
                "Rázcestie Kmeťova",
                "Tokajská",
                "Viničky",
                "Zvolenská",
                "Žilinská",

                // Krškany, Ivánka, Jarok, Branč
                "Branč",
                "Branč, Arkuš",
                "Branč, kult. dom",
                "Branč, Kurucká",
                "Branč, pneuservis",
                "Branč, Veľkoveská",
                "Branč, železničná stanica",
                "Dvorčianska",
                "Hájnická",
                "Horné Krškany",
                "Idea",
                "Ivánka pri Nitre, kult. dom",
                "Ivánka pri Nitre, Luk",
                "Ivánka pri Nitre, Orolská",
                "Ivánka pri Nitre, Texiplast",
                "Ivánka pri Nitre, Žel. stanica",
                "Jakuba Haška",
                "Jarocká",
                "Jurský dvor",
                "Kasárne Krškany",
                "Liaharenský podnik",
                "Lukov dvor",
                "Mevak",
                "Murgašova",
                "Na priehon",
                "Nitrafrost",
                "Nitrianske strojárne",
                "Párovské háje",
                "Plastika",
                "Prameň",
                "Priemyselná",
                "Rázcestie priemyselná",
                "Stromová",
                "Trans Motel",
                "Záborskeho",
                "ZŠ Krškany",

                // Lužianky
                "Lužianky Hlohovecká",
                "Lužianky Korytovská",
                "Lužianky Rastislavova",
                "Lužianky rázc., Vinárska",
                "Lužianky VÚŽV",
                "Lužianky ZŠ",
                "Lužianky, Vinárska",
                "Lužianky, železničná stanica",

                // Mlynárce, Kynek
                "Bolečkova",
                "Cintorín Mlynárce",
                "Dubíkova",
                "Edisonova",
                "Ferrenit",
                "Chotárna",
                "Kynek",
                "NIPEK",
                "Potočná",
                "Rastislavova",
                "Rybárska",
                "Železničná zastávka Mlynárce",

                // Staré mesto
                "8. mája",
                "Braneckého",
                "CENTRUM",
                "Divadlo Andreja Bagara",
                "Ďurková",
                "Fraňa Mojtu",
                "Hlavná",
                "Hodžova",
                "Hollého",
                "Kalvária",
                "Kasalova",
                "Kavcova",
                "Mestský park",
                "Nábrežie mládeže",
                "Palárikova",
                "Párovská",
                "Predmostie",
                "Rázcestie Autobusová stanica",
                "Rázcestie Železničná stanica",
                "Rázusová",
                "Správa ciest",
                "Špitálska",
                "Štúrová",
                "Univerzity",
                "Wilsonovo nábrežie",
                "Záhradná",
                "Železničná stanica Nitra",

                // Zobor, Hrnčiarovce, Štitáre
                "Amfiteáter",
                "Drozdí chodník",
                "Hornozoborská",
                "Hrnčiarovce",
                "Hrnčiarovce Krajná",
                "Hrnčiarovce pod Sokolom",
                "Hrnčiarovce Šopronská",
                "Hrnčiarovce Vinohrady",
                "Hrnčiarovce ZŠ",
                "Chmeľová dolina",
                "Jánskeho",
                "Klinčeková",
                "Lanovka",
                "Martinská dolina",
                "Metodova",
                "Moskovská",
                "Muškátová",
                "Nemocnica Zobor",
                "Orechová",
                "Orgovánová",
                "Panská dolina",
                "Pod Lupkou",
                "Pod Zoborom",
                "Podhájska",
                "Rázcestie Metodova",
                "Rázcestie Moskovská",
                "Rázcestie Panská dolina",
                "Strmá",
                "Šindolka",
                "Šindolka, Dolnohorská",
                "Štitáre",
                "Štitáre ku Gáborke",
                "Štitáre Šoproš",
                "Turistická",
                "Urbancova",
                "Úzka",
                "Vašínova",
                "Veterinárska",
                "Vinárske závody",
                "Zariadenie pre seniorov Zobor",
                "ZŠ pod Zoborom",

                // Obchodné centrá
                "Andreja Hlinkum, Centro",
                "Hypermarket TESCO",
                "Chrenovská MAX",
                "METRO"
        };

        for (int i = 0; i < stops.length; i++) addStop(new Stop(i + 1, stops[i]));
    }
}
