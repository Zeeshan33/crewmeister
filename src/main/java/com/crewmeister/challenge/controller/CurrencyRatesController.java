package com.crewmeister.challenge.controller;

import com.crewmeister.challenge.dto.CurrencyDTO;
import com.crewmeister.challenge.dto.CurrencyRatesDTO;
import com.crewmeister.challenge.mapper.CurrencyMapper;
import com.crewmeister.challenge.mapper.CurrencyRatesMapper;
import com.crewmeister.challenge.model.Currency;
import com.crewmeister.challenge.model.CurrencyRates;
import com.crewmeister.challenge.service.CurrencyRatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api")
public class CurrencyRatesController {

    private final CurrencyRatesService currencyRatesService;

    @Autowired
    public CurrencyRatesController(CurrencyRatesService currencyRatesService) {
        this.currencyRatesService = currencyRatesService;
    }

    /**
     * Retrieve all supported currencies.
     */
    @GetMapping("/currencies")
    public ResponseEntity<List<CurrencyDTO>> getAllCurrencies() {
        log.info("Fetching all available currencies");
        List<Currency> currencies = currencyRatesService.getAllCurrencies();
        List<CurrencyDTO> currencyDto = CurrencyMapper.INSTANCE.toDtoList(currencies);
        return ResponseEntity.ok(currencyDto);
    }

    /**
     * Retrieve paginated currency exchange rates.
     */
    @GetMapping("/rates")
    public ResponseEntity<Page<CurrencyRatesDTO>> getAllCurrencyRates(Pageable pageable) {
        log.info("Fetching all currency rates (page: {}, size: {}).", pageable.getPageNumber(), pageable.getPageSize());
        Page<CurrencyRates> currencyRates = currencyRatesService.getAllCurrencyRates(pageable);
        Page<CurrencyRatesDTO> rates = currencyRates.map(CurrencyRatesMapper.INSTANCE::toDto);
        return ResponseEntity.ok(rates);
    }

    /**
     * Retrieve exchange rates for all currencies on a specific date.
     */
    @GetMapping("/rates/date")
    public ResponseEntity<List<CurrencyRatesDTO>> getCurrencyRatesByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Fetching currency rates for date: {}", date);
        List<CurrencyRates> currencyRates = currencyRatesService.getCurrencyRatesByDate(date);
        List<CurrencyRatesDTO> rates = CurrencyRatesMapper.INSTANCE.toDtoList(currencyRates);
        return ResponseEntity.ok(rates);
    }

    /**
     * Retrieve a specific exchange rate by date and currency code.
     */
    @GetMapping("/rates/date-currency")
    public ResponseEntity<CurrencyRatesDTO> getCurrencyRatesByDateAndCurrency(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("currency") String currency) {
        log.info("Fetching rate for date: {} and currency: {}", date, currency);
        CurrencyRates currencyRates = currencyRatesService.getCurrencyRateByDateAndCurrency(date, currency);
        CurrencyRatesDTO currencyRatesDTO = CurrencyRatesMapper.INSTANCE.toDto(currencyRates);
        return ResponseEntity.ok(currencyRatesDTO);
    }

    /**
     * Convert a given amount from a currency to EUR on a specific date.
     */
    @GetMapping("/convert")
    public ResponseEntity<Double> convertToEur(
            @RequestParam String currency,
            @RequestParam double amount,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("Converting {} {} to EUR for date {}", amount, currency, date);
        Double result = currencyRatesService.convertToEur(currency, amount, date);
        return ResponseEntity.ok(result);
    }
}
