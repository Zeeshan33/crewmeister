# Crewmeister Currency Exchange Challenge

This project is a solution for the Crewmeister backend coding challenge. It is a Spring Boot application that processes historical currency exchange rates from a CSV file and exposes RESTful endpoints for querying the data.

---

## Features

- Parses and imports currency exchange rates from a CSV file
- Stores data in a relational database using JPA
- Provides REST endpoints to:
  - List all available currencies
  - View currency exchange rates (paginated)
  - Filter exchange rates by date
  - Fetch rate by date and currency
  - Convert an amount from a currency to EUR

---

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- H2 Database (in-memory for development/testing)
- JUnit 5 & Mockito for testing
- Maven

## Getting Started

### Prerequisites

- Java 17+
- Maven

### Clone the Repository

```bash
git clone https://github.com/your-username/crewmeister-challenge.git
cd crewmeister-challenge

### Running the application

mvn spring-boot:run
