package com.juliatimofeeva.currencyconverter.data;

import android.support.annotation.NonNull;

import com.juliatimofeeva.currencyconverter.presentation.entities.ConvertionRequest;

import java.util.List;

/**
 * Created by julia on 02.11.17.
 */

public interface CurrencyDataRepository {

    void setListener(CurrencyDataRepositoryImpl.OnRequestCompletionListener listener);

    void removeListener();

    void getCurrencyDataFromNetwork();

    void getCurrencyDataFromCache();

    void convertCurrency(@NonNull ConvertionRequest request);

    void saveCurrencyDataToCache(List<CurrencyInfoModel> data);
}
