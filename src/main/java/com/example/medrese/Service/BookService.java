package com.example.medrese.Service;


import com.example.medrese.DTO.Request.Create.CreateAuthorDTO;
import com.example.medrese.DTO.Request.Create.CreateBookDTO;
import com.example.medrese.DTO.Response.AuthorResponse;
import com.example.medrese.DTO.Response.BookResponse;
import com.example.medrese.Model.Author;
import com.example.medrese.Model.AuthorBook;
import com.example.medrese.Model.Book;
import com.example.medrese.Repository.AuthorBookRepository;
import com.example.medrese.Repository.AuthorRepository;
import com.example.medrese.Repository.BookCategoryRepository;
import com.example.medrese.Repository.BookRepository;
import com.example.medrese.mapper.BookMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class BookService {

    BookRepository bookRepository;
    BookMapper bookMapper;
    AuthorRepository authorRepository;
    AuthorBookRepository authorBookRepository;
    BookCategoryRepository bookCategoryRepository;

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toResponse)
                .toList();
    }

    public BookResponse getBookById(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        return bookMapper.toResponse(book);
    }

    @Transactional
    public BookResponse createBook(CreateBookDTO createBookDTO) {
        if (!authorRepository.existsById(createBookDTO.getAuthorId())) {
            throw new RuntimeException("Author not exists");
        }

        Book book = bookMapper.toEntity(createBookDTO);
        book = bookRepository.save(book);

        AuthorBook authorBook = AuthorBook.builder()
                .authorId(createBookDTO.getAuthorId())
                .bookId(book.getId())
                .build();

        authorBookRepository.save(authorBook);

        return bookMapper.toResponse(book);
    }

    public Book updateBook(Integer id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookMapper not found with id " + id));
        book.setTitle(bookDetails.getTitle());
        book.setImage(bookDetails.getImage());
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);

        if (!authorBookRepository.existsByBookId(id)) {
            throw new RuntimeException("Author book not exists");
        }

        bookCategoryRepository.deleteByBookId(id);

        authorBookRepository.deleteByBookId(id);
    }
}


