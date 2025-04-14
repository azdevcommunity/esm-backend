package com.example.medrese.Service;

import com.example.medrese.DTO.Response.ArticleProjection2;
import com.example.medrese.DTO.Response.BookResponse;
import com.example.medrese.DTO.Response.SearchResponse;
import com.example.medrese.DTO.Response.VideoResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchService {

    CategoryService categoryService;
    BookService bookService;
    ArticleService articleService;
    VideoService videoService;

    public SearchResponse search(Long categoryId, String search) {
        List<ArticleProjection2> articleProjections = articleService.searchArticles(
                4, List.of(categoryId), search
        ).get().toList();

        List<VideoResponse> videos = videoService.searchVideos(4, search);

        List<BookResponse> books = bookService.getAllBooks();

        return SearchResponse.builder()
                .articles(articleProjections)
                .books(books)
                .videos(videos)
                .build();
    }
}
