package com.example.medrese.Service;

import com.example.medrese.DTO.Response.ArticleProjection;
import com.example.medrese.DTO.Response.BookResponse;
import com.example.medrese.DTO.Response.SearchResponse;
import com.example.medrese.DTO.Response.VideoResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchService {

    CategoryService categoryService;
    BookService bookService;
    ArticleService articleService;
    VideoService videoService;

    public SearchResponse search(Long categoryId) {
        List<ArticleProjection> articleProjections = articleService.getAllArticles(
                Pageable.ofSize(10).withPage(0), categoryId
        ).get().toList();


        List<VideoResponse> videos = videoService.findRandomVideos();

        List<BookResponse> books = bookService.getAllBooks();

        return SearchResponse.builder()
                .articles(articleProjections)
                .books(books)
                .videos(videos)
                .build();
    }
}
