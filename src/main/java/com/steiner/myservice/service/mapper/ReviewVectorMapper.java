package com.steiner.myservice.service.mapper;

import com.steiner.myservice.domain.*;
import com.steiner.myservice.service.dto.ReviewVectorDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ReviewVector and its DTO ReviewVectorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReviewVectorMapper {

    @Mapping(source = "review.id", target = "reviewId")
    @Mapping(source = "rankSnapshot.id", target = "rankSnapshotId")
    @Mapping(source = "rankSnapshot.epoch", target = "rankSnapshotEpoch")
    ReviewVectorDTO reviewVectorToReviewVectorDTO(ReviewVector reviewVector);

    List<ReviewVectorDTO> reviewVectorsToReviewVectorDTOs(List<ReviewVector> reviewVectors);

    @Mapping(source = "reviewId", target = "review")
    @Mapping(source = "rankSnapshotId", target = "rankSnapshot")
    ReviewVector reviewVectorDTOToReviewVector(ReviewVectorDTO reviewVectorDTO);

    List<ReviewVector> reviewVectorDTOsToReviewVectors(List<ReviewVectorDTO> reviewVectorDTOs);

    default Review reviewFromId(Long id) {
        if (id == null) {
            return null;
        }
        Review review = new Review();
        review.setId(id);
        return review;
    }

    default RankSnapshot rankSnapshotFromId(Long id) {
        if (id == null) {
            return null;
        }
        RankSnapshot rankSnapshot = new RankSnapshot();
        rankSnapshot.setId(id);
        return rankSnapshot;
    }
}
