package com.juliatimofeeva.currencyconverter.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.juliatimofeeva.currencyconverter.R;
import com.juliatimofeeva.currencyconverter.data.converter.CurrencyConverter;
import com.juliatimofeeva.currencyconverter.data.network.NetworkRepository;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by julia on 02.11.17.
 */

public class CurrencyDataRepositoryImpl implements CurrencyDataRepository {

    private final static String SHARED_PREFERENCES_NAME = "currency_prefs";
    private static final String CURRENCY_FROM_TAG = "currency_from";
    private static final String CURRENCY_TO_TAG = "currency_to";
    private final static String READ_ERR_LOG_MSG =  "Can't read from database";
    private final static String WRITE_ERR_LOG_MSG = "Can't write to database";

    private static final String TAG = CurrencyDataRepositoryImpl.class.getSimpleName();

    private Set<OnDataRequestCompletionListener> dataListenerSet;
    private Set<OnConvertionCompletionListener> convertionListenerSet;

    private ExecutorService executor;
    private NetworkRepository networkRepository;
    private CurrencyDatabase database;
    private CurrencyConverter converter;
    private CurrencyModelState modelState;
    private Context context;


    public CurrencyDataRepositoryImpl(Context context,
                                      NetworkRepository networkRepository,
                                      CurrencyDatabase database,
                                      CurrencyConverter converter) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        dataListenerSet = Collections.newSetFromMap(new ConcurrentHashMap<OnDataRequestCompletionListener, Boolean>());
        convertionListenerSet = Collections.newSetFromMap(new ConcurrentHashMap<OnConvertionCompletionListener, Boolean>());
        this.database = database;
        this.networkRepository = networkRepository;
        this.converter = converter;
        this.modelState = CurrencyModelState.Builder.modelStateBuilder().build();
    }

    public void setCurrencyDataListener(OnDataRequestCompletionListener listener) {
        dataListenerSet.add(listener);
    }

    public void removeCurrencyDataListener(OnDataRequestCompletionListener listener) {
        dataListenerSet.remove(listener);
    }

    public void setConvertionListener(OnConvertionCompletionListener listener) {
        convertionListenerSet.add(listener);
    }

    public void removeConvertionListener(OnConvertionCompletionListener listener) {
        convertionListenerSet.remove(listener);
    }

    @Override
    public CurrencyModelState getCurrencyDataFromNetwork() {
        modelState = CurrencyModelState.Builder.modelStateBuilder()
                .setCurrencyInfoLoading(true)
                .build();
        executor.submit(new NetworkRequestRunnable());
        return modelState;
    }

    @Override
    public CurrencyModelState getCurrencyDataFromCache() {
        modelState = CurrencyModelState.Builder.modelStateBuilder()
                .setCurrencyInfoLoading(true)
                .build();
        executor.submit(new CacheRequestRunnable());
        return modelState;
    }

    @Override
    public CurrencyModelState convertCurrency(@NonNull ConvertionRequest request) {
        modelState = CurrencyModelState.Builder.modelStateBuilder()
                .setConvertionInProgress(true)
                .setCurrencyData(modelState.getCurrencyData())
                .setSelectedCurrencyFrom(getCurrencyFrom())
                .setSelectedCurrencyTo(getCurrencyTo())
                .build();
        executor.submit(new ConvertionRequestRunnable(request));
        return modelState;
    }

    @Override
    public void saveCurrencyDataToCache(List<CurrencyInfoModel> data) {
        executor.submit(new SaveRequestRunnable(data));
    }

    private class NetworkRequestRunnable implements Runnable {

        private static final String REQUEST_URL = "http://www.cbr.ru/scripts/XML_daily.asp";

        @Override
        public void run() {

            URL url = null;
            List<CurrencyInfoModel> result = null;
            try {
                url = new URL(REQUEST_URL);
                result = networkRepository.getValuteListFromNetwork(url);
            } catch (IOException | ResponseParseException exception) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        modelState = CurrencyModelState.Builder.modelStateBuilder()
                                .setCurrencyData(modelState.getCurrencyData())
                                .setInErrorState(true)
                                .setErrorMessage(exception.getMessage())
                                .build();
                        for(OnDataRequestCompletionListener listener : dataListenerSet) {
                            listener.onNetworkRequestError(modelState);
                        }
                    }
                });
                modelState = CurrencyModelState.Builder.modelStateBuilder()
                        .setCurrencyData(modelState.getCurrencyData())
                        .setInErrorState(true)
                        .setErrorMessage(exception.getMessage())
                        .build();
                for(OnDataRequestCompletionListener listener : dataListenerSet) {
                    listener.onNetworkRequestError(modelState);
                }
                return;
            }

            final List<CurrencyInfoModel> finalList = result;

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    modelState = CurrencyModelState.Builder
                            .modelStateBuilder().setCurrencyData(finalList)
                            .setSelectedCurrencyFrom(getCurrencyFrom())
                            .setSelectedCurrencyTo(getCurrencyTo())
                            .build();
                    for(OnDataRequestCompletionListener listener : dataListenerSet) {
                        listener.onNetworkRequestSuccess(modelState);
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
                Log.e(TAG, READ_ERR_LOG_MSG);
            }

            Handler handler = new Handler(Looper.getMainLooper());
            if ((data == null) || (data.size() == 0)) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        modelState = CurrencyModelState.Builder.modelStateBuilder()
                                .setCurrencyData(modelState.getCurrencyData())
                                .setInErrorState(true)
                                .setErrorMessage(context.getString(R.string.err_empty_base))
                                .build();
                        for(OnDataRequestCompletionListener listener : dataListenerSet) {
                            listener.onCacheRequestError(modelState);
                        }
                    }
                });
            } else {
                final List<CurrencyInfoModel> result = data;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        modelState = CurrencyModelState.Builder
                                .modelStateBuilder().setCurrencyData(result)
                                .setSelectedCurrencyFrom(getCurrencyFrom())
                                .setSelectedCurrencyTo(getCurrencyTo())
                                .build();
                        for(OnDataRequestCompletionListener listener : dataListenerSet) {
                            listener.onCacheRequestSuccess(modelState);
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
                Log.e(TAG, WRITE_ERR_LOG_MSG);
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
                        modelState = CurrencyModelState.Builder.modelStateBuilder()
                                .setCurrencyData(modelState.getCurrencyData())
                                .setSelectedCurrencyFrom(getCurrencyFrom())
                                .setSelectedCurrencyTo(getCurrencyTo())
                                .setInErrorState(true)
                                .setErrorMessage(context.getString(R.string.err_convertion))
                                .build();
                        for(OnConvertionCompletionListener listener : convertionListenerSet) {
                            listener.onConvertProcessCompleteError(modelState);
                        }
                    }
                });
            }
            final double finalResult = result;
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    modelState = CurrencyModelState.Builder.modelStateBuilder()
                            .setCurrencyData(modelState.getCurrencyData())
                            .setConvertionResult(finalResult)
                            .setSelectedCurrencyFrom(getCurrencyFrom())
                            .setSelectedCurrencyTo(getCurrencyTo())
                            .build();
                    for(OnConvertionCompletionListener listener : convertionListenerSet) {
                        listener.onConvertProcessCompleteSuccess(modelState);
                    }

                }
            });
        }
    }

    @Override
    public void saveCurrencyFrom(@NonNull String charCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENCY_FROM_TAG, charCode);
        editor.commit();
        modelState = CurrencyModelState.Builder.modelStateBuilder()
                .setCurrencyData(modelState.getCurrencyData())
                .setConvertionResult(modelState.getConvertionResult())
                .setSelectedCurrencyFrom(getCurrencyFrom())
                .setSelectedCurrencyTo(getCurrencyTo())
                .build();
    }

    @Override
    public void saveCurrencyTo(@NonNull String charCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENCY_TO_TAG, charCode);
        editor.commit();
        modelState = CurrencyModelState.Builder.modelStateBuilder()
                .setCurrencyData(modelState.getCurrencyData())
                .setConvertionResult(modelState.getConvertionResult())
                .setSelectedCurrencyFrom(getCurrencyFrom())
                .setSelectedCurrencyTo(getCurrencyTo())
                .build();
    }

    @Nullable
    @Override
    public String getCurrencyFrom() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        String currencyFrom = sharedPreferences.getString(CURRENCY_FROM_TAG, null);
        return currencyFrom;
    }

    @Nullable
    @Override
    public String getCurrencyTo() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        String currencyTo = sharedPreferences.getString(CURRENCY_TO_TAG, null);
        return currencyTo;
    }

    @Override
    public CurrencyModelState getCurrentState() {
        return modelState;
    }


}
