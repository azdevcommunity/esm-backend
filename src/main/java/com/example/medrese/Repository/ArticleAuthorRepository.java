package com.example.medrese.Repository;

import com.example.medrese.Model.ArticleAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleAuthorRepository extends JpaRepository<ArticleAuthor, Integer> {
}
