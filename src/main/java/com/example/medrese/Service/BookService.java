package com.example.medrese.Service;


import com.example.medrese.DTO.Request.Create.CreateBookDTO;
import com.example.medrese.DTO.Response.BookResponse;
import com.example.medrese.Model.AuthorBook;
import com.example.medrese.Model.Book;
import com.example.medrese.Repository.AuthorBookRepository;
import com.example.medrese.Repository.AuthorRepository;
import com.example.medrese.Repository.BookCategoryRepository;
import com.example.medrese.Repository.BookRepository;
import com.example.medrese.mapper.BookMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;
    private final AuthorBookRepository authorBookRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final FileService fileService;

    @Value("folder_root")
    String folderRoot;

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAllWithAuthor();
    }

    public BookResponse getBookById(Integer id) {
        return bookRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Transactional
    public BookResponse createBook(CreateBookDTO createBookDTO) {
        if (!authorRepository.existsById(createBookDTO.getAuthorId())) {
            throw new RuntimeException("Author not exists");
        }

        String image = fileService.uploadFile(createBookDTO.getImage(),folderRoot +"/books");
        createBookDTO.setImage(image);

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


        if (fileService.isBase64(bookDetails.getImage())) {
            fileService.deleteFile(book.getImage(), folderRoot + "/books");
            String image = fileService.uploadFile(bookDetails.getImage(), folderRoot + "/books");
            book.setImage(image);
        }

        book.setTitle(bookDetails.getTitle());

        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + id));

        if (!authorBookRepository.existsByBookId(id)) {
            throw new RuntimeException("Author book not exists");
        }


        bookCategoryRepository.deleteByBookId(id);

        authorBookRepository.deleteByBookId(id);

        bookRepository.delete(book);

        fileService.deleteFile(book.getImage(), folderRoot + "/books");

    }
}


