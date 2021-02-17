package sk.romanstrazanec.mhdnitra

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import sk.romanstrazanec.mhdnitra.entities.*

class DBHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val I = "INTEGER"
        val T = "TEXT"
        val PK = "INTEGER PRIMARY KEY AUTOINCREMENT"
        createTable(db, MyContract.Line.TABLE_NAME, arrayOf(MyContract.Line.COLUMN_ID, MyContract.Line.COLUMN_LINE), arrayOf(PK, T))
        createTable(db, MyContract.Stop.TABLE_NAME, arrayOf(MyContract.Stop.COLUMN_ID, MyContract.Stop.COLUMN_NAME), arrayOf(PK, T))
        createTable(db, MyContract.LineStop.TABLE_NAME, arrayOf(MyContract.LineStop.COLUMN_ID,
                MyContract.LineStop.COLUMN_ID_LINE,
                MyContract.LineStop.COLUMN_ID_STOP,
                MyContract.LineStop.COLUMN_NUMBER,
                MyContract.LineStop.COLUMN_DIRECTION), arrayOf(PK, I, I, I, I))
        createTable(db, MyContract.Time.TABLE_NAME, arrayOf(MyContract.Time.COLUMN_ID,
                MyContract.Time.COLUMN_ID_LINESTOP,
                MyContract.Time.COLUMN_HOUR,
                MyContract.Time.COLUMN_MINUTE,
                MyContract.Time.COLUMN_WEEKEND,
                MyContract.Time.COLUMN_HOLIDAYS), arrayOf(PK, I, I, I, I, I))
        createTable(db, MyContract.FavouriteLine.TABLE_NAME, arrayOf(MyContract.FavouriteLine.COLUMN_ID, MyContract.FavouriteLine.COLUMN_ID_LINE), arrayOf(PK, I))
        createTable(db, MyContract.FavouriteStop.TABLE_NAME, arrayOf(MyContract.FavouriteStop.COLUMN_ID, MyContract.FavouriteStop.COLUMN_ID_STOP), arrayOf(PK, I))
        MHDNitra(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        dropTable(db, MyContract.Line.TABLE_NAME)
        dropTable(db, MyContract.Stop.TABLE_NAME)
        dropTable(db, MyContract.LineStop.TABLE_NAME)
        dropTable(db, MyContract.Time.TABLE_NAME)
        dropTable(db, MyContract.FavouriteLine.TABLE_NAME)
        dropTable(db, MyContract.FavouriteStop.TABLE_NAME)
        onCreate(db)
    }

    private fun dropTable(db: SQLiteDatabase, tableName: String) {
        db.execSQL("DROP TABLE IF EXISTS $tableName")
    }

    private fun createTable(db: SQLiteDatabase, tableName: String, columns: Array<String>, types: Array<String>) {
        if (columns.size != types.size) return
        val c = StringBuilder()
        for (i in columns.indices) {
            c.append(columns[i]).append(" ").append(types[i])
            if (i != columns.size - 1) c.append(", ")
        }
        val query = "CREATE TABLE $tableName ($c)"
        db.execSQL(query)
    }

    fun getCursor(select: Array<String?>?, from: String, join: Array<String?>?, lefton: Array<String?>?, righton: Array<String?>?, where: String?, groupby: String?, orderby: String?): Cursor {
        val db = writableDatabase
        var selection = StringBuilder()
        if (select != null) {
            for (s in select) selection.append(s).append(",")
        } else selection = StringBuilder("*,")
        val query = StringBuilder("SELECT " + selection.substring(0, selection.length - 1) + " FROM " + from)
        if (join != null && lefton != null && righton != null && join.size == lefton.size && lefton.size == righton.size) {
            var leftjoin: String? = from
            for (i in join.indices) {
                query.append(" JOIN ").append(join[i]).append(" ON ").append(leftjoin).append(".").append(lefton[i]).append(" = ").append(join[i]).append(".").append(righton[i])
                leftjoin = join[i]
            }
        }
        if (where != null) query.append(" WHERE ").append(where)
        if (groupby != null) query.append(" GROUP BY ").append(groupby)
        if (orderby != null) query.append(" ORDER BY ").append(orderby)
        val c = db.rawQuery(query.toString(), null)
        c.moveToFirst()
        db.close()
        return c
    }

    private fun addLine(db: SQLiteDatabase, l: Line) {
        var db: SQLiteDatabase? = db
        val values = ContentValues()
        values.put(MyContract.Line.COLUMN_LINE, l.line)
        if (db == null) {
            db = writableDatabase
            db.insert(MyContract.Line.TABLE_NAME, null, values)
            db.close()
        } else db.insert(MyContract.Line.TABLE_NAME, null, values)
    }

    private fun addStop(db: SQLiteDatabase, s: Stop) {
        var db: SQLiteDatabase? = db
        val values = ContentValues()
        values.put(MyContract.Stop.COLUMN_NAME, s.name)
        if (db == null) {
            db = writableDatabase
            db.insert(MyContract.Stop.TABLE_NAME, null, values)
            db.close()
        } else db.insert(MyContract.Stop.TABLE_NAME, null, values)
    }

    private fun addLineStop(db: SQLiteDatabase, ls: LineStop) {
        var db: SQLiteDatabase? = db
        val values = ContentValues()
        values.put(MyContract.LineStop.COLUMN_ID_LINE, ls.IDLine)
        values.put(MyContract.LineStop.COLUMN_ID_STOP, ls.IDStop)
        values.put(MyContract.LineStop.COLUMN_NUMBER, ls.number)
        values.put(MyContract.LineStop.COLUMN_DIRECTION, ls.direction)
        if (db == null) {
            db = writableDatabase
            db.insert(MyContract.LineStop.TABLE_NAME, null, values)
            db.close()
        } else db.insert(MyContract.LineStop.TABLE_NAME, null, values)
    }

    fun addTime(db: SQLiteDatabase?, t: Time) {
        var db = db
        val values = ContentValues()
        values.put(MyContract.Time.COLUMN_ID_LINESTOP, t.iDLineStop)
        values.put(MyContract.Time.COLUMN_HOUR, t.hour)
        values.put(MyContract.Time.COLUMN_MINUTE, t.minute)
        values.put(MyContract.Time.COLUMN_WEEKEND, t.weekend)
        values.put(MyContract.Time.COLUMN_HOLIDAYS, t.holidays)
        if (db == null) {
            db = writableDatabase
            db.insert(MyContract.Time.TABLE_NAME, null, values)
            db.close()
        } else db.insert(MyContract.Time.TABLE_NAME, null, values)
    }

    fun addFavouriteLine(fl: FavouriteLine) {
        val values = ContentValues()
        values.put(MyContract.FavouriteLine.COLUMN_ID_LINE, fl.IDLine)
        val db = writableDatabase
        db.insert(MyContract.FavouriteLine.TABLE_NAME, null, values)
        db.close()
    }

    fun addFavouriteStop(fs: FavouriteStop) {
        val values = ContentValues()
        values.put(MyContract.FavouriteStop.COLUMN_ID_STOP, fs.IDStop)
        val db = writableDatabase
        db.insert(MyContract.FavouriteStop.TABLE_NAME, null, values)
        db.close()
    }

    fun updateLine(l: Line) {
        val values = ContentValues()
        values.put(MyContract.Line.COLUMN_LINE, l.line)
        val db = writableDatabase
        db.update(MyContract.Line.TABLE_NAME, values, MyContract.Line.COLUMN_ID + " = ?", arrayOf(l.ID.toString()))
        db.close()
    }

    fun updateStop(s: Stop) {
        val values = ContentValues()
        values.put(MyContract.Stop.COLUMN_NAME, s.name)
        val db = writableDatabase
        db.update(MyContract.Stop.TABLE_NAME, values, MyContract.Stop.COLUMN_ID + " = ?", arrayOf(s.ID.toString()))
        db.close()
    }

    fun updateLineStop(ls: LineStop) {
        val values = ContentValues()
        values.put(MyContract.LineStop.COLUMN_ID_LINE, ls.IDLine)
        values.put(MyContract.LineStop.COLUMN_ID_STOP, ls.IDStop)
        values.put(MyContract.LineStop.COLUMN_NUMBER, ls.number)
        values.put(MyContract.LineStop.COLUMN_DIRECTION, ls.direction)
        val db = writableDatabase
        db.update(MyContract.LineStop.TABLE_NAME, values, MyContract.LineStop.COLUMN_ID + " = ?", arrayOf(ls.ID.toString()))
        db.close()
    }

    fun updateTime(t: Time) {
        val values = ContentValues()
        values.put(MyContract.Time.COLUMN_ID_LINESTOP, t.iDLineStop)
        values.put(MyContract.Time.COLUMN_MINUTE, t.minute)
        values.put(MyContract.Time.COLUMN_WEEKEND, t.weekend)
        values.put(MyContract.Time.COLUMN_WEEKEND, t.weekend)
        values.put(MyContract.Time.COLUMN_HOLIDAYS, t.holidays)
        val db = writableDatabase
        db.update(MyContract.Time.TABLE_NAME, values, MyContract.Time.COLUMN_ID + " = ?", arrayOf(t.iD.toString()))
        db.close()
    }

    fun updateFavouriteLine(fl: FavouriteLine) {
        val values = ContentValues()
        values.put(MyContract.FavouriteLine.COLUMN_ID_LINE, fl.IDLine)
        val db = writableDatabase
        db.update(MyContract.FavouriteLine.TABLE_NAME, values, MyContract.FavouriteLine.COLUMN_ID + " = ?", arrayOf(fl.ID.toString()))
        db.close()
    }

    fun updateFavouriteStop(fs: FavouriteStop) {
        val values = ContentValues()
        values.put(MyContract.FavouriteStop.COLUMN_ID_STOP, fs.IDStop)
        val db = writableDatabase
        db.update(MyContract.FavouriteStop.TABLE_NAME, values, MyContract.FavouriteStop.COLUMN_ID + " = ?", arrayOf(fs.ID.toString()))
        db.close()
    }

    fun deleteLine(ID: Long) {
        val db = writableDatabase
        db.delete(MyContract.Line.TABLE_NAME, MyContract.Line.COLUMN_ID + " = ?", arrayOf(ID.toString()))
        db.close()
    }

    fun deleteStop(ID: Long) {
        val db = writableDatabase
        db.delete(MyContract.Stop.TABLE_NAME, MyContract.Stop.COLUMN_ID + " = ?", arrayOf(ID.toString()))
        db.close()
    }

    fun deleteLineStop(ID: Long) {
        val db = writableDatabase
        db.delete(MyContract.LineStop.TABLE_NAME, MyContract.LineStop.COLUMN_ID + " = ?", arrayOf(ID.toString()))
        db.close()
    }

    fun deleteTime(ID: Long) {
        val db = writableDatabase
        db.delete(MyContract.Time.TABLE_NAME, MyContract.Time.COLUMN_ID + " = ?", arrayOf(ID.toString()))
        db.close()
    }

    fun deleteFavouriteLine(ID: Long) {
        val db = writableDatabase
        db.delete(MyContract.FavouriteLine.TABLE_NAME, MyContract.FavouriteLine.COLUMN_ID_LINE + " = ?", arrayOf(ID.toString()))
        db.close()
    }

    fun deleteFavouriteStop(ID: Long) {
        val db = writableDatabase
        db.delete(MyContract.FavouriteStop.TABLE_NAME, MyContract.FavouriteStop.COLUMN_ID_STOP + " = ?", arrayOf(ID.toString()))
        db.close()
    }

    private fun deleteAll(db: SQLiteDatabase) {
        db.rawQuery("DELETE FROM " + MyContract.Line.TABLE_NAME, null)
        db.rawQuery("DELETE FROM " + MyContract.Stop.TABLE_NAME, null)
        db.rawQuery("DELETE FROM " + MyContract.LineStop.TABLE_NAME, null)
        db.rawQuery("DELETE FROM " + MyContract.Time.TABLE_NAME, null)
        db.rawQuery("DELETE FROM " + MyContract.FavouriteLine.TABLE_NAME, null)
        db.rawQuery("DELETE FROM " + MyContract.FavouriteStop.TABLE_NAME, null)
    }

    private fun MHDNitra(db: SQLiteDatabase) {
        val lines = arrayOf("1", "2", "4", "6", "7", "8", "9", "10", "11", "12", "13", "14",
                "15", "16", "17", "18", "19", "21", "22", "24", "25", "26", "27", "30", "32", "33", "C35")
        for (i in lines.indices) addLine(db, Line((i + 1).toLong(), lines[i]))
        val stops = arrayOf( // Drážovce
                "Belopotockého",
                "Drážovce",
                "PD Drážovce",
                "Pri kríži",
                "Priemyselný park I",  // 5
                "Priemyselný park II",
                "Priemyselný park III",
                "Priemyselný park IV",
                "Priemyselný park V",
                "Rázcestie priemyselný park",  // 10
                // Chrenová, Janíkovce
                "Atletický štadión",
                "Bohúňová",
                "Ďurčanského",
                "Gorazdova",
                "Chrenovský cintorín",  // 15
                "Janíkovce",
                "Janíkovská cesta",
                "Letecká",
                "Levická",
                "Lomnická",  //20
                "Malé Janíkovce I",
                "Malé Janíkovce II",
                "Mikov dvor",
                "Plynárenská",
                "Poliklinika Chrenová",  // 25
                "Sitnianska",
                "Slamkova",
                "Vinohrady Chrenová",
                "Výstavisko",
                "ZŠ Janíkovce",  // 30
                // Čermáň
                "Cabajská",
                "Červeňova",
                "Dolnočermánska",
                "Edisonova",
                "Golianova",  // 35
                "Hanulova",
                "Hattalova",
                "Kostolná",
                "NAD",
                "Nedbalova",  // 40
                "Nový cintorín",
                "SEC",
                "Stavebná škola",
                "Tehelná",
                "Vodohospodárske stavby",  // 45
                "ZŠ Škultétyho",
                "Železničiarska",  // Klokočina, Diely
                "Bizetova",
                "Čajkovského",
                "Kmeťova",  // 50
                "Mestská hala",
                "Mikovíniho",
                "Murániho",
                "Nitrianska",
                "Partizánska",  // 55
                "Poliklinika Klokočina",
                "Popradská",
                "Považská",
                "Pražská",
                "Rázcestie Kmeťova",  // 60
                "Tokajská",
                "Viničky",
                "Zvolenská",
                "Žilinská",  // Krškany, Ivánka, Jarok, Branč
                "Branč",  // 65
                "Branč, Arkuš",
                "Branč, kult. dom",
                "Branč, Kurucká",
                "Branč, pneuservis",
                "Branč, Veľkoveská",  // 70
                "Branč, železničná stanica",
                "Dvorčianska",
                "Hájnická",
                "Horné Krškany",
                "Idea",  // 75
                "Ivánka pri Nitre, kult. dom",
                "Ivánka pri Nitre, Luk",
                "Ivánka pri Nitre, Orolská",
                "Ivánka pri Nitre, Texiplast",
                "Ivánka pri Nitre, Žel. stanica",  // 80
                "Jakuba Haška",
                "Jarocká",
                "Jurský dvor",
                "Kasárne Krškany",
                "Liaharenský podnik",  // 85
                "Lukov dvor",
                "Mevak",
                "Murgašova",
                "Na priehon",
                "Nitrafrost",  // 90
                "Nitrianske strojárne",
                "Párovské háje",
                "Plastika",
                "Prameň",
                "Priemyselná",  // 95
                "Rázcestie priemyselná",
                "Stromová",
                "Trans Motel",
                "Záborskeho",
                "ZŠ Krškany",  // 100
                // Lužianky
                "Lužianky Hlohovecká",
                "Lužianky Korytovská",
                "Lužianky Rastislavova",
                "Lužianky rázc., Vinárska",
                "Lužianky VÚŽV",  // 105
                "Lužianky ZŠ",
                "Lužianky, Vinárska",
                "Lužianky, železničná stanica",  // Mlynárce, Kynek
                "Bolečkova",
                "Cintorín Mlynárce",  // 110
                "Dubíkova",
                "Ferrenit",
                "Chotárna",
                "Kynek",
                "NIPEK",  // 115
                "Potočná",
                "Rastislavova",
                "Rybárska",
                "Železničná zastávka Mlynárce",  // Staré mesto
                "8. mája",  // 120
                "Braneckého",
                "CENTRUM",
                "Divadlo Andreja Bagara",
                "Ďurková",
                "Fraňa Mojtu",  // 125
                "Hlavná",
                "Hodžova",
                "Hollého",
                "Kalvária",
                "Kasalova",  // 130
                "Kavcova",
                "Mestský park",
                "Nábrežie mládeže",
                "Palárikova",
                "Párovská",  // 135
                "Predmostie",
                "Rázcestie Autobusová stanica",
                "Rázcestie Železničná stanica",
                "Rázusová",
                "Správa ciest",  // 140
                "Špitálska",
                "Štúrová",
                "Univerzity",
                "Wilsonovo nábrežie",
                "Záhradná",  // 145
                "Železničná stanica Nitra",  // Zobor, Hrnčiarovce, Štitáre
                "Amfiteáter",
                "Drozdí chodník",
                "Hornozoborská",
                "Hrnčiarovce",  // 150
                "Hrnčiarovce Krajná",
                "Hrnčiarovce pod Sokolom",
                "Hrnčiarovce Šopronská",
                "Hrnčiarovce Vinohrady",
                "Hrnčiarovce ZŠ",  // 155
                "Chmeľová dolina",
                "Jánskeho",
                "Klinčeková",
                "Lanovka",
                "Martinská dolina",  // 160
                "Metodova",
                "Moskovská",
                "Muškátová",
                "Nemocnica Zobor",
                "Orechová",  // 165
                "Orgovánová",
                "Panská dolina",
                "Pod Lupkou",
                "Pod Zoborom",
                "Podhájska",  // 170
                "Rázcestie Metodova",
                "Rázcestie Moskovská",
                "Rázcestie Panská dolina",
                "Strmá",
                "Šindolka",  // 175
                "Šindolka, Dolnohorská",
                "Štitáre",
                "Štitáre ku Gáborke",
                "Štitáre Šoproš",
                "Turistická",  // 180
                "Urbancova",
                "Úzka",
                "Vašínova",
                "Veterinárska",
                "Vinárske závody",  // 185
                "Zariadenie pre seniorov Zobor",
                "ZŠ pod Zoborom",  // Obchodné centrá
                "Andreja Hlinku, Centro",
                "Hypermarket TESCO",
                "Chrenovská MAX",  // 190
                "METRO"
        )
        for (i in stops.indices) addStop(db, Stop((i + 1).toLong(), stops[i]))
        val linestops = arrayOf(longArrayOf(146, 137, 122, 125, 136, 169, 185, 147, 187, 157, 186, 174, 156, 181, 149, 158, 164), longArrayOf(164, 158, 149, 181, 156, 174, 186, 157, 187, 147, 185, 169, 136, 125, 134, 122, 137, 146), longArrayOf(50, 48, 34, 35, 40, 51, 38, 44, 34, 88, 43, 138, 137, 122, 143, 188, 133, 20, 169, 147, 187, 184, 176, 175, 168, 5, 6, 7, 8, 9, 10, 3, 4, 1, 2), longArrayOf(2, 4, 3, 9, 8, 7, 6, 5, 168, 175, 176, 184, 187, 147, 185, 169, 20, 133, 188, 143, 122, 137, 138, 43, 88, 45, 34, 44, 38, 51, 40, 35, 34, 48, 50), longArrayOf(50, 48, 34, 35, 40, 51, 38, 44, 33, 88, 43, 138, 137, 122, 143, 188, 133, 20, 169, 175, 168, 5, 6, 7, 8, 9, 10, 3, 4, 1, 2), longArrayOf(2, 4, 3, 9, 8, 7, 6, 5, 168, 175, 169, 20, 133, 188, 143, 122, 137, 138, 43, 88, 45, 33, 44, 38, 51, 40, 35, 34, 48, 50), longArrayOf(14, 12, 13, 15, 183, 190, 169, 136, 125, 134, 122, 128, 131, 56, 52, 46, 51, 40, 35, 34), longArrayOf(34, 35, 40, 51, 46, 52, 56, 131, 143, 122, 125, 136, 139, 190, 183, 15, 13, 12, 14), longArrayOf(58, 53, 50, 48, 60, 49, 52, 56, 131, 142, 135, 124, 123, 125, 134, 122, 137, 120, 139, 129), longArrayOf(129, 139, 120, 127, 138, 137, 122, 125, 123, 124, 135, 128, 131, 56, 52, 49, 48, 50, 58), longArrayOf(14, 12, 26, 25, 29, 188, 143, 122, 142, 189, 118, 119, 109, 115, 140, 191, 101, 105, 107), longArrayOf(107, 105, 191, 140, 115, 109, 119, 118, 189, 121, 142, 122, 143, 188, 29, 25, 26, 12, 14), longArrayOf(50, 48, 60, 49, 52, 56, 131, 142, 122, 125, 136, 169, 185, 147, 187, 184, 176, 166, 163, 182, 149, 158, 164), longArrayOf(164, 158, 149, 182, 163, 166, 187, 147, 185, 169, 136, 125, 134, 122, 138, 131, 56, 52, 49, 48, 50), longArrayOf(50, 48, 60, 49, 52, 46, 32, 37, 36, 47, 43, 138, 137, 122, 125, 136, 169, 173, 167, 148, 165, 159), longArrayOf(159, 165, 148, 167, 173, 147, 169, 136, 125, 134, 122, 137, 138, 43, 47, 37, 32, 46, 52, 49, 48, 50), longArrayOf(134, 122, 142, 121, 56, 52, 49, 60, 94, 86, 73, 82, 83, 85, 97, 92), longArrayOf(92, 97, 85, 83, 82, 73, 86, 94, 60, 49, 52, 56, 131, 142, 122), longArrayOf(14, 12, 13, 11, 188, 143, 122, 137, 138, 84, 96, 74, 90, 87, 100, 89, 72, 91, 99, 93, 98, 75, 79, 80, 76, 78, 77, 66, 70, 67, 69, 68, 71), longArrayOf(71, 68, 69, 67, 70, 66, 77, 78, 76, 80, 79, 75, 98, 93, 99, 91, 72, 89, 100, 87, 90, 74, 96, 84, 138, 137, 122, 143, 188, 11, 13, 12, 14), longArrayOf(50, 53, 63, 64, 118, 189, 121, 142, 122, 143, 188, 11, 15, 183, 19, 24, 28), longArrayOf(28, 24, 19, 15, 11, 188, 143, 122, 142, 189, 118, 55, 57, 63, 53, 50), longArrayOf(14, 12, 26, 25, 29, 188, 143, 122, 137, 138, 84, 96, 74, 90, 87, 100, 89, 72, 91, 99, 93), longArrayOf(93, 99, 91, 72, 89, 100, 87, 90, 74, 96, 84, 138, 146, 137, 122, 143, 188, 29, 25, 26, 12, 14), longArrayOf(14, 12, 26, 25, 29, 188, 143, 122, 137, 138, 43, 88, 45, 31, 41, 39, 81, 42), longArrayOf(42, 81, 39, 41, 31, 88, 43, 138, 137, 122, 143, 188, 29, 25, 26, 12, 14), longArrayOf(14, 12, 26, 25, 29, 188, 133, 20, 169, 175, 168, 5, 6, 7, 8, 9), longArrayOf(9, 8, 7, 6, 5, 168, 175, 169, 20, 133, 188, 29, 25, 26, 12, 14), longArrayOf(50, 48, 60, 49, 52, 56, 131, 142, 135, 124, 123, 136, 169, 175, 168, 5, 6, 7, 8, 9), longArrayOf(9, 8, 7, 6, 5, 168, 175, 169, 136, 123, 124, 135, 128, 131, 56, 52, 49, 48, 50), longArrayOf(146, 137, 122, 142, 189, 118, 119, 109, 113, 111, 115, 140, 191, 116, 114, 191), longArrayOf(191, 114, 116, 140, 115, 111, 113, 109, 119, 118, 189, 121, 142, 122, 137, 146), longArrayOf(146, 137, 122, 143, 188, 29, 25, 26, 12, 14, 23, 17, 21, 22, 27, 30, 18, 126, 16), longArrayOf(16, 126, 18, 30, 27, 22, 21, 17, 23, 14, 12, 26, 25, 29, 188, 143, 122, 137, 146), longArrayOf(146, 137, 122, 142, 189, 118, 119, 109, 115, 140, 117, 112, 104, 103, 102, 106, 108), longArrayOf(108, 106, 102, 103, 104, 112, 117, 191, 140, 115, 109, 119, 118, 189, 121, 142, 122, 137, 146), longArrayOf(119, 109, 110, 53, 50, 48, 60, 49, 52, 46, 51, 38, 44, 33, 88, 43, 84, 96, 74, 90, 87, 100, 89, 72, 91, 99, 93, 98, 75), longArrayOf(75, 98, 93, 99, 91, 72, 89, 100, 87, 90, 74, 96, 84, 43, 88, 45, 33, 44, 38, 51, 46, 52, 49, 48, 50, 53, 110, 109, 119, 118, 55), longArrayOf(50, 48, 60, 49, 52, 56, 131, 142, 122, 137, 146), longArrayOf(146, 137, 122, 128, 131, 56, 52, 49, 48, 50), longArrayOf(164, 158, 149, 180, 170, 160, 161, 162, 172, 171, 173, 147, 169, 190, 15, 13, 26, 25, 29, 188, 143, 122, 137, 141, 130, 145, 144, 95, 96, 84, 138), longArrayOf(141, 130, 145, 144, 95, 96, 84, 138, 137, 122, 143, 188, 29, 25, 26, 13, 15, 190, 169, 173, 171, 161, 160, 170, 180, 149, 158, 164), longArrayOf(42, 81, 39, 41, 31, 33, 44, 38, 51, 46, 52, 56, 131, 142, 122, 134, 125, 136, 169, 173, 171, 161, 162, 172), longArrayOf(161, 162, 172, 171, 173, 169, 136, 125, 134, 122, 128, 131, 56, 52, 46, 35, 51, 38, 44, 33, 31, 41, 39, 81, 42), longArrayOf(146, 137, 134, 125, 136, 169, 173, 171, 161, 162, 172, 152, 151, 155, 150, 153, 154, 179, 178, 177), longArrayOf(177, 178, 179, 154, 153, 150, 155, 151, 152, 172, 171, 173, 169, 136, 125, 134, 122, 137, 146), longArrayOf(50, 58, 53, 63, 64, 118, 189, 121, 142, 135, 124, 123, 136, 169, 190, 15, 11, 188, 29, 25, 26, 13, 12, 14), longArrayOf(14, 12, 13, 26, 25, 29, 188, 11, 15, 190, 169, 136, 123, 124, 135, 142, 189, 118, 59, 54, 63, 53, 50, 58), longArrayOf(62, 61, 53, 50, 48, 60, 49, 52, 56, 131, 142, 122, 143, 188, 29, 25, 26, 12, 14), longArrayOf(14, 12, 26, 25, 29, 188, 143, 122, 128, 131, 56, 52, 49, 34, 48, 50, 53, 61, 62), longArrayOf(146, 137, 122, 142, 189, 118, 55, 57, 63, 61, 62), longArrayOf(62, 61, 63, 64, 118, 189, 121, 142, 122, 137, 146), longArrayOf(50, 48, 60, 49, 52, 56, 131, 142, 122, 143, 188, 136, 132, 169, 147, 187, 166, 163, 182, 149, 158, 164), longArrayOf(164, 158, 149, 182, 163, 166, 187, 147, 185, 169, 132, 136, 188, 143, 122, 128, 131, 56, 52, 49, 48, 50))
        var linestopid = 0
        var i = 0
        while (i < linestops.size) {
            linestopid = addLineStops(db, linestops[i], (i / 2 + 1).toLong(), linestopid, 0)
            linestopid = addLineStops(db, linestops[i + 1], (i / 2 + 1).toLong(), linestopid, 1)
            i += 2
        }
    }

    private fun addLineStops(db: SQLiteDatabase, stops: LongArray, idline: Long, linestopid: Int, direction: Int): Int {
        for (i in stops.indices) addLineStop(db, LineStop((i + 1 + linestopid).toLong(), idline, stops[i], i + 1, direction))
        return linestopid + stops.size
    }

    companion object {
        private const val DATABASE_VERSION = 4
        private const val DATABASE_NAME = "mhd"
    }
}