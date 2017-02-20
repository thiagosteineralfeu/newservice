package com.steiner.myservice.service.mapper;

import com.steiner.myservice.domain.*;
import com.steiner.myservice.service.dto.WordOccurrencesDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity WordOccurrences and its DTO WordOccurrencesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WordOccurrencesMapper {

    @Mapping(source = "review.id", target = "reviewId")
    WordOccurrencesDTO wordOccurrencesToWordOccurrencesDTO(WordOccurrences wordOccurrences);

    List<WordOccurrencesDTO> wordOccurrencesToWordOccurrencesDTOs(List<WordOccurrences> wordOccurrences);

    @Mapping(source = "reviewId", target = "review")
    WordOccurrences wordOccurrencesDTOToWordOccurrences(WordOccurrencesDTO wordOccurrencesDTO);

    List<WordOccurrences> wordOccurrencesDTOsToWordOccurrences(List<WordOccurrencesDTO> wordOccurrencesDTOs);

    default Review reviewFromId(Long id) {
        if (id == null) {
            return null;
        }
        Review review = new Review();
        review.setId(id);
        return review;
    }
}
