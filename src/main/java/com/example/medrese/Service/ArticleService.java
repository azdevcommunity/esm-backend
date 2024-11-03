package com.example.medrese.Service;

import com.example.medrese.Model.Article;
import com.example.medrese.Repository.ArticleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Integer id) {
        return articleRepository.findById(id);
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article updateArticle(Integer id, Article articleDetails) {
        return articleRepository.findById(id).map(article -> {
            article.setPublishedAt(articleDetails.getPublishedAt());
            article.setTitle(articleDetails.getTitle());
            article.setContent(articleDetails.getContent());
            return articleRepository.save(article);
        }).orElseThrow(() -> new RuntimeException("Article not found with id " + id));
    }

    public void deleteArticle(Integer id) {
        articleRepository.deleteById(id);
    }



}