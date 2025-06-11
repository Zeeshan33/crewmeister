package com.crewmeister.challenge.repository;

import com.crewmeister.challenge.model.CurrencyRates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing currency exchange rates.
 * Extends JpaRepository to provide standard CRUD operations and custom queries.
 */
@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRates, Long> {

    /**
     * Finds all exchange rates for a specific date.
     *
     * @param date the date to filter by
     * @return list of CurrencyRates on the given date
     */
    List<CurrencyRates> findByDate(LocalDate date);

    /**
     * Finds a specific exchange rate by date and currency name.
     *
     * @param date the date of the rate
     * @param currencyName the currency name (e.g., "USD", "EUR")
     * @return Optional of CurrencyRates if found
     */
    Optional<CurrencyRates> findByDateAndCurrency_CurrencyName(LocalDate date, String currencyName);
}
