package com.juliatimofeeva.currencyconverter.presentation.ui;

import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepository;
import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepositoryImpl;

/**
 * Created by julia on 02.11.17.
 */

public interface ConvertPresenter extends CurrencyDataRepository.OnDataRequestCompletionListener,
        CurrencyDataRepositoryImpl.OnConvertionCompletionListener {

    void attachView(ConverterView view);
    void detachView();
    void processRequest( int positionFrom, int positionTo, String value);
    void setCurrencyFrom (long positionFrom);
    void setCurrencyTo (long setPositionTo);
}
