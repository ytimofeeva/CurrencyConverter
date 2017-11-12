package com.juliatimofeeva.currencyconverter.presentation.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepository;
import com.juliatimofeeva.currencyconverter.data.CurrencyInfoModel;
import com.juliatimofeeva.currencyconverter.data.CurrencyModelState;
import com.juliatimofeeva.currencyconverter.presentation.entities.ConverterUiModel;
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
        CurrencyModelState repositoryModel = currencyDataRepository.getCurrentState();
        if (currencyData == null) {
            if (repositoryModel.getCurrencyData() == null) {
                repositoryModel = currencyDataRepository.getCurrencyDataFromCache();
            } else {
                currencyData = repositoryModel.getCurrencyData();
            }
        }
        ConverterUiModel uiModel = convertStateToUi(repositoryModel);
        view.displayUiModel(uiModel);
    }

    public void detachView() {
        this.view = null;
    }

    @Override
    public void processRequest(int positionFrom, int positionTo, String value) {
        ConverterUiModel uiModel = null;
        if (TextUtils.isEmpty(value)) {
            if (view != null) {
                uiModel = ConverterUiModel.Builder.modelBuilder()
                        .setCurrencyData(getSortedNamesFromModelList(currencyData))
                        .setPositionFrom(positionFrom)
                        .setPositionTo(positionTo)
                        .setErrorMessage(EMPTY_VALUE_IN_REQUEST_ERROR_MSG)
                        .build();

            }
        } else {
            String charCodeFrom = currencyData.get(positionFrom).getCharCode();
            String charCodeTo = currencyData.get(positionTo).getCharCode();
            ConvertionRequest request = new ConvertionRequest(charCodeFrom, charCodeTo, Double.parseDouble(value));
            CurrencyModelState modelState = currencyDataRepository.convertCurrency(request);
            uiModel = convertStateToUi(modelState);

        }
        if (view != null) {
            view.displayUiModel(uiModel);
        }
    }

    @Override
    public void setCurrencyFrom(long positionFrom) {
        currencyDataRepository.saveCurrencyFrom(currencyData.get((int) positionFrom).getCharCode());
    }

    @Override
    public void setCurrencyTo(long positionTo) {
        currencyDataRepository.saveCurrencyTo(currencyData.get((int) positionTo).getCharCode());
    }

    @Override
    public void onNetworkRequestSuccess(CurrencyModelState state) {
        if (currencyData == null) {
            currencyData = state.getCurrencyData();
            view.displayUiModel(convertStateToUi(state));
        }
    }

    @Override
    public void onNetworkRequestError(CurrencyModelState state) {
    }

    @Override
    public void onCacheRequestSuccess(CurrencyModelState state) {
        currencyData = state.getCurrencyData();
        ConverterUiModel model = convertStateToUi(state);
        if (view != null) {
            view.displayUiModel(model);
        }
    }

    @Override
    public void onCacheRequestError(CurrencyModelState state) {
        ConverterUiModel model = convertStateToUi(state);
        if ((currencyData == null) && (view != null)) {
            view.displayUiModel(model);
        }
    }

    @Override
    public void onConvertProcessCompleteSuccess(CurrencyModelState state) {
        ConverterUiModel model = convertStateToUi(state);
        if (view != null) {
            view.displayUiModel(model);
        }
    }

    @Override
    public void onConvertProcessCompleteError(CurrencyModelState state) {
        ConverterUiModel model = convertStateToUi(state);
        if (view != null) {
            view.displayUiModel(model);
        }
    }

    private Set<String> getSortedNamesFromModelList(List<CurrencyInfoModel> data) {
        Set<String> namesList = new TreeSet<>();
        for (CurrencyInfoModel model : data) {
            namesList.add(model.getName());
        }
        return namesList;
    }

    private ConverterUiModel convertStateToUi(@NonNull CurrencyModelState state) {
        ConverterUiModel model = null;
        if (state.isInErrorState()) {
            model = ConverterUiModel.Builder.modelBuilder()
                    .setErrorMessage(state.getErrorMessage())
                    .setPositionFrom(getCurrencyPosition(state.getSelectedCurrencyFrom(), state.getCurrencyData()))
                    .setPositionTo(getCurrencyPosition(state.getSelectedCurrencyTo(), state.getCurrencyData()))
                    .build();
        } else if (state.isConvertionInProgress()) {
            model = ConverterUiModel.Builder.modelBuilder()
                    .setConvertionInProgress(true)
                    .setCurrencyData(getSortedNamesFromModelList(state.getCurrencyData()))
                    .setPositionFrom(getCurrencyPosition(state.getSelectedCurrencyFrom(), state.getCurrencyData()))
                    .setPositionTo(getCurrencyPosition(state.getSelectedCurrencyTo(), state.getCurrencyData()))
                    .build();
        } else if (state.isCurrencyInfoLoading()) {
            model = ConverterUiModel.Builder.modelBuilder()
                    .setCurrencyDataInProgress(true)
                    .build();
        } else {
            ConverterUiModel.Builder modelBuilder = ConverterUiModel.Builder.modelBuilder()
                    .setCurrencyData(getSortedNamesFromModelList(state.getCurrencyData()))
                    .setPositionFrom(getCurrencyPosition(state.getSelectedCurrencyFrom(), state.getCurrencyData()))
                    .setPositionTo(getCurrencyPosition(state.getSelectedCurrencyTo(), state.getCurrencyData()));
            if (state.getConvertionResult() >= 0) {
                modelBuilder.setConvertionResult(String.format("%.2f", state.getConvertionResult()));
            }
            model = modelBuilder.build();
        }
        return model;
    }

    private static int getCurrencyPosition(String charCode, @Nullable List<CurrencyInfoModel> data) {
        if (data == null) {
            return 0;
        }
        int position = 0;
        for (CurrencyInfoModel model : data) {
            if (model.getCharCode().equals(charCode)) {
                break;
            }
            position++;
        }
        if (position >= data.size()) {
            position = 0;
        }
        return position;
    }
}
