package com.steiner.myservice.service;

import com.steiner.myservice.domain.Review;
import com.steiner.myservice.domain.WordOccurrences;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WordoccurrencesService {

    private final Logger log = LoggerFactory.getLogger(WordoccurrencesService.class);

    private final WordOccurrencesRepository wordOccurrencesRepository;

    public WordoccurrencesService(WordOccurrencesRepository wordOccurrencesRepository) {

        this.wordOccurrencesRepository = wordOccurrencesRepository;

    }

    @Async
    public void updateWordOccurrences(Review newReview, Map<String, Integer> myMap) {

        Set<String> keys = myMap.keySet();
        keys.stream().map((key) -> {
            WordOccurrences wordOccurences = new WordOccurrences();
            wordOccurences.setReview(newReview);
            wordOccurences.setAmountoccurrences(myMap.get(key));
            wordOccurences.setWord(key);
            return wordOccurences;
        }).forEachOrdered((wordOccurences) -> {
            wordOccurrencesRepository.save(wordOccurences);
        });

    }
    
     

}
