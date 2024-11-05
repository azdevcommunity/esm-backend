package com.example.medrese.Repository;

import com.example.medrese.Model.AuthorBook;
import com.example.medrese.Model.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryRepository  extends JpaRepository<BookCategory,Integer> {
    boolean existsByBookId(int book);

    void deleteByBookId(int  id);
}
