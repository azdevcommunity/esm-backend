package com.example.medrese.Repository;

import com.example.medrese.DTO.Response.ArticleProjection;
import com.example.medrese.DTO.Response.PopularArticleProjection;
import com.example.medrese.Model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    boolean existsByTitleAndIdNot(String title, Integer id);

    boolean existsByTitle(String title);

    @Query(value = "SELECT a.id AS id, a.title AS title, a.image AS image, " +
            "STRING_AGG(au.name, ', ') AS authors, " +
            "STRING_AGG(c.name, ', ') AS categories " +
            "FROM articles a " +
            "LEFT JOIN author_articles aa ON a.id = aa.article_id " +
            "LEFT JOIN authors au ON aa.author_id = au.id " +
            "LEFT JOIN article_categories ac ON a.id = ac.article_id " +
            "LEFT JOIN categories c ON ac.category_id = c.id " +
            "GROUP BY a.id " +
            "ORDER BY a.published_at DESC",
            countQuery = "SELECT COUNT(*) FROM articles a",
            nativeQuery = true)
    Page<ArticleProjection> findAllArticlesWithAuthorsAndCategories(Pageable pageable);

    @Query(value = "SELECT a.id AS id, a.title AS title, a.image AS image, " +
            "STRING_AGG(au.name, ', ') AS authors, " +
            "STRING_AGG(c.name, ', ') AS categories " +
            "FROM articles a " +
            "LEFT JOIN author_articles aa ON a.id = aa.article_id " +
            "LEFT JOIN authors au ON aa.author_id = au.id " +
            "LEFT JOIN article_categories ac ON a.id = ac.article_id " +
            "LEFT JOIN categories c ON ac.category_id = c.id " +
            "WHERE c.id = :categoryId " +
            "GROUP BY a.id " +
            "ORDER BY a.published_at DESC",
            countQuery = "SELECT COUNT(*) FROM articles a " +
                    "LEFT JOIN article_categories ac ON a.id = ac.article_id " +
                    "LEFT JOIN categories c ON ac.category_id = c.id " +
                    "WHERE c.id = :categoryId",
            nativeQuery = true)
    Page<ArticleProjection> findAllArticlesWithAuthorsAndCategories(Pageable pageable, @Param("categoryId") Long categoryId);

    @Query(value = "SELECT a.id AS id, a.title AS title, a.image AS image, a.published_at AS publishedAt " +
            "FROM articles a " +
            "ORDER BY a.read_count DESC " +
            "LIMIT ?1",
            nativeQuery = true)
    List<PopularArticleProjection> findTopArticles(Integer limit);

}
