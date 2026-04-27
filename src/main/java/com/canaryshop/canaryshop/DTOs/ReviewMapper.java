package com.canaryshop.canaryshop.DTOs;

import com.canaryshop.canaryshop.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewSummaryDTO toSummaryDTO(Review review);
    @Mapping(source = "name", target = "title")
    ReviewDTO toDTO(Review review);
    List<ReviewDTO> toDTOs(Collection<Review> reviews);
}
