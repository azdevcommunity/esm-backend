package com.example.medrese.mapper;

import com.example.medrese.DTO.Request.Create.CreateVideoDTO;
import com.example.medrese.DTO.Response.VideoResponse;
import com.example.medrese.Model.Video;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface VideoMapper {
    Video toEntity(CreateVideoDTO createVideoDTO);
    VideoResponse toResponse(Video video);
}
