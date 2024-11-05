package com.example.medrese.Repository;

import com.example.medrese.Model.AuthorBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorBookRepository extends JpaRepository<AuthorBook,Integer> {

    void deleteByBookId(int bookId);

    Optional<AuthorBook> findByBookId(int bookId);

    boolean existsByBookId(int bookId);

    boolean existsByAuthorId(int authorId);
}
