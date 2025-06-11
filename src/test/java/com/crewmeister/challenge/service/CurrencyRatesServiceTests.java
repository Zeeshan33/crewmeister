package com.crewmeister.challenge.service;

import com.crewmeister.challenge.repository.CurrencyRateRepository;
import com.crewmeister.challenge.repository.CurrencyRepository;
import com.crewmeister.challenge.service.impl.CurrencyRatesServiceImpl;
import com.crewmeister.challenge.model.Currency;
import com.crewmeister.challenge.model.CurrencyRates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrencyRatesServiceTests {

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyRatesServiceImpl service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllCurrencies() {
        List<Currency> currencies = List.of(setCurrency("USD"), setCurrency("EUR"));
        when(currencyRepository.findAllCurrency()).thenReturn(currencies);

        List<Currency> result = service.getAllCurrencies();

        assertEquals(2, result.size());
        verify(currencyRepository).findAllCurrency();
    }

    @Test
    public void shouldReturnPaginatedCurrencyRates() {
        Pageable pageable = PageRequest.of(0, 2);
        List<CurrencyRates> rates = List.of(mock(CurrencyRates.class));
        when(currencyRateRepository.findAll(pageable)).thenReturn(new PageImpl<>(rates));

        Page<CurrencyRates> result = service.getAllCurrencyRates(pageable);

        assertEquals(1, result.getContent().size());
    }

    @Test
    public void shouldReturnCurrencyRatesByDate() {
        LocalDate date = LocalDate.now();
        List<CurrencyRates> rates = List.of(mock(CurrencyRates.class));
        when(currencyRateRepository.findByDate(date)).thenReturn(rates);

        List<CurrencyRates> result = service.getCurrencyRatesByDate(date);
        assertEquals(1, result.size());
    }

    @Test
    public void shouldHandleEmptyCurrencyRatesByDate() {
        LocalDate date = LocalDate.now();
        when(currencyRateRepository.findByDate(date)).thenReturn(Collections.emptyList());

        List<CurrencyRates> result = service.getCurrencyRatesByDate(date);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnCurrencyRateByDateAndCurrency() {
        LocalDate date = LocalDate.now();
        String currency = "USD";
        CurrencyRates rate = mock(CurrencyRates.class);
        when(currencyRateRepository.findByDateAndCurrency_CurrencyName(date, currency)).thenReturn(Optional.of(rate));

        CurrencyRates result = service.getCurrencyRateByDateAndCurrency(date, currency);
        assertNotNull(result);
    }

    @Test
    public void shouldThrowExceptionWhenCurrencyRateNotFound() {
        LocalDate date = LocalDate.now();
        String currency = "USD";
        when(currencyRateRepository.findByDateAndCurrency_CurrencyName(date, currency)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                service.getCurrencyRateByDateAndCurrency(date, currency)
        );
    }

    @Test
    public void shouldConvertToEur() {
        LocalDate date = LocalDate.now();
        String currency = "USD";
        double rateValue = 1.2;
        double amount = 100;

        Currency currencyObj = setCurrency("USD");
        CurrencyRates rate = setCurrencyRates(date, rateValue, currencyObj);

        when(currencyRateRepository.findByDateAndCurrency_CurrencyName(date, currency)).thenReturn(Optional.of(rate));

        Double result = service.convertToEur(currency, amount, date);
        assertEquals(120.0, result);
    }

    @Test
    public void shouldThrowExceptionForNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                service.convertToEur("USD", -10.0, LocalDate.now())
        );
    }

    @Test
    public void shouldThrowExceptionIfRateNotFoundInConversion() {
        when(currencyRateRepository.findByDateAndCurrency_CurrencyName(any(), any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                service.convertToEur("USD", 10.0, LocalDate.now())
        );
    }

    @Test
    public void shouldSaveOnlyNewRates() {
        Currency currency = setCurrency("USD");
        CurrencyRates rate = setCurrencyRates(LocalDate.now(), 1.0, currency);

        when(currencyRateRepository.findByDateAndCurrency_CurrencyName(any(), any())).thenReturn(Optional.empty());

        service.saveRates(List.of(rate));

        verify(currencyRateRepository).save(rate);
    }

    @Test
    public void shouldSkipSavingExistingRates() {
        Currency currency = setCurrency("USD");
        CurrencyRates rate = setCurrencyRates(LocalDate.now(), 1.0, currency);

        when(currencyRateRepository.findByDateAndCurrency_CurrencyName(any(), any())).thenReturn(Optional.of(rate));

        service.saveRates(List.of(rate));

        verify(currencyRateRepository, never()).save(rate);
    }

    @Test
    public void shouldHandleExceptionDuringSave() {
        Currency currency = setCurrency("USD");
        CurrencyRates rate = setCurrencyRates(LocalDate.now(), 1.0, currency);

        when(currencyRateRepository.findByDateAndCurrency_CurrencyName(any(), any()))
                .thenThrow(new RuntimeException("DB error"));

        assertDoesNotThrow(() -> service.saveRates(List.of(rate)));
    }

    Currency setCurrency(String currencyName) {
        Currency currency = new Currency();
        currency.setCurrencyName(currencyName);

        return currency;
    }

    CurrencyRates setCurrencyRates(LocalDate date, double rate,Currency currency) {
        CurrencyRates currencyRates = new CurrencyRates();
        currencyRates.setDate(date);
        currencyRates.setRate(rate);
        currencyRates.setCurrency(currency);

        return currencyRates;
    }
}
