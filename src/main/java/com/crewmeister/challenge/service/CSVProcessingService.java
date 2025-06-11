package com.crewmeister.challenge.service;

/**
 * Service interface for processing currency data from a CSV file.
 * The CSV is expected to contain exchange rate data that will be parsed and stored.
 */
public interface CSVProcessingService {

    /**
     * Reads, parses, and persists currency exchange rate data from a CSV file.
     * The source can be a remote or local CSV file.
     */
    void processCSV();
}
