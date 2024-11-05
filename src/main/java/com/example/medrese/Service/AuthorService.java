package com.example.medrese.Service;


import com.example.medrese.DTO.Request.Create.CreateAuthorDTO;
import com.example.medrese.DTO.Response.AuthorResponse;
import com.example.medrese.Model.Author;
import com.example.medrese.Repository.AuthorRepository;
import com.example.medrese.mapper.AuthorMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

@Service
@RequiredArgsConstructor
public class AuthorService  {
    private AuthorRepository authorRepository;
    AuthorMapper authorMapper;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(Integer id) {
        return authorRepository.findById(id).orElseThrow(()->new RuntimeException("article not found"));
    }

    public AuthorResponse createAuthor(CreateAuthorDTO createAuthorDTO) {
        Author author = authorMapper.toEntity(createAuthorDTO);
        author = authorRepository.save(author);

        return authorMapper.toResponse(author);
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
