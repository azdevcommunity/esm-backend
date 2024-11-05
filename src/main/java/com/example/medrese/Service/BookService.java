package com.example.medrese.Service;


import com.example.medrese.DTO.Request.Create.CreateAuthorDTO;
import com.example.medrese.DTO.Request.Create.CreateBookDTO;
import com.example.medrese.DTO.Response.AuthorResponse;
import com.example.medrese.DTO.Response.BookResponse;
import com.example.medrese.Model.Author;
import com.example.medrese.Model.Book;
import com.example.medrese.Repository.BookRepository;
import com.example.medrese.mapper.BookMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class BookService {

    BookRepository bookRepository;
    BookMapper bookMapper;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public BookResponse getBookById(Integer id) {
        Book book =  bookRepository.findById(id).orElseThrow(()->new RuntimeException("Book not found"));
        return bookMapper.toResponse(book);
    }

    public BookResponse createBook(CreateBookDTO createBookDTO) {
        Book book = bookMapper.toEntity(createBookDTO);
        bookRepository.save(book);
        return bookMapper.toResponse(book);


    }

    public Book updateBook(Integer id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BookMapper not found with id " + id));
        book.setTitle(bookDetails.getTitle());
        book.setImage(bookDetails.getImage());
        return bookRepository.save(book);
    }

    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }
}


