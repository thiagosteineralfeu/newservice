package com.steiner.myservice.service;

import com.steiner.myservice.domain.Book;
import com.steiner.myservice.domain.Review;
import com.steiner.myservice.domain.Word;
import com.steiner.myservice.domain.WordOccurrences;
import com.steiner.myservice.repository.BookRepository;
import com.steiner.myservice.repository.ReviewRepository;
import com.steiner.myservice.repository.WordOccurrencesRepository;
import com.steiner.myservice.repository.WordRepository;
import com.steiner.myservice.service.dto.ReviewDTO;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

    private final Logger log = LoggerFactory.getLogger(ReviewService.class);

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final WordRepository wordRepository;
    private final WordOccurrencesRepository wordOccurrencesRepository;

    public ReviewService(ReviewRepository reviewRepository,
            WordRepository wordRepository,
            WordOccurrencesRepository wordOccurrencesRepository,
            BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.wordRepository = wordRepository;
        this.wordOccurrencesRepository = wordOccurrencesRepository;
        this.bookRepository = bookRepository;
    }

    public Review createReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        Book book = bookRepository.findOne(reviewDTO.getBookId());
        review.setBook(book);
        String cleanreviewstring = this.cleanString(reviewDTO.getReviewstring());
        String cleanreviewtext=this.cleanString(reviewDTO.getReviewtext());
        review.setReviewstring(cleanreviewstring);
        review.setReviewtext(cleanreviewtext);
        Map<String, Integer> myMap = new HashMap<>();
        myMap = CountWords(cleanreviewtext);
        Long id = reviewRepository.save(review).getId();
        review = reviewRepository.findOne(id);
        review = this.updateWordOccurrences(review, myMap);
        reviewRepository.save(review);
        log.debug("Created Review: {}", review);
        return review;
    }

    private Review updateWordOccurrences(Review review,
            Map<String, Integer> myMap) {

        Optional<Word> existingWord;
        Word myWord;
        Set<String> keys = myMap.keySet();
        for (String key : keys) {
            existingWord = wordRepository.findByWordstring(key);
            if (!existingWord.isPresent()) {
                myWord = new Word();
                myWord.setWordstring(key);
                Long id = wordRepository.save(myWord).getId();
                myWord = wordRepository.findOne(id);
            } else {
                myWord = existingWord.get();
            }
            WordOccurrences wordOccurences = new WordOccurrences();
            wordOccurences.setReview(review);
            wordOccurences.setAmountoccurrences(myMap.get(key));
            wordOccurences.setWord(myWord);
            wordOccurences = wordOccurrencesRepository.save(wordOccurences);
        }

        return review;
    }

    /**
     * TO DO
     */
    private String cleanString(String mystring) {

        mystring = mystring.toLowerCase().trim();

        mystring = Jsoup.parse(mystring.toLowerCase()).text();//Remove html
        //Tratamos alguns casos de contração. 
        //Casos com 'd e 's como possuem múltiplos significados serão tratados removendo o apóstrofo
        mystring = mystring.replaceAll("can't", "can not");
        mystring = mystring.replaceAll("n't", " not");
        mystring = mystring.replaceAll("'re", " are");
        mystring = mystring.replaceAll("'m", " am");
        mystring = mystring.replaceAll("'ll", " will");
        mystring = mystring.replaceAll("'ve", " have");
        mystring = mystring.replaceAll("\\.", " ");
        mystring = mystring.replaceAll(",", " ");
        mystring = mystring.replaceAll("\\?", " ");
        mystring = mystring.replaceAll("!", " ");

        //Remove all char not in this range. Replace with no space. There will be some contractions
        mystring = mystring.replaceAll("[^a-zA-Z|\\s]", "");
        mystring = mystring.replaceAll("[\\t]", "");

        //Remove some  contractions
        mystring = mystring.replaceAll("didnt", "did not");
        mystring = mystring.replaceAll("doesnt", "does not");
        mystring = mystring.replaceAll("wasnt", "was not");
        mystring = mystring.replaceAll("havent", "have not");
        mystring = mystring.replaceAll("wouldnt", "would not");
        mystring = mystring.replaceAll("couldnt", "could not");
        mystring = mystring.replaceAll("shouldnt", "should not");

        //use some regex to replace with safety
        mystring = mystring.replaceAll("^.?cant$", "can not");
        mystring = mystring.replaceAll("^.?its$", "it is");
        mystring = mystring.replaceAll("^.?thats$", "that is");

        //Remove urls
        mystring = mystring.replaceAll("\\\\ (?:(?:https?):\\\\/\\\\/www\\\\.\\\\.*?.*?) ", " ");

        //Remove multiple whithe spaces
        mystring = mystring.trim().replaceAll(" +", " ");

        return mystring;
    }

    private Map<String, Integer> CountWords(String cleanreviewstring) {

        Map<String, Integer> myMap = new HashMap<>();

        if (cleanreviewstring != null) {
            cleanreviewstring = cleanreviewstring.trim();
            String[] tokens = cleanreviewstring.split(" ");
            for (String word : tokens) {
                if (myMap.containsKey(word)) {
                    int count = myMap.get(word.trim());
                    myMap.put(word.trim(), count + 1);
                } else {
                    myMap.put(word.trim(), 1);
                }
            }
        } else {

            myMap = null;

        }
        return myMap;
    }

    @Async
    public void processReviewFromCsvFile(String csvfilepath, Long bookId) throws FileNotFoundException, IOException {

        String mystring;
        Pattern myPatterCompileColumnCatcher = Pattern.compile(".*?\\t.*?\\t.*?\\t(\\\".*\\\")");
        int line = 0;
        ReviewDTO reviewDTO;
        Review newReview;
        Optional<Book> existingBook;
        existingBook = Optional.ofNullable(bookRepository.findOne(bookId));

        if (existingBook.isPresent()) {
            try (BufferedReader myBufferedReader = new BufferedReader(new FileReader(csvfilepath))) {

                while ((mystring = myBufferedReader.readLine()) != null) {
                    //System.out.println(mystring);
                    line += 1;
                    
                    
                    
                    Matcher myMacher = myPatterCompileColumnCatcher.matcher(mystring);

                    if (myMacher.find()) {
                        mystring = Jsoup.parse(myMacher.group(1).toLowerCase()).text();//Remove html
                        mystring = mystring.trim();
                        if (mystring != null) {
                            mystring = this.cleanString(mystring);
                            reviewDTO = new ReviewDTO();
                            reviewDTO.setBookId(bookId);
                            reviewDTO.setReviewtext(mystring);
                            reviewDTO.setReviewstring("text");
                            newReview = this.createReview(reviewDTO);                            
                        }
                    }
                }
                myBufferedReader.close();

            } catch (IOException e) {
                log.warn("csv file stop in line '{}'", line, e);
            }
        } else {
            log.debug("Book not bookId:{}", bookId);
        }

    }

}
