package com.crewmeister.challenge.controller;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import com.crewmeister.challenge.dto.CurrencyDTO;
import com.crewmeister.challenge.dto.CurrencyRatesDTO;
import com.crewmeister.challenge.model.Currency;
import com.crewmeister.challenge.model.CurrencyRates;
import com.crewmeister.challenge.service.CurrencyRatesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

public class CurrencyRatesControllerTests {

    @Mock
    private CurrencyRatesService currencyRatesService;

    @InjectMocks
    private CurrencyRatesController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCurrencies_returnsCurrencyDtoList() {
        // Mock Data
        List<Currency> currencies = List.of(setCurrency("USD"), setCurrency("EUR"));
        when(currencyRatesService.getAllCurrencies()).thenReturn(currencies);

        // Assuming CurrencyMapper.INSTANCE.toDtoList converts currencies to DTOs
        // Mock or create CurrencyMapper stub if necessary
        // For simplicity, let's assume CurrencyMapper works as expected

        ResponseEntity<List<CurrencyDTO>> response = controller.getAllCurrencies();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        verify(currencyRatesService).getAllCurrencies();
    }

    @Test
    void getAllCurrencyRates_returnsPagedDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<CurrencyRates> ratesList = List.of(new CurrencyRates(), new CurrencyRates());
        Page<CurrencyRates> ratesPage = new PageImpl<>(ratesList, pageable, ratesList.size());

        when(currencyRatesService.getAllCurrencyRates(pageable)).thenReturn(ratesPage);

        ResponseEntity<Page<CurrencyRatesDTO>> response = controller.getAllCurrencyRates(pageable);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        verify(currencyRatesService).getAllCurrencyRates(pageable);
    }

    @Test
    void getCurrencyRatesByDate_returnsListDto() {
        LocalDate date = LocalDate.of(2023, 6, 1);
        List<CurrencyRates> rates = List.of(new CurrencyRates(), new CurrencyRates());
        when(currencyRatesService.getCurrencyRatesByDate(date)).thenReturn(rates);

        ResponseEntity<List<CurrencyRatesDTO>> response = controller.getCurrencyRatesByDate(date);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        verify(currencyRatesService).getCurrencyRatesByDate(date);
    }

    @Test
    void getCurrencyRatesByDateAndCurrency_returnsDto() {
        LocalDate date = LocalDate.of(2023, 6, 1);
        String currency = "USD";
        CurrencyRates currencyRates = new CurrencyRates();

        when(currencyRatesService.getCurrencyRateByDateAndCurrency(date, currency)).thenReturn(currencyRates);

        ResponseEntity<CurrencyRatesDTO> response = controller.getCurrencyRatesByDateAndCurrency(date, currency);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        verify(currencyRatesService).getCurrencyRateByDateAndCurrency(date, currency);
    }

    @Test
    void convertToEur_returnsConvertedAmount() {
        LocalDate date = LocalDate.of(2023, 6, 1);
        String currency = "USD";
        double amount = 100.0;
        Double converted = 85.0;

        when(currencyRatesService.convertToEur(currency, amount, date)).thenReturn(converted);

        ResponseEntity<Double> response = controller.convertToEur(currency, amount, date);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(converted);
        verify(currencyRatesService).convertToEur(currency, amount, date);
    }

    @Test
    void getCurrencyRatesByDateAndCurrency_whenServiceThrowsException_returnsError() {
        LocalDate date = LocalDate.of(2023, 6, 1);
        String currency = "INVALID";

        when(currencyRatesService.getCurrencyRateByDateAndCurrency(date, currency))
                .thenThrow(new RuntimeException("Currency not found"));

        // Depending on your controller setup, you may want to verify the exception is thrown
        // or wrap it in a ResponseStatusException in real controller code.
        assertThatThrownBy(() -> controller.getCurrencyRatesByDateAndCurrency(date, currency))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Currency not found");

        verify(currencyRatesService).getCurrencyRateByDateAndCurrency(date, currency);
    }

    @Test
    void getAllCurrencies_whenServiceReturnsEmptyList_returnsEmptyList() {
        when(currencyRatesService.getAllCurrencies()).thenReturn(List.of());

        ResponseEntity<List<CurrencyDTO>> response = controller.getAllCurrencies();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEmpty();
    }

    Currency setCurrency(String currencyName) {
        Currency currency = new Currency();
        currency.setCurrencyName(currencyName);

        return currency;
    }
}
