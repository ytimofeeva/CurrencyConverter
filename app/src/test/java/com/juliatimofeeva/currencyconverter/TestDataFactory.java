package com.juliatimofeeva.currencyconverter;

import com.juliatimofeeva.currencyconverter.data.CurrencyInfoModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by julia on 12.11.17.
 */

public class TestDataFactory {

    public static URL getURL() {
        try {
            return new URL("http://www.cbr.ru/scripts/XML_daily.asp");
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static List<CurrencyInfoModel> getCurrencyInfoList() {
        List<CurrencyInfoModel> list = new ArrayList<>();
        list.add(getCurrencyInfoModel());
        list.add(getCurrencyInfoModel());
        return list;
    }

    public static CurrencyInfoModel getCurrencyInfoModel() {
        return new CurrencyInfoModel("RUB", "Российский рубль", 1, 1);
    }

    public static String getCurrencyFromTag() {
        return "currency_from";
    }

    public static String getCurrencyToTag() {
        return "currency_to";
    }

    public static String getCurrencyFrom() {
        return "EUR";
    }

    public static String getCurrencyTo() {
        return "USD";
    }
}
