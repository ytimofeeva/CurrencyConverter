package com.juliatimofeeva.currencyconverter.data;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.juliatimofeeva.currencyconverter.presentation.entities.ConvertionRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by julia on 02.11.17.
 */

public class CurrencyDataRepositoryImpl implements CurrencyDataRepository {

    private OnRequestCompletionListener listener;
    private ExecutorService executor;

    public CurrencyDataRepositoryImpl() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void setListener(OnRequestCompletionListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    @Override
    public void getCurrencyDataFromNetwork() {
        executor.submit(new NetworkRequestRunnable());
    }

    @Override
    public void getCurrencyDataFromCache() {
        executor.submit(new CacheRequestRunnable());
    }

    @Override
    public void convertCurrency(@NonNull ConvertionRequest request) {
        executor.submit(new ConvertionRequestRunnable(request));
    }

    @Override
    public void saveCurrencyDataToCache(List<CurrencyInfoModel> data) {
        //TODO: stub implementation
    }

    public interface OnRequestCompletionListener {
        void onNetworkRequestSuccess(List<CurrencyInfoModel> data);
        void onNetworkRequestError(Throwable error);
        void onCacheRequestSuccess(List<CurrencyInfoModel> data);
        void onCacheRequestError(Throwable error);
        void onConvertProcessCompleteSuccess(double data);
        void onConvertProcessCompleteError(Throwable error);
    }

    private class NetworkRequestRunnable implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (Exception exception) {

            }
            CurrencyInfoModel info1 = new CurrencyInfoModel("RON", "Румынский лей", 14.95, 1);
            CurrencyInfoModel info2 = new CurrencyInfoModel("SGD", "Сингапурский доллар", 72.7078, 1);
            List<CurrencyInfoModel> data = new ArrayList<>();
            data.add(info1);
            data.add(info2);
            final List<CurrencyInfoModel> finalList = data;
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onNetworkRequestSuccess(finalList);
                }
            });
        }
    }

    private class CacheRequestRunnable implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (Exception exception) {

            }
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onCacheRequestError(new Throwable("empty"));
                }
            });
        }
    }

    private class ConvertionRequestRunnable implements Runnable {
        private ConvertionRequest request;

        public ConvertionRequestRunnable(ConvertionRequest request) {
            this.request = request;
        }

        @Override
        public void run() {
            //TODO: two requests to db
            try {
                Thread.sleep(3000);
            } catch (Exception exception) {

            }
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onConvertProcessCompleteSuccess(35.17);
                }
            });
        }
    }

}
