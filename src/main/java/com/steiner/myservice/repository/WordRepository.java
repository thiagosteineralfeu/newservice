package com.steiner.myservice.repository;

import com.steiner.myservice.domain.Word;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Word entity.
 */
@SuppressWarnings("unused")
public interface WordRepository extends JpaRepository<Word,Long> {
    Optional<Word> findByWordstring(String wordstring);
}
