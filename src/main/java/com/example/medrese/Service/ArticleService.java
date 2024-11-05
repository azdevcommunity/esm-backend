package com.example.medrese.Service;

import com.example.medrese.DTO.Request.Create.CreateArticleDTO;
import com.example.medrese.DTO.Request.Update.UpdateArticle;
import com.example.medrese.DTO.Response.ArticleResponse;
import com.example.medrese.Model.Article;
import com.example.medrese.Model.ArticleCategory;
import com.example.medrese.Model.Author;
import com.example.medrese.Model.AuthorArticle;
import com.example.medrese.Repository.*;
import com.example.medrese.mapper.ArticleMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ArticleService {

    ArticleRepository articleRepository;
    ArticleMapper articleMapper;
    CategoryRepository categoryRepository;
    ArticleCategoryRepository articleCategoryRepository;
    AuthorRepository authorRepository;
    AuthorArticleRepository authorArticleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticleById(Integer id) {
        return articleRepository.findById(id).orElseThrow(() -> new RuntimeException("author not found"));
    }


    @Transactional
    public ArticleResponse createArticle(CreateArticleDTO createArticleDTO) {

        Article article = articleMapper.toEntity(createArticleDTO);
        article = articleRepository.save(article);

        for (int category : createArticleDTO.getCategories()) {
            if (categoryRepository.existsById(category)) {
                ArticleCategory articleCategory = ArticleCategory.builder()
                        .categoryId(category)
                        .articleId(article.getId())
                        .build();
                articleCategoryRepository.save(articleCategory);
            }
        }

        for (int authorId : createArticleDTO.getAuthorIds()) {
            if (authorRepository.existsById(authorId)) {
                AuthorArticle authorArticle = AuthorArticle.builder()
                        .authorId(authorId)
                        .articleId(article.getId())
                        .build();
                authorArticleRepository.save(authorArticle);
            }
        }
        return articleMapper.toResponse(article);
    }

    public Article updateArticle(Integer id, UpdateArticle articleDetails) {
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