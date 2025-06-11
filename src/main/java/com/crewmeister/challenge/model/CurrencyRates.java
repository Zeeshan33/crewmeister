package com.crewmeister.challenge.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents an exchange rate for a specific currency on a specific date.
 */
@Entity
@Table(name = "currency_rates",
    indexes = {
        @Index(name = "idx_date_currency", columnList = "date, currencyId") // Optimization for queries filtering by date and currency
    })
@Getter
@Setter
public class CurrencyRates {

    /** Auto-generated primary key for CurrencyRates. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The date of the exchange rate. */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * Many-to-one relationship to Currency.
     * JoinColumn specifies the foreign key as "currencyId".
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId", nullable = false)
    private Currency currency;

    /** The exchange rate for the associated currency on the given date. */
    @Column(nullable = false)
    private double rate;
}

