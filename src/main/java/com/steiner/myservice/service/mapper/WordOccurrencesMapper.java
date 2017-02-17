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

    @Mapping(source = "word.id", target = "wordId")
    @Mapping(source = "word.wordstring", target = "wordWordstring")
    @Mapping(source = "review.id", target = "reviewId")
    WordOccurrencesDTO wordOccurrencesToWordOccurrencesDTO(WordOccurrences wordOccurrences);

    List<WordOccurrencesDTO> wordOccurrencesToWordOccurrencesDTOs(List<WordOccurrences> wordOccurrences);

    @Mapping(source = "wordId", target = "word")
    @Mapping(source = "reviewId", target = "review")
    WordOccurrences wordOccurrencesDTOToWordOccurrences(WordOccurrencesDTO wordOccurrencesDTO);

    List<WordOccurrences> wordOccurrencesDTOsToWordOccurrences(List<WordOccurrencesDTO> wordOccurrencesDTOs);

    default Word wordFromId(Long id) {
        if (id == null) {
            return null;
        }
        Word word = new Word();
        word.setId(id);
        return word;
    }

    default Review reviewFromId(Long id) {
        if (id == null) {
            return null;
        }
        Review review = new Review();
        review.setId(id);
        return review;
    }
}
