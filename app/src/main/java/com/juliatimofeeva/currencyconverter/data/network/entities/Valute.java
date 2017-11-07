package com.juliatimofeeva.currencyconverter.data.network.entities;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by julia on 03.11.17.
 */
@Root(name="Valute")
public class Valute {

    @Attribute(name="ID")
    private String id;

    @Element(name="NumCode")
    private String numCode;

    @Element(name="CharCode")
    private String charCode;

    @Element(name="Nominal")
    private int nominal;

    @Element(name="Name")
    private String name;

    @Element(name="Value")
    private String value;

    public String getId() {
        return id;
    }

    public String getNumCode() {
        return numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public int getNominal() {
        return nominal;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
