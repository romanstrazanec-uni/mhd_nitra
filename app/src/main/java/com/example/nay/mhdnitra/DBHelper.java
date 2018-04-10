package com.example.nay.mhdnitra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nay.mhdnitra.Entities.FavouriteLine;
import com.example.nay.mhdnitra.Entities.FavouriteStop;
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
        createTable(db, MyContract.FavouriteLine.TABLE_NAME, new String[]{MyContract.FavouriteLine.COLUMN_ID, MyContract.FavouriteLine.COLUMN_ID_LINE},
                new String[]{PK, I});
        createTable(db, MyContract.FavouriteStop.TABLE_NAME, new String[]{MyContract.FavouriteStop.COLUMN_ID, MyContract.FavouriteStop.COLUMN_ID_STOP},
                new String[]{PK, I});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        dropTable(db, MyContract.Line.TABLE_NAME);
        dropTable(db, MyContract.Stop.TABLE_NAME);
        dropTable(db, MyContract.LineStop.TABLE_NAME);
        dropTable(db, MyContract.Time.TABLE_NAME);
        dropTable(db, MyContract.FavouriteLine.TABLE_NAME);
        dropTable(db, MyContract.FavouriteStop.TABLE_NAME);
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

    public Cursor getCursor(String[] select, String from, String[] join, String[] lefton, String[] righton, String where, String groupby, String orderby) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = "";
        if (select != null) {
            for (String s : select) selection += s + ",";
        } else selection = "*,";

        String query = "SELECT " + selection.substring(0, selection.length() - 1) + " FROM " + from;

        if (join != null && lefton != null && righton != null && join.length == lefton.length && lefton.length == righton.length) {
            String leftjoin = from;
            for (int i = 0; i < join.length; i++) {
                query += " JOIN " + join[i] + " ON " + leftjoin + "." + lefton[i] + " = " + join[i] + "." + righton[i];
                leftjoin = join[i];
            }
        }

        if (where != null) query += " WHERE " + where;
        if (groupby != null) query += " GROUP BY " + groupby;
        if (orderby != null) query += " ORDER BY " + orderby;
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

    public void addFavouriteLine(FavouriteLine fl) {
        ContentValues values = new ContentValues();
        values.put(MyContract.FavouriteLine.COLUMN_ID_LINE, fl.getIDLine());

        SQLiteDatabase db = getWritableDatabase();
        long newRow = db.insert(MyContract.FavouriteLine.TABLE_NAME, null, values);
        db.close();
    }

    public void addFavouriteStop(FavouriteStop fs) {
        ContentValues values = new ContentValues();
        values.put(MyContract.FavouriteStop.COLUMN_ID_STOP, fs.getIDStop());

        SQLiteDatabase db = getWritableDatabase();
        long newRow = db.insert(MyContract.FavouriteStop.TABLE_NAME, null, values);
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

    public void updateFavouriteLine(FavouriteLine fl) {
        ContentValues values = new ContentValues();
        values.put(MyContract.FavouriteLine.COLUMN_ID_LINE, fl.getIDLine());
        ;

        SQLiteDatabase db = getWritableDatabase();
        db.update(MyContract.FavouriteLine.TABLE_NAME, values, MyContract.FavouriteLine.COLUMN_ID + " = ?", new String[]{String.valueOf(fl.getID())});
        db.close();
    }

    public void updateFavouriteStop(FavouriteStop fs) {
        ContentValues values = new ContentValues();
        values.put(MyContract.FavouriteStop.COLUMN_ID_STOP, fs.getIDStop());
        ;

        SQLiteDatabase db = getWritableDatabase();
        db.update(MyContract.FavouriteStop.TABLE_NAME, values, MyContract.FavouriteStop.COLUMN_ID + " = ?", new String[]{String.valueOf(fs.getID())});
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

    public void deleteFavouriteLine(long ID) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MyContract.FavouriteLine.TABLE_NAME, MyContract.FavouriteLine.COLUMN_ID_LINE + " = ?", new String[]{String.valueOf(ID)});
        db.close();
    }

    public void deleteFavouriteStop(long ID) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MyContract.FavouriteStop.TABLE_NAME, MyContract.FavouriteStop.COLUMN_ID + " = ?", new String[]{String.valueOf(ID)});
        db.close();
    }

    public void MHDNitra() {
        String[] lines = new String[]{"1", "2", "4", "6", "7", "8", "9", "10", "11", "12", "13", "14",
                "15", "16", "17", "18", "19", "21", "22", "24", "25", "26", "27", "30", "32", "33", "C35"};
        for (int i = 0; i < lines.length; i++) addLine(new Line(i + 1, lines[i]));

        String[] stops = new String[]{
                // Drážovce
                "Belopotockého",
                "Drážovce",
                "PD Drážovce",
                "Pri kríži",
                "Priemyselný park I", // 5
                "Priemyselný park II",
                "Priemyselný park III",
                "Priemyselný park IV",
                "Priemyselný park V",
                "Rázcestie priemyselný park", // 10

                // Chrenová, Janíkovce
                "Atletický štadión",
                "Bohúňová",
                "Ďurčanského",
                "Gorazdova",
                "Chrenovský cintorín", // 15
                "Janíkovce",
                "Janíkovská cesta",
                "Letecká",
                "Levická",
                "Lomnická", //20
                "Malé Janíkovce I",
                "Malé Janíkovce II",
                "Mikov dvor",
                "Plynárenská",
                "Poliklinika Chrenová", // 25
                "Sitnianska",
                "Slamkova",
                "Vinohrady Chrenová",
                "Výstavisko",
                "ZŠ Janíkovce", // 30

                // Čermáň
                "Cabajská",
                "Červeňova",
                "Dolnočermánska",
                "Edisonova",
                "Golianova", // 35
                "Hanulova",
                "Hattalova",
                "Kostolná",
                "NAD",
                "Nedbalova", // 40
                "Nový cintorín",
                "SEC",
                "Stavebná škola",
                "Tehelná",
                "Vodohospodárske stavby", // 45
                "ZŠ Škultétyho",
                "Železničiarska",

                // Klokočina, Diely
                "Bizetova",
                "Čajkovského",
                "Kmeťova", // 50
                "Mestská hala",
                "Mikovíniho",
                "Murániho",
                "Nitrianska",
                "Partizánska", // 55
                "Poliklinika Klokočina",
                "Popradská",
                "Považská",
                "Pražská",
                "Rázcestie Kmeťova", // 60
                "Tokajská",
                "Viničky",
                "Zvolenská",
                "Žilinská",

                // Krškany, Ivánka, Jarok, Branč
                "Branč", // 65
                "Branč, Arkuš",
                "Branč, kult. dom",
                "Branč, Kurucká",
                "Branč, pneuservis",
                "Branč, Veľkoveská", // 70
                "Branč, železničná stanica",
                "Dvorčianska",
                "Hájnická",
                "Horné Krškany",
                "Idea", // 75
                "Ivánka pri Nitre, kult. dom",
                "Ivánka pri Nitre, Luk",
                "Ivánka pri Nitre, Orolská",
                "Ivánka pri Nitre, Texiplast",
                "Ivánka pri Nitre, Žel. stanica", // 80
                "Jakuba Haška",
                "Jarocká",
                "Jurský dvor",
                "Kasárne Krškany",
                "Liaharenský podnik", // 85
                "Lukov dvor",
                "Mevak",
                "Murgašova",
                "Na priehon",
                "Nitrafrost", // 90
                "Nitrianske strojárne",
                "Párovské háje",
                "Plastika",
                "Prameň",
                "Priemyselná", // 95
                "Rázcestie priemyselná",
                "Stromová",
                "Trans Motel",
                "Záborskeho",
                "ZŠ Krškany", // 100

                // Lužianky
                "Lužianky Hlohovecká",
                "Lužianky Korytovská",
                "Lužianky Rastislavova",
                "Lužianky rázc., Vinárska",
                "Lužianky VÚŽV", // 105
                "Lužianky ZŠ",
                "Lužianky, Vinárska",
                "Lužianky, železničná stanica",

                // Mlynárce, Kynek
                "Bolečkova",
                "Cintorín Mlynárce", // 110
                "Dubíkova",
                "Ferrenit",
                "Chotárna",
                "Kynek",
                "NIPEK", // 115
                "Potočná",
                "Rastislavova",
                "Rybárska",
                "Železničná zastávka Mlynárce",

                // Staré mesto
                "8. mája", // 120
                "Braneckého",
                "CENTRUM",
                "Divadlo Andreja Bagara",
                "Ďurková",
                "Fraňa Mojtu", // 125
                "Hlavná",
                "Hodžova",
                "Hollého",
                "Kalvária",
                "Kasalova", // 130
                "Kavcova",
                "Mestský park",
                "Nábrežie mládeže",
                "Palárikova",
                "Párovská", // 135
                "Predmostie",
                "Rázcestie Autobusová stanica",
                "Rázcestie Železničná stanica",
                "Rázusová",
                "Správa ciest", // 140
                "Špitálska",
                "Štúrová",
                "Univerzity",
                "Wilsonovo nábrežie",
                "Záhradná", // 145
                "Železničná stanica Nitra",

                // Zobor, Hrnčiarovce, Štitáre
                "Amfiteáter",
                "Drozdí chodník",
                "Hornozoborská",
                "Hrnčiarovce", // 150
                "Hrnčiarovce Krajná",
                "Hrnčiarovce pod Sokolom",
                "Hrnčiarovce Šopronská",
                "Hrnčiarovce Vinohrady",
                "Hrnčiarovce ZŠ", // 155
                "Chmeľová dolina",
                "Jánskeho",
                "Klinčeková",
                "Lanovka",
                "Martinská dolina", // 160
                "Metodova",
                "Moskovská",
                "Muškátová",
                "Nemocnica Zobor",
                "Orechová", // 165
                "Orgovánová",
                "Panská dolina",
                "Pod Lupkou",
                "Pod Zoborom",
                "Podhájska", // 170
                "Rázcestie Metodova",
                "Rázcestie Moskovská",
                "Rázcestie Panská dolina",
                "Strmá",
                "Šindolka", // 175
                "Šindolka, Dolnohorská",
                "Štitáre",
                "Štitáre ku Gáborke",
                "Štitáre Šoproš",
                "Turistická", // 180
                "Urbancova",
                "Úzka",
                "Vašínova",
                "Veterinárska",
                "Vinárske závody", // 185
                "Zariadenie pre seniorov Zobor",
                "ZŠ pod Zoborom",

                // Obchodné centrá
                "Andreja Hlinkum, Centro",
                "Hypermarket TESCO",
                "Chrenovská MAX", // 190
                "METRO"
        };

        for (int i = 0; i < stops.length; i++) addStop(new Stop(i + 1, stops[i]));

        long linestops[][] = new long[][]{
                new long[]{146, 137, 122, 125, 136, 169, 185, 147, 187, 157, 186, 174, 156, 181, 149, 158, 164}, // 1
                new long[]{50, 48, 34, 35, 40, 51, 38, 44, 34, 88, 43, 138, 137, 122, 143, 188, 133, 20, 169, 147, 187, 184, 176, 175, 168, 5, 6, 7, 8, 9, 10, 3, 4, 1, 2}, // 2
                new long[]{50, 48, 34, 35, 40, 51, 38, 44, 33, 88, 43, 138, 137, 122, 143, 188, 133, 20, 169, 175, 168, 5, 6, 7, 8, 9, 10, 3, 4, 1, 2}, // 4
                new long[]{14, 12, 13, 15, 183, 190, 169, 136, 125, 134, 122, 128, 131, 56, 52, 46, 51, 40, 35, 34}, // 6
                new long[]{58, 53, 50, 48, 60, 49, 52, 56, 131, 142, 135, 124, 123, 125, 134, 122, 137, 120, 139, 129} // 7
        };

        int linestopid = 0;
        for (int i = 0; i < linestops.length; i++)
            linestopid = addLineStops(linestops[i], i + 1, linestopid);
    }

    private int addLineStops(long[] stops, long idline, int linestopid) {
        for (int i = 0; i < stops.length; i++)
            addLineStop(new LineStop(i + 1 + linestopid, idline, stops[i], i + 1));
        return linestopid + stops.length;
    }
}
