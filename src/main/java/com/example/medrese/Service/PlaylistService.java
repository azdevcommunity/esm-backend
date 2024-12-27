package com.example.medrese.Service;

import com.example.medrese.DTO.Request.Create.CreatePlaylistDTO;
import com.example.medrese.DTO.Response.PlaylistResponse;
import com.example.medrese.Model.Playlist;
import com.example.medrese.Repository.PlaylistRepository;
import com.example.medrese.Repository.VideoRepository;
import com.example.medrese.mapper.PlaylistMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Log4j2
public class PlaylistService  {
     PlaylistRepository playlistRepository;
     PlaylistMapper playlistMapper;
     VideoService videoService;
     VideoRepository videoRepository;

    @Cacheable(value = "allPlaylists")
    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAllOrderByLatestVideo();
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

    /**
     * YouTube'dan playlist verilerini alır ve veritabanını günceller.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addOrUpdatePlaylists() {
        log.info("Started fetching playlists from YouTube API.");

        try {
            List<com.google.api.services.youtube.model.Playlist> playlistsFromYT = videoService.getPlayLists();
            List<com.example.medrese.Model.Playlist> newPlaylists = new ArrayList<>();
            int existingPlaylists = 0;

            for (com.google.api.services.youtube.model.Playlist playlist : playlistsFromYT) {
                if (playlistRepository.existsByPlaylistId(playlist.getId())) {
                    log.info("Playlist already exists in the database: {}", playlist.getId());
                   playlistRepository.findById(playlist.getId()).ifPresent(exists->{
                        String newThumbnail = playlist.getSnippet().getThumbnails().getDefault().getUrl() + "+" +
                                playlist.getSnippet().getThumbnails().getMedium().getUrl() + "+" +
                                playlist.getSnippet().getThumbnails().getHigh().getUrl();
                            exists.setThumbnail(newThumbnail);
                            playlistRepository.save(exists);
                    });

                    existingPlaylists++;
                    continue;
                }

                com.example.medrese.Model.Playlist pl = new com.example.medrese.Model.Playlist();
                pl.setPlaylistId(playlist.getId());
                pl.setTitle(playlist.getSnippet().getTitle());
                pl.setPublishedAt(playlist.getSnippet().getPublishedAt().toString());
                pl.setThumbnail(
                        playlist.getSnippet().getThumbnails().getDefault().getUrl() + "+" +
                                playlist.getSnippet().getThumbnails().getMedium().getUrl() + "+" +
                                playlist.getSnippet().getThumbnails().getHigh().getUrl()
                );
                pl.setVideoCount(videoRepository.countVideosByPlaylistId(playlist.getId()));

                newPlaylists.add(pl);
                log.info("New playlist added to the list: {}", pl.getPlaylistId());
            }

            if (!newPlaylists.isEmpty()) {
                playlistRepository.saveAll(newPlaylists);
                log.info("Successfully saved {} new playlists to the database.", newPlaylists.size());
            } else {
                log.info("No new playlists to save. {} playlists already existed.", existingPlaylists);
            }

        } catch (Exception e) {
            log.error("Error occurred while fetching and updating playlists: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update playlists", e);
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePlaylistVideoCount() {
        log.info("Started updating video counts for all playlists.");

        List<com.example.medrese.Model.Playlist> playlists = playlistRepository.findAll();
        int updatedCount = 0;

        if (playlists.isEmpty()) {
            log.warn("No playlists found in the database.");
            return;
        }

        for (com.example.medrese.Model.Playlist playlist : playlists) {
            try {
                Long videoCount = videoRepository.countVideosByPlaylistId(playlist.getPlaylistId());
                if (!Objects.equals(playlist.getVideoCount(), videoCount)) {
                    log.debug("Updating video count for playlist: {}. Old count: {}, New count: {}",
                            playlist.getPlaylistId(), playlist.getVideoCount(), videoCount);
                    playlist.setVideoCount(videoCount);
                    updatedCount++;
                } else {
                    log.debug("Video count unchanged for playlist: {}. Current count: {}",
                            playlist.getPlaylistId(), videoCount);
                }
            } catch (Exception e) {
                log.error("Error while updating video count for playlist: {}. Error: {}",
                        playlist.getPlaylistId(), e.getMessage(), e);
            }
        }

        if (updatedCount > 0) {
            playlistRepository.saveAll(playlists);
            log.info("Successfully updated video counts for {} playlists.", updatedCount);
        } else {
            log.info("No playlist video counts needed to be updated.");
        }
    }
}
