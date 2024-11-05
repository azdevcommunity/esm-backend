package com.example.medrese.mapper;

import com.example.medrese.DTO.Request.Create.CreateStatisticDTO;
import com.example.medrese.DTO.Response.StatisticResponse;
import com.example.medrese.Model.Statistic;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface StatisticMapper {
    Statistic toEntity(CreateStatisticDTO createStatisticDTO);
    StatisticResponse toResponse(Statistic statistic);
}
