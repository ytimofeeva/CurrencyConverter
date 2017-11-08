package com.juliatimofeeva.currencyconverter.data.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.juliatimofeeva.currencyconverter.data.CurrencyInfoModel;
import com.juliatimofeeva.currencyconverter.data.network.entities.ValCurs;
import com.juliatimofeeva.currencyconverter.data.network.entities.Valute;
import com.juliatimofeeva.currencyconverter.data.network.exceptions.ResponseParseException;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by julia on 03.11.17.
 */

public class NetworkRequestProcessor {

    @Nullable
    public static List<CurrencyInfoModel> getValuteListFromNetwork(URL url) throws IOException, ResponseParseException {
        List<CurrencyInfoModel> result = null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Serializer serializer = new Persister();
            ValCurs data = serializer.read(ValCurs.class, in);
            if (data != null) {
                result = convertFromRawToModel(data);
            }
        } catch (Exception e) {
            throw new ResponseParseException(e.getMessage());
        }
        finally {
            urlConnection.disconnect();
        }
        return result;
    }

    private static List<CurrencyInfoModel> convertFromRawToModel(@NonNull ValCurs data) {
        List<Valute> valuteList = data.getList();
        List<CurrencyInfoModel> result = new ArrayList<>();
        for(Valute element : valuteList) {
            CurrencyInfoModel model = new CurrencyInfoModel(element.getCharCode(),
                    element.getName(), Double.parseDouble(element.getValue().replace(",",".")), element.getNominal());
            result.add(model);
        }
        return result;
    }
}
