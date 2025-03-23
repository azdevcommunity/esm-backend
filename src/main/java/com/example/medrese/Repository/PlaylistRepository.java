package com.example.medrese.Repository;


import com.example.medrese.Model.Playlist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    boolean existsByPlaylistId(String id);


//    @Query("""
//            SELECT p FROM Playlist p
//            where p.videoCount > 0
//            ORDER BY p.videoCount DESC
//            """)
//    List<Playlist> findAllOrderByVideoCountDesc();

    @Query("""
            SELECT p
            FROM Playlist p
            JOIN PlaylistVideo pv ON p.playlistId = pv.playlistId
            JOIN Video v ON pv.videoId = v.videoId
            WHERE v.publishedAt IS NOT NULL and v.isPrivate = false
            GROUP BY p
            HAVING COUNT(v) > 0
            ORDER BY MAX(v.publishedAt) DESC
            """)
    List<Playlist> findAllOrderByLatestVideo();

    Optional<Playlist> findByPlaylistId(String id);


    @Query("""
                SELECT p
                FROM Playlist p
                JOIN PlaylistVideo pv ON p.playlistId = pv.playlistId
                WHERE pv.videoId = :videoId
            """)
    List<Playlist> getAllByVideoId(@Param("videoId") String videoId);
}
