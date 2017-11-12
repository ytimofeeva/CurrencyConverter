package com.juliatimofeeva.currencyconverter.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.juliatimofeeva.currencyconverter.presentation.entities.ConvertionRequest;

import java.util.List;

/**
 * Created by julia on 02.11.17.
 */

public interface CurrencyDataRepository {

    void setCurrencyDataListener(CurrencyDataRepositoryImpl.OnDataRequestCompletionListener listener);

    void removeCurrencyDataListener(CurrencyDataRepositoryImpl.OnDataRequestCompletionListener listener);

    void setConvertionListener(CurrencyDataRepositoryImpl.OnConvertionCompletionListener listener);

    void removeConvertionListener(CurrencyDataRepositoryImpl.OnConvertionCompletionListener listener);

    void getCurrencyDataFromNetwork();

    void getCurrencyDataFromCache();

    CurrencyModelState convertCurrency(@NonNull ConvertionRequest request);

    void saveCurrencyDataToCache(@NonNull List<CurrencyInfoModel> data);

    void saveCurrencyFrom (@NonNull String charCode );

    void saveCurrencyTo (@NonNull String charCode );

    @Nullable
    String getCurrencyFrom();

    @Nullable
    String getCurrencyTo();

    CurrencyModelState getCurrentState();

}
