package com.juliatimofeeva.currencyconverter;

import android.app.Application;

import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepositoryImpl;
import com.juliatimofeeva.currencyconverter.data.CurrencyModelState;
import com.juliatimofeeva.currencyconverter.factory.FactoryProvider;

/**
 * Created by julia on 03.11.17.
 */

public class CurrencyApplication extends Application implements CurrencyDataRepositoryImpl.OnDataRequestCompletionListener {

    private static FactoryProvider factoryProvider;

    public static FactoryProvider getFactoryProvider() {
        return factoryProvider;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        factoryProvider = new FactoryProvider(getApplicationContext());
        factoryProvider.getDataLayerFactory().getCurrencyDataRepository().setCurrencyDataListener(this);
        factoryProvider.getDataLayerFactory().getCurrencyDataRepository().getCurrencyDataFromNetwork();
    }

    @Override
    public void onNetworkRequestSuccess(CurrencyModelState state) {
        if ((state.getCurrencyData() != null) && (state.getCurrencyData().size() > 0)) {
            factoryProvider.getDataLayerFactory()
                    .getCurrencyDataRepository()
                    .saveCurrencyDataToCache(state.getCurrencyData());
        }
        factoryProvider.getDataLayerFactory().getCurrencyDataRepository().removeCurrencyDataListener(this);
    }

    @Override
    public void onNetworkRequestError(CurrencyModelState state) {
    }

    @Override
    public void onCacheRequestSuccess(CurrencyModelState state) {
    }

    @Override
    public void onCacheRequestError(CurrencyModelState state) {
    }

}
