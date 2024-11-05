package com.example.medrese.mapper;

import com.example.medrese.DTO.Request.Create.CreateAuthorDTO;
import com.example.medrese.DTO.Response.AuthorResponse;
import com.example.medrese.Model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorMapper {
    Author toEntity(CreateAuthorDTO createAuthorDTO);

    AuthorResponse toResponse(Author author);
}
