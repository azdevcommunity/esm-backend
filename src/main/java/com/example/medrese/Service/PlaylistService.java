package com.example.medrese.Service;

import com.example.medrese.Model.Playlist;
import com.example.medrese.Repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService  {
    @Autowired
    private PlaylistRepository playlistRepository;

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    public Optional<Playlist> getPlaylistById(String playlistId) {
        return playlistRepository.findById(playlistId);
    }

    public Playlist createPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public Playlist updatePlaylist(String playlistId, Playlist playlistDetails) {
        return playlistRepository.findById(playlistId).map(playlist -> {
            playlist.setTitle(playlistDetails.getTitle());
            playlist.setVideoCount(playlistDetails.getVideoCount());
            return playlistRepository.save(playlist);
        }).orElseThrow(() -> new RuntimeException("Playlist not found with id " + playlistId));
    }

    public void deletePlaylist(String playlistId) {
        playlistRepository.deleteById(playlistId);
    }
}
