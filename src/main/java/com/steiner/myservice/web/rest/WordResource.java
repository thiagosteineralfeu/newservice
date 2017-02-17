package com.steiner.myservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.steiner.myservice.domain.Word;

import com.steiner.myservice.repository.WordRepository;
import com.steiner.myservice.web.rest.util.HeaderUtil;
import com.steiner.myservice.service.dto.WordDTO;
import com.steiner.myservice.service.mapper.WordMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Word.
 */
@RestController
@RequestMapping("/api")
public class WordResource {

    private final Logger log = LoggerFactory.getLogger(WordResource.class);

    private static final String ENTITY_NAME = "word";

    private final WordRepository wordRepository;

    private final WordMapper wordMapper;

    public WordResource(WordRepository wordRepository, WordMapper wordMapper) {
        this.wordRepository = wordRepository;
        this.wordMapper = wordMapper;
    }

    /**
     * POST /words : Create a new word.
     *
     * @param wordDTO the wordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new wordDTO, with status 400 (Bad Request) if the word has already an ID
     * or with status 500 (Internal Server Error) if the wordDTO couldnt be
     * created
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/words")
    @Timed
    public ResponseEntity<WordDTO> createWord(@Valid @RequestBody WordDTO wordDTO) throws URISyntaxException {
        log.debug("REST request to save Word : {}", wordDTO);
        if (wordDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new word cannot already have an ID")).body(null);
        }
        Word word = wordMapper.wordDTOToWord(wordDTO);
        word = wordRepository.save(word);
        WordDTO result = wordMapper.wordToWordDTO(word);
        return ResponseEntity.created(new URI("/api/words/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * POST /wordslist : Create new word based on list of words
     *
     * @param wordDTOlist the list od wordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new wordDTO, with status 400 (Bad Request) if the word has already an ID
     * or with status 500 (Internal Server Error) if the wordDTO couldnt be
     * created
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wordslist")
    @Timed
    public ResponseEntity<List<WordDTO>> createWord(@Valid @RequestBody List<WordDTO> wordDTOlist) throws URISyntaxException {
        log.debug("REST request to save a List of Words : {}", wordDTOlist);

        for (Iterator<WordDTO> it = wordDTOlist
                .iterator(); it.hasNext();) {
            if (it.next().getId() != null) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new word cannot already have an ID")).body(null);
            }
        }
        List<WordDTO> resultlist;
        resultlist = new ArrayList<WordDTO>();
        for (Iterator<WordDTO> it = wordDTOlist
                .iterator(); it.hasNext();) {
            WordDTO wordDTO = it.next();
            Word word = wordMapper.wordDTOToWord(wordDTO);
            word = wordRepository.save(word);
            WordDTO result = wordMapper.wordToWordDTO(word);
            resultlist.add(result);
        }
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resultlist.toString()))
                .body(resultlist);
    }

    /**
     * PUT /words : Updates an existing word.
     *
     * @param wordDTO the wordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * wordDTO, or with status 400 (Bad Request) if the wordDTO is not valid, or
     * with status 500 (Internal Server Error) if the wordDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/words")
    @Timed
    public ResponseEntity<WordDTO> updateWord(@Valid @RequestBody WordDTO wordDTO) throws URISyntaxException {
        log.debug("REST request to update Word : {}", wordDTO);
        if (wordDTO.getId() == null) {
            return createWord(wordDTO);
        }
        Word word = wordMapper.wordDTOToWord(wordDTO);
        word = wordRepository.save(word);
        WordDTO result = wordMapper.wordToWordDTO(word);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wordDTO.getId().toString()))
                .body(result);
    }

    /**
     * GET /words : get all the words.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of words in
     * body
     */
    @GetMapping("/words")
    @Timed
    public List<WordDTO> getAllWords() {
        log.debug("REST request to get all Words");
        List<Word> words = wordRepository.findAll();
        return wordMapper.wordsToWordDTOs(words);
    }

    /**
     * GET /words/:id : get the "id" word.
     *
     * @param id the id of the wordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * wordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/words/{id}")
    @Timed
    public ResponseEntity<WordDTO> getWord(@PathVariable Long id) {
        log.debug("REST request to get Word : {}", id);
        Word word = wordRepository.findOne(id);
        WordDTO wordDTO = wordMapper.wordToWordDTO(word);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wordDTO));
    }

    /**
     * GET /words/:wordstring: get the "wordstring" word.
     *
     * @param wordstring the wordstring of the wordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * wordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/wordstring/{wordstring}")
    @Timed
    public ResponseEntity<WordDTO> getWordstring(@PathVariable String wordstring) {
        log.debug("REST request to get Word : {}", wordstring);
        Optional<Word> existingWord = wordRepository.findByWordstring(wordstring);
        WordDTO wordDTO = null;
        if (existingWord.isPresent()) {
            wordDTO = wordMapper.wordToWordDTO(existingWord.get());
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(wordDTO));
    }

    /**
     * DELETE /words/:id : delete the "id" word.
     *
     * @param id the id of the wordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/words/{id}")
    @Timed
    public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
        log.debug("REST request to delete Word : {}", id);
        wordRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
