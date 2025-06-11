package com.crewmeister.challenge.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a currency rate for a given date.
 *
 * @param id       The unique identifier of the currency rate.
 * @param date     The date the exchange rate applies to.
 * @param rate     The exchange rate of the currency to EUR.
 * @param currency The associated CurrencyDTO.
 */
public record CurrencyRatesDTO(Long id, LocalDate date, double rate, CurrencyDTO currency) { }