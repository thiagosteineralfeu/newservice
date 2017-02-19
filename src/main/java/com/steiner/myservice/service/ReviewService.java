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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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

    public Review createReview(ReviewDTO reviewDTO, HashMap<String, Long> wordIdMap) {
        Review review = new Review();
        Book book = bookRepository.findOne(reviewDTO.getBookId());
        review.setBook(book);
        String cleanreviewstring = this.cleanString(reviewDTO.getReviewstring());
        String cleanreviewtext = this.cleanString(reviewDTO.getReviewtext());
        //Todo test reviewtext null or empity
        review.setReviewstring(cleanreviewstring);
        review.setReviewtext(cleanreviewtext);
        Map<String, Integer> myMap;
        myMap = CountWords(cleanreviewtext);
        review = reviewRepository.save(review);
        review = this.updateWordOccurrences(review, myMap, wordIdMap);
        reviewRepository.save(review);
        log.debug("Created Review: {}", review);
        return review;
    }

    private Review updateWordOccurrences(Review review,
            Map<String, Integer> myMap, HashMap<String, Long> wordIdMap) {

        Optional<Long> existingWordId;
        Optional<Word> existingWord;
        Word myWord=null;
        Long myWordId;

        Set<String> keys = myMap.keySet();
        for (String key : keys) {
            existingWordId = Optional.ofNullable(wordIdMap.get(key));
            
            if (existingWordId.isPresent()) {
                    myWordId = existingWordId.get();
                    myWord = wordRepository.findOne(myWordId);
                    //Todo Make a Local HashMap will new words
                }else if (!existingWordId.isPresent() && ! wordRepository.findByWordstring(key).isPresent()) {
                    myWord = new Word();
                    myWord.setWordstring(key);
                    myWord = wordRepository.save(myWord);
                }
                
            else {
                                 
                    myWord = wordRepository.findByWordstring(key).get();

                }
            

            WordOccurrences wordOccurences = new WordOccurrences();
            wordOccurences.setReview(review);
            wordOccurences.setAmountoccurrences(myMap.get(key));
            wordOccurences.setWord(myWord);
            wordOccurrencesRepository.save(wordOccurences);
        }

        return review;
    }

    /**
     * TO DO
     */
    private String cleanString(String mystring) {

        mystring = mystring.toLowerCase().trim();

        //mystring = Jsoup.parse(mystring.toLowerCase()).text();//Remove html
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
    public void processReviewFromCsvFile(String csvfilepath, Long bookId,
            HashMap<String, Long> wordIdMap) throws FileNotFoundException, IOException {

        String mystring;
        Pattern myPatterCompileColumnCatcher
                = Pattern.compile(".*?\\t.*?\\t.*?\\t(\\\".*\\\")");
        int line = 0;
        // Start time
        long startTime = System.nanoTime();
        Book book;
        book = bookRepository.findOne(bookId);
        Optional<Book> existingBook;
        existingBook = Optional.ofNullable(bookRepository.findOne(bookId));

        if (existingBook.isPresent()) {
            try (BufferedReader myBufferedReader = new BufferedReader(new FileReader(csvfilepath))) {

                while ((mystring = myBufferedReader.readLine()) != null) {
                    //System.out.println(mystring);
                    line += 1;
                    System.out.println("Line:" + line);

                    Matcher myMacher = myPatterCompileColumnCatcher.matcher(mystring);

                    if (myMacher.find()) {
                        mystring = Jsoup.parse(myMacher.group(1).toLowerCase()).text();//Remove html
                        mystring = mystring.trim();
                        if (mystring != null && !mystring.isEmpty()) {
                            mystring = this.cleanString(mystring);
                            Review newReview = new Review();
                            newReview.setBook(book);
                            newReview.setReviewstring("text");
                            newReview.setReviewtext(mystring);
                            Map<String, Integer> myMap;
                            myMap = CountWords(mystring);
                            newReview = reviewRepository.save(newReview);
                            newReview = this.updateWordOccurrences(newReview, myMap, wordIdMap);
                            reviewRepository.save(newReview);
                        }
                    }
                }
                myBufferedReader.close();
                long endTime = System.nanoTime();
                long duration = endTime - startTime;
                System.out.println("Tempo(milisegundos)="+TimeUnit.NANOSECONDS.toMillis(duration));

            } catch (IOException e) {
                log.warn("csv file stop in line '{}'", line, e);
            }
        } else {
            log.debug("Book not bookId:{}", bookId);
        }

    }

}
