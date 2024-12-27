package com.example.medrese.Repository;


import com.example.medrese.Model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, String> {
    boolean existsByPlaylistId(String id);


//    @Query("""
//            SELECT p FROM Playlist p
//            where p.videoCount > 0
//            ORDER BY p.videoCount DESC
//            """)
//    List<Playlist> findAllOrderByVideoCountDesc();

    @Query("""
        SELECT p FROM Playlist p
        WHERE p.videoCount > 0
        ORDER BY p.publishedAt DESC
        """)
    List<Playlist> findAllOrderByLatestVideo();
}
