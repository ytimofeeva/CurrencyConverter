package com.juliatimofeeva.currencyconverter.data.converter;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import java.util.NoSuchElementException;

/**
 * Created by julia on 08.11.17.
 */

public interface CurrencyConverter {

    @WorkerThread
    double convert(@NonNull String codeFrom, @NonNull String codeTo, double value)
            throws NoSuchElementException;

}
