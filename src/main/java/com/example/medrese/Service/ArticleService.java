package com.example.medrese.Service;

import com.example.medrese.DTO.Request.Create.CreateArticleDTO;
import com.example.medrese.DTO.Request.Update.UpdateArticle;
import com.example.medrese.DTO.Response.ArticleProjection;
import com.example.medrese.DTO.Response.ArticleResponse;
import com.example.medrese.DTO.Response.AuthorResponse;
import com.example.medrese.DTO.Response.PopularArticleProjection;
import com.example.medrese.Model.Article;
import com.example.medrese.Model.ArticleCategory;
import com.example.medrese.Model.AuthorArticle;
import com.example.medrese.Repository.*;
import com.example.medrese.mapper.ArticleMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public Page<ArticleProjection> getAllArticles(Pageable pageable, Long categoryId) {
        if (ObjectUtils.isEmpty(categoryId) || categoryId == 0) {
            return articleRepository.findAllArticlesWithAuthorsAndCategories(pageable);
        }
        return articleRepository.findAllArticlesWithAuthorsAndCategories(pageable, categoryId);
    }

    public ArticleResponse getArticleById(Integer id, boolean isAdminRequest) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("author not found"));
        List<AuthorResponse> authors = authorRepository.findByArticleId(id);
        List<Integer> categories = articleCategoryRepository.findByArticleId(article.getId()).stream()
                .map(ArticleCategory::getCategoryId)
                .collect(Collectors.toSet())
                .stream()
                .toList();

        if (!isAdminRequest) {
            Long count = article.getReadCount();
            if (ObjectUtils.isEmpty(count)) {
                count = 0L;
            }
            article.setReadCount(count + 1);
            articleRepository.save(article);
        }

        ArticleResponse articleResponse = articleMapper.toResponse(article);
        articleResponse.setAuthors(authors);
        articleResponse.setCategories(categories);


        return articleResponse;
    }


    @Transactional
    public ArticleResponse createArticle(CreateArticleDTO createArticleDTO) {
        if (articleRepository.existsByTitle(createArticleDTO.getTitle())) {
            throw new RuntimeException("article with same title already exists");
        }

        Article article = articleMapper.toEntity(createArticleDTO);

        if (fileService.isBase64(createArticleDTO.getImage())) {
            article.setImage(fileService.uploadFile(createArticleDTO.getImage()));
        }

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

    @Transactional
    public Article updateArticle(Integer id, UpdateArticle articleDetails) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id " + id));

        if (articleRepository.existsByTitleAndIdNot(articleDetails.getTitle(), article.getId())) {
            throw new RuntimeException("article with same title already exists");
        }

        if (fileService.isBase64(articleDetails.getImage())) {
            fileService.deleteFile(article.getImage());
            String image = fileService.uploadFile(articleDetails.getImage());
            article.setImage(image);
        }


        article.setPublishedAt(articleDetails.getPublishedAt());
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article = articleRepository.save(article);

        if (!ObjectUtils.isEmpty(articleDetails.getCategories())) {
            articleCategoryRepository.deleteByArticleIdInAndArticleId(articleDetails.getCategories().stream().toList(), article.getId());
            for (Integer category : articleDetails.getCategories()) {
                if (categoryRepository.existsById(category)) {
                    ArticleCategory articleCategory = ArticleCategory.builder()
                            .categoryId(category)
                            .articleId(article.getId())
                            .build();
                    articleCategoryRepository.save(articleCategory);
                }
            }
        }

        if (!ObjectUtils.isEmpty(articleDetails.getAuthorIds())) {
            authorArticleRepository.deleteByAuthorIdInAndArticleId(articleDetails.getAuthorIds().stream().toList(), article.getId());
            for (Integer author : articleDetails.getAuthorIds()) {
                if (authorRepository.existsById(author)) {
                    AuthorArticle authorArticle = AuthorArticle.builder()
                            .articleId(article.getId())
                            .authorId(author)
                            .build();
                    authorArticleRepository.save(authorArticle);
                }
            }

        }


        return article;
    }

    public void deleteArticle(Integer id) {

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id " + id));

        articleCategoryRepository.deleteByArticleId(id);

        articleRepository.delete(article);

        fileService.deleteFile(article.getImage());

    }


    public List<PopularArticleProjection> getPopularArticles() {
        return articleRepository.findTopArticles(4);

    }

}