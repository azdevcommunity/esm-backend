package com.example.medrese.Repository;

import com.example.medrese.DTO.Response.BookResponse;
import com.example.medrese.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query("delete from BookMapper b where b.author.id = ?1")
//    void deleteByAuthorId(int authorId);


    @Query("""
            select new  com.example.medrese.DTO.Response.BookResponse (
            book.id,book.title,book.image,author.id,author.name            )
            from Book book
            left join AuthorBook ab on book.id = ab.bookId
            left join Author author on ab.authorId = author.id
            """)
    List<BookResponse> findAllWithAuthor();

    @Query("""
            select new  com.example.medrese.DTO.Response.BookResponse (
            book.id,book.title,book.image,author.id,author.name            )
            from Book book
            left join AuthorBook ab on book.id = ab.bookId
            left join Author author on ab.authorId = author.id
            where book.id =:id
            """)
    Optional<BookResponse> findByIdWithAuthor(Integer id);
}
