package com.steiner.myservice.service;

import com.steiner.myservice.domain.Book;
import com.steiner.myservice.domain.Review;
import com.steiner.myservice.repository.BookRepository;
import com.steiner.myservice.repository.ReviewRepository;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import com.steiner.myservice.repository.WordRepository;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreatereviewcsvService {

    private final Logger log = LoggerFactory.getLogger(CreatereviewcsvService.class);

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final WordRepository wordRepository;
    private final WordOccurrencesRepository wordOccurrencesRepository;

    public CreatereviewcsvService(ReviewRepository reviewRepository,
            WordRepository wordRepository,
            WordOccurrencesRepository wordOccurrencesRepository,
            BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.wordRepository = wordRepository;
        this.wordOccurrencesRepository = wordOccurrencesRepository;
        this.bookRepository = bookRepository;
    }

    @Async
    public void createreviewcsv(String mystring, Book book, HashMap<String, Long> wordIdMap) {
        UtilService utilService = new UtilService();
        Review newReview = new Review();
        newReview.setBook(book);
        newReview.setReviewstring("reviewstring");
        newReview.setReviewtext(mystring);
        Map<String, Integer> myMap;
        myMap = utilService.CountWords(mystring);
        newReview = reviewRepository.save(newReview);
        WordoccurrencesService wordoccurrencesService
                = new WordoccurrencesService(wordRepository,
                        wordOccurrencesRepository);
        reviewRepository.save(newReview);
        wordoccurrencesService.updateWordOccurrences(newReview, myMap, wordIdMap);

    }

}