package com.juliatimofeeva.currencyconverter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;

import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepository;
import com.juliatimofeeva.currencyconverter.data.CurrencyDataRepositoryImpl;
import com.juliatimofeeva.currencyconverter.data.CurrencyModelState;
import com.juliatimofeeva.currencyconverter.data.converter.CurrencyConverter;
import com.juliatimofeeva.currencyconverter.data.network.NetworkRepository;
import com.juliatimofeeva.currencyconverter.data.network.exceptions.ResponseParseException;
import com.juliatimofeeva.currencyconverter.data.storage.CurrencyDatabase;

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

    private Semaphore semaphore = new Semaphore(0);

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

        }

        @Override
        public void onCacheRequestError(CurrencyModelState modelState) {

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
        currencyDataRepository.setCurrencyDataListener(dataRequestCompletionListener);
        AndroidMainThreadMockUtil.mockMainThreadHandler();

    }

    @Test
    public void getCurrencyDataFromNetworkSuccess() throws IOException, ResponseParseException, InterruptedException {

       when(networkRepository.getValuteListFromNetwork(TestDataFactory.getURL()))
               .thenReturn(TestDataFactory.getCurrencyInfoList());
       when(sharedPreferences.getString(TestDataFactory.getCurrencyFromTag(), null)).thenReturn(TestDataFactory.getCurrencyFrom());
       when(sharedPreferences.getString(TestDataFactory.getCurrencyToTag(), null)).thenReturn(TestDataFactory.getCurrencyTo());
       currencyDataRepository.getCurrencyDataFromNetwork();
       semaphore.acquire();
       CurrencyModelState modelState = currencyDataRepository.getCurrentState();
       verify(dataRequestCompletionListener, atLeastOnce()).onNetworkRequestSuccess(modelState);
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
    }
}
