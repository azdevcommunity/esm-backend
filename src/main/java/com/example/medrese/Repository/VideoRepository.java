package com.example.medrese.Repository;

import com.example.medrese.DTO.Response.VideoResponse;
import com.example.medrese.Model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    Optional<Video> findByVideoId(String id);

    @Modifying
    void deleteByVideoId(String id);

    //    List<Video> findAllByPlaylistId(String playlistId);
    @Query("""
            SELECT v
            FROM Video v
            JOIN PlaylistVideo pv ON v.videoId = pv.videoId
            WHERE pv.playlistId = :playlistId
            """)
    Page<Video> findVideosByPlaylistId(@Param("playlistId") String playlistId, Pageable pageable);

    @Query("""
            SELECT new com.example.medrese.DTO.Response.VideoResponse(
                v.videoId,
                v.publishedAt,
                v.thumbnail,
                v.title,
                pv.playlistId
            )
            FROM Video v
            JOIN PlaylistVideo pv ON v.videoId = pv.videoId
            WHERE pv.playlistId = :playlistId
            """)
    List<VideoResponse> findAllByPlaylistId(@Param("playlistId") String playlistId, Sort sort);

    //    Long countVideosByPlaylistId(String playlistId);
//    List<Video> findAllByPlaylistIdOrderByPublishedAtDesc(String playlistId);
    @Query("""
            SELECT new com.example.medrese.DTO.Response.VideoResponse(
                v.videoId,
                v.publishedAt,
                v.thumbnail,
                v.title,
                pv.playlistId
            )
            FROM Video v
            JOIN PlaylistVideo pv ON v.videoId = pv.videoId
            WHERE pv.playlistId = :playlistId
            ORDER BY v.publishedAt DESC
            """)
    List<VideoResponse> findAllByPlaylistIdOrderByPublishedAtDesc(@Param("playlistId") String playlistId);

    //    @Query(value = "SELECT * FROM videos ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
//    List<Video> searchVideos(@Param("limit") int limit);
    @Query("""
            SELECT new com.example.medrese.DTO.Response.VideoResponse(
                v.videoId,
                v.publishedAt,
                v.thumbnail,
                v.title,
                p.playlistId
            )
            FROM Video v
            LEFT JOIN PlaylistVideo pv ON v.videoId = pv.videoId
            LEFT JOIN Playlist p ON pv.playlistId = p.playlistId
            ORDER BY FUNCTION('RANDOM')
            LIMIT :limit
            """)
    List<VideoResponse> searchVideos(@Param("limit") int limit);

//    @Query(value = """
//    SELECT *
//    FROM videos
//    WHERE
//       (:search IS NULL OR :search = '')
//       OR (playlist_id ILIKE CONCAT('%', :search, '%')
//           OR title ILIKE CONCAT('%', :search, '%'))
//    ORDER BY RANDOM()
//    LIMIT :limit
//    """, nativeQuery = true)
//    List<Video> searchVideos(
//            @Param("search") String search,
//            @Param("limit") int limit
//    );

    @Query("""
            SELECT new com.example.medrese.DTO.Response.VideoResponse(
                v.videoId,
                v.publishedAt,
                v.thumbnail,
                v.title,
                p.playlistId
            )
            FROM Video v
            LEFT JOIN PlaylistVideo pv ON v.videoId = pv.videoId
            LEFT JOIN Playlist p ON pv.playlistId = p.playlistId
            WHERE (:search IS NULL OR :search = '')
               OR (p.playlistId LIKE CONCAT('%', :search, '%')
                   OR v.title LIKE CONCAT('%', :search, '%'))
            ORDER BY FUNCTION('RANDOM')
            LIMIT :limit
            """)
    List<VideoResponse> searchVideos(@Param("search") String search, @Param("limit") int limit);


    @Query("""
            SELECT new com.example.medrese.DTO.Response.VideoResponse(
                v.videoId,
                v.publishedAt,
                v.thumbnail,
                v.title,
                pv.playlistId
            )
            FROM Video v
            JOIN PlaylistVideo pv ON v.videoId = pv.videoId
            order by v.publishedAt DESC
            """)
    List<VideoResponse> findAllOrderByPublishedAt();



    @Query("""
            SELECT new com.example.medrese.DTO.Response.VideoResponse(
                v.videoId,
                v.publishedAt,
                v.thumbnail,
                v.title,
                pv.playlistId
            )
            FROM Video v
            JOIN PlaylistVideo pv ON v.videoId = pv.videoId
              WHERE (:search IS NULL OR :search = '')
                   OR v.title LIKE CONCAT('%', :search, '%')
            order by v.publishedAt DESC
            """)
    Page<VideoResponse> findAllPagingOrderByPublishedAt(Pageable pageable, String search);

}
