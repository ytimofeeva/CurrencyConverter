package com.juliatimofeeva.currencyconverter;

import android.content.Context;

import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepository;
import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepositoryImpl;
import com.juliatimofeeva.currencyconverter.data.converter.CurrencyConverter;
import com.juliatimofeeva.currencyconverter.data.converter.CurrencyConverterImpl;
import com.juliatimofeeva.currencyconverter.data.storage.CurrencyDatabase;
import com.juliatimofeeva.currencyconverter.data.storage.CurrencyDatabaseImpl;
import com.juliatimofeeva.currencyconverter.data.storage.sqlite.CurrencyDbHelper;

/**
 * Created by julia on 03.11.17.
 */

public class DataLayerFactory {

    private Context context;

    public DataLayerFactory(Context context) {
        this.context  = context;
    }

    private static CurrencyDataRepository currencyDataRepository;
    private static CurrencyDbHelper currencyDbHelper;
    private static CurrencyDatabase currencyDatabase;
    private CurrencyConverter currencyConverter;

    public synchronized CurrencyDataRepository getCurrencyDataRepository() {
        if (currencyDataRepository == null) {
            currencyDataRepository = new CurrencyDataRepositoryImpl(getCurrencyDatabase(),
                    getCurrencyConverter());
        }
        return currencyDataRepository;
    }

    private CurrencyDatabase getCurrencyDatabase() {
        if (currencyDatabase == null) {
            currencyDbHelper = new CurrencyDbHelper(context);
            currencyDatabase = new CurrencyDatabaseImpl(currencyDbHelper);
        }
        return currencyDatabase;
    }

    private CurrencyConverter getCurrencyConverter() {
        if (currencyConverter == null) {
            currencyConverter = new CurrencyConverterImpl(getCurrencyDatabase());
        }
        return currencyConverter;
    }


}
