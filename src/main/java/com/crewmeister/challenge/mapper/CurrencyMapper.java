package com.crewmeister.challenge.mapper;

import com.crewmeister.challenge.dto.CurrencyDTO;
import com.crewmeister.challenge.model.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper interface for converting between {@link Currency} entity and {@link CurrencyDTO}.
 */
@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    /**
     * Converts a Currency entity to CurrencyDTO.
     *
     * @param currency the Currency entity
     * @return the corresponding CurrencyDTO
     */
    CurrencyDTO toDto(Currency currency);

    /**
     * Converts a CurrencyDTO to Currency entity.
     *
     * @param dto the CurrencyDTO
     * @return the corresponding Currency entity
     */
    Currency toEntity(CurrencyDTO dto);

    /**
     * Converts a list of Currency entities to a list of CurrencyDTOs.
     *
     * @param currencies list of entities
     * @return list of DTOs
     */
    List<CurrencyDTO> toDtoList(List<Currency> currencies);

    /**
     * Converts a list of CurrencyDTOs to a list of Currency entities.
     *
     * @param dtos list of DTOs
     * @return list of entities
     */
    List<Currency> toEntityList(List<CurrencyDTO> dtos);
}
