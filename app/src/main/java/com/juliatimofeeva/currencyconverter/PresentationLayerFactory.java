package com.juliatimofeeva.currencyconverter;

import com.juliatimofeeva.currencyconverter.presentation.ui.ConvertPresenter;
import com.juliatimofeeva.currencyconverter.presentation.ui.ConvertPresenterImpl;

/**
 * Created by julia on 03.11.17.
 */

public class PresentationLayerFactory {

    public ConvertPresenter getConvertPresenter() {
        ConvertPresenter presenter = new ConvertPresenterImpl(CurrencyApplication.getFactoryProvider()
                .getDataLayerFactory()
                .getCurrencyDataRepository());
        CurrencyApplication.getFactoryProvider()
                .getDataLayerFactory()
                .getCurrencyDataRepository()
                .setListener(presenter);
        return presenter;
    }
}
