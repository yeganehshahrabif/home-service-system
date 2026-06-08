package ir.maktabsharif138.home_service_system.mapper;

import ir.maktabsharif138.home_service_system.dto.request.ReviewCreateRequest;
import ir.maktabsharif138.home_service_system.dto.response.ReviewResponse;
import ir.maktabsharif138.home_service_system.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviewDate", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "expert", ignore = true)
    @Mapping(target = "customerOrder", ignore = true)
    Review toReview(ReviewCreateRequest request);

    ReviewResponse toResponse(Review review);

    void updateReview(@MappingTarget Review review, ReviewCreateRequest request);

    List<ReviewResponse> toReviewResponse(List<Review> reviews);
}
