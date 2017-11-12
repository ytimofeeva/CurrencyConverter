package com.juliatimofeeva.currencyconverter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Looper;

import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepository;
import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepositoryImpl;
import com.juliatimofeeva.currencyconverter.data.CurrencyModelState;
import com.juliatimofeeva.currencyconverter.data.converter.CurrencyConverter;
import com.juliatimofeeva.currencyconverter.data.network.NetworkRepository;
import com.juliatimofeeva.currencyconverter.data.network.exceptions.ResponseParseException;
import com.juliatimofeeva.currencyconverter.data.storage.CurrencyDatabase;
import com.juliatimofeeva.currencyconverter.util.AndroidMainThreadMockUtil;
import com.juliatimofeeva.currencyconverter.util.TestDataFactory;
import com.juliatimofeeva.currencyconverter.util.TestListenerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by julia on 12.11.17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Looper.class, CurrencyDataRepositoryImpl.class})
public class CurrencyDataRepositoryTest {

    @Mock
    private Context mockContext;

    private CurrencyDataRepository currencyDataRepository;

    @Mock
    private NetworkRepository networkRepository;

    @Mock
    private CurrencyDatabase currencyDatabase;

    @Mock
    private CurrencyConverter currencyConverter;

    @Mock
    private SharedPreferences sharedPreferences;

    private Semaphore semaphore;

    private CurrencyDataRepositoryImpl.OnDataRequestCompletionListener dataRequestCompletionListener = Mockito.spy( new CurrencyDataRepositoryImpl.OnDataRequestCompletionListener() {
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
    });

    private CurrencyDataRepositoryImpl.OnConvertionCompletionListener convertionListener = Mockito.spy(new CurrencyDataRepositoryImpl.OnConvertionCompletionListener() {
        @Override
        public void onConvertProcessCompleteSuccess(CurrencyModelState modelState) {
            semaphore.release();
        }

        @Override
        public void onConvertProcessCompleteError(CurrencyModelState modelState) {
            semaphore.release();
        }
    });

    @Before
    public void beforeTest() throws Exception {
        MockitoAnnotations.initMocks(this);
        currencyDataRepository = new CurrencyDataRepositoryImpl(mockContext,
                networkRepository,
                currencyDatabase,
                currencyConverter);
        when(mockContext.getSharedPreferences(anyString(),anyInt())).thenReturn(sharedPreferences);
        semaphore  = new Semaphore(0);
        dataRequestCompletionListener = Mockito.spy(TestListenerFactory.getTestRequestCompletionListener(semaphore));
        convertionListener = Mockito.spy(TestListenerFactory.getTestConvertionListener(semaphore));
        currencyDataRepository.setCurrencyDataListener(dataRequestCompletionListener);
        currencyDataRepository.setConvertionListener(convertionListener);
        AndroidMainThreadMockUtil.mockMainThreadHandler();


    }

    @After
    public void afterTest() {
        currencyDataRepository.removeCurrencyDataListener(dataRequestCompletionListener);
        currencyDataRepository.removeConvertionListener(convertionListener);
    }

    @Test
    public void getCurrencyDataFromNetworkSuccess() throws IOException, ResponseParseException, InterruptedException {

       when(networkRepository.getValuteListFromNetwork(TestDataFactory.getURL()))
               .thenReturn(TestDataFactory.getCurrencyInfoList(3));
       when(sharedPreferences.getString(TestDataFactory.getCurrencyFromTag(), null)).thenReturn(TestDataFactory.getCurrencyFrom());
       when(sharedPreferences.getString(TestDataFactory.getCurrencyToTag(), null)).thenReturn(TestDataFactory.getCurrencyTo());
       currencyDataRepository.getCurrencyDataFromNetwork();
       semaphore.acquire();
       CurrencyModelState modelState = currencyDataRepository.getCurrentState();
       verify(dataRequestCompletionListener, atLeastOnce()).onNetworkRequestSuccess(modelState);
       assertNotNull(modelState.getCurrencyData());
    }

    @Test
    public void getCurrencyDataFromNetworkError() throws IOException, ResponseParseException, InterruptedException {

        when(networkRepository.getValuteListFromNetwork(TestDataFactory.getURL()))
                .thenThrow(new IOException());
        when(sharedPreferences.getString(TestDataFactory.getCurrencyFromTag(), null)).thenReturn(TestDataFactory.getCurrencyFrom());
        when(sharedPreferences.getString(TestDataFactory.getCurrencyToTag(), null)).thenReturn(TestDataFactory.getCurrencyTo());
        currencyDataRepository.getCurrencyDataFromNetwork();
        semaphore.acquire();
        CurrencyModelState modelState = currencyDataRepository.getCurrentState();
        verify(dataRequestCompletionListener, atLeastOnce()).onNetworkRequestError(modelState);
        assertNull(modelState.getCurrencyData());
        assertTrue(modelState.isInErrorState());
    }

    @Test
    public void getCurrencyDataFromCacheSuccess() throws InterruptedException {
        when(currencyDatabase.getAllCurrencyItems()).thenReturn(TestDataFactory.getCurrencyInfoList(3));
        when(sharedPreferences.getString(TestDataFactory.getCurrencyFromTag(), null)).thenReturn(TestDataFactory.getCurrencyFrom());
        when(sharedPreferences.getString(TestDataFactory.getCurrencyToTag(), null)).thenReturn(TestDataFactory.getCurrencyTo());
        currencyDataRepository.getCurrencyDataFromCache();
        semaphore.acquire();
        CurrencyModelState modelState = currencyDataRepository.getCurrentState();
        verify(dataRequestCompletionListener, atLeastOnce()).onCacheRequestSuccess(modelState);
        assertNotNull(modelState.getCurrencyData());
    }

    @Test
    public void getCurrencyDataFromCacheError() throws InterruptedException {
        when(currencyDatabase.getAllCurrencyItems()).thenReturn(TestDataFactory.getCurrencyInfoList(0));
        when(sharedPreferences.getString(TestDataFactory.getCurrencyFromTag(), null))
                .thenReturn(TestDataFactory.getCurrencyFrom());
        when(sharedPreferences.getString(TestDataFactory.getCurrencyToTag(), null))
                .thenReturn(TestDataFactory.getCurrencyTo());
        currencyDataRepository.getCurrencyDataFromCache();
        semaphore.acquire();
        CurrencyModelState modelState = currencyDataRepository.getCurrentState();
        verify(dataRequestCompletionListener, atLeastOnce()).onCacheRequestError(modelState);
        assertNull(modelState.getCurrencyData());
        assertTrue(modelState.isInErrorState());
    }

    @Test
    public void convertCurrencySuccess() throws InterruptedException {
        when(currencyConverter.convert(anyString(), anyString(), anyDouble()))
                .thenReturn(TestDataFactory.getConvertionResult());
        currencyDataRepository.convertCurrency(TestDataFactory.getConvertionRequest());
        semaphore.acquire();
        CurrencyModelState modelState = currencyDataRepository.getCurrentState();
        verify(convertionListener, atLeastOnce()).onConvertProcessCompleteSuccess(modelState);
        assertNotNull(modelState.getConvertionResult());
    }

    @Test
    public void convertCurrencyError() throws InterruptedException {
        when(currencyConverter.convert(anyString(), anyString(), anyDouble()))
                .thenThrow(new SQLiteException());
        currencyDataRepository.convertCurrency(TestDataFactory.getConvertionRequest());
        semaphore.acquire();
        CurrencyModelState modelState = currencyDataRepository.getCurrentState();
        verify(convertionListener, atLeastOnce()).onConvertProcessCompleteError(modelState);
        assertTrue(modelState.isInErrorState());
    }
}
