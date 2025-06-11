package com.crewmeister.challenge.dto;

/**
 * Data Transfer Object (DTO) representing a Currency.
 *
 * @param id           The unique identifier for the currency.
 * @param currencyName The name/code of the currency (e.g., "USD", "EUR").
 */
public record CurrencyDTO(Long id, String currencyName) { }
