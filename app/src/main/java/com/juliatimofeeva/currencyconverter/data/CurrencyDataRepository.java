package com.juliatimofeeva.currencyconverter.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.juliatimofeeva.currencyconverter.presentation.entities.ConvertionRequest;

import java.util.List;

/**
 * Created by julia on 02.11.17.
 */

public interface CurrencyDataRepository {

    void setListener(CurrencyDataRepositoryImpl.OnRequestCompletionListener listener);

    void removeListener(CurrencyDataRepositoryImpl.OnRequestCompletionListener listener);

    void getCurrencyDataFromNetwork();

    void getCurrencyDataFromCache();

    CurrencyModelState convertCurrency(@NonNull ConvertionRequest request);

    void saveCurrencyDataToCache(List<CurrencyInfoModel> data);

    void saveCurrencyFrom (@NonNull String charCode );

    void saveCurrencyTo (@NonNull String charCode );

    @Nullable
    String getCurrencyFrom();

    @Nullable
    String getCurrencyTo();

    CurrencyModelState getCurrentState();

}
