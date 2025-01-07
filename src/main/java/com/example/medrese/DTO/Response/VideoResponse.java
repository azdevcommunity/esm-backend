package com.example.medrese.DTO.Response;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoResponse {
    String videoId;
    OffsetDateTime publishedAt;
    String thumbnail;
    String title;
    String playlistId;
}
