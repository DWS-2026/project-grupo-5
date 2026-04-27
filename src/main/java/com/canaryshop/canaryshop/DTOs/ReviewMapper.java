package com.canaryshop.canaryshop.DTOs;

import com.canaryshop.canaryshop.entities.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewSummaryDTO toSummaryDTO(Review review);
}
