package com.juliatimofeeva.currencyconverter.data.network.entities;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by julia on 03.11.17.
 */
@Root(name="ValCurs")
public class ValCurs {

    @Attribute(name = "Date")
    private String date;

    @Attribute
    private String name;

    @ElementList(inline=true)
    private List<Valute> list;

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public List<Valute> getList() {
        return list;
    }
}
