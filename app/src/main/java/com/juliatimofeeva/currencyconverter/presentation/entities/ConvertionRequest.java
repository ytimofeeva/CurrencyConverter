package com.juliatimofeeva.currencyconverter.presentation.entities;

/**
 * Created by julia on 03.11.17.
 */

public class ConvertionRequest {

    private String currencyCodeFrom;
    private String currencyCodeTo;
    private double requestValue;

    public ConvertionRequest(String currencyCodeFrom, String currencyCodeTo, double requestValue) {
        this.currencyCodeFrom = currencyCodeFrom;
        this.currencyCodeTo = currencyCodeTo;
        this.requestValue = requestValue;
    }

    public String getCurrencyCodeFrom() {
        return currencyCodeFrom;
    }

    public String getCurrencyCodeTo() {
        return currencyCodeTo;
    }

    public double getRequestValue() {
        return requestValue;
    }
}
