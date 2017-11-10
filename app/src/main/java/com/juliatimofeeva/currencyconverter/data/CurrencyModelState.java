package com.juliatimofeeva.currencyconverter.data;

import java.util.List;

/**
 * Created by julia on 08.11.17.
 */

public class CurrencyModelState {

    private final boolean currencyInfoLoading;
    private final boolean convertionInProgress;
    private final boolean inErrorState;
    private final String errorMessage;
    private final List<CurrencyInfoModel> currencyData;
    private final double convertionResult;
    private final String selectedCurrencyFrom;
    private final String selectedCurrencyTo;


    private CurrencyModelState(boolean currencyInfoLoading,
                               boolean convertionInProgress,
                               boolean inErrorState,
                               String errorMessage,
                               List<CurrencyInfoModel> currencyData,
                               double convertionResult,
                               String selectedFrom,
                               String selectedTo)
    {
        this.currencyInfoLoading = currencyInfoLoading;
        this.convertionInProgress = convertionInProgress;
        this.inErrorState = inErrorState;
        this.errorMessage = errorMessage;
        this.currencyData = currencyData;
        this.convertionResult = convertionResult;
        this.selectedCurrencyFrom = selectedFrom;
        this.selectedCurrencyTo = selectedTo;
    }

    public boolean isCurrencyInfoLoading() {
        return currencyInfoLoading;
    }

    public boolean isConvertionInProgress() {
        return convertionInProgress;
    }

    public boolean isInErrorState() {
        return inErrorState;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<CurrencyInfoModel> getCurrencyData() {
        return currencyData;
    }

    public String getSelectedCurrencyFrom() {
        return selectedCurrencyFrom;
    }

    public String getSelectedCurrencyTo() {
        return selectedCurrencyTo;
    }

    public double getConvertionResult() {
        return convertionResult;
    }

    public static class Builder {
        private  boolean currencyInfoLoading;
        private  boolean convertionInProgress;
        private  boolean inErrorState;
        private  String errorMessage;
        private  List<CurrencyInfoModel> currencyData;
        private double convertionResult;
        private  String selectedCurrencyFrom;
        private  String selectedCurrencyTo;

        public static Builder modelStateBuilder() {
            return new Builder();
        }

        private Builder() {
            currencyInfoLoading = false;
            convertionInProgress = false;
            inErrorState = false;
            errorMessage = null;
            convertionResult = -1;
        }

        public Builder setCurrencyInfoLoading(boolean currencyInfoLoading) {
            this.currencyInfoLoading = currencyInfoLoading;
            return this;
        }

        public Builder setConvertionInProgress(boolean convertionInProgress) {
            this.convertionInProgress = convertionInProgress;
            return this;
        }

        public Builder setInErrorState(boolean inErrorState) {
            this.inErrorState = inErrorState;
            return this;
        }


        public Builder setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder setCurrencyData(List<CurrencyInfoModel> currencyData) {
            this.currencyData = currencyData;
            return this;
        }

        public Builder setSelectedCurrencyFrom(String selectedCurrencyFrom) {
            this.selectedCurrencyFrom = selectedCurrencyFrom;
            return this;
        }

        public Builder setSelectedCurrencyTo(String selectedCurrencyTo) {
            this.selectedCurrencyTo = selectedCurrencyTo;
            return this;
        }

        public Builder setConvertionResult(double result) {
            this.convertionResult = result;
            return this;
        }

        public CurrencyModelState build() {
            return new CurrencyModelState(currencyInfoLoading,
                    convertionInProgress,
                    inErrorState,
                    errorMessage,
                    currencyData,
                    convertionResult,
                    selectedCurrencyFrom,
                    selectedCurrencyTo);
        }
    }
}
