package com.example.medrese.Repository;

import com.example.medrese.DTO.Response.CategoryResponse;
import com.example.medrese.Model.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, Integer> {

    boolean existsByCategoryId(Integer id);

    void deleteByArticleId(int id);

    void deleteByArticleIdInAndArticleId(List<Integer> ids, Integer id);

    @Modifying
    @Query(value = "DELETE FROM article_categories WHERE article_id = :articleId", nativeQuery = true)
    void deleteByArticleId(@Param("articleId") Integer articleId);

    List<ArticleCategory> findByArticleId(int id);

    Optional<ArticleCategory> findByArticleIdAndCategoryId(int id, int id2);

}
