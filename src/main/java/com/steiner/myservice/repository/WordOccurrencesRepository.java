package com.steiner.myservice.repository;

import com.steiner.myservice.domain.WordOccurrences;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WordOccurrences entity.
 */
@SuppressWarnings("unused")
public interface WordOccurrencesRepository extends JpaRepository<WordOccurrences,Long> {

}
