package com.canaryshop.canaryshop.DTOs;

import com.canaryshop.canaryshop.entities.Image;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDTO toDTO(Image image);
    List<ImageDTO> toDTOs(Collection<Image> images);
}
