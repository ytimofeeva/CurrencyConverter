package com.juliatimofeeva.currencyconverter;

import android.content.Context;

/**
 * Created by julia on 03.11.17.
 */

public class FactoryProvider {

    private Context context;

    private static DataLayerFactory dataLayerFactory;
    private static PresentationLayerFactory presentationLayerFactory;

    public FactoryProvider(Context context) {
        this.context = context;
    }

    public synchronized DataLayerFactory getDataLayerFactory() {
        if (dataLayerFactory == null) {
            dataLayerFactory = new DataLayerFactory(context);
        }
        return dataLayerFactory;
    }

    public synchronized PresentationLayerFactory getPresentationLayerFactory() {
        if (presentationLayerFactory == null) {
            presentationLayerFactory = new PresentationLayerFactory();
        }
        return presentationLayerFactory;
    }
}
