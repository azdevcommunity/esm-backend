package com.example.medrese.Repository;

import com.example.medrese.Model.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);


    List<Category> findAllByIsActiveTrue();
    Optional<Category> findByIdAndIsActiveTrue(int id);

    boolean existsByNameAndIdNot(String name, Integer id);
}
