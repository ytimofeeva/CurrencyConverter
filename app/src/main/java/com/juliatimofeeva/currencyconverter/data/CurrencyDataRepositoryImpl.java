package com.juliatimofeeva.currencyconverter.data;

import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.juliatimofeeva.currencyconverter.data.converter.CurrencyConverter;
import com.juliatimofeeva.currencyconverter.data.network.NetworkRequestProcessor;
import com.juliatimofeeva.currencyconverter.data.network.exceptions.ResponseParseException;
import com.juliatimofeeva.currencyconverter.data.storage.CurrencyDatabase;
import com.juliatimofeeva.currencyconverter.presentation.entities.ConvertionRequest;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by julia on 02.11.17.
 */

public class CurrencyDataRepositoryImpl implements CurrencyDataRepository {

    private static final String TAG = CurrencyDataRepositoryImpl.class.getSimpleName();
    private Set<OnRequestCompletionListener> listenerSet;
    private ExecutorService executor;
    private CurrencyDatabase database;
    private CurrencyConverter converter;

    public CurrencyDataRepositoryImpl(CurrencyDatabase database, CurrencyConverter converter) {
        this.executor = Executors.newSingleThreadExecutor();
        listenerSet = Collections.newSetFromMap(new ConcurrentHashMap<OnRequestCompletionListener, Boolean>());
        this.database = database;
        this.converter = converter;
    }

    public void setListener(OnRequestCompletionListener listener) {
        listenerSet.add(listener);
    }

    public void removeListener(OnRequestCompletionListener listener) {
        listenerSet.remove(listener);
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
        if ((data != null) && (data.size() > 0)) {
            executor.submit(new SaveRequestRunnable(data));
        }
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

        private static final String REQUEST_URL = "http://www.cbr.ru/scripts/XML_daily.asp";

        @Override
        public void run() {
            URL url = null;
            List<CurrencyInfoModel> result = null;
            try {
                url = new URL(REQUEST_URL);
                result = NetworkRequestProcessor.getValuteListFromNetwork(url);
            } catch (IOException | ResponseParseException exception) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for(OnRequestCompletionListener listener : listenerSet) {
                            listener.onNetworkRequestError(exception);
                        }
                    }
                });
                return;
            }

            final List<CurrencyInfoModel> finalList = result;

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for(OnRequestCompletionListener listener : listenerSet) {
                        listener.onNetworkRequestSuccess(finalList);
                    }
                }
            });
        }
    }

    private class CacheRequestRunnable implements Runnable {

        @Override
        public void run() {
            List<CurrencyInfoModel> data = null;
            try {
                data = database.getAllCurrencyItems();
            } catch (SQLiteException exception) {
                Log.e(TAG, "Can't read from database");
            }

            Handler handler = new Handler(Looper.getMainLooper());
            if (data == null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for(OnRequestCompletionListener listener : listenerSet) {
                            listener.onCacheRequestError(new Throwable("empty"));
                        }
                    }
                });
            } else {
                final List<CurrencyInfoModel> result = data;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for(OnRequestCompletionListener listener : listenerSet) {
                            listener.onCacheRequestSuccess(result);
                        }
                    }
                });

            }
        }
    }

    private class SaveRequestRunnable implements Runnable {

        public List<CurrencyInfoModel> dataForSave;

        public SaveRequestRunnable(@NonNull List<CurrencyInfoModel> data) {
            this.dataForSave = data;
        }
        @Override
        public void run() {
            try {
                database.insertCurrencyItems(dataForSave);
            } catch (SQLiteException exception) {
                Log.e(TAG, "Can't write to database");
            }
        }
    }

    private class ConvertionRequestRunnable implements Runnable {
        private ConvertionRequest request;

        public ConvertionRequestRunnable(ConvertionRequest request) {
            this.request = request;
        }

        @Override
        public void run() {
            double result = 0;
            try {
                result = converter.convert(request.getCurrencyCodeFrom(), request.getCurrencyCodeTo(), request.getRequestValue());
            } catch (SQLiteException|NoSuchElementException exception) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for(OnRequestCompletionListener listener : listenerSet) {
                            listener.onCacheRequestError(new Throwable("Try again later"));
                        }
                    }
                });
            }
            final double finalResult = result;
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    for(OnRequestCompletionListener listener : listenerSet) {
                        listener.onConvertProcessCompleteSuccess(finalResult);
                    }
                }
            });
        }
    }

}
