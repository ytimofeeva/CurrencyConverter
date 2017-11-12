package com.juliatimofeeva.currencyconverter.util;

import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepository;
import com.juliatimofeeva.currencyconverter.data.CurrencyModelState;

import java.util.concurrent.Semaphore;

/**
 * Created by julia on 13.11.17.
 */

public class TestListenerFactory {

    public static CurrencyDataRepository.OnDataRequestCompletionListener getTestRequestCompletionListener(Semaphore semaphore) {
        return new TestRequestCompletionListener(semaphore);
    }

    public static CurrencyDataRepository.OnConvertionCompletionListener getTestConvertionListener(Semaphore semaphore) {
        return new TestConvertionListener(semaphore);
    }


    public static class TestRequestCompletionListener implements CurrencyDataRepository.OnDataRequestCompletionListener {

        private Semaphore semaphore;

        public TestRequestCompletionListener(Semaphore sem) {
            semaphore = sem;
        }
        @Override
        public void onNetworkRequestSuccess(CurrencyModelState modelState) {
            semaphore.release();
        }

        @Override
        public void onNetworkRequestError(CurrencyModelState modelState) {
            semaphore.release();
        }

        @Override
        public void onCacheRequestSuccess(CurrencyModelState modelState) {
            semaphore.release();
        }

        @Override
        public void onCacheRequestError(CurrencyModelState modelState) {
            semaphore.release();
        }
    }

    public static class TestConvertionListener implements CurrencyDataRepository.OnConvertionCompletionListener {
        private Semaphore semaphore;

        public TestConvertionListener(Semaphore sem) {
            semaphore = sem;
        }
        @Override
        public void onConvertProcessCompleteSuccess(CurrencyModelState modelState) {
            semaphore.release();
        }

        @Override
        public void onConvertProcessCompleteError(CurrencyModelState modelState) {
            semaphore.release();
        }
    }
}
