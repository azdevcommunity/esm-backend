package com.example.medrese.DTO.Response;

import java.util.List;

public interface ArticleProjection {
    Long getId();
    String getTitle();
    String getImage();
    List<String> getAuthors();
    List<String> getCategories();
}