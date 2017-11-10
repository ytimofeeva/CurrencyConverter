package com.juliatimofeeva.currencyconverter.presentation.ui;

import java.util.Set;

/**
 * Created by julia on 08.11.17.
 */

public class ConverterUiModel {
    private final String convertionResult;
    private final String errorMessage;
    private final Set<String> currencyData;
    private final boolean currencyDataInProgress;
    private final boolean convertionInProgress;
    private final int positionFrom;

    public int getPositionFrom() {
        return positionFrom;
    }

    public int getPositionTo() {
        return positionTo;
    }

    private final int positionTo;

    private ConverterUiModel(String convertionResult,
                             String errorMessage,
                             Set<String> currencyData,
                             boolean currencyDataInProgress,
                             boolean convertionInProgress,
                             int positionFrom,
                             int positionTo) {
        this.convertionResult = convertionResult;
        this.errorMessage = errorMessage;
        this.currencyData = currencyData;
        this.currencyDataInProgress = currencyDataInProgress;
        this.convertionInProgress = convertionInProgress;
        this.positionFrom = positionFrom;
        this.positionTo = positionTo;
    }

    public String getConvertionResult() {
        return convertionResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Set<String> getCurrencyData() {
        return currencyData;
    }

    public boolean isCurrencyDataInProgress() {
        return currencyDataInProgress;
    }

    public boolean isConvertionInProgress() {
        return convertionInProgress;
    }


    public static class Builder {
        private String convertionResult;
        private String errorMessage;
        private Set<String> currencyData;
        private boolean currencyDataInProgress;
        private boolean convertionInProgress;
        private int positionFrom;
        private int positionTo;

        private Builder() {
            this.convertionResult = null;
            this.errorMessage = null;
            this.currencyData = null;
            this.convertionInProgress = false;
            this.currencyDataInProgress = false;
            this.positionFrom = 0;
            this.positionTo = 0;
        }
        public static Builder modelBuilder() {
            return new Builder();
        }

        public Builder setConvertionResult(String result) {
            this.convertionResult = result;
            return this;
        }

        public Builder setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder setCurrencyData(Set<String> currencyData) {
            this.currencyData = currencyData;
            return this;
        }

        public Builder setCurrencyDataInProgress(boolean currencyDataInProgress) {
            this.currencyDataInProgress = currencyDataInProgress;
            return this;
        }

        public Builder setConvertionInProgress(boolean convertionInProgress) {
            this.convertionInProgress = convertionInProgress;
            return this;
        }

        public Builder setPositionFrom(int positionFrom) {
            this.positionFrom = positionFrom;
            return this;
        }

        public Builder setPositionTo(int positionTo) {
            this.positionTo = positionTo;
            return this;
        }

        public ConverterUiModel build() {
            return new ConverterUiModel(convertionResult,
                    errorMessage,
                    currencyData,
                    currencyDataInProgress,
                    convertionInProgress,
                    positionFrom,
                    positionTo);
        }
    }


}
