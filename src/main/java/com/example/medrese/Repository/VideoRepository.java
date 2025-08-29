package com.example.medrese.Repository;

import com.example.medrese.DTO.Response.VideoResponse;
import com.example.medrese.Model.Video;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
            WHERE pv.playlistId = :playlistId and v.isPrivate = false
            """)
    Page<Video> findVideosByPlaylistId(@Param("playlistId") String playlistId, Pageable pageable);

    @Query("""
            SELECT new com.example.medrese.DTO.Response.VideoResponse(
                v.videoId,
                v.publishedAt,
                v.thumbnail,
                v.title,
                pv.playlistId,
                v.description
            )
            FROM Video v
            JOIN PlaylistVideo pv ON v.videoId = pv.videoId
            WHERE pv.playlistId = :playlistId and v.isPrivate = false
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
                pv.playlistId,
                v.description
            )
            FROM Video v
            JOIN PlaylistVideo pv ON v.videoId = pv.videoId
            WHERE pv.playlistId = :playlistId and v.isPrivate = false
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
                p.playlistId,
                v.description
            )
            FROM Video v
            LEFT JOIN PlaylistVideo pv ON v.videoId = pv.videoId
            LEFT JOIN Playlist p ON pv.playlistId = p.playlistId
            where v.isPrivate = false
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
                p.playlistId,
                v.description
            )
            FROM Video v
            LEFT JOIN PlaylistVideo pv ON v.videoId = pv.videoId
            LEFT JOIN Playlist p ON pv.playlistId = p.playlistId
            WHERE (:search IS NULL OR :search = '')
               OR (p.playlistId LIKE CONCAT('%', :search, '%')
                   OR v.title LIKE CONCAT('%', :search, '%'))
            and v.isPrivate = false
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
                pv.playlistId,
                v.description
            )
            FROM Video v
            JOIN PlaylistVideo pv ON v.videoId = pv.videoId
            where v.isPrivate = false
            order by v.publishedAt DESC
            """)
    List<VideoResponse> findAllOrderByPublishedAt();



    @Query(value = """
        SELECT new com.example.medrese.DTO.Response.VideoResponse(
            v.videoId,
            v.publishedAt,
            v.thumbnail,
            v.title,
            null,
                v.description
        )
        FROM Video v
        WHERE ((:search IS NULL OR :search = '')
               OR LOWER(v.title) LIKE LOWER(CONCAT('%', :search, '%')))
          AND v.isPrivate = false and v.isShort = :isShort
        ORDER BY v.publishedAt DESC
        """,
            countQuery = """
                    SELECT COUNT(v)
                    FROM Video v
                    WHERE ((:search IS NULL OR :search = '')
                           OR LOWER(v.title) LIKE LOWER(CONCAT('%', :search, '%')))
                      AND v.isPrivate = false  and v.isShort = :isShort
                    """)
    Page<VideoResponse> findAllPagingOrderByPublishedAt(Pageable pageable, String search, Boolean isShort);


    @Query(value = """
            SELECT v.video_id, v.published_at, v.thumbnail, v.title, NULL, v.description  
                        FROM video v  
                        ORDER BY v.published_at DESC  
                        LIMIT 1 
            """, nativeQuery = true)
    VideoResponse findLatestVideo();


    @Query(value = """
             select count(v) from videos v where v.is_private = false
            """, nativeQuery = true)
    long countActiveVideos();
}
