package com.example.medrese.DTO.Response;

import com.example.medrese.Model.Article;
import com.example.medrese.Model.Author;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArticleResponse {
    Integer id;
    String publishedAt;
    String title;
    String content;
    AuthorResponse author;
    List<Integer> categories;
    String image;
    Long readCount;
}
