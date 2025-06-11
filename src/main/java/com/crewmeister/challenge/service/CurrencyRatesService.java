package com.crewmeister.challenge.service;

import com.crewmeister.challenge.model.Currency;
import com.crewmeister.challenge.model.CurrencyRates;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for handling operations related to currencies and their exchange rates.
 */
public interface CurrencyRatesService {

    /**
     * Retrieves all available currencies.
     *
     * @return list of all {@link Currency} entities.
     */
    List<Currency> getAllCurrencies();

    /**
     * Retrieves all currency exchange rates in a paginated format.
     *
     * @param pageable pagination information.
     * @return paginated list of {@link CurrencyRates}.
     */
    Page<CurrencyRates> getAllCurrencyRates(Pageable pageable);

    /**
     * Retrieves currency exchange rates for a specific date.
     *
     * @param date the date for which exchange rates are requested.
     * @return list of {@link CurrencyRates} for the given date.
     */
    List<CurrencyRates> getCurrencyRatesByDate(LocalDate date);

    /**
     * Retrieves the exchange rate for a specific currency on a given date.
     *
     * @param date the date of the exchange rate.
     * @param currency the name of the currency (e.g., "USD").
     * @return the corresponding {@link CurrencyRates} entity.
     */
    CurrencyRates getCurrencyRateByDateAndCurrency(LocalDate date, String currency);

    /**
     * Converts a given amount in a specified currency to its equivalent in Euros (EUR) based on the exchange rate of a given date.
     *
     * @param currency the source currency name (e.g., "USD").
     * @param amount the amount in the source currency.
     * @param date the date to use for the conversion rate.
     * @return the converted amount in Euros.
     */
    Double convertToEur(String currency, double amount, LocalDate date);

    /**
     * Persists a list of currency exchange rate records.
     *
     * @param rates list of {@link CurrencyRates} to be saved.
     */
    void saveRates(List<CurrencyRates> rates);
}