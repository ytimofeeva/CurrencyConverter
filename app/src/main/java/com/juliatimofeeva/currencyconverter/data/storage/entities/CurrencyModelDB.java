package com.juliatimofeeva.currencyconverter.data.storage.entities;

/**
 * Created by julia on 07.11.17.
 */

public class CurrencyModelDB {

    private int id;
    private String charCode;
    private String name;
    private double value;
    private int nominal;

    public CurrencyModelDB(int id, String charCode, String name, double value, int nominal) {
        this.id = id;
        this.charCode = charCode;
        this.name = name;
        this.value = value;
        this.nominal = nominal;
    }

    public int getId() {
        return id;
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
