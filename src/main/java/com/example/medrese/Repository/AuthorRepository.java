package com.example.medrese.Repository;
import com.example.medrese.DTO.Response.AuthorResponse;
import com.example.medrese.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
//    boolean existsAuthorById(int id);

    boolean existsByName(String name);

    @Query("""
        select new com.example.medrese.DTO.Response.AuthorResponse(
        a.id,a.name,a.image
      )
        from Author a
        left join AuthorArticle  aa on a.id = aa.authorId
        where aa.articleId = :id
""")
    List<AuthorResponse> findByArticleId(int id);
}
