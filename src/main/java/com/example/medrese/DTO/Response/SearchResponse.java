package com.example.medrese.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class SearchResponse {
    List<ArticleProjection2> articles;
    List<BookResponse> books;
    List<VideoResponse> videos;
}
