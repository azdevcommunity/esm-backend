package com.example.medrese.DTO.Response;


public interface PopularArticleProjection {
    String getId();
    String getTitle();
    String getImage();
    String getPublishedAt();
    String getAuthorName();
    String getAuthorImage();
    Long getReadCount();
}
