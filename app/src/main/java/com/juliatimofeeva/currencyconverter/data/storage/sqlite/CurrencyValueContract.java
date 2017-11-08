package com.juliatimofeeva.currencyconverter.data.storage.sqlite;

import android.provider.BaseColumns;

/**
 * Created by julia on 03.11.17.
 */

public final class CurrencyValueContract {

    private CurrencyValueContract() {}

    public static class CurrencyEntry implements BaseColumns {
        public static final String TABLE_NAME = "CurrencyInfo";
        public static final String COLUMN_NAME_CHAR_CODE = "CharCode";
        public static final String COLUMN_NAME_CURRECY_NAME = "Name";
        public static final String COLUMN_NAME_NOMINAL = "Nominal";
        public static final String COLUMN_NAME_VALUE = "Value";
    }
}
