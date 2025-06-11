package com.crewmeister.challenge.service.impl;

import com.crewmeister.challenge.constants.Constants;
import com.crewmeister.challenge.model.Currency;
import com.crewmeister.challenge.model.CurrencyRates;
import com.crewmeister.challenge.repository.CurrencyRepository;
import com.crewmeister.challenge.service.CSVProcessingService;
import com.crewmeister.challenge.service.CurrencyRatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service to process CSV containing currency rates data.
 * Reads a CSV file from resources, parses it, saves currency and currency rates data.
 */
@Service
public class CSVProcessingServiceImpl implements CSVProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(CSVProcessingServiceImpl.class);

    private final CurrencyRatesService currencyRatesService;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CSVProcessingServiceImpl(CurrencyRatesService currencyRatesService,
                                    CurrencyRepository currencyRepository) {
        this.currencyRatesService = currencyRatesService;
        this.currencyRepository = currencyRepository;
    }

    /**
     * Scheduled method to process CSV file daily at 1 AM.
     * Reads currencies and rates, saves to database.
     */
    @Override
    @Scheduled(cron = "0 0 1 * * *") // Runs daily at 1 AM
    public void processCSV() {
        logger.info("Starting CSV processing for exchange rates.");

        try {
            ClassPathResource resource = new ClassPathResource(Constants.EXCHANGE_RATES_CSV);
            try (InputStream inputStream = resource.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                // Read the header line to get currency codes
                String headerLine = reader.readLine();
                if (headerLine == null) {
                    logger.warn("CSV file is empty or missing header.");
                    return;
                }

                String[] headers = headerLine.split(Constants.REGEX);
                List<String> currencyCodes = Arrays.asList(headers).subList(1, headers.length);

                // Ensure all currencies exist in DB, cache in a map
                Map<String, Currency> currencyMap = new HashMap<>();
                for (String code : currencyCodes) {
                    Currency currency = currencyRepository.findByCurrencyName(code)
                            .orElseGet(() -> {
                                logger.info("Currency '{}' not found. Creating new entry.", code);
                                Currency newCurrency = new Currency();
                                newCurrency.setCurrencyName(code);
                                return currencyRepository.save(newCurrency);
                            });
                    currencyMap.put(code, currency);
                }

                List<CurrencyRates> allRates = new ArrayList<>();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.M_D_YYYY);

                String line;
                int lineCount = 1; // For logging/debugging

                while ((line = reader.readLine()) != null) {
                    lineCount++;
                    try {
                        String[] values = line.split(Constants.REGEX);
                        LocalDate date = LocalDate.parse(values[0], formatter);

                        for (int i = 1; i < values.length; i++) {
                            String code = headers[i];
                            double rate = Double.parseDouble(values[i]);

                            CurrencyRates rateEntry = new CurrencyRates();
                            rateEntry.setDate(date);
                            rateEntry.setCurrency(currencyMap.get(code));
                            rateEntry.setRate(rate);

                            allRates.add(rateEntry);
                        }
                    } catch (Exception parseEx) {
                        logger.error("Failed to parse CSV line {}: '{}'. Error: {}", lineCount, line, parseEx.getMessage());
                        // Continue processing remaining lines
                    }
                }

                // Batch save all currency rates
                currencyRatesService.saveRates(allRates);
                logger.info("CSV import completed successfully: {} rates saved.", allRates.size());
            }

        } catch (Exception e) {
            logger.error("Failed to process CSV for currency rates", e);
            // Rethrow to global exception handler or handle accordingly
            throw new RuntimeException("Error processing CSV file", e);
        }
    }
}
