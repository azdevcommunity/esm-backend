package com.example.medrese.Service;

import com.example.medrese.DTO.Request.Create.CreatePlaylistDTO;
import com.example.medrese.DTO.Response.PlaylistResponse;
import com.example.medrese.Model.Playlist;
import com.example.medrese.Repository.PlaylistRepository;
import com.example.medrese.mapper.PlaylistMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class PlaylistService  {
     PlaylistRepository playlistRepository;
     PlaylistMapper playlistMapper;

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    public Playlist getPlaylistById(String playlistId) {
        return  playlistRepository.findById(playlistId).orElseThrow(()->new RuntimeException("playlist not found"));
    }

    public PlaylistResponse createPlaylist(CreatePlaylistDTO createPlaylistDTO) {
        Playlist playlist = playlistMapper.toEntity(createPlaylistDTO);
        playlist = playlistRepository.save(playlist);
        return playlistMapper.toResponse(playlist);
    }

    public Playlist updatePlaylist(String playlistId, Playlist playlistDetails) {
        return playlistRepository.findById(playlistId).map(playlist -> {
            playlist.setTitle(playlistDetails.getTitle());
            playlist.setVideoCount(playlistDetails.getVideoCount());
            return playlistRepository.save(playlist);
        }).orElseThrow(() -> new RuntimeException("Playlist not found with id " + playlistId));
    }

    public void deletePlaylist(String playlistId) {
        if (!playlistRepository.existsById(playlistId)){
            throw new RuntimeException("doesnt exist");
        }
        playlistRepository.deleteById(playlistId);
    }
}
