package com.example.medrese.DTO.Response;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoResponse {
    String videoId;
    String publishedAt;
    String thumbnail;
    String title;
    String playlistId;
}
