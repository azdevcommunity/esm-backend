package com.example.medrese.DTO.Response;

import java.util.List;

public interface ArticleProjection {
    Long getId();
    String getTitle();
    String getImage();
    String getAuthorName();
    List<String> getCategories();
}

