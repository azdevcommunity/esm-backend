package com.example.medrese.mapper;

import com.example.medrese.DTO.Request.Create.CreateCategoryDTO;
import com.example.medrese.DTO.Response.CategoryResponse;
import com.example.medrese.Model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface CategoryMapper {
    Category toEntity(CreateCategoryDTO createCategoryDTO);
    CategoryResponse toResponse(Category category);
}
