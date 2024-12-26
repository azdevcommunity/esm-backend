package com.example.medrese.Repository;

import com.example.medrese.Model.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VideoRepository extends JpaRepository<Video, String> {
    List<Video> findAllByPlaylistId(String playlistId);
    List<Video> findVideosByPlaylistId(String playlistId, Pageable pageable);
    List<Video> findAllByPlaylistId(String playlistId, Sort sort);
    Long countVideosByPlaylistId(String playlistId);
    List<Video> findAllByPlaylistIdOrderByPublishedAtDesc(String playlistId);

    @Query(value = "SELECT * FROM videos ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Video> searchVideos(@Param("limit") int limit);

    @Query(value = """
    SELECT *
    FROM videos
    WHERE 
      CASE
        WHEN :search IS NULL OR :search = '' 
          THEN 1  -- if search is null/empty, we don't filter anything
        WHEN (playlist_id ILIKE CONCAT('%', :search, '%')
              OR title ILIKE CONCAT('%', :search, '%'))
          THEN 1  -- if search is not null/empty, check playlist_id/title
        ELSE 0
      END = 1
    ORDER BY RANDOM()
    LIMIT :limit
    """, nativeQuery = true)
    List<Video> searchVideos(
            @Param("search") String search,
            @Param("limit") int limit
    );
}
