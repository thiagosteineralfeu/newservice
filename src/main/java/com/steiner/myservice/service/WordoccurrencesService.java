package com.steiner.myservice.service;

import com.steiner.myservice.domain.Review;
import com.steiner.myservice.domain.Word;
import com.steiner.myservice.domain.WordOccurrences;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import com.steiner.myservice.repository.WordRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

    private final WordRepository wordRepository;
    private final WordOccurrencesRepository wordOccurrencesRepository;

    public WordoccurrencesService(WordRepository wordRepository,
            WordOccurrencesRepository wordOccurrencesRepository) {

        this.wordRepository = wordRepository;
        this.wordOccurrencesRepository = wordOccurrencesRepository;

    }

    @Async
    public void updateWordOccurrences(Review newReview, Map<String, Integer> myMap, HashMap<String, Long> wordIdMap) {

        Optional<Long> existingWordId;

        Word myWord = null;
        Long myWordId;
        WordService wordService = new WordService(wordRepository);

        Set<String> keys = myMap.keySet();
        for (String key : keys) {
            existingWordId = Optional.ofNullable(wordIdMap.get(key));

            if (existingWordId.isPresent()) {
                myWordId = existingWordId.get();
                myWord = wordRepository.findOne(myWordId);
                //Todo Make a Local HashMap will new words

            } else {

                try {
                    myWord = wordService.findOrSaveWord(key);

                } catch (Exception e) {
                    log.debug("Error creating word: {}", e);
                    myWord = wordRepository.findByWordstring(key).get();

                }

            }

            WordOccurrences wordOccurences = new WordOccurrences();
            wordOccurences.setReview(newReview);
            wordOccurences.setAmountoccurrences(myMap.get(key));
            wordOccurences.setWord(myWord);
            wordOccurrencesRepository.save(wordOccurences);
        }

    }

}


