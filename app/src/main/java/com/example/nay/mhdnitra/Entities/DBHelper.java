package com.example.nay.mhdnitra.Entities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nay.mhdnitra.MyContract;

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

        String SQL = "CREATE TABLE " + tableName +  " (" + c + ")";
        db.execSQL(SQL);
    }
}
