package com.crewmeister.challenge.service.impl;

import com.crewmeister.challenge.model.Currency;
import com.crewmeister.challenge.model.CurrencyRates;
import com.crewmeister.challenge.repository.CurrencyRateRepository;
import com.crewmeister.challenge.repository.CurrencyRepository;
import com.crewmeister.challenge.service.CurrencyRatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Service implementation for managing currency rates.
 * Provides operations to retrieve and save currency and rates data.
 */
@Service
public class CurrencyRatesServiceImpl implements CurrencyRatesService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyRatesServiceImpl.class);

    private final CurrencyRateRepository currencyRateRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyRatesServiceImpl(CurrencyRateRepository currencyRateRepository,
                                    CurrencyRepository currencyRepository) {
        this.currencyRateRepository = currencyRateRepository;
        this.currencyRepository = currencyRepository;
    }

    /**
     * Retrieve all available currencies.
     *
     * @return list of Currency entities
     */
    @Override
    public List<Currency> getAllCurrencies() {
        logger.debug("Fetching all currencies from database");
        List<Currency> currencies = currencyRepository.findAllCurrency();
        logger.info("Fetched {} currencies", currencies.size());
        return currencies;
    }

    /**
     * Retrieve all currency rates with pagination support.
     *
     * @param pageable pagination information
     * @return paged currency rates
     */
    @Override
    public Page<CurrencyRates> getAllCurrencyRates(Pageable pageable) {
        logger.debug("Fetching all currency rates with pageable: {}", pageable);
        Page<CurrencyRates> page = currencyRateRepository.findAll(pageable);
        logger.info("Fetched {} currency rates on current page", page.getNumberOfElements());
        return page;
    }

    /**
     * Retrieve all currency rates for a specific date.
     *
     * @param date the date for which rates are fetched
     * @return list of CurrencyRates for the given date
     */
    @Override
    public List<CurrencyRates> getCurrencyRatesByDate(LocalDate date) {
        logger.debug("Fetching currency rates for date: {}", date);
        List<CurrencyRates> rates = currencyRateRepository.findByDate(date);
        if (rates.isEmpty()) {
            logger.warn("No currency rates found for date: {}", date);
        } else {
            logger.info("Found {} currency rates for date {}", rates.size(), date);
        }
        return rates;
    }

    /**
     * Retrieve a currency rate for a specific currency and date.
     *
     * @param date the date of the rate
     * @param currency the currency code
     * @return CurrencyRates entity
     * @throws NoSuchElementException if no rate found
     */
    @Override
    public CurrencyRates getCurrencyRateByDateAndCurrency(LocalDate date, String currency) {
        logger.debug("Fetching currency rate for currency '{}' on date {}", currency, date);
        Optional<CurrencyRates> currencyRate = currencyRateRepository.findByDateAndCurrency_CurrencyName(date, currency);
        return currencyRate.orElseThrow(() -> {
            logger.error("No currency rate found for currency '{}' on date {}", currency, date);
            return new NoSuchElementException("No record for currency or date found");
        });
    }

    /**
     * Converts the given amount of the specified currency to EUR based on the rate on the specified date.
     *
     * @param currency the currency code
     * @param amount the amount to convert
     * @param date the date of the conversion rate
     * @return converted amount in EUR
     * @throws NoSuchElementException if rate not found
     * @throws IllegalArgumentException if amount is negative
     */
    @Override
    public Double convertToEur(String currency, double amount, LocalDate date) {
        logger.debug("Converting amount {} {} to EUR on date {}", amount, currency, date);

        if (amount < 0) {
            logger.error("Invalid amount for conversion: {}", amount);
            throw new IllegalArgumentException("Amount must be non-negative");
        }

        Optional<CurrencyRates> currencyRate = currencyRateRepository.findByDateAndCurrency_CurrencyName(date, currency);
        return currencyRate.map(rate -> {
            double converted = rate.getRate() * amount;
            logger.info("Conversion result: {} {} = {} EUR on {}", amount, currency, converted, date);
            return converted;
        })
        .orElseThrow(() -> {
            logger.error("No currency rate found for currency '{}' on date {}", currency, date);
            return new NoSuchElementException("No record for currency or date found");
        });
    }

    /**
     * Saves a batch of currency rates, avoiding duplicates.
     * Uses SERIALIZABLE isolation to ensure consistency during concurrent saves.
     *
     * @param rates list of CurrencyRates to save
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void saveRates(List<CurrencyRates> rates) {
        logger.debug("Saving batch of {} currency rates", rates.size());
        int savedCount = 0;
        for (CurrencyRates rate : rates) {
            try {
                Optional<CurrencyRates> existingRate = currencyRateRepository.findByDateAndCurrency_CurrencyName(
                    rate.getDate(), rate.getCurrency().getCurrencyName());

                if (existingRate.isEmpty()) {
                    currencyRateRepository.save(rate);
                    savedCount++;
                } else {
                    logger.debug("Currency rate already exists for currency '{}' on date {}, skipping save.",
                        rate.getCurrency().getCurrencyName(), rate.getDate());
                }
            } catch (Exception e) {
                logger.error("Error saving currency rate for currency '{}' on date {}: {}",
                    rate.getCurrency().getCurrencyName(), rate.getDate(), e.getMessage());
            }
        }
        logger.info("Saved {} new currency rates out of {}", savedCount, rates.size());
    }
}
