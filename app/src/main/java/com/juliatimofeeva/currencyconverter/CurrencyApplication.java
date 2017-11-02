package com.juliatimofeeva.currencyconverter;

import android.app.Application;

/**
 * Created by julia on 03.11.17.
 */

public class CurrencyApplication extends Application{

    private static FactoryProvider factoryProvider;

    public static FactoryProvider getFactoryProvider() {
        return factoryProvider;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        factoryProvider = new FactoryProvider();
    }
}
