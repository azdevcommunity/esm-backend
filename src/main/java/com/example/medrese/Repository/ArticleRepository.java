package com.example.medrese.Repository;

import com.example.medrese.DTO.Response.ArticleProjection;
import com.example.medrese.DTO.Response.ArticleResponse;
import com.example.medrese.Model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
            "GROUP BY a.id",
            nativeQuery = true)
    List<ArticleProjection> findAllArticlesWithAuthorsAndCategories();

}
