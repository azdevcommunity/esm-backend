package com.example.medrese.Repository;

import com.example.medrese.Model.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, Integer> {

    boolean existsByCategoryId(Integer id);

    void deleteByArticleId(int id);

    void deleteByArticleIdIn(List<Integer> ids);
}
