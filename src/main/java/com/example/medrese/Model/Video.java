package com.example.medrese.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
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
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name = "video_id")
    String videoId;

    @Column(name = "published_at")
    OffsetDateTime publishedAt;

    @Column(name = "thumbnail")
    String thumbnail;

    @NotBlank
    @Column(name = "title")
    String title;

    @Column(name = "is_private")
    Boolean isPrivate;

    @Column(name = "is_short")
    Boolean isShort;

    @Column(name = "description", columnDefinition = "text")
    String description;

//    @JoinColumn(name = "playlist_id")
//    String playlistId;

}