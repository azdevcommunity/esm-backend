package com.example.medrese.Repository;

import com.example.medrese.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

//    @Modifying(clearAutomatically = true)
//    @Transactional
//    @Query("delete from BookMapper b where b.author.id = ?1")
//    void deleteByAuthorId(int authorId);
}
