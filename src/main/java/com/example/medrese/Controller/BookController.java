package com.example.medrese.Controller;


import com.example.medrese.DTO.Request.Create.CreateBookDTO;
import com.example.medrese.DTO.Response.BookResponse;
import com.example.medrese.Model.Book;
import com.example.medrese.Service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid CreateBookDTO createBookDTO) {
        return ResponseEntity.ok(bookService.createBook(createBookDTO)) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Integer id, @RequestBody Book bookDetails) {
            Book updatedBook = bookService.updateBook(id, bookDetails);
            return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}

