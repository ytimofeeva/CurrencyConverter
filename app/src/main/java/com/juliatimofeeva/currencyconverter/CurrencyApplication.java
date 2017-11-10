package com.juliatimofeeva.currencyconverter;

import android.app.Application;

import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepositoryImpl;
import com.juliatimofeeva.currencyconverter.data.CurrencyModelState;

/**
 * Created by julia on 03.11.17.
 */

public class CurrencyApplication extends Application implements CurrencyDataRepositoryImpl.OnRequestCompletionListener {

    private static FactoryProvider factoryProvider;

    public static FactoryProvider getFactoryProvider() {
        return factoryProvider;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        factoryProvider = new FactoryProvider(getApplicationContext());
        factoryProvider.getDataLayerFactory().getCurrencyDataRepository().setListener(this);
        factoryProvider.getDataLayerFactory().getCurrencyDataRepository().getCurrencyDataFromNetwork();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    @Override
    public void onNetworkRequestSuccess(CurrencyModelState state) {
        factoryProvider.getDataLayerFactory()
                .getCurrencyDataRepository()
                .saveCurrencyDataToCache(state.getCurrencyData());
    }

    @Override
    public void onNetworkRequestError(CurrencyModelState state) {
        factoryProvider.getDataLayerFactory().getCurrencyDataRepository().removeListener(this);
    }

    @Override
    public void onCacheRequestSuccess(CurrencyModelState state) {
        factoryProvider.getDataLayerFactory().getCurrencyDataRepository().removeListener(this);
    }

    @Override
    public void onCacheRequestError(CurrencyModelState state) {
        factoryProvider.getDataLayerFactory().getCurrencyDataRepository().removeListener(this);
    }

    @Override
    public void onConvertProcessCompleteSuccess(CurrencyModelState state) {

    }

    @Override
    public void onConvertProcessCompleteError(CurrencyModelState state) {

    }


}
