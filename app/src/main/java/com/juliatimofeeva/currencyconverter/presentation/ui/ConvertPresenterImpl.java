package com.juliatimofeeva.currencyconverter.presentation.ui;

import android.text.TextUtils;

import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepository;
import com.juliatimofeeva.currencyconverter.data.CurrencyInfoModel;
import com.juliatimofeeva.currencyconverter.presentation.entities.ConvertionRequest;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by julia on 02.11.17.
 */

public class ConvertPresenterImpl implements ConvertPresenter {

    private CurrencyDataRepository currencyDataRepository;
    private ConverterView view;
    private static final String EMPTY_VALUE_IN_REQUEST_ERROR_MSG = "Empty value in request";
    private List<CurrencyInfoModel> currencyData;

    public ConvertPresenterImpl(CurrencyDataRepository repository) {
            this.currencyDataRepository = repository;
    }

    public void attachView(ConverterView view) {
        this.view = view;
        currencyDataRepository.getCurrencyDataFromNetwork();
    }

    public void detachView() {
        this.view = null;
    }

    @Override
    public void processRequest(int positionFrom, int positionTo, String value) {
        if (TextUtils.isEmpty(value)) {
            if (view != null) {
                view.showError(EMPTY_VALUE_IN_REQUEST_ERROR_MSG);
            }
        } else {
            String charCodeFrom = currencyData.get(positionFrom).getCharCode();
            String charCodeTo = currencyData.get(positionTo).getCharCode();
            ConvertionRequest request = new ConvertionRequest(charCodeFrom, charCodeTo, Integer.parseInt(value));
            currencyDataRepository.convertCurrency(request);
        }
    }

    @Override
    public void onNetworkRequestSuccess(List<CurrencyInfoModel> data) {
        currencyDataRepository.saveCurrencyDataToCache(data);
        currencyData = data;
        if (view != null) {
            view.showCurrencyNames(getSortedNamesFromModelList(data));
        }
    }

    @Override
    public void onNetworkRequestError(Throwable error) {
        currencyDataRepository.getCurrencyDataFromCache();
    }

    @Override
    //already sorted list
    public void onCacheRequestSuccess(List<CurrencyInfoModel> data) {
        if (view != null) {
            view.showCurrencyNames(getSortedNamesFromModelList(data));
        }
    }

    @Override
    public void onCacheRequestError(Throwable error) {
        if (view != null) {
            view.showError(error.getLocalizedMessage());
        }
    }

    @Override
    public void onConvertProcessCompleteSuccess(double data) {
        if (view != null) {
            view.showResult(String.valueOf(data));
        }
    }

    @Override
    public void onConvertProcessCompleteError(Throwable error) {

    }

    private Set<String> getSortedNamesFromModelList(List<CurrencyInfoModel> data) {
        Set<String> namesList = new TreeSet<>();
        for(CurrencyInfoModel model : data) {
            namesList.add(model.getName());
        }
        return namesList;
    }
}
