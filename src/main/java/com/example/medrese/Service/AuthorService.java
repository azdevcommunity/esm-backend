package com.example.medrese.Service;


import com.example.medrese.DTO.Request.Create.CreateAuthorDTO;
import com.example.medrese.DTO.Request.Update.UpdateAuthor;
import com.example.medrese.DTO.Response.AuthorResponse;
import com.example.medrese.Model.Author;
import com.example.medrese.Repository.AuthorArticleRepository;
import com.example.medrese.Repository.AuthorBookRepository;
import com.example.medrese.Repository.AuthorRepository;
import com.example.medrese.mapper.AuthorMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final AuthorBookRepository authorBookRepository;
    private final AuthorArticleRepository authorArticleRepository;
    private final FileService fileService;

    public List<AuthorResponse> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toResponse)
                .toList();
    }

    public AuthorResponse getAuthorById(Integer id) {
        return authorRepository.findById(id)
                .map(authorMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("article not found"));
    }

    public AuthorResponse createAuthor(CreateAuthorDTO createAuthorDTO) {
        if (authorRepository.existsByName(createAuthorDTO.getName())) {
            throw new RuntimeException("author with same name already exists");
        }


        String image = fileService.uploadFile(createAuthorDTO.getImage());
        createAuthorDTO.setImage(image);

        Author author = authorMapper.toEntity(createAuthorDTO);
        author = authorRepository.save(author);

        return authorMapper.toResponse(author);
    }

    public AuthorResponse updateAuthor(Integer id, UpdateAuthor authorDetails) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id " + id));

        if (Objects.nonNull(authorDetails.getName())) {
            author.setName(authorDetails.getName());
        }

        if (fileService.isBase64(authorDetails.getImage())) {
            fileService.deleteFile(author.getImage());
            String image = fileService.uploadFile(authorDetails.getImage());
            author.setImage(image);
        }

        author = authorRepository.save(author);

        return authorMapper.toResponse(author);
    }

    public void deleteAuthor(Integer id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id " + id));

        if (authorBookRepository.existsByAuthorId(id)) {
            throw new RuntimeException("Bu authorun kitabi var sile bilmersen");
        }

        if (authorArticleRepository.existsByAuthorId(id)) {
            throw new RuntimeException("Bu authorun meqalesi var sile bilmersen");
        }

        authorRepository.delete(author);



        authorRepository.deleteById(id);

        fileService.deleteFile(author.getImage());
    }


}
