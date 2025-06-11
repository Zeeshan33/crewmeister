package com.crewmeister.challenge.mapper;

import com.crewmeister.challenge.dto.CurrencyRatesDTO;
import com.crewmeister.challenge.model.CurrencyRates;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper interface for converting between {@link CurrencyRates} entity and {@link CurrencyRatesDTO}.
 */
@Mapper(uses = CurrencyMapper.class)
public interface CurrencyRatesMapper {
    CurrencyRatesMapper INSTANCE = Mappers.getMapper(CurrencyRatesMapper.class);

    /**
     * Converts a CurrencyRates entity to CurrencyRatesDTO.
     *
     * @param currencyRate the CurrencyRates entity
     * @return the corresponding DTO
     */
    CurrencyRatesDTO toDto(CurrencyRates currencyRate);

    /**
     * Converts a CurrencyRatesDTO to CurrencyRates entity.
     * Avoids infinite recursion by ignoring the nested list in the Currency object.
     *
     * @param dto the CurrencyRatesDTO
     * @return the corresponding CurrencyRates entity
     */
    @Mapping(target = "currency.currencyRates", ignore = true)
    CurrencyRates toEntity(CurrencyRatesDTO dto);

    /**
     * Converts a list of CurrencyRates entities to a list of CurrencyRatesDTOs.
     *
     * @param entities list of CurrencyRates entities
     * @return list of DTOs
     */
    List<CurrencyRatesDTO> toDtoList(List<CurrencyRates> entities);

    /**
     * Converts a list of CurrencyRatesDTOs to a list of CurrencyRates entities.
     * Ignores nested lists in the Currency objects to avoid recursion issues.
     *
     * @param dtos list of DTOs
     * @return list of entities
     */
    @Mapping(target = "currency.currencyRates", ignore = true)
    List<CurrencyRates> toEntityList(List<CurrencyRatesDTO> dtos);
}
