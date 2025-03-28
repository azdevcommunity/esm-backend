package com.example.medrese.Controller;

import com.example.medrese.Model.Playlist;
import com.example.medrese.Service.PlaylistService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    private final PlaylistService playlistService;

    @GetMapping(params = {"search"})
    public List<Playlist> getAllPlaylists(@RequestParam(required = false, name = "search") String search) {
        return playlistService.getAllPlaylistsWithSearch(search);
    }


    @GetMapping("/of-video/{videoId}")
    public ResponseEntity<Playlist> getByOfVideo(@PathVariable String videoId) {
        return ResponseEntity.ok(playlistService.getByOfVideo(videoId));
    }


    @GetMapping("/{playlistId}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable String playlistId) {
        return ResponseEntity.ok(playlistService.getPlaylistById(playlistId));
    }

//    @PostMapping
//    public ResponseEntity<PlaylistResponse> createPlaylist(@RequestBody CreatePlaylistDTO createPlaylistDTO) {
//        return ResponseEntity.ok(playlistService.createPlaylist(createPlaylistDTO)) ;
//    }
//
//    @PutMapping("/{playlistId}")
//    public ResponseEntity<Playlist> updatePlaylist(@PathVariable String playlistId, @RequestBody Playlist playlistDetails) {
//        try {
//            Playlist updatedPlaylist = playlistService.updatePlaylist(playlistId, playlistDetails);
//            return ResponseEntity.ok(updatedPlaylist);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    @DeleteMapping("/{playlistId}")
//    public ResponseEntity<Void> deletePlaylist(@PathVariable String playlistId) {
//        playlistService.deletePlaylist(playlistId);
//        return ResponseEntity.noContent().build();
//    }
}

