package com.example.medrese.mapper;

import com.example.medrese.DTO.Request.Create.CreatePlaylistDTO;
import com.example.medrese.DTO.Response.PlaylistResponse;
import com.example.medrese.Model.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface PlaylistMapper {
    Playlist toEntity(CreatePlaylistDTO createPlaylistDTO);
    PlaylistResponse toResponse(Playlist playlist);
}
