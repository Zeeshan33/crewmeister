package com.crewmeister.challenge.repository;

import com.crewmeister.challenge.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Currency entity.
 * Provides CRUD operations and custom queries on Currency table.
 */
@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    /**
     * Find a currency by its name.
     *
     * @param currencyName the name of the currency (e.g., USD, EUR)
     * @return Optional of Currency if found
     */
    Optional<Currency> findByCurrencyName(String currencyName);

    /**
     * Fetch all currency records.
     * Note: This is equivalent to findAll(), hence redundant unless future logic is added.
     *
     * @return List of all currencies
     */
    @Query("SELECT c FROM Currency c")
    List<Currency> findAllCurrency();  // consider removing if no special logic
}
