package com.example.medrese.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cache")
@Validated
public class CacheController {

    @GetMapping("/evict-allPlaylists")
    @CacheEvict(value = "allPlaylists", allEntries = true)
    public String evictAllCaches() {
        return "All cache entries for allPlaylists have been evicted.";
    }

    @GetMapping("/evict-videosByPlaylistId")
    @CacheEvict(value = "videosByPlaylistId", allEntries = true)
    public String evictVideosByPlaylistIdCache() {
        return "All cache entries for videosByPlaylistId have been evicted.";
    }
}
