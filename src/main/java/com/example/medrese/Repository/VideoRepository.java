package com.example.medrese.Repository;

import com.example.medrese.Model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VideoRepository extends JpaRepository<Video, String> {
//    List<Video> findAllByPlaylistId(String playlistId);
//    List<Video> findVideosByPlaylistId(String playlistId, Pageable pageable);
//    List<Video> findAllByPlaylistId(String playlistId,Sort sort);
}
