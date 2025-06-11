package com.crewmeister.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a currency type (e.g., USD, EUR, etc.).
 */
@Entity
@Table(name = "currency")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    /** Auto-generated primary key for Currency. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The name or code of the currency (e.g., USD, EUR). */
    @Column(nullable = false, unique = true)
    private String currencyName;

    /**
     * One-to-many relationship with CurrencyRates.
     * LAZY to avoid unnecessary loading unless explicitly accessed.
     * JsonIgnore prevents serialization loops in REST responses.
     */
    @OneToMany(mappedBy = "currency", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CurrencyRates> currencyRates;
}
