package com.example.medrese.mapper;

import com.example.medrese.DTO.Request.Create.CreateBookDTO;
import com.example.medrese.DTO.Response.BookResponse;
import com.example.medrese.Model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface BookMapper {
    Book toEntity(CreateBookDTO createBookDTO);

    BookResponse toResponse(Book book);

}
