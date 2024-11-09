package com.example.medrese.Repository;

import com.example.medrese.Model.AuthorArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorArticleRepository extends JpaRepository<AuthorArticle, Integer> {

    boolean existsByAuthorId(int id);

    void deleteByAuthorIdInAndArticleId(List<Integer> ids, int id);

    List<AuthorArticle> findByArticleId(int id);
}

