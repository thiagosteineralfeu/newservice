package com.steiner.myservice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the WordOccurrences entity.
 */
public class WordOccurrencesDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer amountoccurrences;

    private Long wordId;

    private String wordWordstring;

    private Long reviewId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getAmountoccurrences() {
        return amountoccurrences;
    }

    public void setAmountoccurrences(Integer amountoccurrences) {
        this.amountoccurrences = amountoccurrences;
    }

    public Long getWordId() {
        return wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public String getWordWordstring() {
        return wordWordstring;
    }

    public void setWordWordstring(String wordWordstring) {
        this.wordWordstring = wordWordstring;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WordOccurrencesDTO wordOccurrencesDTO = (WordOccurrencesDTO) o;

        if ( ! Objects.equals(id, wordOccurrencesDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "WordOccurrencesDTO{" +
            "id=" + id +
            ", amountoccurrences='" + amountoccurrences + "'" +
            '}';
    }
}
