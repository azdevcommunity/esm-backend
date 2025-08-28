package com.example.medrese.Service;

import com.example.medrese.Core.Util.Exceptions.Global.NotFoundException;
import com.example.medrese.DTO.Request.Create.CreateArticleDTO;
import com.example.medrese.DTO.Request.Update.DeleteArticles;
import com.example.medrese.DTO.Request.Update.UpdateArticle;
import com.example.medrese.DTO.Response.ArticleIdProjection;
import com.example.medrese.DTO.Response.ArticleProjection2;
import com.example.medrese.DTO.Response.ArticleResponse;
import com.example.medrese.DTO.Response.ArticleStatisticsResponse;
import com.example.medrese.DTO.Response.AuthorResponse;
import com.example.medrese.DTO.Response.PopularArticleProjection;
import com.example.medrese.Model.Article;
import com.example.medrese.Model.ArticleCategory;
import com.example.medrese.Repository.ArticleCategoryRepository;
import com.example.medrese.Repository.ArticleRepository;
import com.example.medrese.Repository.AuthorRepository;
import com.example.medrese.Repository.CategoryRepository;
import com.example.medrese.mapper.ArticleMapper;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ArticleService {

    ArticleRepository articleRepository;
    ArticleMapper articleMapper;
    CategoryRepository categoryRepository;
    ArticleCategoryRepository articleCategoryRepository;
    AuthorRepository authorRepository;
    //    AuthorArticleRepository authorArticleRepository;
    FileService fileService;

    public Page<ArticleProjection2> getAllArticles(Pageable pageable, List<Long> categoryIds) {
        if (ObjectUtils.isEmpty(categoryIds)) {
            return articleRepository.findAllArticlesWithAuthorsAndCategories(pageable);
        }
        return articleRepository.findAllArticlesWithAuthorsAndCategories(pageable, categoryIds);
    }

    public List<ArticleIdProjection> getAllArticles() {
        return articleRepository.findAllArticlesWithAuthorsAndCategories();
    }

    public ArticleResponse getArticleById(Integer id, boolean isAdminRequest) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new NotFoundException("article not found"));
        AuthorResponse author = authorRepository.findById(article.getAuthorId())
                .map(a -> new AuthorResponse(
                        a.getId(), a.getName(), a.getImage()
                ))
                .orElseThrow(() -> new RuntimeException("Author not found"));
        ;
        List<Integer> categories = articleCategoryRepository.findByArticleId(article.getId()).stream()
                .map(ArticleCategory::getCategoryId)
                .collect(Collectors.toSet())
                .stream()
                .toList();

        ArticleResponse articleResponse = articleMapper.toResponse(article);
        articleResponse.setAuthor(author);
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
            article.setImage(fileService.uploadFile(createArticleDTO.getImage(), "esm/articles"));
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


        if (!authorRepository.existsById(createArticleDTO.getAuthorId())) {
            throw new RuntimeException("author not found");
//                AuthorArticle authorArticle = AuthorArticle.builder()
//                        .authorId(createArticleDTO.getAuthorId())
//                        .articleId(article.getId())
//                        .build();
//                authorArticleRepository.save(authorArticle);
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
            fileService.deleteFile(article.getImage(),"esm/articles");
            String image = fileService.uploadFile(articleDetails.getImage(),"esm/articles");
            article.setImage(image);
        }


        article.setPublishedAt(articleDetails.getPublishedAt());
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article = articleRepository.save(article);

        if (!ObjectUtils.isEmpty(articleDetails.getCategories())) {
//            articleCategoryRepository.deleteByArticleIdInAndArticleId(articleDetails.getCategories().stream().toList(), article.getId());
            articleCategoryRepository.deleteByArticleId(article.getId());
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

        if (!ObjectUtils.isEmpty(articleDetails.getAuthorId())) {
//            authorArticleRepository.deleteByAuthorIdInAndArticleId(List.of(articleDetails.getAuthorId()), article.getId());

            if (!authorRepository.existsById(articleDetails.getAuthorId())) {
                throw new RuntimeException("author not found");
//                    AuthorArticle authorArticle = AuthorArticle.builder()
//                            .articleId(article.getId())
//                            .authorId(articleDetails.getAuthorIds())
//                            .build();
//                    authorArticleRepository.save(authorArticle);

                }


        }


        return article;
    }

    @Transactional
    public void deleteArticle(Integer id) {

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id " + id));

        articleCategoryRepository.deleteByArticleId(id);

        articleRepository.delete(article);

        fileService.deleteFile(article.getImage(), "esm/articles");

    }

    @Transactional
    public void deleteArticles(DeleteArticles request) {
        for (Integer id : request.getIds()) {
            deleteArticle(id);
        }
    }


    public List<PopularArticleProjection> getPopularArticles() {
        return articleRepository.findTopArticles(4);

    }

    @Transactional
    public void incrementReadCount(Integer id) {
        articleRepository.incrementReadCount(id);
    }

    public Page<ArticleProjection2> searchArticles(int limit, List<Long> categoryId, String search) {
        Pageable pageable = Pageable.ofSize(limit).withPage(0);
        if(Objects.nonNull(categoryId)){
            return articleRepository.findAllArticlesWithAuthorsAndCategories(pageable, categoryId);
        }else{
            return articleRepository.findAllArticlesWithAuthorsAndCategoriesBySearch(search,pageable);
        }

    }

    public ArticleStatisticsResponse getArticleStatistics() {
        Long totalArticles = articleRepository.countTotalArticles();
        Long totalCategories = articleRepository.countDistinctCategories();
        Long totalAuthors = articleRepository.countDistinctAuthors();
        Long totalReadCount = articleRepository.sumTotalReadCount();

        return new ArticleStatisticsResponse(totalArticles, totalCategories, totalAuthors, totalReadCount);
    }
}