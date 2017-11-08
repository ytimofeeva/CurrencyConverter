package com.juliatimofeeva.currencyconverter;

import android.app.Application;

import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepositoryImpl;
import com.juliatimofeeva.currencyconverter.data.CurrencyInfoModel;

import java.util.List;

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
    public void onNetworkRequestSuccess(List<CurrencyInfoModel> data) {
        factoryProvider.getDataLayerFactory().getCurrencyDataRepository().saveCurrencyDataToCache(data);
    }

    @Override
    public void onNetworkRequestError(Throwable error) {
        factoryProvider.getDataLayerFactory().getCurrencyDataRepository().removeListener(this);
    }

    @Override
    public void onCacheRequestSuccess(List<CurrencyInfoModel> data) {
        factoryProvider.getDataLayerFactory().getCurrencyDataRepository().removeListener(this);
    }

    @Override
    public void onCacheRequestError(Throwable error) {
        factoryProvider.getDataLayerFactory().getCurrencyDataRepository().removeListener(this);
    }

    @Override
    public void onConvertProcessCompleteSuccess(double data) {

    }

    @Override
    public void onConvertProcessCompleteError(Throwable error) {

    }


}
