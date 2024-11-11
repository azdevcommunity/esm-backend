package com.example.medrese.Service;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@Log4j2
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class YoutubeService {

    VideoService videoService;
    PlaylistService playlistService;



    @Scheduled(cron = "0 0 2 * * ?")
    public void call() {
        log.info("Scheduled task started at 02:00 AM.");

        playlistService.addOrUpdatePlaylists();
        videoService.addOrUpdateVideos();
        videoService.addOrUpdateSearch();
        playlistService.updatePlaylistVideoCount();

        log.info("Scheduled task completed.");
    }

}