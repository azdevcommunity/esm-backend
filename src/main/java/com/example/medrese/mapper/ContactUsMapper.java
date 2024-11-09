package com.example.medrese.mapper;

import com.example.medrese.DTO.Request.Create.CreateContactUsDTO;
import com.example.medrese.DTO.Response.ContactUsResponseDTO;
import com.example.medrese.Model.ContactUs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface ContactUsMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    ContactUs toEntity(CreateContactUsDTO dto);

    ContactUsResponseDTO toResponseDTO(ContactUs contactUs);

}