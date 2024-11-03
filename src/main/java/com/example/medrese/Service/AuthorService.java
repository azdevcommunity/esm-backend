package com.example.medrese.Service;


import com.example.medrese.Model.Author;
import com.example.medrese.Repository.AuthorRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthorService  {
    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthorById(Integer id) {
        return authorRepository.findById(id);
    }

    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author updateAuthor(Integer id, Author authorDetails) {
        return authorRepository.findById(id).map(author -> {
            author.setName(authorDetails.getName());
            author.setImage(authorDetails.getImage());
            return authorRepository.save(author);
        }).orElseThrow(() -> new RuntimeException("Author not found with id " + id));
    }

    public void deleteAuthor(Integer id) {
        authorRepository.deleteById(id);
    }


}
