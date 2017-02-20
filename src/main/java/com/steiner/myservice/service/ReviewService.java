package com.steiner.myservice.service;

import com.steiner.myservice.domain.Book;
import com.steiner.myservice.domain.Review;
import com.steiner.myservice.repository.BookRepository;
import com.steiner.myservice.repository.ReviewRepository;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import com.steiner.myservice.repository.WordRepository;
import com.steiner.myservice.service.dto.ReviewDTO;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

    private final Logger log = LoggerFactory.getLogger(ReviewService.class);

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final WordOccurrencesRepository wordOccurrencesRepository;

    public ReviewService(ReviewRepository reviewRepository,
            WordRepository wordRepository,
            WordOccurrencesRepository wordOccurrencesRepository,
            BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.wordOccurrencesRepository = wordOccurrencesRepository;
        this.bookRepository = bookRepository;
    }

    public Review createReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        UtilService utilService = new UtilService();
        WordoccurrencesService wordoccurrencesService
                = new WordoccurrencesService(wordOccurrencesRepository);
        Book book = bookRepository.findOne(reviewDTO.getBookId());
        review.setBook(book);
        String cleanreviewstring = (reviewDTO.getReviewstring());
        String cleanreviewtext = utilService.cleanString(reviewDTO.getReviewtext());
        //Todo test reviewtext null or empity
        review.setReviewstring(cleanreviewstring);
        review.setReviewtext(cleanreviewtext);
        Map<String, Integer> myMap;
        myMap = utilService.CountWords(cleanreviewtext);
        review = reviewRepository.save(review);
        wordoccurrencesService.updateWordOccurrences(review, myMap);
        log.debug("Created Review: {}", review);
        return review;
    }

}
