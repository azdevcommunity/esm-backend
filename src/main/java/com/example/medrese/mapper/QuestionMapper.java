package com.example.medrese.mapper;

import com.example.medrese.DTO.Request.Create.CreateQuestionDTO;
import com.example.medrese.DTO.Response.QuestionResponse;
import com.example.medrese.Model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface QuestionMapper {
    Question toEntity(CreateQuestionDTO createQuestionDTO);
    QuestionResponse toResponse(Question question);
}
