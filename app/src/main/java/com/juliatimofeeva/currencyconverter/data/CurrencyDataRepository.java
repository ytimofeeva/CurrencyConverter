package com.juliatimofeeva.currencyconverter.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.juliatimofeeva.currencyconverter.presentation.entities.ConvertionRequest;

import java.util.List;

/**
 * Created by julia on 02.11.17.
 */

public interface CurrencyDataRepository {

    void setCurrencyDataListener(CurrencyDataRepository.OnDataRequestCompletionListener listener);

    void removeCurrencyDataListener(CurrencyDataRepository.OnDataRequestCompletionListener listener);

    void setConvertionListener(CurrencyDataRepository.OnConvertionCompletionListener listener);

    void removeConvertionListener(CurrencyDataRepository.OnConvertionCompletionListener listener);

    CurrencyModelState getCurrencyDataFromNetwork();

    CurrencyModelState getCurrencyDataFromCache();

    CurrencyModelState convertCurrency(@NonNull ConvertionRequest request);

    void saveCurrencyDataToCache(@NonNull List<CurrencyInfoModel> data);

    void saveCurrencyFrom (@NonNull String charCode );

    void saveCurrencyTo (@NonNull String charCode );

    @Nullable
    String getCurrencyFrom();

    @Nullable
    String getCurrencyTo();

    CurrencyModelState getCurrentState();

    interface OnDataRequestCompletionListener {
        void onNetworkRequestSuccess(CurrencyModelState modelState);

        void onNetworkRequestError(CurrencyModelState modelState);

        void onCacheRequestSuccess(CurrencyModelState modelState);

        void onCacheRequestError(CurrencyModelState modelState);
    }

    interface OnConvertionCompletionListener {
        void onConvertProcessCompleteSuccess(CurrencyModelState modelState);

        void onConvertProcessCompleteError(CurrencyModelState modelState);
    }
}
