package com.juliatimofeeva.currencyconverter.util;

import com.juliatimofeeva.currencyconverter.data.CurrencyInfoModel;
import com.juliatimofeeva.currencyconverter.presentation.entities.ConvertionRequest;

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

    public static List<CurrencyInfoModel> getCurrencyInfoList(int size) {
        List<CurrencyInfoModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(getCurrencyInfoModel());
        }
        return list;
    }

    public static CurrencyInfoModel getCurrencyInfoModel() {
        return new CurrencyInfoModel("RUB", "Российский рубль", 1, 1);
    }

    public static CurrencyInfoModel getUSDCurrencyInfoModel() {
        return new CurrencyInfoModel("USD", "Доллар США", 59.2808, 1);
    }

    public static CurrencyInfoModel getEURCurrencyInfoModel() {
        return new CurrencyInfoModel("EUR", "Евро", 68.9791, 1);
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

    public static ConvertionRequest getConvertionRequest() {
        return new ConvertionRequest(getCurrencyFrom(), getCurrencyTo(), 10);
    }

    public static Double getConvertionResult() {
        return new Double(11.635993441384057);
    }
}
