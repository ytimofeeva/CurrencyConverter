package com.juliatimofeeva.currencyconverter.data.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;

import com.juliatimofeeva.currencyconverter.data.CurrencyInfoModel;
import com.juliatimofeeva.currencyconverter.data.storage.entities.CurrencyModelDB;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by julia on 07.11.17.
 */

public class CurrencyDatabaseImpl implements CurrencyDatabase {

    private CurrencyDbHelper dbHelper;
    private SQLiteDatabase currencyDatabase;

    public CurrencyDatabaseImpl(CurrencyDbHelper helper) {
        this.dbHelper = helper;
    }

    @Override
    public List<CurrencyInfoModel> getAllCurrencyItems() throws SQLiteException {
        currencyDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = currencyDatabase.query(CurrencyValueContract.CurrencyEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                CurrencyValueContract.CurrencyEntry.COLUMN_NAME_CURRECY_NAME);
        List<CurrencyInfoModel> result = new ArrayList<>();
        if (cursor == null) {
            return null;
        }
        if (cursor.moveToFirst()) {
            do {
                String charCode = cursor.getString(cursor.getColumnIndex(CurrencyValueContract.CurrencyEntry.COLUMN_NAME_CHAR_CODE));
                String currencyName = cursor.getString(cursor.getColumnIndex(CurrencyValueContract.CurrencyEntry.COLUMN_NAME_CURRECY_NAME));
                int nominal = cursor.getInt(cursor.getColumnIndex(CurrencyValueContract.CurrencyEntry.COLUMN_NAME_NOMINAL));
                double value = cursor.getDouble(cursor.getColumnIndex(CurrencyValueContract.CurrencyEntry.COLUMN_NAME_VALUE));
                CurrencyInfoModel model = new CurrencyInfoModel(charCode, currencyName, value, nominal);
                result.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }


    private CurrencyModelDB getDBItemWithCode(@NonNull String charCode) {
        currencyDatabase = dbHelper.getReadableDatabase();
        String selection = CurrencyValueContract.CurrencyEntry.COLUMN_NAME_CHAR_CODE + " =?";
        String[] selectionArgs = new String[] {charCode};
        Cursor cursor = currencyDatabase.query(CurrencyValueContract.CurrencyEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor == null) {
            return null;
        }
        CurrencyModelDB model = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(CurrencyValueContract.CurrencyEntry._ID));
            charCode = cursor.getString(cursor.getColumnIndex(CurrencyValueContract.CurrencyEntry.COLUMN_NAME_CHAR_CODE));
            String currencyName = cursor.getString(cursor.getColumnIndex(CurrencyValueContract.CurrencyEntry.COLUMN_NAME_CURRECY_NAME));
            int nominal = cursor.getInt(cursor.getColumnIndex(CurrencyValueContract.CurrencyEntry.COLUMN_NAME_NOMINAL));
            double value = cursor.getDouble(cursor.getColumnIndex(CurrencyValueContract.CurrencyEntry.COLUMN_NAME_VALUE));
            model = new CurrencyModelDB(id, charCode, currencyName, value, nominal);
        }
        return model;
    }
    @Override
    public CurrencyInfoModel getItemWithCode(@NonNull String charCode) throws SQLiteException {
        CurrencyModelDB modelDB = getDBItemWithCode(charCode);
        CurrencyInfoModel infoModel = null;
        if (modelDB != null) {
            infoModel = new CurrencyInfoModel(modelDB.getCharCode(),
                    modelDB.getName(),
                    modelDB.getValue(),
                    modelDB.getNominal());
        }
        return infoModel;
    }

    @Override
    public void insertCurrencyItems(@NonNull List<CurrencyInfoModel> data) throws SQLiteException {
        Log.d(TAG, "insert data size " + data.size());
        currencyDatabase = dbHelper.getWritableDatabase();
        for (CurrencyInfoModel item : data) {
            String charCode = item.getCharCode();
            CurrencyModelDB model = getDBItemWithCode(charCode);
            String whereClause = CurrencyValueContract.CurrencyEntry.COLUMN_NAME_CHAR_CODE + " =?";
            String [] whereArgs = new String[] {charCode};
            ContentValues cv = new ContentValues();
            cv.put(CurrencyValueContract.CurrencyEntry.COLUMN_NAME_CHAR_CODE, charCode);
            cv.put(CurrencyValueContract.CurrencyEntry.COLUMN_NAME_CURRECY_NAME, item.getName());
            cv.put(CurrencyValueContract.CurrencyEntry.COLUMN_NAME_NOMINAL, item.getNominal());
            cv.put(CurrencyValueContract.CurrencyEntry.COLUMN_NAME_VALUE, item.getValue());
            if (model != null) {
                cv.put(CurrencyValueContract.CurrencyEntry._ID, model.getId());
                int number = currencyDatabase.update(CurrencyValueContract.CurrencyEntry.TABLE_NAME,
                        cv,
                        whereClause,
                        whereArgs);
                Log.d("tag", "updated values " + String.valueOf(number));
            } else {
                long rowId = currencyDatabase.insert(CurrencyValueContract.CurrencyEntry.TABLE_NAME,
                        null,
                        cv);
                Log.d("tag", "inserted row " + String.valueOf(rowId));
            }
        }

    }


}
