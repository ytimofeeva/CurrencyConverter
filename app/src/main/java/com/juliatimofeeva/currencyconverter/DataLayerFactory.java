package com.juliatimofeeva.currencyconverter;

import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepository;
import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepositoryImpl;

/**
 * Created by julia on 03.11.17.
 */

public class DataLayerFactory {
    private static CurrencyDataRepository currencyDataRepository;

    public synchronized CurrencyDataRepository getCurrencyDataRepository() {
        if (currencyDataRepository == null) {
            currencyDataRepository = new CurrencyDataRepositoryImpl();
        }
        return currencyDataRepository;
    }
}
