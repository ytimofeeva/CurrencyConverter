package com.juliatimofeeva.currencyconverter.data;

/**
 * Created by julia on 02.11.17.
 */

public class CurrencyInfoModel {

    private String charCode;
    private String name;
    private double value;
    private int nominal;

    public CurrencyInfoModel(String charCode, String name, double value, int nominal) {
        this.charCode = charCode;
        this.name = name;
        this.value = value;
        this.nominal = nominal;
    }

    public String getCharCode() {
        return charCode;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public int getNominal() {
        return nominal;
    }
}
