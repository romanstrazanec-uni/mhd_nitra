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
        String ct = "CREATE TABLE ({0})";

        String SQL_CREATE_TABLE_LINE = String.format(ct,
                MyContract.Line.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MyContract.Line.COLUMN_LINE + " TEXT");

        String SQL_CREATE_TABLE_STOP = String.format(ct,
                MyContract.Stop.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MyContract.Stop.COLUMN_NAME + " TEXT");

        String SQL_CREATE_TABLE_LINESTOP = String.format(ct,
                MyContract.LineStop.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MyContract.LineStop.COLUMN_ID_LINE + " INTEGER, " +
                MyContract.LineStop.COLUMN_ID_STOP + " INTEGER, " +
                MyContract.LineStop.COLUMN_NUMBER + " INTEGER");

        String SQL_CREATE_TABLE_TIME = String.format(ct,
                MyContract.Time.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MyContract.Time.COLUMN_ID_LINESTOP + " INTEGER, " +
                MyContract.Time.COLUMN_TIME + " TEXT, " +
                MyContract.Time.COLUMN_WEEKEND + " INTEGER, " +
                MyContract.Time.COLUMN_HOLIDAYS + " INTEGER");

        db.execSQL(SQL_CREATE_TABLE_LINE);
        db.execSQL(SQL_CREATE_TABLE_STOP);
        db.execSQL(SQL_CREATE_TABLE_LINESTOP);
        db.execSQL(SQL_CREATE_TABLE_TIME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
