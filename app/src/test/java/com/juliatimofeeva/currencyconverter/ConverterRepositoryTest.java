package com.juliatimofeeva.currencyconverter;

import com.juliatimofeeva.currencyconverter.data.converter.CurrencyConverter;
import com.juliatimofeeva.currencyconverter.data.converter.CurrencyConverterImpl;
import com.juliatimofeeva.currencyconverter.data.storage.CurrencyDatabase;
import com.juliatimofeeva.currencyconverter.util.TestDataFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by julia on 13.11.17.
 */

public class ConverterRepositoryTest {

    @Mock
    private CurrencyDatabase database;

    private CurrencyConverter currencyConverter;

    @Before
    public void beforeTest() {
        MockitoAnnotations.initMocks(this);
        currencyConverter = new CurrencyConverterImpl(database);
    }

    @Test
    public void convertionSuccess() {
        when(database.getItemWithCode(TestDataFactory.getConvertionRequest().getCurrencyCodeFrom()))
                .thenReturn(TestDataFactory.getEURCurrencyInfoModel());
        when(database.getItemWithCode(TestDataFactory.getConvertionRequest().getCurrencyCodeTo()))
                .thenReturn(TestDataFactory.getUSDCurrencyInfoModel());
        double result  = currencyConverter.convert(TestDataFactory.getConvertionRequest().getCurrencyCodeFrom(),
                TestDataFactory.getConvertionRequest().getCurrencyCodeTo(),
                TestDataFactory.getConvertionRequest().getRequestValue());
        assertEquals(result, TestDataFactory.getConvertionResult());
    }
}
