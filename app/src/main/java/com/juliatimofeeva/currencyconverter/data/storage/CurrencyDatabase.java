package com.juliatimofeeva.currencyconverter.data.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.juliatimofeeva.currencyconverter.data.CurrencyInfoModel;

import java.util.List;

/**
 * Created by julia on 07.11.17.
 */

public interface CurrencyDatabase {

    @Nullable
    @WorkerThread
    List<CurrencyInfoModel> getAllCurrencyItems();

    @Nullable
    @WorkerThread
    CurrencyInfoModel getItemWithCode(@NonNull String charCode);

    @WorkerThread
    void insertCurrencyItems(@NonNull List<CurrencyInfoModel> data);
}
