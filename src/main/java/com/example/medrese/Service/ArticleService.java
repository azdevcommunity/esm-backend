package com.example.medrese.Service;

import com.example.medrese.DTO.Request.Create.CreateArticleDTO;
import com.example.medrese.DTO.Request.Update.UpdateArticle;
import com.example.medrese.DTO.Response.ArticleResponse;
import com.example.medrese.Model.Article;
import com.example.medrese.Model.ArticleCategory;
import com.example.medrese.Model.AuthorArticle;
import com.example.medrese.Repository.*;
import com.example.medrese.mapper.ArticleMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    FileService fileService;

    public List<ArticleResponse> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(articleMapper::toResponse)
                .toList();
    }

    public Article getArticleById(Integer id) {
        return articleRepository.findById(id).orElseThrow(() -> new RuntimeException("author not found"));
    }


    @Transactional
    public ArticleResponse createArticle(CreateArticleDTO createArticleDTO) {
        if (articleRepository.existsByTitle(createArticleDTO.getTitle())) {
            throw new RuntimeException("article with same title already exists");
        }

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
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id " + id));

        if (articleRepository.existsByTitleAndIdNot(articleDetails.getTitle(), article.getId())) {
            throw new RuntimeException("article with same title already exists");
        }

        article.setPublishedAt(articleDetails.getPublishedAt());
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article = articleRepository.save(article);

        articleCategoryRepository.deleteByArticleIdIn(articleDetails.getCategories().stream().toList());
        for (Integer category : articleDetails.getCategories()) {
            if (articleCategoryRepository.existsByCategoryId(category)) {
                ArticleCategory articleCategory = ArticleCategory.builder()
                        .categoryId(category)
                        .articleId(article.getId())
                        .build();
                articleCategoryRepository.save(articleCategory);
            }
        }

        authorArticleRepository.deleteByAuthorIdInAndArticleId(articleDetails.getAuthorIds().stream().toList(),article.getId());
        for (Integer author : articleDetails.getAuthorIds()) {
            if (authorArticleRepository.existsByAuthorId(author)) {
                AuthorArticle authorArticle = AuthorArticle.builder()
                        .articleId(article.getId())
                        .authorId(author)
                        .build();
                authorArticleRepository.save(authorArticle);
            }
        }


        return article;
    }

    public void deleteArticle(Integer id) {
        if (articleRepository.existsById(id)) {
            throw new RuntimeException("does not exist");
        }

        articleCategoryRepository.deleteByArticleId(id);

        articleRepository.deleteById(id);
    }


}