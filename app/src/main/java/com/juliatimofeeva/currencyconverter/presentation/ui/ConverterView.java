package com.juliatimofeeva.currencyconverter.presentation.ui;

import android.support.annotation.NonNull;

import com.juliatimofeeva.currencyconverter.presentation.entities.ConverterUiModel;

/**
 * Created by julia on 02.11.17.
 */

public interface ConverterView {

    void displayUiModel(@NonNull ConverterUiModel converterUiModel);
}
