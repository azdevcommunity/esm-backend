package com.example.medrese.Repository;

import com.example.medrese.DTO.Response.ArticleIdProjection;
import com.example.medrese.DTO.Response.ArticleProjection;
import com.example.medrese.DTO.Response.ArticleProjection2;
import com.example.medrese.DTO.Response.PopularArticleProjection;
import com.example.medrese.Model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    boolean existsByTitleAndIdNot(String title, Integer id);

    boolean existsByTitle(String title);

    @Query(value = """
            SELECT a.id AS id, a.title AS title, a.image AS image,
                               a.published_at as publishedAt,
                        au.name AS authorName,
                        au.image AS authorImage,
                        STRING_AGG(c.name, ', ') AS categories
                        FROM esm.articles a
                        LEFT JOIN esm.authors au ON a.author_id = au.id
                        LEFT JOIN esm.article_categories ac ON a.id = ac.article_id
                        LEFT JOIN esm.categories c ON ac.category_id = c.id
                        GROUP BY a.id, a.title, a.image, au.name, au.image, a.published_at
                        ORDER BY a.published_at DESC
            """,
            countQuery = "SELECT COUNT(*) FROM esm.articles a",
            nativeQuery = true)
    Page<ArticleProjection2> findAllArticlesWithAuthorsAndCategories(Pageable pageable);

    @Query(value = "WITH RECURSIVE category_tree AS ( " +
            "    SELECT id, parent_id " +
            "    FROM esm.categories " +
            "    WHERE id = :categoryId " +
            "    UNION ALL " +
            "    SELECT c.id, c.parent_id " +
            "    FROM esm.categories c " +
            "    INNER JOIN category_tree ct ON c.parent_id = ct.id " +
            ") " +
            "SELECT a.id AS id, a.title AS title, a.image AS image, " +
            "       a.published_at as publishedAt, " +
            "       au.name  AS authorName, " +
            "       au.image  AS authorImage, " +
            "       STRING_AGG(c.name, ', ') AS categories " +
            "FROM esm.articles a " +
            "LEFT JOIN esm.authors au ON a.author_id = au.id " +
            "LEFT JOIN esm.article_categories ac ON a.id = ac.article_id " +
            "LEFT JOIN esm.categories c ON ac.category_id = c.id " +
            "WHERE c.id IN (SELECT id FROM category_tree) " +
            "GROUP BY a.id, a.title, a.image, au.name, au.image, a.published_at " +
            "ORDER BY a.published_at DESC",
            countQuery = "WITH RECURSIVE category_tree AS ( " +
                    "    SELECT id, parent_id " +
                    "    FROM esm.categories " +
                    "    WHERE id = :categoryId " +
                    "    UNION ALL " +
                    "    SELECT c.id, c.parent_id " +
                    "    FROM esm.categories c " +
                    "    INNER JOIN category_tree ct ON c.parent_id = ct.id " +
                    ") " +
                    "SELECT COUNT(*) " +
                    "FROM esm.articles a " +
                    "LEFT JOIN esm.article_categories ac ON a.id = ac.article_id " +
                    "LEFT JOIN esm.categories c ON ac.category_id = c.id " +
                    "WHERE c.id IN (SELECT id FROM category_tree)",
            nativeQuery = true)
    Page<ArticleProjection2> findAllArticlesWithAuthorsAndCategories(Pageable pageable, @Param("categoryId") Long categoryId);


    @Query(value = """
            SELECT 
               a.id AS id,
               a.title AS title,
               a.image AS image,
               au.name  AS authorName,
               au.image  AS authorImage,
               STRING_AGG(DISTINCT c.name, ', ') AS categories
            FROM esm.articles a
            LEFT JOIN esm.authors au ON a.author_id = au.id
            LEFT JOIN esm.article_categories ac ON a.id = ac.article_id
            LEFT JOIN esm.categories c ON ac.category_id = c.id
            WHERE
               (
                 -- If search is empty, show all
                 (:search IS NULL OR :search = '')
                 OR
                 -- Otherwise, match title/content/authors/categories
                 (
                   a.title ILIKE CONCAT('%', :search, '%')
                   OR a.content ILIKE CONCAT('%', :search, '%')
                   OR au.name ILIKE CONCAT('%', :search, '%')
                   OR c.name ILIKE CONCAT('%', :search, '%')
                 )
               )
            GROUP BY a.id
            ORDER BY a.published_at DESC
            """,
            countQuery = """
    SELECT
       COUNT(DISTINCT a.id)
    FROM esm.articles a
    LEFT JOIN esm.authors au ON a.author_id = au.id
    LEFT JOIN esm.article_categories ac ON a.id = ac.article_id
    LEFT JOIN esm.categories c ON ac.category_id = c.id
    WHERE
       (
         (:search IS NULL OR :search = '')
         OR
         (
           a.title ILIKE CONCAT('%', :search, '%')
           OR a.content ILIKE CONCAT('%', :search, '%')
           OR au.name ILIKE CONCAT('%', :search, '%')
           OR c.name ILIKE CONCAT('%', :search, '%')
         )
       )
    """,
            nativeQuery = true)
    Page<ArticleProjection2> findAllArticlesWithAuthorsAndCategoriesBySearch(
            @Param("search") String search,
            Pageable pageable
    );

    @Query(value = """
            SELECT a.id AS id,
                   a.title AS title,
                   a.image AS image,
                   a.published_at AS publishedAt,
                   au.name AS authorName,
                   au.image AS authorImage
            FROM esm.articles a
            LEFT JOIN esm.authors au ON a.author_id = au.id
            ORDER BY a.read_count DESC
            LIMIT ?1
            """,
            nativeQuery = true)
    List<PopularArticleProjection> findTopArticles(Integer limit);


    @Query(value = """
            SELECT a.id AS id
                    FROM esm.articles a
            """
            ,
            nativeQuery = true)
    List<ArticleIdProjection> findAllArticlesWithAuthorsAndCategories();


    @Modifying
    @Query("UPDATE Article a SET a.readCount = a.readCount + 1 WHERE a.id = :id")
    void incrementReadCount(@Param("id") Integer id);

    boolean existsByAuthorId(Integer id);
}
