package com.example.medrese.DTO.Response;

import java.util.List;

public interface ArticleProjection2{
    Long getId();
    String getTitle();
    String getImage();
    String getPublishedAt();
    String getAuthorName();
    String getAuthorImage();

    List<String> getCategories();
}
