package com.example.medrese.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleAuthorRepository extends JpaRepository<ArticleAuthorRepository, Integer> {
}
