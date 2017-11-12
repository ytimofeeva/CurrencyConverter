package com.juliatimofeeva.currencyconverter.factory;

import com.juliatimofeeva.currencyconverter.CurrencyApplication;
import com.juliatimofeeva.currencyconverter.presentation.ui.ConvertPresenter;
import com.juliatimofeeva.currencyconverter.presentation.ui.ConvertPresenterImpl;

/**
 * Created by julia on 03.11.17.
 */

public class PresentationLayerFactory {

    private ConvertPresenter convertPresenter;


    public synchronized ConvertPresenter getConvertPresenter() {
        if ( convertPresenter == null ) {
            convertPresenter = new ConvertPresenterImpl(CurrencyApplication.getFactoryProvider()
                    .getDataLayerFactory()
                    .getCurrencyDataRepository());
            CurrencyApplication.getFactoryProvider()
                    .getDataLayerFactory()
                    .getCurrencyDataRepository()
                    .setCurrencyDataListener(convertPresenter);
            CurrencyApplication.getFactoryProvider()
                    .getDataLayerFactory()
                    .getCurrencyDataRepository()
                    .setConvertionListener(convertPresenter);
        }
        return convertPresenter;
    }
}
