package com.example.medrese.mapper;

import com.example.medrese.DTO.Request.Create.CreateArticleDTO;
import com.example.medrese.DTO.Response.ArticleResponse;
import com.example.medrese.Model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleMapper {
    Article toEntity(CreateArticleDTO createArticleDTO);

    ArticleResponse toResponse(Article article);
}
