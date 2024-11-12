package com.example.medrese.Controller;

import com.example.medrese.DTO.Request.Create.CreateArticleDTO;
import com.example.medrese.DTO.Request.Update.UpdateArticle;
import com.example.medrese.DTO.Response.ArticleIdProjection;
import com.example.medrese.DTO.Response.ArticleProjection;
import com.example.medrese.DTO.Response.ArticleResponse;
import com.example.medrese.Model.Article;
import com.example.medrese.Model.Author;
import com.example.medrese.Service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<Page<ArticleProjection>> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ArticleProjection> articles = articleService.getAllArticles(pageable, categoryId);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ArticleIdProjection>> getAllArticles() {
        List<ArticleIdProjection> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticle(
            @PathVariable Integer id,
            @RequestHeader(value = "X-Admin-Request", defaultValue = "false") boolean isAdminRequest) {

        ArticleResponse article = articleService.getArticleById(id, isAdminRequest);
        return ResponseEntity.ok(article);
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopularArticles() {
        return ResponseEntity.ok(articleService.getPopularArticles());
    }

    @PutMapping("/count/{id}")
    public ResponseEntity<?> incrementReadCount(@PathVariable Integer id) {
        articleService.incrementReadCount(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody CreateArticleDTO article) {
        return ResponseEntity.ok(articleService.createArticle(article));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Integer id, @RequestBody UpdateArticle articleDetails) {
            Article updatedArticle = articleService.updateArticle(id, articleDetails);
            return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Integer id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}