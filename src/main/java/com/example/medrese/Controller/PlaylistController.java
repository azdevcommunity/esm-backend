package com.example.medrese.Controller;

import com.example.medrese.DTO.Request.Create.CreatePlaylistDTO;
import com.example.medrese.DTO.Response.PlaylistResponse;
import com.example.medrese.Model.Playlist;
import com.example.medrese.Service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;

    @GetMapping
    public List<Playlist> getAllPlaylists() {
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable String playlistId) {
        return ResponseEntity.ok(playlistService.getPlaylistById(playlistId));
    }

    @PostMapping
    public ResponseEntity<PlaylistResponse> createPlaylist(@RequestBody CreatePlaylistDTO createPlaylistDTO) {
        return ResponseEntity.ok(playlistService.createPlaylist(createPlaylistDTO)) ;
    }

    @PutMapping("/{playlistId}")
    public ResponseEntity<Playlist> updatePlaylist(@PathVariable String playlistId, @RequestBody Playlist playlistDetails) {
        try {
            Playlist updatedPlaylist = playlistService.updatePlaylist(playlistId, playlistDetails);
            return ResponseEntity.ok(updatedPlaylist);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable String playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.noContent().build();
    }
}

