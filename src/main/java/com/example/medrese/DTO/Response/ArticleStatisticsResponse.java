package com.example.medrese.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleStatisticsResponse {
    private Long totalArticles;
    private Long totalCategories;
    private Long totalAuthors;
    private Long totalReadCount;
}