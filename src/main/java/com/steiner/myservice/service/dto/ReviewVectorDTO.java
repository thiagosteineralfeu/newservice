package com.steiner.myservice.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ReviewVector entity.
 */
public class ReviewVectorDTO implements Serializable {

    private Long id;

    @NotNull
    private String vector;

    private Long reviewId;

    private Long rankSnapshotId;

    private String rankSnapshotEpoch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getRankSnapshotId() {
        return rankSnapshotId;
    }

    public void setRankSnapshotId(Long rankSnapshotId) {
        this.rankSnapshotId = rankSnapshotId;
    }

    public String getRankSnapshotEpoch() {
        return rankSnapshotEpoch;
    }

    public void setRankSnapshotEpoch(String rankSnapshotEpoch) {
        this.rankSnapshotEpoch = rankSnapshotEpoch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReviewVectorDTO reviewVectorDTO = (ReviewVectorDTO) o;

        if ( ! Objects.equals(id, reviewVectorDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ReviewVectorDTO{" +
            "id=" + id +
            ", vector='" + vector + "'" +
            '}';
    }
}
