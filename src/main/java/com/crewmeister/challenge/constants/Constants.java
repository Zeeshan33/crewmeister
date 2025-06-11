package com.crewmeister.challenge.constants;

/**
 * This class holds application-wide constant values.
 * These constants are reused across the application to improve maintainability and readability.
 */
public final class Constants {

    // Private constructor to prevent instantiation
    private Constants() {}

    /**
     * Name of the CSV file containing exchange rate data, stored in the resources directory.
     */
    public static final String EXCHANGE_RATES_CSV = "exchange_rates.csv";

    /**
     * Date format pattern used for parsing dates like "1/2/2023".
     * Single-digit months and days are allowed.
     */
    public static final String M_D_YYYY = "M/d/yyyy";

    /**
     * Regular expression used to split CSV lines by commas.
     */
    public static final String REGEX = ",";

}
