package com.example.medrese.Controller;


import com.example.medrese.DTO.Request.Create.CreateAuthorDTO;
import com.example.medrese.DTO.Response.AuthorResponse;
import com.example.medrese.Model.Author;
import com.example.medrese.Service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Integer id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(@RequestBody CreateAuthorDTO createAuthorDTO) {
        return ResponseEntity.ok(authorService.createAuthor(createAuthorDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Integer id, @RequestBody Author authorDetails) {
        Author updatedAuthor = authorService.updateAuthor(id, authorDetails);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Integer id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
