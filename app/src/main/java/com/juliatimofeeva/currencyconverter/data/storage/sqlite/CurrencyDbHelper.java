package com.juliatimofeeva.currencyconverter.data.storage.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by julia on 03.11.17.
 */

public class CurrencyDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="currency.db";
    private static final int DATABASE_VERSION = 1;

    public CurrencyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                CurrencyValueContract.CurrencyEntry.TABLE_NAME + "(" +
                CurrencyValueContract.CurrencyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CurrencyValueContract.CurrencyEntry.COLUMN_NAME_CHAR_CODE + " TEXT NOT NULL UNIQUE, " +
                CurrencyValueContract.CurrencyEntry.COLUMN_NAME_CURRECY_NAME + " TEXT NOT NULL, " +
                CurrencyValueContract.CurrencyEntry.COLUMN_NAME_NOMINAL + " INTEGER NOT NULL, " +
                CurrencyValueContract.CurrencyEntry.COLUMN_NAME_VALUE + " REAL NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO: implement this
    }
}
