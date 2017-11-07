package com.juliatimofeeva.currencyconverter.data.converter;

import android.support.annotation.NonNull;

import com.juliatimofeeva.currencyconverter.data.CurrencyInfoModel;
import com.juliatimofeeva.currencyconverter.data.storage.CurrencyDatabase;

import java.util.NoSuchElementException;

/**
 * Created by julia on 08.11.17.
 */

public class CurrencyConverterImpl implements CurrencyConverter {

    private CurrencyDatabase database;

    public CurrencyConverterImpl(CurrencyDatabase database) {
        this.database = database;
    }
    @Override
    public double convert(@NonNull String codeFrom, @NonNull String codeTo, double value)
    throws NoSuchElementException {
        CurrencyInfoModel modelFrom = database.getItemWithCode(codeFrom);
        CurrencyInfoModel modelTo = database.getItemWithCode(codeTo);
        if ((modelFrom == null) || (modelTo == null)) {
            throw new NoSuchElementException();
        }
        double middleValue = modelFrom.getValue() / modelFrom.getNominal() * value;
        double resultValue = middleValue / (modelTo.getValue() / modelTo.getNominal());
        return resultValue;
    }
}
