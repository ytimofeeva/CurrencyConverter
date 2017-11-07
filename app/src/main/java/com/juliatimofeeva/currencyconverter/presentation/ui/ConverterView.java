package com.juliatimofeeva.currencyconverter.presentation.ui;

import android.support.annotation.NonNull;

import java.util.Set;

/**
 * Created by julia on 02.11.17.
 */

public interface ConverterView {

    void showCurrencyNames(@NonNull Set<String> names);
    void showError(String msg);
    void showResult(String result);
}