package com.steiner.myservice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;

/**
 * A ReviewVector.
 */
@Entity
@Table(name = "review_vector")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Transactional
public class ReviewVector implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "vector", nullable = false)
    private String vector;

    @ManyToOne(optional = false)
    @NotNull
    private Review review;

    @ManyToOne(optional = false)
    @NotNull
    private RankSnapshot rankSnapshot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVector() {
        return vector;
    }

    public ReviewVector vector(String vector) {
        this.vector = vector;
        return this;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public Review getReview() {
        return review;
    }

    public ReviewVector review(Review review) {
        this.review = review;
        return this;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public RankSnapshot getRankSnapshot() {
        return rankSnapshot;
    }

    public ReviewVector rankSnapshot(RankSnapshot rankSnapshot) {
        this.rankSnapshot = rankSnapshot;
        return this;
    }

    public void setRankSnapshot(RankSnapshot rankSnapshot) {
        this.rankSnapshot = rankSnapshot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReviewVector reviewVector = (ReviewVector) o;
        if (reviewVector.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, reviewVector.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReviewVector{" +
            "id=" + id +
            ", vector='" + vector + "'" +
            '}';
    }
}
