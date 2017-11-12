package com.juliatimofeeva.currencyconverter.data.network;

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.juliatimofeeva.currencyconverter.data.CurrencyInfoModel;
import com.juliatimofeeva.currencyconverter.data.network.exceptions.ResponseParseException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by julia on 11.11.17.
 */

public interface NetworkRepository {

    @Nullable
    @WorkerThread
    List<CurrencyInfoModel> getValuteListFromNetwork(URL url) throws IOException, ResponseParseException;


}
