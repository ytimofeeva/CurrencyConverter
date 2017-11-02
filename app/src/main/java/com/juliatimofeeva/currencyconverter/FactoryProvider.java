package com.juliatimofeeva.currencyconverter;

/**
 * Created by julia on 03.11.17.
 */

public class FactoryProvider {

    private static DataLayerFactory dataLayerFactory;
    private static PresentationLayerFactory presentationLayerFactory;

    public synchronized DataLayerFactory getDataLayerFactory() {
        if (dataLayerFactory == null) {
            dataLayerFactory = new DataLayerFactory();
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
