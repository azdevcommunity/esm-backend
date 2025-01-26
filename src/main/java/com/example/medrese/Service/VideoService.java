package com.example.medrese.Service;


import com.example.medrese.Core.Util.Exceptions.PlaylistNotFoundException;
import com.example.medrese.Core.Util.Exceptions.VideoNotFoundException;
import com.example.medrese.Core.Util.Rules.CheckIds;
import com.example.medrese.DTO.Request.Update.UpdateVideo;
import com.example.medrese.DTO.Response.PaginitionVideosResponse;
import com.example.medrese.DTO.Response.VideoResponse;
import com.example.medrese.Model.Video;
import com.example.medrese.Repository.VideoRepository;
import com.example.medrese.mapper.VideoMapper;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.medrese.Service.ConnectYoutubeApi.youtubeService;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Log4j2
public class VideoService {

    VideoRepository videoRepository;
    VideoMapper videoMapper;
    //    private static YouTube.Search.List request;
    private static final YouTube.Search.List request;
    private static final YouTube.Playlists.List requestPlayList;
    private static final YouTube.PlaylistItems.List requestPlayListItemList;
    private static PlaylistListResponse responsePlayList;
    private static PlaylistItemListResponse responsePlayListItemList;

    static {

        YouTube youTube = youtubeService;

        try {

            request = youTube.search().list("snippet");
            requestPlayList = youTube.playlists().list("snippet");
//            responsePlayList = sendRequestPlayList();
//            SearchListResponse response = sendRequest();
            requestPlayListItemList = youTube.playlistItems().list("snippet");
//            responsePlayListItemList = requestPlayListItemList
//                    .setKey(ConnectYoutubeApi.DEVELOPER_KEY)
//                    .setPlaylistId("PLU43-RoCoSfNG4hFDOwsh3TrRljtbuezZ")
//                    .setMaxResults(50L).execute();

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

    public List<VideoResponse> getAll() {

//        List<Video> videos = videoRepository.findAll();
//        List<VideoResponse> responseVideos = videos.stream()
//                .map(v -> VideoResponse.builder().build()).toList();

        return videoRepository.findAllOrderByPublishedAt();

    }

    public ResponseEntity<VideoResponse> delete(String id) {

        Video video = videoRepository.findByVideoId(id)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + id));

        videoRepository.deleteByVideoId(id);

        VideoResponse videoResponse = VideoResponse.builder().build();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(videoResponse);

    }

    public ResponseEntity<UpdateVideo> update(UpdateVideo video, String id) {

        Video findedVideo = videoRepository.findByVideoId(id)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + id));

        CheckIds.checkForPlayListOrVideo(findedVideo.getVideoId(), id);

        Video updatedVideo = new Video();

        updatedVideo.setVideoId(id);

        videoRepository.save(updatedVideo);

        return ResponseEntity.ok(video);
    }


//    @Cacheable(value = "videosByPlaylistId", key = "#playlistId")
    public List<VideoResponse> getByPlaylistId(String playlistId) {
        return videoRepository.findAllByPlaylistIdOrderByPublishedAtDesc(playlistId);
//                .stream()
//                .map(videoMapper::toResponse)
//                .toList();
    }


    // Hər gün saat 08:00, 16:00, 21:00 da update edəcək
//    @Scheduled(cron = "0 0 8,16,21 ? * *")
//    public void addOrUpdateVideos() {
//
//        try {
//
//            List<Video> videos = getAllVideosFromYouTubeAPI().stream()
//                    .map(video -> {
//
//                        Video video1 = new Video();
//                        video1.setVideoId(video.getSnippet().getResourceId().getVideoId());
//
//                        video1.setThumbnail(video.getSnippet().getThumbnails().getDefault().getUrl() + "+" +
//                                video.getSnippet().getThumbnails().getMedium().getUrl() + "+" +
//                                video.getSnippet().getThumbnails().getHigh().getUrl());
//
//                        return video1;
//
//                    }).toList();
//            videoRepository.saveAll(videos);
//        } catch (IOException e) {
//
//            throw new RuntimeException(e);
//
//        }
//    }

    public ResponseEntity<List<VideoResponse>> getSortedVideosBySpecificField(String fieldName, String sortOrder) {

        List<Video> videos = videoRepository.findAll(Sort.by(Sort.Direction.fromString(sortOrder), fieldName));
        List<VideoResponse> responseVideos = videos.stream()
                .map(v -> VideoResponse.builder().build()).toList();

        return ResponseEntity.ok(responseVideos);

    }

    public ResponseEntity<List<VideoResponse>> getSortedPlayListVideosBySpecificField(String fieldName, String playListId, String sortOrder) {
//change
        List<VideoResponse> responseVideos = videoRepository.findAllByPlaylistId(playListId, Sort.by(Sort.Direction.fromString(sortOrder), fieldName));
//        List<VideoResponse> responseVideos = videos.stream()
//                .map(v -> VideoResponse.builder().build()).toList();

        return ResponseEntity.ok(responseVideos);
    }

    public ResponseEntity<?> getByPlaylistIdAndPagination(String playListId, int page, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Invalid maxResult value. maxResult cannot be zero or negative");

        }

        List<VideoResponse> totalVideoList = videoRepository.findAllByPlaylistIdOrderByPublishedAtDesc(playListId);
//                .stream()
//                .map(videoMapper::toResponse)
//                .toList();

        int totalVideo = totalVideoList != null ? totalVideoList.size() : 0;
        if (totalVideo == 0) {
            throw new PlaylistNotFoundException("Videos not found for given playlist id");
        }
        System.out.println(totalVideo / size);
        System.out.println(totalVideo / size == totalVideo);
        int totalPageCount = totalVideo / size == 1 ? 1 : totalVideo / size + 1;

        if (page > totalPageCount) {
            throw new IllegalArgumentException(String.format("Invalid number of pages. Maximum number of pages is %d", totalPageCount));
        }

        Pageable pageable = PageRequest
                .of(page - 1,
                        Math.min(size, totalVideoList.size()),
                        Sort.by("publishedAt").descending());
        List<VideoResponse> videoResponses =
                videoRepository.findVideosByPlaylistId(playListId, pageable).stream()
                        .map(v -> VideoResponse.builder().build()).toList();


        return new ResponseEntity<>(new PaginitionVideosResponse(
                videoResponses,
                videoResponses.size(),
                totalVideo,
                page,
                totalPageCount),
                HttpStatus.OK);
    }

    public List<Playlist> getPlayLists() throws IOException {

        List<Playlist> playlists = new ArrayList<>();
        String nextPage = null;

        do {
            responsePlayList = requestPlayList.setKey(ConnectYoutubeApi.DEVELOPER_KEY)
                    .setChannelId(ConnectYoutubeApi.CHANNEL_ID)
                    .setMaxResults(50L)
                    .setPageToken(nextPage)
                    .execute();
//            requestPlayList.setPageToken(nextPage);
//            responsePlayList = requestPlayList.execute();

            playlists.addAll(responsePlayList.getItems());

            nextPage = responsePlayList.getNextPageToken();

        } while (nextPage != null);

        return playlists;
    }

    /**
     * Bu metod youtube API-sinə bağlanıb özəl məntiq ilə kanaldakı bütün videoları gətirir.<p>
     * Aralarında gizli, silinmiş videoları götürmür. Həmçinin şort (short) videoları da gətirir.</p>
     *
     * @return List<PlaylistItem>
     * @throws IOException
     * @author Maqsud Safin
     * @since v1
     */
    public List<PlaylistItem> getAllVideosFromYouTubeAPI() throws IOException {

        String nextPage = null;
        List<Playlist> playlistList = getPlayLists();
        List<PlaylistItem> videoList = new ArrayList<>();

        for (Playlist playlist : playlistList) {

            do {
                responsePlayListItemList = requestPlayListItemList
                        .setKey(ConnectYoutubeApi.DEVELOPER_KEY)
//                        .setPlaylistId("PLU43-RoCoSfNG4hFDOwsh3TrRljtbuezZ")
                        .setPlaylistId(playlist.getId())
                        .setPageToken(nextPage)
                        .setMaxResults(50L)
                        .execute();

                List<PlaylistItem> items = responsePlayListItemList.getItems();
                if (items != null && !items.isEmpty()) {
                    videoList.addAll(items.stream()
                            .filter(playlistItem -> playlistItem.getSnippet().getThumbnails().getMedium() != null)
                            .toList());
                }

                nextPage = responsePlayListItemList.getNextPageToken();
            } while (nextPage != null);

        }

        return videoList;
    }

    public static PlaylistListResponse sendRequestPlayList() throws IOException {
        return requestPlayList
                .setKey(ConnectYoutubeApi.DEVELOPER_KEY)
                .setChannelId(ConnectYoutubeApi.CHANNEL_ID)
                .setMaxResults(50L)
                .execute();
    }

    /**
     * YouTube'dan video verilerini alır ve veritabanını günceller.
     */
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void addOrUpdateVideos() {
//        log.info("Started fetching videos from YouTube API.");
//
//        try {
//            List<PlaylistItem> playlistItems = getAllVideosFromYouTubeAPI();
//
//
//            log.info("Found {} videos from YouTube API.", playlistItems.size());
//            List<Video> videos = new ArrayList<>();
//            int existingVideos = 0;
//
//            for (PlaylistItem playlistItem : playlistItems) {
//                if (videoRepository.existsById(playlistItem.getSnippet().getResourceId().getVideoId())) {
//                    log.info("Video already exists in the database: {}", playlistItem.getSnippet().getResourceId().getVideoId());
//                    existingVideos++;
//                    continue;
//                }
//
//                Video video = new Video();
//                video.setTitle(playlistItem.getSnippet().getTitle());
//                video.setPublishedAt(playlistItem.getSnippet().getPublishedAt().toString());
//                video.setPlaylistId(playlistItem.getSnippet().getPlaylistId());
//                video.setVideoId(playlistItem.getSnippet().getResourceId().getVideoId());
//                video.setThumbnail(
//                        playlistItem.getSnippet().getThumbnails().getDefault().getUrl() + "+" +
//                                playlistItem.getSnippet().getThumbnails().getMedium().getUrl() + "+" +
//                                playlistItem.getSnippet().getThumbnails().getHigh().getUrl()
//                );
//
//                videos.add(video);
//                log.info("New video added to the list: {}", video.getVideoId());
//            }
//
//            if (!videos.isEmpty()) {
//                videoRepository.saveAll(videos);
//                log.info("Successfully saved {} new videos to the database.", videos.size());
//            } else {
//                log.info("No new videos to save. {} videos already existed.", existingVideos);
//            }
//
//        } catch (IOException e) {
//            log.error("Error occurred while fetching and updating videos: {}", e.getMessage(), e);
//            throw new RuntimeException("Failed to update videos", e);
//        }
//    }
//
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void addOrUpdateSearch() {
//        try {
//            List<Video> videos = new ArrayList<>();
//            List<SearchResult> searchResults = getAllShortVideos();
//            int existingVideos = 0;
//            log.info("Found search {} videos from YouTube API.", searchResults.size());
//
//
//            for (SearchResult searchResult : searchResults) {
//                if (videoRepository.existsById(searchResult.getId().getVideoId())) {
//                    log.info("Video already exists in the database: {}", searchResult.getId().getVideoId());
//                    existingVideos++;
//                    continue;
//                }
//
//                Video video = new Video();
//                video.setTitle(searchResult.getSnippet().getTitle());
//                video.setPublishedAt(searchResult.getSnippet().getPublishedAt().toString());
//                video.setPlaylistId(searchResult.getId().getPlaylistId());
//                video.setVideoId(searchResult.getId().getVideoId());
//                video.setThumbnail(
//                        searchResult.getSnippet().getThumbnails().getDefault().getUrl() + "+" +
//                                searchResult.getSnippet().getThumbnails().getMedium().getUrl() + "+" +
//                                searchResult.getSnippet().getThumbnails().getHigh().getUrl()
//                );
//
//                videos.add(video);
//                log.info("New video added to the list: {}", video.getVideoId());
//            }
//
//
//            if (!videos.isEmpty()) {
//                videoRepository.saveAll(videos);
//                log.info("Successfully saved {} new videos to the database.", videos.size());
//            } else {
//                log.info("No new videos to save. {} videos already existed.", existingVideos);
//            }
//
//        } catch (IOException e) {
//            log.error("Error occurred while fetching and updating videos: {}", e.getMessage(), e);
//            throw new RuntimeException("Failed to update videos", e);
//        }
//    }
//
//    public List<SearchResult> getAllShortVideos() throws IOException {
//        String nextPageToken = null;
//        List<SearchResult> allShortVideos = new ArrayList<>();
//
//        do {
//            // API çağrısı
//            YouTube.Search.List searchRequest = youtubeService.search()
//                    .list("snippet")
//                    .setChannelId(ConnectYoutubeApi.CHANNEL_ID)
//                    .setType("video")
//                    .setMaxResults(50L)
//                    .setKey(ConnectYoutubeApi.DEVELOPER_KEY) // API Key ekledik
////                    .setVideoDuration("short")
//                    .setPageToken(nextPageToken);
//
//            SearchListResponse searchResponse = searchRequest.execute();
//
//            // Gelen videoları listeye ekle
//            List<SearchResult> items = searchResponse.getItems();
//            if (items != null && !items.isEmpty()) {
//                allShortVideos.addAll(items);
//            }
//
//            // Bir sonraki sayfa token'ı
//            nextPageToken = searchResponse.getNextPageToken();
//
//        } while (nextPageToken != null);
//
//        return allShortVideos;
//    }
//
    public List<VideoResponse> searchVideos(int limit, String search){
        List<VideoResponse> videos;
        if(Objects.nonNull(search)){
            videos = videoRepository.searchVideos(search, limit);
        }else {
            videos = videoRepository.searchVideos(limit);
        }
//        return videos.stream()
//                .map(video->VideoResponse.builder()
//                        .title(video.getTitle())
//                        .videoId(video.getVideoId())
//                        .publishedAt(video.getPublishedAt())
//                        .thumbnail(video.getThumbnail())
//                        .title(video.getTitle())
//                        .playlistId(video.getPlaylistId())
//                        .build())
//                .toList();
        return videos;
    }

}