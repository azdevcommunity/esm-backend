package com.example.medrese.Repository;
import com.example.medrese.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
//    boolean existsAuthorById(int id);

    boolean existsByName(String name);
}
