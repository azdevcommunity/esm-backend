package com.example.medrese.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "videos")
public class Video  {

    @Id
    @Column(name = "video_id")
    String videoId;

    @Column(name = "published_at")
    String publishedAt;

    @Column(name = "thumbnail")
    String thumbnail;

    @NotBlank
    @Column(name = "title")
    String title;

    @JoinColumn(name = "playlist_id")
    String playlistId;

}