package com.example.medrese.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "playlists")
public class Playlist  {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @NotBlank  @Size(max = 40)
    @Column(name = "playlist_id")
    String playlistId;

    @Column(name = "published_at")
    OffsetDateTime publishedAt;

    @Column(name = "thumbnail")
    String thumbnail;

    @NotBlank
    @Column(name = "title")
    String title;

    @Column(name = "video_count")
    Long videoCount;
}
